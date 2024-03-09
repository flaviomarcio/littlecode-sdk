package com.littlecode.cloud.s3.started;

import com.littlecode.cloud.s3.privates.S3Config;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class S3Started {
    @Getter
    private static S3Config S_3_CONFIG;

    public S3Started(S3Config s3Config) {
        S_3_CONFIG = s3Config;
    }
}
