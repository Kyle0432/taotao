package com.taotao.portal.service;

import com.taotao.pojo.TbUser;

/**
 * @author Kyle
 * @create 2019-07-21 15:48
 */
public interface UserService {

    TbUser getUserByToken(String token);
}
