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
    
    
**liquibase整合**

导出数据库表
mvn liquibase:generateChangeLog -Dliquibase.diffTypes=tables,views,columns,indexs,foreignkeys,primarykeys,uniqueconstraints,data