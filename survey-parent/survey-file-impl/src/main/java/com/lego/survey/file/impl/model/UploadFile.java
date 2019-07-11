package com.lego.survey.file.impl.model;

import java.io.Serializable;

public class UploadFile implements Serializable {


    private static final long serialVersionUID = -3415192779423592605L;

    private String fileName;

    private byte[] content;

    private String ext;

    private String url;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {

        this.fileName = fileName;
    }

    public byte[] getContent() {

        return content;
    }

    public void setContent(byte[] content) {

        this.content = content;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
