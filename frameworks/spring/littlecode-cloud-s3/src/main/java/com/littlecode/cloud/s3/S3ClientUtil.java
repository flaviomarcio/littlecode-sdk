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
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.*;
import java.net.URI;
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

    public S3Client newClient() {

        var endpoint=this.getEndpoint()==null?"":this.getEndpoint().trim();
        var region=this.getRegion()==null?"":this.getRegion().trim();
        var bucket=this.getBucket()==null?"":this.getBucket().trim();
        var accessKey=this.getAccessKey()==null?"":this.getAccessKey().trim();
        var secretKey=this.getSecretKey()==null?"":this.getSecretKey().trim();
        var hashClient= HashUtil.toMd5Uuid(List.of(endpoint, region, bucket, accessKey, secretKey));
        if(this.s3Client!=null && hashClient.equals(this.hashClient))
            return this.s3Client;

        if (PrimitiveUtil.isEmpty(region))
            throw ExceptionBuilder.ofNullPointer("Invalid clientSecret");

        if (PrimitiveUtil.isEmpty(bucket))
            throw ExceptionBuilder.ofNullPointer("Invalid bucket");

        this.hashClient= hashClient;

        if (endpoint==null || endpoint.isEmpty()) {
            return this.s3Client=S3Client.builder()
                    .credentialsProvider(this::createAwsBasicCredentials)
                    .region(Region.of(this.getRegion()))
                    .build();
        }

        return this.s3Client=S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(this::createAwsBasicCredentials)
                .region(Region.of(this.getRegion()))
                .build();
    }

    public AwsBasicCredentials createAwsBasicCredentials() {
        var accessKey=this.getAccessKey()==null?"":this.getAccessKey().trim();
        var secretKey=this.getSecretKey()==null?"":this.getSecretKey().trim();
        if(accessKey.isEmpty() || secretKey.isEmpty())
            return null;
        return AwsBasicCredentials.create(accessKey, secretKey);
    }

    private boolean internalPut(File source, String filename, boolean deleteOnFinished) {
        var s3Client = this.newClient();
        var putObjectRequest = PutObjectRequest
                .builder()
                .bucket(this.getBucket())
                .key(filename)
                .contentLength(0L)//inputStream.available()
                .build();
        s3Client.putObject(putObjectRequest, source.toPath());
        if (deleteOnFinished)
            source.delete();
        return true;
    }

    public boolean exists(String filename) {
        try (var s3Client = this.newClient()) {
            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(GetObjectRequest.builder().bucket(this.bucket).key(filename).build());
            if(response!=null)
                return response.read() > 0;
        } catch (Exception ignored) {
        }
        return false;
    }

    public File get(String filename) throws IOException {
        var s3Client = this.newClient();

        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(GetObjectRequest.builder().bucket(this.bucket).key(filename).build());
        if(response!=null){
            var outputFile = File.createTempFile(UUID.randomUUID().toString(),".tmp");
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
        }
        return null;
    }

    public boolean put(Object source, String fileName) throws IOException {
        if (source instanceof File sourceValues) {
            return this.internalPut(sourceValues, fileName, false);
        } else {
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
            return this.internalPut(tmpFile, fileName, true);
        }
    }

    public boolean delete(String fileName) {
        return false;
    }

}