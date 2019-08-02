package com.lego.survey.file.impl.controller;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.lego.survey.file.impl.model.OssUploadFile;
import com.lego.survey.file.impl.service.ICephOSSService;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.utils.UuidUtils;
import com.survey.lib.common.vo.RespDataVO;
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
import java.io.*;
import java.net.URL;
import java.util.*;

@Controller
@RequestMapping("/oss/v1")
@Api(tags = "对象存储")
public class CephOSSController {

    private static final Logger log = LoggerFactory.getLogger(CephOSSController.class);

    @Autowired
    private ICephOSSService cephOSSService;

    @ApiOperation(value = "创建Bucket", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucketName", value = "Bucket名称", dataType = "String", required = true, example = "1", paramType = "query"),
    })
    @ResponseBody
    @RequestMapping(value = "/createBucket", method = RequestMethod.GET)
    public RespVO createBucket(@RequestParam String bucketName) {

        return cephOSSService.createBucket(bucketName);
    }


    @ApiOperation(value = "获取BucketList", httpMethod = "GET")
    @ApiImplicitParams({

    })
    @ResponseBody
    @RequestMapping(value = "/findBucketList", method = RequestMethod.GET)
    public RespVO findBucketList() {

        return cephOSSService.findBucketList();
    }


    @ApiOperation(value = "获取文件对象列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucketName", value = "Bucket名称", dataType = "String", required = true, example = "1", paramType = "query"),
    })
    @ResponseBody
    @RequestMapping(value = "/getObjects", method = RequestMethod.GET)
    public RespVO<RespDataVO<S3ObjectSummary>> getObjects(@RequestParam String bucketName) {

        return cephOSSService.getObjects(bucketName);
    }


    @ApiOperation(value = "获取文件对象", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucketName", value = "Bucket名称", dataType = "String", required = true, example = "1", paramType = "query"),
            @ApiImplicitParam(name = "key", value = "key", dataType = "String", required = true, example = "1", paramType = "query"),
    })
    @RequestMapping(value = "/getObject", method = RequestMethod.GET)
    public void getObject(@RequestParam String bucketName, @RequestParam String key, HttpServletResponse res) throws
            IOException {
        InputStream in = cephOSSService.getObject(bucketName, key);
        OutputStream out = null;
        try {
            if (null != in) {
                out = res.getOutputStream();
                int len = 0;
                byte[] datas = new byte[2048];
                while ((len = in.read(datas)) > 0) {
                    out.write(datas, 0, len);
                }
            }
        } catch (IOException e) {

        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != in) {
                in.close();
            }
        }
    }


    @ApiOperation(value = "删除文件对象", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucketName", value = "Bucket名称", dataType = "String", required = true, example = "1", paramType = "query"),
            @ApiImplicitParam(name = "key", value = "key", dataType = "String", required = true, example = "1", paramType = "query"),
    })
    @ResponseBody
    @RequestMapping(value = "/deleteObject", method = RequestMethod.GET)
    public RespVO deleteObject(@RequestParam String bucketName, @RequestParam String key) {
        return cephOSSService.deleteObject(bucketName, key);
    }


    @ApiOperation(value = "保存文件对象", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucketName", value = "Bucket名称", dataType = "String", required = true, example = "1", paramType = "query"),
    })
    @ResponseBody
    @RequestMapping(value = "/putObject/{bucketName}", method = RequestMethod.POST)
    public RespVO putObject(@PathVariable String bucketName, HttpServletRequest req) {
        try {
            List<MultipartFile> fileList = new ArrayList<>();
            if (req instanceof MultipartHttpServletRequest) {
                fileList = ((MultipartHttpServletRequest) req).getFiles("file");
            }
            Map<String, Object> resultMap = new HashMap<>(16);
            List<Map<String, Object>> returnList = new ArrayList<>();
            for (MultipartFile file : fileList) {

                OssUploadFile uploadFile = new OssUploadFile();
                uploadFile.setFileName(file.getOriginalFilename());
                uploadFile.setStream(file.getInputStream());
                uploadFile.setKey(UuidUtils.generate16Uuid());

                if (!StringUtils.isEmpty(file.getOriginalFilename())) {
                    int pos = file.getOriginalFilename().lastIndexOf(".");
                    if (pos > -1 && pos + 1 < file.getOriginalFilename().length()) {
                        uploadFile.setExt(file.getOriginalFilename().substring(pos + 1));
                    }
                }

                RespVO<URL> respVO = cephOSSService.put(uploadFile);
                if (respVO.getRetCode() == 1) {
                    Map<String, Object> f = new HashMap<>(16);
                    f.put("fileName", file.getOriginalFilename());
                    f.put("url", respVO.getInfo());
                    returnList.add(f);
                }
            }
            resultMap.put("datas", returnList);
            if (!resultMap.isEmpty()) {
                return RespVOBuilder.success();
            }
        } catch (IOException e) {
            log.error("upload file error", e);
        }
        return RespVOBuilder.failure("参数错误");
    }
}
