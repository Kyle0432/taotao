package com.taotao.service.impl;

import com.taotao.service.PictureService;
import com.taotao.utils.FtpUtil;
import com.taotao.utils.IDUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片上传服务
 * @author Kyle
 * @create 2019-05-17 12:37
 */
@Service
public class PictureServiceImpl implements PictureService {

    //spring容器会自动的把@Value里面的值注入到下面这个属性值里
    @Value("${FTP_ADDRESS}")
    private String FTP_ADDRESS;
    @Value("${FTP_PORT}")
    private Integer FTP_PORT;
    @Value("${FTP_USERNAME}")
    private String FTP_USERNAME;
    @Value("${FTP_PASSWORD}")
    private String FTP_PASSWORD;
    @Value("${FTP_BASE_PATH}")
    private String FTP_BASE_PATH;
    @Value("${IMAGE_BASE_URL}")
    private String IMAGE_BASE_URL;

    @Override
    public Map uploadPicture(MultipartFile uploadFile){
        Map<String,Object> resultMap = new HashMap<>();
        //生成一个新的文件名
        //取原生文件名
        try {
            String originalFilename = uploadFile.getOriginalFilename();
            //生成新文件名
            //UUID.randomUUID();
            String newName = IDUtils.genImageName();
            //取出原生文件名的后缀赋给新的文件
            newName = newName + originalFilename.substring(originalFilename.lastIndexOf("."));
            //图片上传
            String imagePath = new DateTime().toString("/yyyy/MM/dd");
            boolean result = FtpUtil.uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD,
                    FTP_BASE_PATH, imagePath, newName, uploadFile.getInputStream());
            //返回结果
            if(!result){
                resultMap.put("error",1);
                resultMap.put("message","文件上传失败!");
                return resultMap;
            }
            resultMap.put("error",0);
            //上传成功返回的url地址
            resultMap.put("url",IMAGE_BASE_URL+imagePath+"/"+newName);
            return resultMap;

        }catch (Exception e){
            resultMap.put("error",1);
            resultMap.put("message","文件发生异常!");
            return resultMap;
        }
    }
}
