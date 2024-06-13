package com.littlecode.web.flux;

import com.littlecode.util.BeanUtil;
import com.littlecode.web.core.RequestContext;
import com.littlecode.web.core.RequestContextHolder;
import com.littlecode.web.core.helper.UrlHelper;
import com.littlecode.web.core.interfaces.RequestAuthChecker;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NotNull @Nullable HttpServletRequest request, @NotNull @Nullable HttpServletResponse response, @NotNull @Nullable FilterChain filterChain) throws ServletException, IOException {
        if (request == null || response == null) {
            return;
        }
        var contextPath = request.getRequestURI().toLowerCase();

        if (!UrlHelper.isTrustedOpenUrl(contextPath)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Map<String,String> headers=new HashMap<>();
        {
            var it=request.getHeaderNames().asIterator();
            while(it.hasNext()) {
                it.next();
                headers.put(it.next(), request.getHeader(it.next()));
            }
        }

        var requestContext= RequestContext
                .builder()
                .contextPath(contextPath)
                .method(request.getMethod().toUpperCase())
                .headers(headers)
                .paramaters(new HashMap<>())
                .build();

        RequestContextHolder.setConstRequestContext(requestContext);

        var requestAuthChecker=(new BeanUtil()).as(RequestAuthChecker.class);
        if(requestAuthChecker==null){
            filterChain.doFilter(request, response);
            return;
        }

        if(!requestAuthChecker.check(requestContext)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

}