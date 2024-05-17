package com.littlecode.network.clients;

import com.littlecode.network.RequestUtil;

import java.net.http.HttpClient;

public interface RequestClient {
    void call(RequestUtil rqUtil);
    HttpClient createHttpClient(RequestUtil rqUtil);
}
