package com.lego.survey.file.impl.service.impl;
import com.lego.survey.base.utils.FpFileUtil;
import com.lego.survey.file.impl.service.IFpFileService;
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
        String uuid = FpFileUtil.getUUID();
        String fileName = file.getOriginalFilename();
        String type=fileName.substring(fileName.indexOf("."));
        Map<String, Object> map = new HashMap<>(16);
        String newFileName = uuid + type;
        map.put("data", FpFileUtil.getFileUrl(fileUrl, null, null, newFileName));
        map.put("fileName", newFileName);
        File tempFile = new File(FpFileUtil.getFileUrl(fpFileRootPath, null, null, newFileName));
        try {
            if (!tempFile.getParentFile().exists()) {
                tempFile.getParentFile().mkdirs();//创建父级文件路径
            }
            tempFile.createNewFile();//创建文件
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
