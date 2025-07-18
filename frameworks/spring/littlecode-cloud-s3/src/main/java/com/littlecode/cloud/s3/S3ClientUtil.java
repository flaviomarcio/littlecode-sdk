package com.littlecode.cloud.s3;

import com.littlecode.files.IOUtil;
import com.littlecode.parsers.ExceptionBuilder;
import com.littlecode.parsers.HashUtil;
import com.littlecode.parsers.ObjectUtil;
import com.littlecode.parsers.PrimitiveUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

//ref https://docs.aws.amazon.com/pt_br/AmazonS3/latest/userguide/checking-object-integrity.html
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class S3ClientUtil {
    @Getter
    @Setter
    private String endpoint;
    @Getter
    @Setter
    private String region;
    @Getter
    @Setter
    private String bucket;
    @Getter
    @Setter
    private String accessKey;
    @Getter
    @Setter
    private String secretKey;
    @Getter
    @Setter
    private S3Client s3Client;
    private UUID hashClient;

    public S3ClientUtil close(){
        if(this.s3Client!=null)
            this.s3Client.close();
        this.s3Client=null;
        return this;
    }

    public UUID makeHashClient() {
        var endpoint = this.getEndpoint() == null ? "" : this.getEndpoint().trim();
        var region = this.getRegion() == null ? "" : this.getRegion().trim();
        var bucket = this.getBucket() == null ? "" : this.getBucket().trim();
        var accessKey = this.getAccessKey() == null ? "" : this.getAccessKey().trim();
        var secretKey = this.getSecretKey() == null ? "" : this.getSecretKey().trim();
        return HashUtil.toMd5Uuid(List.of(endpoint, region, bucket, accessKey, secretKey));
    }

    public S3Client createClient() {

        var endpoint = this.getEndpoint() == null ? "" : this.getEndpoint().trim();
        var region = this.getRegion() == null ? "" : this.getRegion().trim();
        var bucket = this.getBucket() == null ? "" : this.getBucket().trim();



        if (PrimitiveUtil.isEmpty(region))
            throw ExceptionBuilder.ofNullPointer("Invalid clientSecret");

        if (PrimitiveUtil.isEmpty(bucket))
            throw ExceptionBuilder.ofNullPointer("Invalid bucket");

        S3Client s3Client;
        if (endpoint.trim().isEmpty()) {
            s3Client=
                    S3Client
                            .builder()
                            .credentialsProvider(this::createAwsBasicCredentials)
                            .region(Region.of(this.getRegion()))
                            .forcePathStyle(true)
                            .serviceConfiguration(
                                    S3Configuration
                                            .builder()
                                            .chunkedEncodingEnabled(false) // <=== DESATIVA o modo problemático
                                            .build()
                            )
                            .build();
        }
        else{
            s3Client= S3Client.builder()
                    .endpointOverride(URI.create(endpoint))
                    .credentialsProvider(this::createAwsBasicCredentials)
                    .region(Region.of(this.getRegion()))
                    .forcePathStyle(true)
                    .serviceConfiguration(
                            S3Configuration
                                    .builder()
                                    .chunkedEncodingEnabled(false) // <=== DESATIVA o modo problemático
                                    .build()
                    )
                    .build();
        }

        this.hashClient=makeHashClient();
        return s3Client;
    }


    public S3Client newClient() {
        var hashClient=makeHashClient();
        if (this.s3Client != null && hashClient.equals(this.hashClient))
            return this.s3Client;

        if (PrimitiveUtil.isEmpty(this.region))
            throw ExceptionBuilder.ofNullPointer("Invalid clientSecret");

        if (PrimitiveUtil.isEmpty(this.bucket))
            throw ExceptionBuilder.ofNullPointer("Invalid bucket");


        return this.createClient();
    }

    public AwsBasicCredentials createAwsBasicCredentials() {
        var accessKey = this.getAccessKey() == null ? "" : this.getAccessKey().trim();
        var secretKey = this.getSecretKey() == null ? "" : this.getSecretKey().trim();
        if (accessKey.isEmpty() || secretKey.isEmpty())
            return null;
        return AwsBasicCredentials.create(accessKey, secretKey);
    }

    private boolean internalPut(File source, String fileName) throws Exception {
        log.debug("Put: started, file: {}",source.getAbsolutePath());
        this.s3Client = this.newClient();
        try (InputStream inputStream = new FileInputStream(source)) {
            PutObjectResponse response = s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(this.getBucket())
                            .key(fileName)
                            .build(),
                    RequestBody.fromInputStream(inputStream, source.length())
            );
            log.debug("Put response: eTag: {}", response.eTag());
            return true;
        }finally {
            log.debug("Put: finished, file: {}",source.getAbsolutePath());
        }
    }

    public long size(String filename){
        var s3Client = this.newClient();
        GetObjectAttributesResponse objectAttributes =
                s3Client
                        .getObjectAttributes(
                                GetObjectAttributesRequest
                                        .builder()
                                        .bucket(this.bucket)
                                        .key(filename)
                                        .objectAttributes(ObjectAttributes.OBJECT_SIZE)
                                        .build()
                        );
        return objectAttributes==null?0:objectAttributes.objectSize();
    }

    public String eTag(String filename){
        this.s3Client = this.newClient();
        GetObjectAttributesResponse objectAttributes =
                s3Client
                        .getObjectAttributes(
                                GetObjectAttributesRequest
                                        .builder()
                                        .bucket(this.bucket)
                                        .key(filename)
                                        .objectAttributes(ObjectAttributes.E_TAG)
                                        .build()
                        );
        return objectAttributes==null?"":objectAttributes.eTag();
    }

    public String checksum(String filename){
        this.s3Client = this.newClient();
        GetObjectAttributesResponse objectAttributes =
                s3Client
                        .getObjectAttributes(
                                GetObjectAttributesRequest
                                        .builder()
                                        .bucket(this.bucket)
                                        .key(filename)
                                        .objectAttributes(ObjectAttributes.CHECKSUM)
                                        .build()
                        );
        return objectAttributes==null?"":objectAttributes.checksum().toString();
    }

    public boolean exists(String filename) {
        var eTag=this.eTag(filename);
        return !eTag.trim().isEmpty();
    }

    public File get(String filename){
        var s3Client = this.newClient();
        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(GetObjectRequest.builder().bucket(this.bucket).key(filename).build());
        if(response!=null){
            try {
                var outputFile = File.createTempFile(UUID.randomUUID().toString(),".tmp");
                FileOutputStream outFile = new FileOutputStream(outputFile);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = response.read(buffer)) != -1) {
                    outFile.write(buffer, 0, bytesRead);
                }
                outFile.flush();
                outFile.close();
                return outputFile;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public boolean put(Object source, String fileName) throws Exception {
        if (source instanceof File sourceValues)
            return this.internalPut(sourceValues, fileName);

        if (source instanceof Path sourceValues)
            return this.internalPut(sourceValues.toFile(), fileName);


        var tmpFile = IOUtil.target(IOUtil.createFileTemp()).toFile();
        if (source instanceof InputStream sourceValues) {
            IOUtil
                    .target(tmpFile)
                    .writeAll(Arrays.toString(sourceValues.readAllBytes()));
        } else if (source instanceof String sourceValues) {
            IOUtil
                    .target(tmpFile)
                    .writeAll(sourceValues);
        } else {
            IOUtil
                    .target(tmpFile)
                    .writeAll(ObjectUtil.toString(source));
        }
        return this.internalPut(tmpFile, fileName);
    }

    public boolean delete(String fileName) {
        var s3Client = this.newClient();
        s3Client.deleteObject(DeleteObjectRequest.builder().bucket(this.bucket).key(fileName).build());
        return true;
    }

}