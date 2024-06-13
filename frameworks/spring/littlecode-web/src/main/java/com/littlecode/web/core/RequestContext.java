package com.littlecode.web.core;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class RequestContext {
    private String method;
    private String contextPath;
    private Map<String,String> paramaters;
    private Map<String,String> headers;
}
