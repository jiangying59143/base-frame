**java**
jdk1.8.0_341

**maven**
version 3.6.3

**mariaDB**
10.9

**liquibase整合**

导出数据库表
mvn liquibase:generateChangeLog -Dliquibase.diffTypes=tables,views,columns,indexs,foreignkeys,primarykeys,uniqueconstraints,data
更新数据库
mvn liquibase:update

**qq 邮箱授权码**
登录 qq 邮箱网页版 -> 语言设置为 “简体中文” -> 设置 -> 账户 -> 生成授权码

**OSS和高德地图 因为下面地方不能用 五个x的地方需要替换成相应的值**

_AliyunOSSClientUtil_
    //主账户
    private static final String accessKeyId = "xxxxx";
    private static final String accessKeySecret = "xxxxx";

    //子账户
    private static final String subAccessKeyId = "xxxxx";
    private static final String subAccessKeySecret = "xxxxx";_
    
_GmapUtil_
    private static String key = "xxxxx";
    
_location_info.js_
    _`$("#map").html("<img src='https://restapi.amap.com/v3/staticmap?markers=mid,0xFF0000,A:" + ui.item.value[0] + "," + ui.item.value[1] +"&key=xxxxx'/>");`_

**打包命令**
mvn clean package -Pproduce -DskipTests
nohup java -jar base-frame-1.0.0.jar --server.port=80 --chatgpt.apiKey=${OPEN_API_KEY} --spring.mail.password=${QQ_API_KEY} --spring.datasource.password=!qazxsw222 --log.path=/root
