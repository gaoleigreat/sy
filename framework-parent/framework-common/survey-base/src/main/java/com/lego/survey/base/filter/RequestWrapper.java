package com.lego.survey.base.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * @author yanglf
 * @description
 * @since 2019/1/25
 **/
public class RequestWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = readBytes(request.getReader(), "utf-8");
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), "utf-8"));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bis = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {
            }

            @Override
            public int read() throws IOException {
                return bis.read();
            }
        };
    }


    private byte[] readBytes(BufferedReader br, String encoding) throws IOException {
        String str;
        StringBuilder sb = new StringBuilder();
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        return sb.toString().getBytes(Charset.forName(encoding));
    }

}
