package com.mymall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FTPUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPass = PropertiesUtil.getProperty("ftp.pass");

    private static String remotePath = "img";

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;


    public FTPUtil(String ip, int port, String user, String pwd){
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    /**
     * 将文件上传到ftp服务器
     * @param fileList 文件流集合
     * @return true/false
     * @throws IOException 上传文件io异常
     */
    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPass);

        logger.info("········开始连接ftp服务器·········");

        boolean result = ftpUtil.uploadFile("img", fileList);

        logger.info("········结束上传，上传结果：{}·········");

        return result;

    }


    /**
     * 封装上传到远程ftp服务器的功能
     * @param remotePath 远程服务器上的存储目录
     * @param fileList 文件流集合
     * @return true/false
     */
    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fileInputStream = null;

        //连接ftp服务器
        if (connectServer(this.ip, this.port, this.user, this.pwd)){

            try {
                ftpClient.changeWorkingDirectory(remotePath);   //（是否需要）更改存储目录
                ftpClient.setBufferSize(1024);  //设置缓冲区
                ftpClient.setControlEncoding("UTF-8");  //设置编码
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  //设置存储格式
                ftpClient.enterLocalPassiveMode();  //打开本地被动模式

                //批量上传
                for (File fileItem : fileList){
                    fileInputStream = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(), fileInputStream);
                }

            } catch (IOException e) {
                logger.error("上传文件异常!", e);
                uploaded = false;
                e.printStackTrace();
            } finally {
                fileInputStream.close();
                ftpClient.disconnect();
            }
        }

        return uploaded;
    }

    /**
     * 连接FTP服务器
     * @param ip 服务器ip
     * @param port 端口
     * @param user 用户名
     * @param pwd 密码
     * @return 成功检测 true/false
     */
    private boolean connectServer(String ip, int port, String user, String pwd){
        boolean isSuccess = false;
        ftpClient = new FTPClient();

        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user, pwd);
        } catch (IOException e) {
            logger.error("连接ftp服务器异常", e);
        }

        return isSuccess;

    }



    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
