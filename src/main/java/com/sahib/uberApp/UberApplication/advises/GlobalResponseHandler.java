package com.sahib.uberApp.UberApplication.advises;

import com.sahib.uberApp.UberApplication.annotations.Skip;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

        if (returnType.getDeclaringClass().isAnnotationPresent(Skip.class)){
            return false;
        }

        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType, Class<? extends HttpMessageConverter<?>>
                                              selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        List<String> urlList = List.of("/v3/api-docs" , "/swagger-ui/index.html");
        String path = request.getURI().getPath();
        boolean anyMatch = urlList.stream() .anyMatch(path::startsWith);

        if (body instanceof ApiResponse<?> || anyMatch){
            return body;
        }
        return new ApiResponse<>(body);
    }
}
