package com.lego.survey.file.impl.service.impl;
import com.lego.survey.file.impl.model.UploadFile;
import com.lego.survey.file.impl.service.IFdfsFileService;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.csource.common.MyException;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerGroup;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FdfsFileServiceImpl implements IFdfsFileService {

    public static final Logger log = LoggerFactory.getLogger(FdfsFileServiceImpl.class);

    @Autowired
    private TrackerGroup trackerGroup;

    @Value("${file.service.url}")
    private String fileServiceUrl;

    @Override
    public RespVO<Map<String, Object>> upload(UploadFile file) {
        Map<String, Object> map = new HashMap<>(16);
        StorageClient storageClient;
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerGroup.getConnection();
            storageClient = new StorageClient(trackerServer, null);

            String[] ary = storageClient.upload_file(file.getContent(), file.getExt(), null);
            String groupName = ary[0];
            String remoteName = ary[1];
            map.put("data", fileServiceUrl + groupName+ "/"+ remoteName+"?fileName="+ file.getFileName());
        } catch (IOException e) {
            log.error("upload file to [fastdfs] system error",e);
            return RespVOBuilder.failure(RespConsts.FAIL_RESULT_CODE,"上传失败");
        } catch (MyException e) {
            log.error("upload file to [fastdfs] system error",e);
            return RespVOBuilder.failure(RespConsts.FAIL_RESULT_CODE,"上传失败");
        } finally {
            if(null != trackerServer){
                try {
                    trackerServer.close();
                } catch (IOException e) {
                    log.error("close tracker server error",e);
                }
            }
        }
        return RespVOBuilder.success(map);
    }

    @Override
    public byte[] download(String groupName, String remoteFile) {
        StorageClient storageClient = null;
        TrackerServer trackerServer = null;
        byte[] datas = null;
        try {
            trackerServer = trackerGroup.getConnection();
            storageClient = new StorageClient(trackerServer, null);
            datas = storageClient.download_file(groupName, remoteFile);
        } catch (IOException e) {
            log.error("download file from [fastdfs] system error",e);
            return datas;
        } catch (MyException e) {
            log.error("download file from [fastdfs] system error",e);
            return datas;
        } finally {
            if(null != trackerServer){
                try {
                    trackerServer.close();
                } catch (IOException e) {
                    log.error("close tracker server error",e);
                }
            }
        }
        return datas;
    }

}
