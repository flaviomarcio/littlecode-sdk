package com.littlecode.web.core.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.littlecode.config.UtilCoreConfig;
import com.littlecode.parsers.ExceptionBuilder;
import com.littlecode.parsers.PrimitiveUtil;
import com.littlecode.web.core.RequestToken;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Getter
public class JwtParserUtil implements Serializable {

    private final String value;

    public JwtParserUtil(String value) {
        super();
        this.value = value;
    }

    public static RequestToken parser(String tokenBytes) {
        return new JwtParserUtil(tokenBytes).getToken();
    }

    public RequestToken getToken() {
        DecodedJWT decodedJWT = decodeToken(value);
        var payloadAsJson = decodeTokenPayloadToJsonObject(decodedJWT);
        Map<String, String> payload = new HashMap<>();
        for (Map.Entry<String, Object> entry : payloadAsJson.entrySet()) {
            String key = entry.getKey();
            String v = entry.getValue().toString();
            payload.put(key, v);
        }
        return RequestToken.builder()
                .iss(payloadAsJson.get("iss").toString())
                .sub(payloadAsJson.get("sub").toString())
                .exp(PrimitiveUtil.toLong(payloadAsJson.get("exp")))
                .iat(PrimitiveUtil.toLong(payloadAsJson.get("iat")))
                .token(value)
                .payload(payload)
                .build();

    }

    @SuppressWarnings("unused")
    public Collection<? extends GrantedAuthority> getAuthorities() {
        DecodedJWT decodedJWT = decodeToken(value);
        var payloadAsJson = decodeTokenPayloadToJsonObject(decodedJWT);

        var realmAccess = (Map) payloadAsJson.get("realm_access");
        var roles = (List) realmAccess.get("roles");

        Collection<SimpleGrantedAuthority> __return = new Stack<>();
        var objectMapper = UtilCoreConfig.newObjectMapper();
        for (var role : roles) {
            var granted = objectMapper.convertValue(role, SimpleGrantedAuthority.class);
            if (granted != null) {
                __return.add(granted);
            }
        }

        return __return;

//        return StreamSupport
//                .stream(payloadAsJson.get("realm_access").getAsJsonArray("roles").spliterator(), false)
//                .map(JsonElement::getAsString).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    private DecodedJWT decodeToken(String value) {
        if (value == null || value.trim().isEmpty())
            throw ExceptionBuilder.ofFrameWork("Token has not been provided");
        return JWT.decode(value);
    }

    private Map<String, Object> decodeTokenPayloadToJsonObject(DecodedJWT decodedJWT) {
        try {
            String payloadAsString = decodedJWT.getPayload();
            var objectMapper = UtilCoreConfig.newObjectMapper();
            return objectMapper.readValue(new String(Base64.getDecoder().decode(payloadAsString), StandardCharsets.UTF_8), Map.class);
        } catch (JsonProcessingException exception) {
            throw ExceptionBuilder.ofFrameWork("Invalid JWT or JSON format of each of the jwt parts", exception);
        }
    }

}