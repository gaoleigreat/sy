package com.lego.survey.file.impl.service;
import com.survey.lib.common.vo.RespVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @author gaolei
 * @date 2019/5/30/
 */
public interface IFpFileService {
    /**
     * 上传
     *
     * @param file
     * @return
     */
    RespVO upload(MultipartFile file) throws IOException;

    /**
     * 下载
     *
     * @param remoteFile
     * @param groupName
     * @return
     */
    byte[] download(String groupName, String remoteFile);
}
