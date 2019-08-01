package com.lego.survey.config;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author yanglf
 * @description
 * @since 2019/8/1
 **/
public class NetworkInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        return chain.proceed(request);
    }
}
