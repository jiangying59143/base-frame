package com.yjiang.base.modular.health.service.impl;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SongDownload {
    private static Logger logger = LoggerFactory.getLogger(SongDownload.class);
    private static final String diverPath = "D:\\driver\\chromedriver104.exe";
    private static final String url = "https://music.liuzhijin.cn/";
    private static final String diskPath = "D:\\song-download\\children\\";
    private static final String songs = "捉泥鳅||虫儿飞||我是一条小青龙||我真的很不错||采蘑菇的小姑娘||小燕子||外婆的澎湖湾||祝你生日快乐||一闪一闪亮晶晶||春天在哪里||小兔乖乖||我爱北京天安门||歌唱二小放牛郎||蜗牛与黄鹂鸟||兔子舞||世上只有妈妈好||小老鼠上灯台||歌声与微笑||白龙马||童年||我们是共产主义接班人||歌唱祖国||让我们荡起双浆||乡间小路||两只老虎||小苹果||健康歌||鲁冰花||小手拍拍||让我们荡起双桨||少先队队歌||小螺号||校园的早晨||好爸爸坏爸爸||一只哈巴狗||我有一只小毛驴||共产儿童团歌||儿歌串烧||宝贝去哪儿||小白兔白又白||我爱洗澡||丢手绢||兰花草||妈妈的吻||摇篮曲||纯音乐||找朋友||黑猫警长||小草||小小少年||学习雷锋好榜样||七子之歌||每当我走过老师的窗前||马兰谣||拍手歌||莫扎特效应之摇篮曲||古典精选篇-||扎特的儿歌||小星星||花仙子之歌||英文字母歌||爸爸妈妈听我说||小毛驴||金色童年||铃儿响叮当||春天在哪里||小白兔乖乖||种太阳||莫扎特小步舞曲||莫扎特降B大调小提琴协奏曲第一乐章||葫芦娃||家庭礼貌称呼歌||英文儿歌||唢呐配喇叭||同一首歌||幸福拍手歌||打电话||小鸭子||唱响童年||小猫叫||眼保健操||米老鼠之歌||(莫扎特)小夜曲||B大调小提琴协奏曲第三乐章 莫扎特||Body Song||大家一起喜羊羊||劳动最光荣||一周七天 Days of the Week||茉莉花||三个和尚||长亭外古道边||静夜思(李白)||We Wish You A Merry Christmas||彩虹的约定||新年好||卖报歌||上学歌||六一国际儿童节||阿童木之歌||神笔马良||拔萝卜||小白兔||乡间的小路||阳光少年||忆江南||小耗子上灯台||公鸡 母鸡和小鸡||猴哥||人之初||Finger Song||小白船||聪明的一休||吃饭歌||咱们从小讲礼貌||我的童年||歌声与微笑||快乐成长||明天会更好||生日快乐||古典精选篇-||多芬精选||ABC字母歌||草原小姐妹||老虎是Tiger||飞吧 小蜜蜂||小鸭嘎嘎||大风车(儿歌)(《大风车》节目主题曲)||拉丁舞曲 恰恰 伦巴 牛仔 Tudo||古典精选篇-||鹅||江南||我爱雪莲花||友谊之光 粤语||花儿与少年小提琴曲||流浪者之歌-古典乐||安睡吧小宝贝||十个小手指Ten Little Fingers||捉泥鳅(英文版)||铃儿响叮当(英文版)||麻雀学校||小二郎上学||洋娃娃和小熊跳舞||古典精选篇-||邦的儿歌||宝宝睡觉了||小龙人||雪绒花||小鸡与小鸭||小老鼠||字母歌||我还有点小糊涂||学唱数字歌||小蝌蚪找妈妈||嘀哩嘀哩||亲亲猪猪宝贝||我是一个粉刷匠||apple tree 苹果树||爸爸妈妈听我说||好像老猫叫||声声慢 李清照||汉语拼音||加油歌||吉祥三宝||布娃娃||Bingo||大苹果||大头儿子和小头爸爸||袋鼠妈妈||小鸭追太阳||小二郎||两只小象||铃儿叮当响||我不上你的当||小红帽||中国人民解放军进行曲||小螺号||快乐的节日||琵琶行 白居易||早上好Good Morning||小兔子乖乖||踏浪||生日快乐歌||红星歌||唐诗联唱||猪八戒背媳妇||粉刷匠||两只老虎||happy birthday 祝你生日快乐||小手爬呀爬||海尔兄弟||春晓||Hello,Hello(儿歌)||吹起小喇叭||多啦A梦(《机器猫》动漫主题曲)||贝多芬《D大调小提琴协奏曲》第一乐章||Twins - Doremi||Walking Walking||大海啊故乡||听妈妈讲那过去的事情||大公鸡||卖汤圆||宝宝八音盒||江南春绝句 杜牧||送别||宝宝摇篮曲||爱尔兰摇篮曲||小年英雄小哪吒||祝福你亲爱的祖国||起床起床起床床||茉莉花(清晰版)||小兵丁||非驴非马||每当我走过老师窗前||摇篮曲(舒伯特)||我上幼儿园||Good Morning||大风车||娃哈哈||少年少年祖国的春天||数鸭子||少年强则中国强||清早听到公鸡叫||小斑马 上学校||刷牙歌||赶海的小姑娘||Hand In Hand||奥特曼||儿童歌曲串烧||||蓝精灵之歌||泥娃娃||读书郎||冰糖葫芦||古典精选篇-||斯冥想曲||三个和尚||生日快乐(Happy Birthday To You)||巴啦啦小魔仙||大西瓜||新年恰恰||我的好妈妈||小红花||我在马路边捡到一分钱||兰花草||秧歌舞||D大调小提琴协奏曲||克罗地亚狂想曲-马克西姆.MP||聪明的一休日语||友谊地久天长||小宝贝快快睡||Barbie Girl||幼儿园毕业歌||丢手绢儿歌||走在乡间的小路上||小星星 中文版||中国少年先锋队队歌||小鸟小鸟||三只小猫||花儿微微笑||Old Macdonald had a Farm||Ten Little Indian Boy||葫芦兄弟||天空之城||贝多芬《D大调小提琴协奏曲》第三乐||门德尔松E小调小提琴协奏曲第三乐章春之歌-门德尔松||摇篮曲勃拉姆斯||少年英雄小哪吒||萤火虫||疯狂果宝||小猫小猫你别吵||小小世界||大手牵小手||好宝宝||胜利||祝你健康进行曲||小鹦鹉学说话||天黑黑 闽南语||宝贝||睡吧小宝贝||快乐的六一||猪之歌||好爸爸||小乌龟||开火车||纳西摇篮曲||新年喜洋洋||清早听到公鸡叫||妈妈教我一支歌||雨花石||咏鹅||蓝皮鼠和大脸猫||花儿开||拉丁美洲_探戈嘉年华||赛船||青蛙跳水||谷建芬-明日歌||八哥学话||小雨点||六一儿童节||小青蛙||七色光之歌||明日歌||布娃娃||小蜜蜂(儿歌)||小宝贝||洗手歌||春晓 唐诗宋词||赠汪伦 李白||小狗汪汪叫||古典精选篇-||母玛利亚||圣母玛丽亚催眠曲||动物园||北京的金山上||我有一个好爸爸||敢问路在何方||三字经||好一个乖宝宝||路灯下的小姑娘||四季歌||拔萝卜||小红帽||小松鼠的家庭剧场||世界名曲||小汽车||小背篓||十二生肖歌||爸爸妈妈最爱我||爸爸妈妈去上班我去幼儿园||采磨菇的小姑娘||中国人民解放军进行曲||登鹳雀楼||欢乐颂||青春舞曲||铃儿响叮当||娃娃催眠歌||蝴蝶飞||沉思曲||凉州词(王之涣)||满江红 岳飞||古典精选篇-||之梦||Head And Shoulders||Shake My Hand||我爱你中国||小手牵大手||大自然是我美丽的家||欢迎你到新疆来||华尔兹舞曲||婚礼进行曲.MP||咏柳||小夜曲舒伯特||我有一个家||Just For You||Good Night||手指操||拨浪鼓||三只小熊||红星照我去战斗||舒克和贝塔||催眠曲||升国旗||我是好宝宝||提琴 菊次郎的夏天||菊次郎的夏天.MP||登高 杜甫||布谷鸟||珍重再见||暴风雨中催眠曲||恭喜发财||校园的小路||金色的童年||爸爸的雪花||大家来做广播操||读书好||five little monkey||稍息立正站好-范晓萱||爷爷为我打月饼||||歌唱祖国||小星星 英文版||花儿与少年||王老先生有块地||陪我入睡的月亮||美丽大自然||晚安||trick or treat儿歌||回乡偶书(贺知章)||悯农||渔家傲 李清照||贝多芬||谷建芬-咏鹅||河边有只羊||少年先锋队队歌||学习雷锋好榜样伴奏||大头儿子小头爸爸||小红帽||同桌的你||虹彩妹妹||虫儿飞||校园的钟声||快乐小孩||龙的传人||不怕不怕||小白兔||小红花 古筝曲||爸爸||音乐-环球宝贝||舒伯特小夜曲||嘉禾舞曲||古典精选篇-||谐曲||古典精选篇-||线上的爱丽亚||微风吹拂的方式||谷建芬-江南||哪吒闹海||拆拆拆(喜羊羊与灰太狼之兔年顶呱呱插曲)||Sunshine||我爱爸爸我爱妈妈||哈啰歌||妈妈告诉我一句话||静夜思||月亮船||拾豆豆||头发肩膀膝盖脚(儿歌)||学做解放军||恭祝生日快乐(粤语)||锄禾日当午||哈利波特||妈妈的太阳(电视剧《少年特工》主题曲TV Verison)||睡吧，宝贝||老师，您好||江雪 柳宗元||鹿柴 王维||大雨和小雨||宝宝要睡觉||IQ 博士 粤语||好少年莫等闲||红黄蓝亲子歌曲||Le Papillon||龙舟舟||大笨象会跳舞||小花小树||小蘑菇||夏天||踏雪寻梅||辣妹子||我爱老师的目光||我是一只小小鸟(童声)||晚风||音乐||When I Build My House||老虎吃糖||水调歌头 苏轼||相思||古典精选篇-||夜曲||摇篮曲(肖邦)||谷建芬-静夜思||热豌豆粥Pease Porridge Hot||东方红||童心是小鸟||鱼儿水中游||How Is The Weather||The More We Get Together||祝福你||水果歌||游子吟||小号手之歌||丑小鸭||爸爸的假期||么么哒||三只小猪盖房子||九月九日忆山东兄弟||清明||黄鹤楼送孟浩然之广陵(李白)||我是巧虎||快安睡||小儿郎||新年儿歌||时钟在说话||校园青春圆舞曲||蜗牛和黄鹂鸟||嘚啵嘚啵嘚||大公鸡喔喔叫||小叮当||小蜜蜂||花仙子||长歌行||真善美的小世界||哆来咪发梭拉西||小放牛||两只老虎||小玉米||小猪吃的饱饱||jingle bells||望庐山瀑布||黄鹤楼 崔颢||闪烁的小星||放松心情篇-||光森林||积羽沉舟||谷建芬-悯农||谷建芬-村居||谷建芬-游子吟||东郭先生和狼||巧儿姑娘||两只小眼睛Two Little Eyes||那是雷声There is Thunder||编花篮||谢谢你||国家";

    public static void main(String[] args) {
        /*String[] songArr = songs.split("\\|\\|");
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (String searchKey : songArr) {
            executorService.submit(()-> {
                try {
                    singleDownload(searchKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }*/
        deleteZeroFile();
    }


    public static void singleDownload(String searchKey){
        RemoteWebDriver driver = null;
        try {
            driver = init();
            search(driver, searchKey);
            String songUrl = driver.findElementById("j-src-btn").getAttribute("href");
            String name = driver.findElementById("j-src-btn").getAttribute("download");
            System.out.println(songUrl + "  " + name);
            downloadNet(songUrl, name);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(searchKey);
        }finally {
            if(driver != null)driver.close();
        }
    }


    public static void search(RemoteWebDriver driver, String searchKey){
        driver.findElementById("j-input").sendKeys(searchKey);
        driver.findElementById("j-submit").click();
        while(driver.findElementById("j-src-btn").getAttribute("href") == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        driver.findElementByXPath("//div[@class='aplayer-list']/ol[1]/li[2]").click();
    }

    public static void downloadNet(String songUrl, String name){
        int byteRead = 0;
        try {
            URL url = new URL(songUrl);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream(diskPath + name);
            byte[] buffer = new byte[1024];
            while(( byteRead = is.read(buffer)) != -1){
                fs.write(buffer, 0, byteRead);
            }
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void deleteZeroFile(){
        File file = new File("D:\\song-download\\80year");
        File[] childrenFiles = file.listFiles();
        System.out.println(childrenFiles.length);
        for (File childrenFile : childrenFiles) {
            if(childrenFile.length()==0){
                System.out.println(childrenFile.getName());
                childrenFile.delete();
            }
        }
    }

    public static RemoteWebDriver init() {
        RemoteWebDriver driver = null;
        try {
            System.setProperty("webdriver.chrome.driver", diverPath);
            ChromeOptions options = new ChromeOptions();
//            if (!"local".equals(activeProfile)) {
//                options.addArguments("--headless", "--no-sandbox", "--disable-gpu", "--whitelisted-ips");
//            }

            ChromeDriverService.Builder builder = new ChromeDriverService.Builder();
            ChromeDriverService chromeService = builder.usingDriverExecutable(new File(diverPath)).usingPort(4444).build();
            try {
                chromeService.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

            driver = new ChromeDriver(chromeService, options);
            driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.MINUTES);
            //定位对象时给10s 的时间, 如果10s 内还定位不到则抛出异常 不注释会报org.openqa.selenium.TimeoutException: timeout
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(3, TimeUnit.SECONDS);
            driver.get(url);
        }catch(Exception e){
            logger.error("webdriver 初始化失败，应该是网站无法访问", e);
        }
        return driver;
    }
}
