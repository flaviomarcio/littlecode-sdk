package com.littlecode.cloud.s3;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.littlecode.cloud.s3.started.S3Started;
import com.littlecode.files.IOUtil;
import com.littlecode.parsers.ExceptionBuilder;
import com.littlecode.parsers.ObjectUtil;
import com.littlecode.parsers.PrimitiveUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.InputStream;

@Slf4j
@Builder
@AllArgsConstructor
public class S3Client {
    private String url;
    private String region;
    private String bucket;
    private String clientId;
    private String clientSecret;

    public boolean exists(String filename) {
        try {
            var s3Client = this.newClient();
            var s3Item = s3Client.getObject(new GetObjectRequest(bucket, filename));
            if (s3Item != null)
                return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return false;
    }

    public InputStream get(String filename) {
        try {
            var s3Client = this.newClient();
            var s3Item = s3Client.getObject(new GetObjectRequest(bucket, filename));
            if (s3Item == null)
                throw ExceptionBuilder.ofNotFound("Invalid bucket: %s", filename);
            return s3Item.getObjectContent();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public boolean put(InputStream inputStream, String filename) {
        var s3Client = this.newClient();
        var s3Metadata = new ObjectMetadata();
        try {
            s3Metadata.setContentLength(inputStream.available());
            s3Client.putObject(new PutObjectRequest(bucket, filename, inputStream, s3Metadata));
            return this.exists(filename);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public boolean put(Object o) {
        var tmpFile = IOUtil.target(IOUtil.createFileTemp()).toFile();
        var bodyBytes = ObjectUtil.toString(o);
        IOUtil
                .target(tmpFile)
                .writeAll(bodyBytes);
        try {
            var filename = ObjectUtil.toMd5(o);
            return this.put(new FileInputStream(tmpFile), filename);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    private AmazonS3 newClient() {

        if (PrimitiveUtil.isEmpty(this.url))
            this.url = S3Started.getS_3_CONFIG().getS3Url();

        if (PrimitiveUtil.isEmpty(this.region))
            this.region = S3Started.getS_3_CONFIG().getS3Region();

        if (PrimitiveUtil.isEmpty(this.bucket))
            this.bucket = S3Started.getS_3_CONFIG().getS3Bucket();

        if (PrimitiveUtil.isEmpty(this.clientId))
            this.clientId = S3Started.getS_3_CONFIG().getS3ClientId();

        if (PrimitiveUtil.isEmpty(this.clientSecret))
            this.clientSecret = S3Started.getS_3_CONFIG().getS3ClientSecret();

        if (PrimitiveUtil.isEmpty(clientId))
            throw ExceptionBuilder.ofFrameWork("Invalid clientId");

        if (PrimitiveUtil.isEmpty(clientSecret))
            throw ExceptionBuilder.ofFrameWork("Invalid clientSecret");

        if (PrimitiveUtil.isEmpty(bucket))
            throw ExceptionBuilder.ofFrameWork("Invalid bucket");

        var clientCredentials = new BasicAWSCredentials(clientId, clientSecret);
        var config = new ClientConfiguration();

        if (PrimitiveUtil.isEmpty(this.url))
            config.setProtocol(Protocol.HTTPS);
        else if (this.url.toLowerCase().startsWith("https:"))
            config.setProtocol(Protocol.HTTPS);
        else if (this.url.toLowerCase().startsWith("http:"))
            config.setProtocol(Protocol.HTTP);

        if (url.isEmpty()) {
            var s3client = AmazonS3ClientBuilder
                    .standard()
                    .withPathStyleAccessEnabled(true)
                    .withClientConfiguration(config)
                    .withCredentials(new AWSStaticCredentialsProvider(clientCredentials))
                    .withRegion(region)
                    .build();
            if (!s3client.doesBucketExistV2(bucket))
                throw ExceptionBuilder.ofNotFound(String.format("Bucket not found, name %s ", bucket));
            return s3client;
        }

        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(url, region))
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(config)
                .withCredentials(new AWSStaticCredentialsProvider(clientCredentials))
                .build();
    }
}