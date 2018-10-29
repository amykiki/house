package com.april.house.biz.service;

import com.google.common.base.Splitter;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public class FTPService {
    private static final Logger logger = LogManager.getLogger(FTPService.class);

    @Value("${vsftpd.server:}")
    private String ftpServer;
    @Value("${vsftpd.user:}")
    private String ftpUser;
    @Value("${vsftpd.password:}")
    private String ftpPassword;
    @Value("${vsftpd.port:0}")
    private int ftpPort;

    private FTPClient ftpClient;



    public boolean uploadFile(String localFileDir, List<File> localFiles, String remotePath) {
        boolean uploaded = true;
        String uploadDir = remotePath + "/" + localFileDir;
        String currentFileName = "";

        if (connectServer()) {
            try {
                changeWorkingDirectory(uploadDir);
                for (File file : localFiles) {
                    currentFileName = file.getAbsolutePath();
                    storeFile(file);
                }

            } catch (Exception e) {
                logger.error("上传文件[{}]失败", currentFileName, e);
                uploaded = false;
            } finally {
                closeConnect();
            }

        }
        return uploaded;

    }

    private void storeFile(File file) throws Exception{
        if (file == null) {
            logger.error("上传的文件为空");
            throw new RuntimeException("上传的文件为空");
        }

        FileInputStream input = new FileInputStream(file);
        ftpClient.storeFile(file.getName(), input);
        input.close();
    }

    private void changeWorkingDirectory(String directory) {
        List<String> dirs = Splitter.on("/").trimResults().omitEmptyStrings().splitToList(directory);
        dirs.forEach(dir -> {
            try {
                if (ftpClient.changeWorkingDirectory(dir)) {
                    ftpClient.makeDirectory(dir);
                }
            } catch (IOException e) {
                logger.error("切换工作目录失败{}", dir, e);
                throw new RuntimeException("切换FTP工作目录失败");
            }
        });
    }

    private boolean connectServer() {
        logger.info("开始连接 {} FTP服务器", ftpServer);
        boolean isSuccess = false;
        if (ftpClient == null) {
            ftpClient = new FTPClient();
            int reply;
            try {
                ftpClient.connect(ftpServer, ftpPort);
                ftpClient.login(ftpUser, ftpPassword);
                //被动模式，每次数据连接之前，ftp client告诉ftp server开通一个端口来传输数据
                ftpClient.enterLocalPassiveMode();
                //二进制传输，防止乱码
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setBufferSize(1024);

                reply = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftpClient.disconnect();
                }
                isSuccess = true;

            } catch (IOException e) {
                logger.error("连接 {} FTP服务器异常", ftpServer, e);
            }
        }


        if (isSuccess) {
            logger.info("连接 {} FTP服务器成功", ftpServer);
        }
        return isSuccess;
    }

    private void closeConnect() {
        logger.info("关闭FTP服务器连接");
        if (ftpClient != null) {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                logger.error(e);
                throw new RuntimeException("关闭FTP连接发生异常!", e);
            }
        }
    }
}
