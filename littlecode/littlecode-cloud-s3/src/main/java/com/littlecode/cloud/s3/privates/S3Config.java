package com.littlecode.cloud.s3.privates;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class S3Config {
    @Value("${little-code.cloud.s3.url:}")
    private String s3Url;

    @Value("${little-code.cloud.s3.region:}")
    private String s3Region;

    @Value("${little-code.cloud.s3.bucket:}")
    private String s3Bucket;

    @Value("${little-code.cloud.s3.client.id:}")
    private String S3ClientId;

    @Value("${little-code.cloud.s3.client.secret:}")
    private String S3ClientSecret;
}
