package com.lego.survey.file.impl.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Random;
import java.util.UUID;

/**
 * @author gaolei
 * @date 2019/5/30/0030
 */

public class FpFileUtil {
    public static final Logger log = LoggerFactory.getLogger(FpFileUtil.class);

    private static FpFileUtil instance;

    private FpFileUtil() {

    }

    public static synchronized FpFileUtil getInstance() {
        if (instance == null) {
            instance = new FpFileUtil();
        }
        return instance;
    }

    /**
     * 生成任意文件夾（由数字组成，在0-255之间)
     *
     * @return
     */

    public static String getFolder() {
        Random random = new Random();
        String folderName = new String() + random.nextInt(256);
        return folderName;
    }

    /**
     * 生成存儲文件url
     *
     * @param routing
     * @param folder1
     * @param folder2
     * @param filaname
     * @return
     */
    public static String getFileUrl(String routing, String folder1, String folder2, String filaname) {
        String url = new StringBuffer(routing)
                .append(File.separator)
                .append(folder1)
                .append(File.separator)
                .append(folder2)
                .append(File.separator)
                .append(filaname).toString();
        return url;
    }

    /**
     * 活的文件在服务器存储路径
     *
     * @param routPath
     * @param folder1
     * @param folder2
     * @return
     */
    public static String getFilePath(String routPath, String folder1, String folder2) {
        String filePath = new StringBuffer(routPath)
                .append(File.separator)
                .append(folder1)
                .append(File.separator)
                .append(folder2)
                .append(File.separator).toString();
        return filePath;
    }

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        // 去掉"-"符号
        String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
        return temp;
    }

}
