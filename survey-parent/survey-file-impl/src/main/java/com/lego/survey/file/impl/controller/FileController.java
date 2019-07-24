package com.lego.survey.file.impl.controller;
import com.lego.survey.file.impl.model.UploadFile;
import com.lego.survey.file.impl.service.IFdfsFileService;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value="FileController",description = "fastdfs文件上传")
@RequestMapping("/file/v1")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private IFdfsFileService fdfsFileService;

    @ApiOperation(value = "app文件上传", httpMethod = "POST")
    @ApiImplicitParams({
    })
    @RequestMapping(value="/app/upload", method = RequestMethod.POST)
    public RespVO appUpload(@RequestBody UploadFile uploadFile){
        return fdfsFileService.upload(uploadFile);
    }


    @ApiOperation(value = "web文件上传", httpMethod = "POST")
    @ApiImplicitParams({
    })
    @RequestMapping(value="/web/upload", method = RequestMethod.POST)
    public RespVO webUpload(HttpServletRequest req){
        List<MultipartFile> fileList = new ArrayList<>();
        if(req instanceof MultipartHttpServletRequest){
            fileList = ((MultipartHttpServletRequest) req).getFiles("file");
        }
        Map<String,Object> resultMap  = new HashMap<>();
        try {
            List<Map<String, Object>> returnList = new ArrayList<>();
            for(MultipartFile file : fileList){
                UploadFile uploadFile = new UploadFile();
                uploadFile.setFileName(file.getOriginalFilename());

                    uploadFile.setContent(file.getBytes());
                if(!StringUtils.isEmpty(file.getOriginalFilename())){
                    int pos = file.getOriginalFilename().lastIndexOf(".");
                    if(pos > -1 && pos + 1 < file.getOriginalFilename().length()){
                        uploadFile.setExt(file.getOriginalFilename().substring(pos + 1));
                    }
                }
                RespVO<Map<String, Object>> upload = fdfsFileService.upload(uploadFile);
                if(1==upload.getRetCode()){
                    Map f = new HashMap();
                    f.put("fileName", file.getOriginalFilename());
                    f.put("url", upload.getInfo().get("data"));
                    returnList.add(f);
                }
            }
            resultMap.put("datas",returnList);
        } catch (IOException e) {
            log.error("upload file error", e);
        }
        if(!resultMap.isEmpty()){
            return RespVOBuilder.success(resultMap);
        }
        return RespVOBuilder.failure("参数错误");
    }


    @ApiOperation(value = "app文件下载", httpMethod = "GET")
    @ApiImplicitParams({
    })
    @RequestMapping(value="/download/{groupName}/**", method = RequestMethod.GET)
    public void appUpload(HttpServletRequest req, HttpServletResponse res, @PathVariable String groupName, @RequestParam(required = false) String fileName){
        try {
            String uri = req.getRequestURI().toString();
            int position = uri.indexOf(groupName);
            position = position + groupName.length()+1;
            String remoteName = "";
            if(position > -1 && position < uri.length()){
                remoteName = uri.substring(position);
            }
            res.setContentType("multipart/form-data");
            if(!StringUtils.isEmpty(fileName)){
                res.setHeader("Content-Disposition", "attachment;fileName="+URLEncoder.encode(fileName,"utf-8"));
            }else{
                res.setHeader("Content-Disposition", "attachment;fileName=");
            }
            byte[] datas = fdfsFileService.download(groupName, remoteName);
            res.getOutputStream().write(datas);
        } catch (UnsupportedEncodingException e) {

            log.error("download file error",e);
        } catch (IOException e) {

            log.error("download file error",e);
        }
    }
}
