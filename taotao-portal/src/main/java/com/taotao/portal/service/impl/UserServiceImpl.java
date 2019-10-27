package com.taotao.portal.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.portal.service.UserService;
import com.taotao.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 调用taotao-sso服务,根据token取用户信息
 * @author Kyle
 * @create 2019-07-21 15:49
 */
@Service
public class UserServiceImpl implements UserService {

    @Value("${SSO_BASE_URL}")
    public  String SSO_BASE_URL;

    @Value("${SS0_USER_TOKEN}")
    private String SS0_USER_TOKEN;

    @Value("${SSO_PAGE_LOGIN}")
    public String SSO_PAGE_LOGIN;

    @Override
    public TbUser getUserByToken(String token) {
        try {
            //得到的是一个tbUser对象的字符串
            String json = HttpClientUtil.doGet(SSO_BASE_URL + SS0_USER_TOKEN + token);
            //把这个json转换转换成TaotaoResult,目的是为了更好判断和获取值
            TaotaoResult result = TaotaoResult.formatToPojo(json, TbUser.class);
            if(result.getStatus() == 200){
                TbUser tbUser = (TbUser) result.getData();
                return  tbUser;
            }
        }catch (Exception e){
           e.printStackTrace();
        }
       //只要没有返回对象就直接返回null
        return null;
    }
}
