package com.lego.survey.file.impl.controller;

import com.alibaba.fastjson.JSONObject;
import com.lego.survey.base.utils.FpFileUtil;
import com.lego.survey.file.impl.service.IFpFileService;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaolei
 * @date 2019/5/30/0030
 */
@RestController
@Api(value = "FpFileController", description = "fp文件存储")
@RequestMapping("/file")
public class FpFileController {

    private static final Logger log = LoggerFactory.getLogger(FpFileController.class);
    @Autowired
    private IFpFileService fpFileService;

    @Value("${fpfile.path}")
    private String fpFileRootPath;


    @ApiOperation(value = "文件上传", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/fpfile/upload", method = RequestMethod.POST)
    public RespVO fpFileUpload(HttpServletRequest req) {
        List<MultipartFile> fileList = new ArrayList<>();
        if (req instanceof MultipartHttpServletRequest) {
            fileList = ((MultipartHttpServletRequest) req).getFiles("file");
        }
        Map<String, Object> resultMap = new HashMap<>(16);
        try {
            List<Map<String, Object>> returnList = new ArrayList<>();
            for (MultipartFile file : fileList) {
                if (StringUtils.isEmpty(file.getOriginalFilename())) {
                    return RespVOBuilder.failure("参数错误");
                }
                RespVO<Map<String, Object>> upload = fpFileService.upload(file);
                if (upload.getRetCode() == 1) {

                    Map result = new HashMap(16);
                    result.put("fileName", upload.getInfo().get("fileName"));
                    result.put("url", upload.getInfo().get("data"));
                    returnList.add(result);
                }
            }
            resultMap.put("datas", returnList);
        } catch (IOException e) {
            log.error("upload file error", e);
            return RespVOBuilder.failure("上传失败");
        }
        if (!resultMap.isEmpty()) {
            log.info(JSONObject.toJSONString(resultMap));
            return RespVOBuilder.success(resultMap);
        }
        return RespVOBuilder.failure("参数错误");
    }


    @ApiOperation(value = "文件下载", httpMethod = "GET")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/fpfile/download/{fileName}", method = RequestMethod.GET)
    public void downloadFile(HttpServletRequest request, HttpServletResponse response,
                             @PathVariable String fileName) throws UnsupportedEncodingException {
        if (null == fileName) {
            log.error("download file error,filname is null");
        }
        String filePath = FpFileUtil.getFilePath(fpFileRootPath, fileName);
        File file = new File(filePath);
        if (file.exists()) {
            // 设置强制下载不打开
            response.setContentType("application/force-download");
            // 设置文件名
            response.addHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                log.error("文件下载失败 {}", e.getMessage());
            } finally {
                try {
                    bis.close();
                    fis.close();
                } catch (IOException e) {
                    log.error("文件下载失败 {}", e.getMessage());
                }
            }
        }
    }
}