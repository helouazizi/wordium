package com.wordium.posts.clients;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component 
public class InternalServiceInterceptor implements RequestInterceptor {

    @Value("${internal.service.token}")
    private String internalToken;

    @Override
    public void apply(RequestTemplate template) {
        template.header("Internal-Service-Token", internalToken);

        ServletRequestAttributes attributes = 
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            String userId = attributes.getRequest().getHeader("User-Id");
            if (userId != null) {
                template.header("User-Id", userId);
            }
            String userRole = attributes.getRequest().getHeader("User-Role");
            if (userRole != null) {
                template.header("User-Role", userRole);
            }
        }
    }
}