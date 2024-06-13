package com.littlecode.web.core;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Builder
@Getter
@Setter
public class RequestToken {
    private String iss;
    private String sub;
    private Long exp;
    private Long iat;
    private String token;
    private Map<String, String> payload;

    public boolean isExpired() {
        var tokenDt = LocalDateTime.ofEpochSecond(this.exp, 0, ZoneOffset.MAX);
        return tokenDt.isBefore(LocalDateTime.now());
    }

}
