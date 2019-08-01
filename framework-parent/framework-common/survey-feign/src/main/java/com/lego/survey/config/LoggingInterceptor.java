package com.lego.survey.config;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;

/**
 * @author yanglf
 * @description
 * @since 2019/8/1
 **/
@Slf4j
public class LoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long startTime = System.nanoTime();
        log.debug(String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);
        long entTime = System.nanoTime();
        log.debug(String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (entTime - startTime) / 1e6d, response.headers()));

        MediaType mediaType = response.body().contentType();
        String content = response.body().string();

        log.debug(content);

        response = response.newBuilder()
                .body(ResponseBody.create(mediaType, content))
                .build();

        return response;
    }
}
