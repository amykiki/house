package com.april.house.biz.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    Logger logger = LogManager.getLogger(FileService.class);
    // TODO: 2018/10/21 获取filePath目录
    private String filePath;
    // TODO: 2018/10/21
    public List<String> getImgPaths(ArrayList<MultipartFile> multipartFiles) {
        if (Strings.isNullOrEmpty(filePath)) {
            filePath = getResourcePath();
        }

        List<String> paths = Lists.newArrayList();
        multipartFiles.forEach(file -> {
            File localFile = null;
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

    public static String getResourcePath() {
        File file = new File(".");
        String absolutePath = file.getAbsolutePath();
        return absolutePath;
    }
}
