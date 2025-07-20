package com.caoximu.bookmanger.config;

import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class GlobalCorsFilter extends OncePerRequestFilter implements Ordered {

    /**
     * 这里为支持的请求头，如果有自定义的header字段请自己添加
     */
    private static final String ALLOWED_HEADERS =
            "X-Requested-With, Content-Language, Accept-Language, Content-Type, " +
                    "Authorization, clientid, credential, X-XSRF-TOKEN, " +
                    "X-Nonce, X-Timestamp, X-Signature, App-Token, Encrypt-Key, isEncrypt";

    private static final String ALLOWED_METHODS = "GET,POST,PUT,DELETE,OPTIONS,HEAD";
    private static final String ALLOWED_ORIGIN = "*";
    private static final String ALLOWED_EXPOSE = "*";
    private static final String MAX_AGE = "18000L";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isCorsRequest(request)) {
            response.setHeader("Access-Control-Allow-Headers", ALLOWED_HEADERS);
            response.setHeader("Access-Control-Allow-Methods", ALLOWED_METHODS);
            response.setHeader("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
            response.setHeader("Access-Control-Expose-Headers", ALLOWED_EXPOSE);
            response.setHeader("Access-Control-Max-Age", MAX_AGE);
            response.setHeader("Access-Control-Allow-Credentials", "true");

            if (HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
                response.setStatus(HttpStatus.OK.value());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isCorsRequest(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.ORIGIN) != null;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}