package com.mymall.service.impl;

import com.google.common.collect.Lists;
import com.mymall.service.FileService;
import com.mymall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("fileService")
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String upload(MultipartFile file, String path){
        String fileName = file.getOriginalFilename();

        //获取扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);

        //制定文件名
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        logger.info("开始上传文件，上传文件的名字：{}，上传的路径：{}，新文件名：{}", fileName, path, uploadFileName);

        //通过传递下来的path参数创建路径，这里先把文件集中到tomcat下
        File fileDir = new File(path);
        if (!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        //声明一个文件流
        File targetFile = new File(path, uploadFileName);

        //将该文件上传
        try {
            file.transferTo(targetFile);

            //将targetFile上传到ftp服务器
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));

            //上传成功后，删除upload里的文件
            targetFile.delete();

        } catch (IOException e) {
            logger.error("上传失败", e);
            return null;
        }




        return targetFile.getName();
    }

//    public static void main(String[] args) {
//        String fileName = "abc.jpg";
//        System.out.println(fileName.substring(fileName.lastIndexOf("." ) + 1));
//    }
}
