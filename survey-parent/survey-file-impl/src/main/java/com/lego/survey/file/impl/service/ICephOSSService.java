package com.lego.survey.file.impl.service;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.lego.survey.file.impl.model.OssUploadFile;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * ceph对象存储
 * weihao 2019-03-05
 */
public interface ICephOSSService {

    /**
     * 创建 存储空间
     * @param bucketName
     * @return
     */
    RespVO createBucket(String bucketName);

    /**
     * 存储空间是否存在
     * @param bucketName
     * @return
     */
    Boolean isBucketExists(String bucketName);

    /**
     * 查询存储空间列表
     * @return
     */
    RespVO findBucketList();

    /**
     * 查询存储空间下 对象列表
     * @param bucketName
     * @return
     */
    RespVO<RespDataVO<S3ObjectSummary>> getObjects(String bucketName);

    /**
     * 查询对象
     * @param bucketName
     * @param key
     * @return
     */
    InputStream getObject(String bucketName, String key);

    /**
     * 删除对象
     * @param bucketName
     * @param key
     * @return
     */
    RespVO deleteObject(String bucketName, String key);

    /**
     * 上传对象
     * @param uploadFile
     * @return
     */
    RespVO<URL> put(OssUploadFile uploadFile);
}
