package com.qianfeng.v13centerweb.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.qianfeng.v13.common.pojo.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @author huangguizhao
 */
@Controller
@RequestMapping("file")
public class FileController {

    @Autowired
    private FastFileStorageClient client;

    @Value("${image.server}")
    private String imageServer;

    @PostMapping("upload")
    @ResponseBody
    public ResultBean upload(MultipartFile file){
        //将获取到的文件上传到FastDFS
        String originalFilename = file.getOriginalFilename();//**.**
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
        try {
            StorePath storePath =
                    client.uploadFile(file.getInputStream(), file.getSize(), extName, null);
            String path = new StringBuilder(imageServer).append(storePath.getFullPath()).toString();
            return new ResultBean("200",path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResultBean("404","由于网络发生抖动，上传失败！");
    }
}
