package com.april.house.biz.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    Logger logger = LogManager.getLogger(FileService.class);
    @Value("${file.path:}")
    private String filePath;
    @Value("${vsftpd.img.path:")
    private String imgPath;

    @Value("${vsftpd.upload.enabled:false}")
    private Boolean vsftpdEnable;

    // TODO: 2018/10/21
    public List<String> getImgPaths(ArrayList<MultipartFile> multipartFiles) {
        if (Strings.isNullOrEmpty(filePath)) {
            filePath = getResourcePath();
        }

        List<String> paths = Lists.newArrayList();
        multipartFiles.forEach(file -> {
            File localFile;
            if (!file.isEmpty()) {
                try {
                    localFile = saveToLocal(file, filePath);
                    String path = StringUtils.substringAfterLast(localFile.getAbsolutePath(), filePath);
                    paths.add(path);
                } catch (IOException e) {
                    logger.error("save file to local fail, filename: " + file.getOriginalFilename());
                }
            }
        });

        if (vsftpdEnable) {

        }

        return paths;
    }

    private File saveToLocal(MultipartFile file, String filePath) throws IOException {
        File newFile = new File(filePath + "/" + Instant.now().getEpochSecond() + "/" + file.getOriginalFilename());
        if (!newFile.exists()) {
            newFile.getParentFile().mkdirs();
            newFile.createNewFile();
        }

        Files.write(file.getBytes(), newFile);
        return newFile;
    }

    public String getResourcePath() {
        try {
            File file = new File(ResourceUtils.getURL("classpath:").getPath());
            //如果文件夹不存在则创建
            if (!file.exists()) {
                file.mkdir();
            }
            String absolutePath = file.getAbsolutePath();
            logger.info("classpath absolute path is {}", absolutePath);
            File upload = new File(absolutePath, "static/images/upload");
            if (!upload.exists()) {
                upload.mkdirs();
            }
            logger.info("upload path is {}", upload.getAbsolutePath());
            return upload.getAbsolutePath();
        } catch (FileNotFoundException e) {
            logger.error("获取根目录失败",e);
        }
        return "";

    }
}
