package com.taotao.sso.controller;

import com.mysql.jdbc.StringUtils;
import com.sun.org.apache.regexp.internal.RE;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import com.taotao.utils.ExceptionUtil;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller层
 * 用户登录和注册
 * @author Kyle
 * @create 2019-07-19 17:13
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 校验该用户是否可用
     * @param param:内容参数
     * @param type:1、2、3分别代表username、phone、email
     * @param callback：回调函数
     * @return TaotaoResult
     */
    @RequestMapping("/check/{param}/{type}")
    @ResponseBody
    public Object checkData(@PathVariable String param,@PathVariable Integer type,String callback){

     TaotaoResult result = null;

     //参数有效校验
     if(StringUtils.isNullOrEmpty(param)){
         result = TaotaoResult.build(400,"校验内容不能为空");
     }
     if(type == null){
         result = TaotaoResult.build(400,"校验内容类型不能为空");
     }
     if(type != 1 && type != 2 && type != 3){
         result = TaotaoResult.build(400,"校验内容类型错误");
     }
     //校验出错
     if(null != result){
         if (null != callback){
             //相当于 result进行包装
             MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
             mappingJacksonValue.setJsonpFunction(callback);
             return mappingJacksonValue;
         }else {
             return result;
         }
     }
     //调用服务
        try {
             result = userService.checkData(param, type);
        }catch (Exception e){
            e.printStackTrace();
            result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
        if (null != callback){
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }else {
            return result;
        }
    }

    /**
     * 用户注册
     * @param tbUser:前端接收的对象
     * @return TaotaoResult
     */
    //注意如果这里没有指定是用的什么方法,它默认POST和GET方法都支持
    @RequestMapping(value="/register",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult createUse(TbUser tbUser){
        try {
            TaotaoResult result = userService.createUser(tbUser);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return TaotaoResult.build(500,ExceptionUtil.getStackTrace(e));
        }
    }

    /**
     * 用户登录
     * @param username:用户名
     * @param password:密码
     * @param request:请求为了cookie共享
     * @param response:响应
     * @return TaotaoResult
     */
    @RequestMapping(value="/login" ,method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult userLogin(String username, String password,
            HttpServletRequest request, HttpServletResponse response){
        try {
            TaotaoResult result = userService.userLogin(username, password,request,response);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return TaotaoResult.build(500,ExceptionUtil.getStackTrace(e));
        }
    }

    /**
     * 通货token获取user信息,
     * @param token
     * @param callback
     * @return
     */
    @RequestMapping("/token/{token}")
    @ResponseBody
    public Object getUserByToken(@PathVariable String token,String callback){
        TaotaoResult result = null;
        try {
            result = userService.getUserByToken(token);
        }catch (Exception e){
            e.printStackTrace();
            result = TaotaoResult.build(500,ExceptionUtil.getStackTrace(e));
        }
        //判断是否为jsonp调用
        if(StringUtils.isNullOrEmpty(callback)){
            return  result;
        }else{
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
    }
}
