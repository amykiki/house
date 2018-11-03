package com.april.house.biz.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.UUID;

@Service
public class FileService {
    Logger logger = LogManager.getLogger(FileService.class);
    @Value("${file.path:}")
    private String filePath;
    @Value("${vsftpd.img.path:}")
    private String imgPath;

    @Value("${vsftpd.upload.enabled:false}")
    private Boolean vsftpdEnable;

    @Autowired
    private FTPService ftpService;

    // TODO: 2018/10/21
    public List<String> getImgPaths(ArrayList<MultipartFile> multipartFiles) {
        if (Strings.isNullOrEmpty(filePath)) {
            filePath = getResourcePath();
        }
        String parentDirName = Instant.now().getEpochSecond() + UUID.randomUUID().toString();

        List<String> paths = Lists.newArrayList();
        List<File> files = Lists.newArrayList();
        multipartFiles.forEach(file -> {
            File localFile;
            if (!file.isEmpty()) {
                try {
                    localFile = saveToLocal(file, filePath, parentDirName);
                    String path = StringUtils.substringAfterLast(localFile.getAbsolutePath(), filePath);
                    paths.add(path);
                    files.add(localFile);
                } catch (IOException e) {
                    logger.error("save file to local fail, filename: " + file.getOriginalFilename());
                }
            }
        });

        boolean result = false;
        if (vsftpdEnable) {
             result = ftpService.uploadFile(parentDirName, files, imgPath);
        }
        if (!result) {
            return null;
        }

        return paths;
    }

    private File saveToLocal(MultipartFile file, String filePath, String parentDirName) throws IOException {
        File newFile = new File(filePath + File.separator + parentDirName + File.separator + file.getOriginalFilename());

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
