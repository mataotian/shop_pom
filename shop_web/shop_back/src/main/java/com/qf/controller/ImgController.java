package com.qf.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

@Controller
@RequestMapping("/imgs")
public class ImgController {

    private static final String outPath="C:\\images\\";
    @RequestMapping("/uploader")
    @ResponseBody
    public String upload(MultipartFile file){
        System.out.println(file.getOriginalFilename());
        try(
                InputStream in=file.getInputStream();
                OutputStream out=new FileOutputStream(outPath+UUID.randomUUID().toString());
        ){
            IOUtils.copy(in,out);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }
}
