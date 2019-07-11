package com.lego.survey.file.impl.model;

import java.io.InputStream;
import java.io.Serializable;

public class OssUploadFile implements Serializable {

    private static final long serialVersionUID = 3776351662079029609L;

    private String bucketName;

    private String fileName;

    private String key;

    private InputStream stream;

    private String ext;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}
