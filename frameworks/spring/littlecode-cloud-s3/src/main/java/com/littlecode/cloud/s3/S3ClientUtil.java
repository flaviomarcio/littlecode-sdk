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
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
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

    public S3Client newClient() {

        var endpoint = this.getEndpoint() == null ? "" : this.getEndpoint().trim();
        var region = this.getRegion() == null ? "" : this.getRegion().trim();
        var bucket = this.getBucket() == null ? "" : this.getBucket().trim();
        var accessKey = this.getAccessKey() == null ? "" : this.getAccessKey().trim();
        var secretKey = this.getSecretKey() == null ? "" : this.getSecretKey().trim();
        var hashClient = HashUtil.toMd5Uuid(List.of(endpoint, region, bucket, accessKey, secretKey));
        if (this.s3Client != null && hashClient.equals(this.hashClient)){
            return this.s3Client;
        }

        if (PrimitiveUtil.isEmpty(region))
            throw ExceptionBuilder.ofNullPointer("Invalid clientSecret");

        if (PrimitiveUtil.isEmpty(bucket))
            throw ExceptionBuilder.ofNullPointer("Invalid bucket");

        this.hashClient = hashClient;

        if (endpoint == null || endpoint.isEmpty()) {
            return this.s3Client = S3Client.builder()
                    .credentialsProvider(this::createAwsBasicCredentials)
                    .region(Region.of(this.getRegion()))
                    .build();
        }

        return this.s3Client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(this::createAwsBasicCredentials)
                .region(Region.of(this.getRegion()))
                .build();
    }

    public AwsBasicCredentials createAwsBasicCredentials() {
        var accessKey = this.getAccessKey() == null ? "" : this.getAccessKey().trim();
        var secretKey = this.getSecretKey() == null ? "" : this.getSecretKey().trim();
        if (accessKey.isEmpty() || secretKey.isEmpty())
            return null;
        return AwsBasicCredentials.create(accessKey, secretKey);
    }

    private boolean internalPut(File source, String filename, boolean deleteOnFinished) {
        var s3Client = this.newClient();
        var putObjectRequest = PutObjectRequest
                .builder()
                .bucket(this.getBucket())
                .key(filename)
                .contentLength(source.length())//inputStream.available()
                .build();
        s3Client.putObject(putObjectRequest, source.toPath());
        if (deleteOnFinished)
            source.delete();
        return true;
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
        var s3Client = this.newClient();
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
        var s3Client = this.newClient();
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
            File outputFile = null;
            try {
                outputFile = File.createTempFile(UUID.randomUUID().toString(),".tmp");
                FileOutputStream outFile = new FileOutputStream(outputFile);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = response.read(buffer)) != -1) {
                    outFile.write(buffer, 0, bytesRead);
                }
                outFile.flush();
                outFile.close();
                s3Client.close();
                return outputFile;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public boolean put(Object source, String fileName){
        if (source instanceof File sourceValues) {
            return this.internalPut(sourceValues, fileName, false);
        }else if (source instanceof Path sourceValues) {
            return this.internalPut(sourceValues.toFile(), fileName, false);
        } else {
            var tmpFile = IOUtil.target(IOUtil.createFileTemp()).toFile();
            try {
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
                return this.internalPut(tmpFile, fileName, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean delete(String fileName) {
        var s3Client = this.newClient();
        s3Client.deleteObject(DeleteObjectRequest.builder().bucket(this.bucket).key(fileName).build());
        return true;
    }

}