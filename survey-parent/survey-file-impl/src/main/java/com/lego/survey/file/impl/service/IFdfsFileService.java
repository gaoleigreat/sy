package com.lego.survey.file.impl.service;
import com.lego.survey.file.impl.model.UploadFile;
import com.survey.lib.common.vo.RespVO;

import java.util.Map;

public interface IFdfsFileService {

    /**
     * 上传
     * @param file
     * @return
     */
    RespVO upload(UploadFile file);

    /**
     * 下载
     * @param remoteFile
     * @param groupName
     * @return
     */
    byte[] download(String groupName, String remoteFile);
}
