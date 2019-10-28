2019.02 - 2019.07   淘淘商城     
开发环境： IDEA+Maven+Git+Jdk1.8+Tomcat8.0  
软件架构： Mysql+Mybatis+Spring+Springmvc+Redis+Solr+Httpclient  
项目描述：淘淘网上商城是一个综合性的B2C平台，类似京东商城、天猫商城。会员可以在商城浏览商品、下订单，  
以及参加各种活动。淘淘商城采用分布式系统架构，子系统之间都是调用服务来实现系统之间的通信，  
使用http协议传递json数据方式实现。这样降低了系统之间的耦合度，提高了系统的扩展性。  
为了提高系统的性能使用redis做系统缓存，并使用redis实现session共享。  
为了保证redis的性能使用redis的集群。搜索功能使用solrCloud做搜索引擎。  
 
系统主要包括以下模块：  
后台管理系统：管理商品、订单、类目、商品规格属性、用户管理以及内容发布等功能。  
前台系统：用户可以在前台系统中进行注册、登录、浏览商品、首页、下单等操作。  
会员系统：用户可以在该系统中查询已下的订单、收藏的商品、我的优惠券、团购等信息。  
订单系统：提供下单、查询订单、修改订单状态、定时处理订单。  
搜索系统：提供商品的搜索功能。  
单点登录系统：为多个系统之间提供用户登录凭证以及查询登录用户的信息。  
