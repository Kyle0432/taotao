package com.taotao.service;

import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Kyle
 * @create 2019-05-17 12:01
 */
public interface PictureService {

    Map uploadPicture(MultipartFile uploadFile);
}
