package com.taotao.sso.service.impl;

import com.mysql.jdbc.StringUtils;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.dao.JedisClient;
import com.taotao.sso.service.UserService;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Service层
 * @author Kyle
 * @create 2019-07-19 16:44
 *
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_USER_SESSION_KEY}")
    private String REDIS_USER_SESSION_KEY;

    @Value("${SSO_SESSION_EXPIRE}")
    private Integer  SSO_SESSION_EXPIRE;

    /**
     * 校验该用户是否可用
     * @param content:内容
     * @param type：类型  1、2、3分别代表username、phone、email
     * @return TaotaoResult
     */
    @Override
    public TaotaoResult checkData(String content, Integer type) {
        //创建查询条件
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        //对数据进行校验  1、2、3分别代表username、phone、email
        //用户名校验
        if(1 == type){
           criteria.andUsernameEqualTo(content);
        //电话校验
        } else if(2 == type){
            criteria.andPhoneEqualTo(content);
        //email校验
        }else{
            criteria.andEmailEqualTo(content);
        }
        //执行查询
        List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
        //只要满足其中一个条件就执行if体
        if(list == null || list.size() == 0){
            return TaotaoResult.ok(true);
        }
        return TaotaoResult.ok(false);
    }

    /**
     * 用户注册
     * @param tbUser:对象
     * @return TaotaoResult
     */
    @Override
    public TaotaoResult createUser(TbUser tbUser) {
        //补全该对象的其他属性,id是自增的无需补全
        tbUser.setCreated(new Date());
        tbUser.setUpdated(new Date());
        //spring提供的md5加密
        tbUser.setPassword(DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes()));
        //把传的对象插入数据库
        tbUserMapper.insertSelective(tbUser);
        return TaotaoResult.ok();
    }

    /**
     * 用户登录
     * @param username：用户名
     * @param password:密码
     * @return TaotaoResult
     */
    @Override
    public TaotaoResult userLogin(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
        //如果list为空表示没有查到
        if(null == list || list.size() == 0){
            return TaotaoResult.build(400,"用户名或密码错误!");
        }
        //获取查到的该用户信息
        TbUser user = list.get(0);
        //密码进行加密后再进行比对
        if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
            return TaotaoResult.build(400,"用户名或密码错误!");
        }
        //密码比对成功生成唯一token
        String token = UUID.randomUUID().toString();
        //保存用户信息之前,把用户对象的密码清空,为了安全起见.
        user.setPassword(null);
        //把用户信息写入redis,并且要把user对象转换成字符串.因为set是存字符串的
        jedisClient.set(REDIS_USER_SESSION_KEY+":"+ token, JsonUtils.objectToJson(user));
        //并且设置session的过期时间
        jedisClient.expire(REDIS_USER_SESSION_KEY+":"+ token,SSO_SESSION_EXPIRE);
        //添加写cookie的逻辑,cookie的有效期是关闭浏览器就失效,安全性更高.
        CookieUtils.setCookie(request,response,"TT_TOKEN",token);
        //返回token
        return TaotaoResult.ok(token);
    }

    /**
     * 根据token从redis中查询用户信息,判断用户是否登录或者session是否失效
     * @param token redis中的key
     * @return TaotaoResult
     */
    @Override
    public TaotaoResult getUserByToken(String token) {
        //根据token从redis中查询用户信息
        String json = jedisClient.get(REDIS_USER_SESSION_KEY + ":" + token);
        //判断是否为空
        if(StringUtils.isNullOrEmpty(json)){
            return TaotaoResult.build(400,"此session已经过期,请重新登录!");
        }
        //不为空,就需要更新过期时间
        jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token,SSO_SESSION_EXPIRE);
        //返回用户信息,因为要返回对象,所以需要把字符串转换为对象
        return TaotaoResult.ok(JsonUtils.jsonToPojo(json,TbUser.class));
    }
}
