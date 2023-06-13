# 烟火app

<p align=center>
    <img src="./ccimg-admin-vue/public/title.png" alt="烟火" style="width:200px;height:200px">
</p>
<p align=center>
   烟火app，一个基于微服务架构的前后端分离项目
</p>
<p align="center">
<a target="_blank" href="https://gitee.com/moxi159753/mogu_blog_v2">
    	<img src="https://img.shields.io/hexpm/l/plug.svg" ></img>
		<img src="https://img.shields.io/badge/JDK-1.8+-green.svg" ></img>
        <img src="https://img.shields.io/badge/nodejs-12.x-green" ></img>
        <img src="https://img.shields.io/badge/springboot-2.2.2.RELEASE-green" ></img>
        <img src="https://img.shields.io/badge/SpringCloud-Hoxton.RELEASE-brightgreen" ></img>
        <img src="https://img.shields.io/badge/vue-2.5.17-green" ></img>
        <img src="https://img.shields.io/badge/swagger-3.0.0-brightgreen" ></img>
        <img src="https://img.shields.io/badge/mybatis--plus-3.1.2-green" ></img>
</a></p>



[项目介绍](#项目介绍) | [运行配置](#运行配置) | [测试账号](#测试账号) | [项目技术](#项目特点及功能) | [技术选型](#技术选型) |   | [关注&交流](#关注&交流) | [赞赏](#赞赏) | [项目截图](#项目截图)


## 项目介绍

烟火app一个**基于微服务架构的前后端分离系统**。**Web** 端使用 **Vue** + **ElementUi** , 移动端使用 **uniapp** 和 **ThorUI**。后端使用 **SpringCloud** + **SpringBoot** + **Mybatis-plus**进行开发，使用 **ElasticSearch**  作为全文检索服务，使用**webSocket**做聊天和消息推送，文件支持**七牛云**和**阿里云上传**.并支持本地**QQ**,**微信**和**微博**登录。

- 烟火app完整项目均是由自己一个开发完成，主要是想学习一下uniapp技术和积累一些项目经验，因此项目中可能会有一些bug，欢迎大家加我qq或微信联系。
- 推荐功能需要一个压缩模型，无法上传，需要加我qq。如果不使用推荐功能也可以运行。里面也有使用协同过滤算法做推荐功能
- 如果对uniapp项目不熟悉，本地仓库中还有一个只使用vue技术实现的一个完整的前后端分离的视频播放器项目。项目地址https://gitee.com/xzjsccz/cc_video

【联系我】**QQ** 484235492或3044606584 **微信** 18572755162
## 运行配置

**后端启动**

- 首先需要把`redis`，`nacos`环境配置好并启动。

- 启动`ccimgcloud-auth`,`ccimgcloud-gateway`,`ccimgcloud-platform`,`ccimgcloud-recommend`这四个服务就可以正常浏览项目。

- 如果需要使用图片上传,短信，邮箱,搜索功能，需要配置es环境并启动，并在`ccimgcloud-utils`模块中连接自己的oss账号，然后启动`ccimgcloud-search`和`ccimgcloud-utils`这两个服务。

- 如果只需要运行后台页面，则启动`ccimgcloud-admin`和`ccimgcloud-manage`模块即可

**前端启动**
- 将`ccimg-uniapp`项目导入到`hbuilder`中，正常启动即可
- `ccimg-admin-vue`直接启动即可

## 测试账号

仓库中有apk测试文件，欢迎大家下载测试。当然也可以导入项目以后只在hbuilder中启动ccimg-uniapp也是可以正常浏览项目。

烟火app测试账号 xiaozhao \
烟火app测试密码 123456

烟火app测试账号 qwer \
烟火app测试密码 123456

烟火app测试账号 qwer1234 \
烟火app测试密码 123456

## 后期维护

项目移动端基本所有功能都做完，web端使用的人人开源管理项目，web端只实现了部分功能，后续有时间再完善。



## 项目特点及功能

- 使用springboot+mybatis_plus+vue+uniapp框架
- 采用 Nacos 作为服务发现和配置中心
- 使用gateway做网关过滤，对发送的请求做过滤。（部分请求放行，比如登录请求，首页数据请求）
- 支持七牛云对象存储和阿里云oss对象存储。
- 采用自定义参数校验注解，轻松实现后端参数校验
- 使用推荐算法做首页推荐功能（使用协同过滤算法及结合simhash和海明距离共同做推荐功能，新版本使用机器学习算法做推荐功能。
- 支持三方登录功能（支持qq，微信，微博登录）也可以手机号验证登录
- 使用ElasticSearch做搜索功能
- 使用websocket做私信聊天和实时通知
- 使用redis做对象缓存
- 采用uniapp 和ThorUi 完成烟火app的移动端门户页面搭建

## 项目地址

目前项目托管在 **Gitee** 和 **Github** 平台上中，欢迎大家 **Star** 和 **Fork** 支持~

- Gitee地址：https://gitee.com/xzjsccz/springboot-vue-ccimgcloud
- Github地址：https://github.com/xiaozhao-66/ccimgcloud-springboot-vue
 
## 项目目录

- ccimg-admin-vue 后台管理页面
- ccimg-uniapp 移动端页面
- ccimgcloud-admin 后台管理服务
- ccimgcloud-api 提供外部访问的api接口（未开发）
- ccimgcloud-auth 认证服务
- ccimgcloud-common 公共模块,存放一些工具类或公用类
- ccimgcloud-datasource 数据源配置
- ccimgcloud-generator 代码生成器（不重要）
- ccimgcloud-manager 后台服务管理，主要是管理烟火app各个功能的后台服务。
- ccimgcloud-platform 烟火app主要功能模块
- ccimgcloud-recommend 推荐系统模块
- ccimgcloud-search 搜索模块
- ccimgcloud-utils  第三方服务模块，邮箱短信，oss对象存储服务
  

## 技术选型

### 后端技术

|      技术      |           版本           |      
| :------------: | :-----------------------: 
|   SpringBoot   |         2.3.2.RELEASE      |  
|  SpringCloudAlibaba	   |        Hoxton.SR6    |   
| Shiro |      1.10.0       |  
|  MyBatis-Plus  |              -        |          
| Elasticsearch  |         7.16.3       |   
|     Redis      |        4.2.2         |  
|     Druid      |              |  
|     oss对象存储     |     -     |            
|      JWT       |        0.7.0        |                
|     Lombok     |     -      |
|     Nginx      |  1.12.2  |         
|     Hutool     |      -       |               
|   websocket    |     2.3.2.RELEASE      |   

### 前端技术

|      技术      |           版本           |      
| :------------: | :-----------------------: 
|   nodejs	   |         12.14.0（最好一致）    |  
|  vue		   |    -     |   
| Shiro |        -    |  
|  uniapp	  |              -        |          
| vueX	  |        -      |   
|     axios	      |      -       |  
|     其他组件      |    -          |  
   



## 关注&交流

一个仿照小红书设计的浏览图片uniapp项目,有问题加q3044606584或者q484235492，微信 18572755162



## 赞赏
因为博主还没有正式毕业所以服务器和其他需要费用的服务管理都比较差，因此为了正常维持这个项目，如果有小伙伴需要我帮忙部署项目，可能需要收取一定的费用
。


## 项目截图

|                        Admin端                         |                                                       |
| :----------------------------------------------------: | :---------------------------------------------------: |
|      ![image text](./ccimg-admin-vue/public/img/login.png)       |    ![image text](./ccimg-admin-vue/public/img/regist.png)    |
|        ![image text](./ccimg-admin-vue/public/img/index.png)       |    ![image text](./ccimg-admin-vue/public/img/follow.png)     |
|    ![image text](./ccimg-admin-vue/public/img/hot.png)    |     ![image text](./ccimg-admin-vue/public/img/main.png)      |
|      ![image text](./ccimg-admin-vue/public/img/main-down.png)      |   ![image text](./ccimg-admin-vue/public/img/main-edit.png)   |
|      ![image text](./ccimg-admin-vue/public/img/batchSave.png)      |    ![image text](./ccimg-admin-vue/public/img/search.png)     |
|      ![image text](./ccimg-admin-vue/public/img/search-input.png)      |       ![image text](./ccimg-admin-vue/public/img/searchImg.png)        |
|      ![image text](./ccimg-admin-vue/public/img/category.png)      |       ![image text](./ccimg-admin-vue/public/img/chatList.png)       |
|      ![image text](./ccimg-admin-vue/public/img/agree-collect.png)      |  ![image text](./ccimg-admin-vue/public/img/addfollow.png)    |
|                                                        |                                                       |
|      ![image text](./ccimg-admin-vue/public/img/addcomment.png)      |    ![image text](./ccimg-admin-vue/public/img/chat.png)    |
|      ![image text](./ccimg-admin-vue/public/img/user.png)      |    ![image text](./ccimg-admin-vue/public/img/albums.png)      |
|     ![image text](./ccimg-admin-vue/public/img/collection-img.png)       |  ![image text](./ccimg-admin-vue/public/img/collection-album.png)   |
|                                                        |                                                       |
|   ![image text](./ccimg-admin-vue/public/img/userInfo.png)    | ![image text](./ccimg-admin-vue/public/img/otherUser.png)  |
|   ![image text](./ccimg-admin-vue/public/img/createAlbum.png)    | ![image text](./ccimg-admin-vue/public/img/albumInfo.png)  |
|   ![image text](./ccimg-admin-vue/public/img/album-edit.png)    | ![image text](./ccimg-admin-vue/public/img/albumIndo-delete.png)  |
|   ![image text](./ccimg-admin-vue/public/img/publish.png)    | ![image text](./ccimg-admin-vue/public/img/tags.png)  |
|   ![image text](./ccimg-admin-vue/public/img/addAlbum.png)    |  |

|                       **Web端**                        
|    ![image text](./ccimg-admin-vue/public/img/admin.jpg)     
