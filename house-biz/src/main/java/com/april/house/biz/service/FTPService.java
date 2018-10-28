package com.april.house.biz.service;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
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
    @Value("${vsftpd.port:}")
    private int ftpPort;

    private FTPClient ftpClient;








    public boolean uploadFile(String localFileDir, String remotePath, List<String> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;

        if (connectServer()) {
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                //二进制传输，防止乱码
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //服务器端设置了被动模式
                ftpClient.enterLocalPassiveMode();
                for (String file : fileList) {
                    fis = new FileInputStream(file);
                    fis.close();
                }
            } catch (IOException e) {
                logger.error("上传FTP文件异常", e);
                uploaded = false;
            } finally {
                ftpClient.disconnect();
            }
        }
        return uploaded;

    }

    private boolean connectServer() {
        logger.info("开始连接 {} FTP服务器", ftpServer);
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftpServer);
            isSuccess = ftpClient.login(ftpUser, ftpPassword);
        } catch (IOException e) {
            logger.error("连接 {} FTP服务器异常", ftpServer, e);
        }
        if (isSuccess) {
            logger.info("连接 {} FTP服务器成功", ftpServer);
        }
        return isSuccess;
    }
}
