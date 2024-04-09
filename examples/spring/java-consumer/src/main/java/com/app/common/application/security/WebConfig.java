package com.app.common.application.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
//import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@RequiredArgsConstructor
//@EnableWebSecurity
public class WebConfig {
    @Value("${server.servlet.context-path}")
    private String server_servlet_context_path;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        WebUrlHelper.setContextPath(server_servlet_context_path);
//        http.authorizeHttpRequests((authorizeHttpRequests) ->
//                authorizeHttpRequests
//                        .requestMatchers(WebUrlHelper.getTrustedUrl()).permitAll()
//                        .anyRequest().denyAll()
//        );
////        http
////                .csrf(AbstractHttpConfigurer::disable)
////                .httpBasic(AbstractHttpConfigurer::disable);
//        return http.build();
//    }

}
