package com.lego.survey.file.impl.service.impl;

import com.amazonaws.services.s3.model.*;
import com.lego.survey.file.impl.init.CephInit;
import com.lego.survey.file.impl.model.OssUploadFile;
import com.lego.survey.file.impl.service.ICephOSSService;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CephOSSServiceImpl implements ICephOSSService {

    @Autowired
    private CephInit cephInit;

    @Override
    public RespVO createBucket(String bucketName){

        Bucket bucket = cephInit.getAmazonS3Client().createBucket(bucketName);

        return RespVOBuilder.success();
    }

    @Override
    public Boolean isBucketExists(String bucketName) {
        return null;
    }

    @Override
    public RespVO findBucketList(){

        List<Map<String, String>> lst = new ArrayList<>();

        List<Bucket> buckets = cephInit.getAmazonS3Client().listBuckets();

        for(Bucket bucket : buckets){
            Map<String, String> map = new HashMap<>(16);
            map.put(bucket.getName(),bucket.toString());
            lst.add(map);
        }

        return RespVOBuilder.success(lst);
    }

    @Override
    public RespVO<RespDataVO<S3ObjectSummary>> getObjects(String buckName){

        List<S3ObjectSummary> list = new ArrayList<>();

        ObjectListing objectListing = cephInit.getAmazonS3Client().listObjects(buckName);
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        for(S3ObjectSummary objectSummary : objectSummaries){
            list.add(objectSummary);
        }

        return RespVOBuilder.success(list);
    }

    @Override
    public InputStream getObject(String bucketName , String key){
        S3Object s3Object = cephInit.getAmazonS3Client().getObject(bucketName, key);

        return s3Object.getObjectContent();
    }

    @Override
    public RespVO deleteObject(String bucketName, String key) {
        try{
            cephInit.getAmazonS3Client().deleteObject(bucketName, key);
        }catch (Exception e){

            return RespVOBuilder.success(e.getMessage());
        }
        return RespVOBuilder.success();
    }

    @Override
    public RespVO<URL> put(OssUploadFile uploadFile){
        ObjectMetadata objectMetadata = new ObjectMetadata();
        PutObjectRequest request = new PutObjectRequest(uploadFile.getBucketName(), uploadFile.getKey(), uploadFile.getStream(), objectMetadata);
        request.withCannedAcl(CannedAccessControlList.PublicRead);
        PutObjectResult result = cephInit.getAmazonS3Client().putObject(request);
        URL url = cephInit.getAmazonS3Client().getUrl(uploadFile.getBucketName(), uploadFile.getKey());
        return RespVOBuilder.success(url);
    }
}
