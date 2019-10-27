package com.taotao.test.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;

/**
 * @author Kyle
 * @create 2019-07-17 10:31
 */
public class JedisTest {

    @Test
    /**
     * 单机测试Jedis
     */
    public void testJedisSingle(){
        //创建一个jedis的对象
        Jedis jedis = new Jedis("192.168.110.130",6379);
        //调用jedis对象的方法,方法名称和redis的命令一致
        jedis.set("k1","jedisTest");
        jedis.get("k1");
        //关闭jedis
        jedis.close();
    }

    /**
     *使用连接池,这样不用多次创建对象
     */
     public void  testJedisPool(){
         //创建Jedis连接池
         JedisPool jedispool = new JedisPool("192.168.110.139",6279);
         //从连接池中获取Jedis对象
         Jedis jedis = jedispool.getResource();
         //jedis可以调用redis里的方法.
         String k1 = jedis.get("k1");
         System.out.println(k1);
         //关闭redis
         jedis.close();
         //关闭连接池
         jedispool.close();
     }

    /**
     * 集群测试redis
     */
    public void redisClusterTest(){

        HashSet<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.110.130",6379));
        nodes.add(new HostAndPort("192.168.110.130",6380));
        nodes.add(new HostAndPort("192.168.110.130",6381));
        nodes.add(new HostAndPort("192.168.110.130",6382));
        JedisCluster cluster  = new JedisCluster(nodes);
        cluster.set("k2","kyle");
        String k2 = cluster.get("k2");
        System.out.println(k2);

    }

    /**
     * Jedis和spring整合的单机版测试
     */
    @Test
    public void testSpringJedisSingle(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath：spring/applicationContext-*.xml");
        JedisPool  pool = (JedisPool) applicationContext.getBean("redisClient");
        Jedis jedis = pool.getResource();
        String k1 = jedis.get("k1");
        System.out.println(k1);
        jedis.close();
    }

    /**
     * Jedis和spring整合的集群版测试
     */
    @Test
     public void testSpringJedisCluster(){
       ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
       JedisCluster jedisCluster = (JedisCluster) applicationContext.getBean("redisClientCluster");
       String k1 = jedisCluster.get("k1");
       System.out.println(k1);
       jedisCluster.close();
    }
}
