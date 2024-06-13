package com.littlecode.web.flux;

import com.littlecode.web.core.helper.UrlHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class RequestFilterConfig {
    private final Environment environment;

    public RequestFilterConfig(Environment environment) {
        this.environment = environment;
        this.urlConfigure();
    }

    @Bean
    public RequestFilter tokenAuthenticationFilter() {
        return new RequestFilter();
    }

    private void urlConfigure() {
        var contextPath = environment.getProperty("server.servlet.context-path", "").trim();
        if (contextPath.equals("/"))
            contextPath = "";
        UrlHelper.setContextPath(contextPath);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        http
                .securityMatcher("/v1/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeRequests().anyRequest().authenticated();

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

