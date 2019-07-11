package com.lego.survey.file.impl.service.impl;
import com.lego.survey.file.impl.service.IFpFileService;
import com.lego.survey.file.impl.util.FpFileUtil;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gaolei
 * @date 2019/5/30/0030
 */
@Service
public class FpFileServiceImpl implements IFpFileService {
    public static final Logger log = LoggerFactory.getLogger(FpFileServiceImpl.class);

    /**
     * 文件访问路径
     */
    @Value("${fpfile.url}")
    private String fileUrl;
    /**
     * 文件保存路径
     */
    @Value("${fpfile.path}")
    private String fpFileRootPath;


    @Override
    public RespVO<Map<String, Object>> upload(MultipartFile file) {
        String folder1 = FpFileUtil.getFolder();
        String folder2 = FpFileUtil.getFolder();
        String uuid = FpFileUtil.getUUID();
        String fileName = file.getOriginalFilename();
        Map<String, Object> map = new HashMap<>();
        map.put("data", FpFileUtil.getFileUrl(fileUrl, folder1, folder2, uuid + "?name=" + fileName));
        map.put("fileName", fileName);
        File tempFile = new File(FpFileUtil.getFileUrl(fpFileRootPath, folder1, folder2, uuid));
        try {
            if (!tempFile.getParentFile().exists()) {
                tempFile.getParentFile().mkdirs();//创建父级文件路径
                tempFile.createNewFile();//创建文件
            }
            // 转存文件
            file.transferTo(tempFile);
        } catch (Exception e) {
            log.error("upload file to  system error", e);
            return RespVOBuilder.failure(RespConsts.FAIL_RESULT_CODE, "上传失败");
        }
        return RespVOBuilder.success(map);
    }


    @Override
    public byte[] download(String groupName, String remoteFile) {
        return new byte[0];
    }

}
