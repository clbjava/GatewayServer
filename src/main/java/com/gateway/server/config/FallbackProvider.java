package com.gateway.server.config;

import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@Component
public class FallbackProvider implements ZuulFallbackProvider {
    @Override
    public String getRoute() {
        // 表明是为所有微服务提供回退
        //return "service-provider";
    	return null;
    }

    @Override
    public ClientHttpResponse fallbackResponse() {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                // fallback时的状态码
                return HttpStatus.BAD_GATEWAY;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                // 数字类型的状态码，本例返回的其实就是200，详见HttpStatus
                return this.getStatusCode().value();
            }

            @Override
            public String getStatusText() throws IOException {
                // 状态文本，本例返回的其实就是OK，详见HttpStatus
                return this.getStatusCode().getReasonPhrase();
            }

            @Override
            public void close() {}

            @Override
            public InputStream getBody() throws IOException {
                // 响应体
                return new ByteArrayInputStream("用户微服务不可用，请稍后再试。".getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                // headers设定
                HttpHeaders headers = new HttpHeaders();
                MediaType mt = new MediaType("application", "json", Charset.forName("UTF-8"));
                headers.setContentType(mt);

                return headers;
            }
        };
    }
}
