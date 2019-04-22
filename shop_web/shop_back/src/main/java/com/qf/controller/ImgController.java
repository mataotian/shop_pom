package com.qf.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Controller
@RequestMapping("/imgs")
public class ImgController {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    private static final String outPath="C:\\images\\";
    @RequestMapping("/uploader")
    @ResponseBody
    public String upload(MultipartFile file){
        System.out.println(file.getOriginalFilename());
        val index = file.getOriginalFilename().indexOf(".");
        String substring = file.getOriginalFilename().substring(index+1);
        try {
            StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), substring, null);
            String fullPath = storePath.getFullPath();
            System.out.println("上传图片的路径:"+fullPath);
            return "{\"uploadPath\":\"" + fullPath + "\"}";
        } catch (IOException e) {
            e.printStackTrace();
        }
        //本地上传
//        try(
//                InputStream in=file.getInputStream();
//                OutputStream out=new FileOutputStream(outPath+UUID.randomUUID().toString());
//        ){
//            IOUtils.copy(in,out);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        return null;
    }
}
