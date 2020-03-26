package com.yjiang.base.lottery;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yjiang.base.core.util.MailUtils;
import com.yjiang.base.core.util.PicRecognizeUtils;
import com.yjiang.base.modular.SsqLottery.service.ISsqLotteryService;
import com.yjiang.base.modular.system.model.SsqLottery;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class CaipiaoServiceImp implements CaipiaoService {

	@Autowired
	private ISsqLotteryService lotteryService;

	private static final String baseUrl = "https://www.cjcp.com.cn/";
	private static final Map<String, Integer> ballPicMap = new HashMap<>();
	static{
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902501531.png",1);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902101600.png",1);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902102212.png",2);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902502184.png",2);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902103176.png",3);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902503880.png",3);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902104855.png",4);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902504143.png",4);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902505688.png",5);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902105626.png",5);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902106488.png",6);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902506270.png",6);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902107839.png",7);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902507239.png",7);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902108769.png",8);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902508548.png",8);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902509772.png",9);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902109210.png",9);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902510405.png",10);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902110435.png",10);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902511276.png",11);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902111316.png",11);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902512964.png",12);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902112341.png",12);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902513798.png",13);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902113809.png",13);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902114700.png",14);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902514377.png",14);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902115788.png",15);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902515166.png",15);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902116463.png",16);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902516449.png",16);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902117946.png",17);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902118438.png",18);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902119334.png",19);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902120376.png",20);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902121785.png",21);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902122325.png",22);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902123264.png",23);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902124365.png",24);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902125143.png",25);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902126490.png",26);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902127951.png",27);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902128235.png",28);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902129619.png",29);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902130131.png",30);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902131493.png",31);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902132554.png",32);
		ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902133669.png",33);
	}

	@Scheduled(cron="0 21,25,30,35 21 ? * TUE,THU,SUN")
	public void initLatestRecord() {
		System.out.println("initLatestRecord启动啦，=================");
		Elements tds = getTds();
		Wrapper<SsqLottery> wrapper = new EntityWrapper<>();
		wrapper.eq("number", tds.get(0).text());
		if(lotteryService.selectOne(wrapper) == null){
			SsqLottery ssqLottery = processSingleRow(tds);
			String html = "<br/>" +
					"<h1>" + "&nbsp;&nbsp;" +
					"<span style='color:red'>"
					+ ssqLottery.getRedBall1() + "&nbsp;&nbsp;"
					+ ssqLottery.getRedBall2() + "&nbsp;&nbsp;"
					+ ssqLottery.getRedBall3() + "&nbsp;&nbsp;"
					+ ssqLottery.getRedBall4() + "&nbsp;&nbsp;"
					+ ssqLottery.getRedBall5() + "&nbsp;&nbsp;"
					+ ssqLottery.getRedBall6() + "&nbsp;&nbsp;" +
					"</span>" +
					"<span style='color:blue'>"
					+ ssqLottery.getBlueBall1() + "&nbsp;&nbsp;" +
					"</span>"
					+"</h1>";
			html += "<br/><br/><div align='right'>" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss") + "</div>";
			MailUtils.sendHtmlMail("907292671@qq.com", DateUtil.format(ssqLottery.getOpenDate(), "yyyy-MM-dd") + " 双色球：", html);
		}
	}

	@Scheduled(cron="0 0/30 * * * ?")
	public void updateLatestRecord() {
		System.out.println("updateLatestRecord启动啦，=================");
		Elements tds = getTds();
		if(needUpdateForRow(tds.get(0).text()) && tds.get(3).text().length() > 1){
			SsqLottery ssqLottery = processSingleRow(tds);
			MailUtils.sendSimpleMail("907292671@qq.com", DateUtil.format(ssqLottery.getOpenDate(), "yyyy-MM-dd") + " 双色球：", "更新成功！！！");
		}
	}

	private Elements getTds() {
		String ssqBaseUrl = baseUrl + "kaijiang/ssqmingxi.html";
		try {
			Connection conn = Jsoup.connect(ssqBaseUrl)
					.header("Cookie", "Hm_lvt_78803024be030ae6c48f7d9d0f3b6f03=1583651059; Hm_lpvt_78803024be030ae6c48f7d9d0f3b6f03=1583651059; wzws_cid=b7ce79d7bde11f72105bca5c47e81582c4bfa6d412ada8c7055f5919c4479cdcbd7fa3dabb910641adad64a278d6866e4d96e4dd9615542637aa474310a6f88e1caf91fae18f1d0f7fdeba71c5e23b51")
					.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
			Document doc = conn.get();
			Element tableBody = doc.getElementById("kjnum");
			Elements trs = tableBody.getElementsByTag("tr");
			if(CollectionUtils.isEmpty(trs)){
				return null;
			}
			Element tr = trs.get(0);
			Elements tds = tr.getElementsByTag("td");
			return tds;
		} catch (IOException e) {
			return null;
		}
	}

	public void init() {
		String ssqBaseUrl = baseUrl + "kaijiang/ssqmingxi.html";
		String url = ssqBaseUrl;
		int i = 2;
		while(true) {
			try {
				Connection conn = Jsoup.connect(url)
						.header("Cookie", "Hm_lvt_78803024be030ae6c48f7d9d0f3b6f03=1583651059; Hm_lpvt_78803024be030ae6c48f7d9d0f3b6f03=1583651059; wzws_cid=b7ce79d7bde11f72105bca5c47e81582c4bfa6d412ada8c7055f5919c4479cdcbd7fa3dabb910641adad64a278d6866e4d96e4dd9615542637aa474310a6f88e1caf91fae18f1d0f7fdeba71c5e23b51")
						.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
				Document doc = conn.get();
				Element tableBody = doc.getElementById("kjnum");
				Elements trs = tableBody.getElementsByTag("tr");
				if(CollectionUtils.isEmpty(trs)){
					break;
				}
				processSinglePage(trs, ballPicMap);
			} catch (IOException e) {
				break;
			}
			System.out.println(url);
			url = baseUrl + "kaijiang/ssqmingxi_" + (i++)  + ".html";
		}
	}

	private void processSinglePage(Elements trs, Map<String, Integer> ballPicMap) {
		for (Element tr : trs) {
			Elements tds = tr.getElementsByTag("td");
			if(!isExistedForRow(tds.get(0).text())){
				processSingleRow(tds);
			}
		}
	}

	private SsqLottery processSingleRow(Elements tds) {
		SsqLottery ssqLottery = new SsqLottery();

		Wrapper<SsqLottery> wrapper = new EntityWrapper<>();
		wrapper.eq("number", tds.get(0).text());
		wrapper.isNull("totalSale");
		SsqLottery ssqLottery1 = lotteryService.selectOne(wrapper);
		if(ssqLottery1 != null){
			ssqLottery = ssqLottery1;
		}

		for (int i = 0; i < tds.size(); i++) {
			Element td = tds.get(i);
			switch (i) {
				case 0:
					ssqLottery.setNumber(td.text());
					break;
				case 1:
					try {
						ssqLottery.setOpenDate(DateUtils.parseDate(td.text(), "yyyy-MM-dd"));
					} catch (ParseException e) {
						e.printStackTrace();
						ssqLottery.setOpenDate(new Date());
					}
					break;
				case 2:
					Elements imgs = td.getElementsByTag("img");
					processBalls(imgs, ssqLottery, ballPicMap);
					break;
				case 3:
					if(td.text().length() == 0 || td.text().length() == 1){
						break;
					}
					ssqLottery.setTotalSale(new BigDecimal(td.text().substring(0, td.text().length()-1)));
					break;
				case 4:
					if(td.text().length() == 0 || td.text().length() == 1){
						break;
					}
					ssqLottery.setPrizePond(new BigDecimal(td.text().substring(0, td.text().length()-1)));
					break;
				case 5:
					if(td.text().length() == 0 || td.text().length() == 1){
						break;
					}
					ssqLottery.setFirstCount(Integer.parseInt(td.text().substring(0, td.text().length()-1)));
					break;
				case 6:
					if(td.text().length() == 0 || td.text().length() == 1){
						break;
					}
					ssqLottery.setFirstAmountMoney(new BigDecimal(td.text().substring(0, td.text().length()-1)));
					break;
				case 7:
					if(td.text().length() == 0 || td.text().length() == 1){
						break;
					}
					ssqLottery.setSecondCount(Integer.parseInt(td.text()));
					break;
				case 8:
					if(td.text().length() == 0 || td.text().length() == 1){
						break;
					}
					ssqLottery.setSecondAmountMoney(new BigDecimal(td.text().substring(0, td.text().length()-1)));
					break;
				case 9:
					if(td.text() == null || td.text().length() == 0){
						break;
					}
					ssqLottery.setThirdCount(Integer.parseInt(td.text()));
					break;
				case 10:
					ssqLottery.setThirdAmountMoney(new BigDecimal(td.text().substring(0, td.text().length()-1)));
					break;
			}
		}
		ssqLottery.setCreateBy(1L);
		ssqLottery.setCreateDate(new Date());
		lotteryService.insertOrUpdate(ssqLottery);
		return ssqLottery;
	}

	private boolean needUpdateForRow(String termNumber){
		Wrapper<SsqLottery> wrapper = new EntityWrapper<>();
		wrapper.eq("number", termNumber);
		wrapper.isNull("totalSale");
		if(lotteryService.selectOne(wrapper) != null){
			return true;
		}
		return false;
	}

	private boolean isExistedForRow(String termNumber){
		Wrapper<SsqLottery> wrapper = new EntityWrapper<>();
		wrapper.eq("number", termNumber);
		wrapper.isNotNull("totalSale");
		if(lotteryService.selectOne(wrapper) != null){
			return true;
		}
		return false;
	}

	private void processBalls(Elements imgs, SsqLottery ssqLottery, Map<String, Integer> ballPicMap){
		for (int i1 = 0; i1 < imgs.size(); i1++) {
			String picUrl = baseUrl + imgs.get(i1).attr("src");
//			recogAndMap(picUrl);
			switch(i1){
				case 0:
					ssqLottery.setRedBall1(ballPicMap.get(picUrl));
					break;
				case 1:
					ssqLottery.setRedBall2(ballPicMap.get(picUrl));
					break;
				case 2:
					ssqLottery.setRedBall3(ballPicMap.get(picUrl));
					break;
				case 3:
					ssqLottery.setRedBall4(ballPicMap.get(picUrl));
					break;
				case 4:
					ssqLottery.setRedBall5(ballPicMap.get(picUrl));
					break;
				case 5:
					ssqLottery.setRedBall6(ballPicMap.get(picUrl));
					break;
				case 6:
					ssqLottery.setBlueBall1(ballPicMap.get(picUrl));
					break;
			}
		}
	}

	private void recogAndMap(String picUrl){
		if(!ballPicMap.containsKey(picUrl)){
			try {
				int ballNum = Integer.parseInt(PicRecognizeUtils.basicGeneralUrl(picUrl));
				ballPicMap.put(picUrl, ballNum);
			}catch(Exception e){
				e.printStackTrace();
				ballPicMap.put(picUrl, null);
			}

		}
	}



		/**
	 * 获取彩票概率
	 *
	 * @return
	 */
	public int getCaiPiaoWinRate() {
		int a = 33 * 32 * 31 * 30 * 29 * 28 / 6 / 5 / 4 / 3 / 2 * 16;
		return a;
	}

	/**
	 * 每个数字往期出现的次数
	 */
	public Map<String, List<KeyValue>> calEachNumCount(Integer limit) {

		Wrapper<SsqLottery> wrapper = new EntityWrapper<>();
		wrapper.orderBy("number", false);

		List<SsqLottery> ssqLotteries = null;
		if(limit == null){
			ssqLotteries = lotteryService.selectList(wrapper);
		}else{
			ssqLotteries = lotteryService.selectPage(new Page<>(0, limit), wrapper).getRecords();
		}

		Map<Integer, Integer> redBallMap = new HashMap<>();
		Map<Integer, Integer> blueBallMap = new HashMap<>();
		for (SsqLottery ssqLottery : ssqLotteries) {
			redBallMap.put(ssqLottery.getRedBall1(), redBallMap.get(ssqLottery.getRedBall1()) == null ? 1 : redBallMap.get(ssqLottery.getRedBall1()) + 1);
			redBallMap.put(ssqLottery.getRedBall2(), redBallMap.get(ssqLottery.getRedBall2()) == null ? 1 : redBallMap.get(ssqLottery.getRedBall2()) + 1);
			redBallMap.put(ssqLottery.getRedBall3(), redBallMap.get(ssqLottery.getRedBall3()) == null ? 1 : redBallMap.get(ssqLottery.getRedBall3()) + 1);
			redBallMap.put(ssqLottery.getRedBall4(), redBallMap.get(ssqLottery.getRedBall4()) == null ? 1 : redBallMap.get(ssqLottery.getRedBall4()) + 1);
			redBallMap.put(ssqLottery.getRedBall5(), redBallMap.get(ssqLottery.getRedBall5()) == null ? 1 : redBallMap.get(ssqLottery.getRedBall5()) + 1);
			redBallMap.put(ssqLottery.getRedBall6(), redBallMap.get(ssqLottery.getRedBall6()) == null ? 1 : redBallMap.get(ssqLottery.getRedBall6()) + 1);
			blueBallMap.put(ssqLottery.getBlueBall1(), blueBallMap.get(ssqLottery.getBlueBall1()) == null ? 1 : blueBallMap.get(ssqLottery.getBlueBall1()) + 1);
		}
		Map<String, List<KeyValue>> returnMap = new HashMap<>();
		returnMap.put("red", redBallMap.entrySet().stream().map(e->new KeyValue(e.getKey(), e.getValue())).sorted(Comparator.comparingInt(e -> e.value)).collect(Collectors.toList()));
		returnMap.put("blue", blueBallMap.entrySet().stream().map(e->new KeyValue(e.getKey(), e.getValue())).sorted(Comparator.comparingInt(e -> e.value)).collect(Collectors.toList()));
		return returnMap;
	}

	class KeyValue{
		private int key;
		private int value;

		public KeyValue(int key, int value) {
			this.key = key;
			this.value = value;
		}

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}

	/**
	 * 生成红球序列
	 *
	 * @return
	 */
	public List<Integer> listComposedRed() {
		List<Integer> list = new ArrayList<Integer>();
		int red = composeRed();
		while (true) {
			if (!list.contains(red)) {
				list.add(red);
			} else {
				red = composeRed();
			}
			if (list.size() == 6) {
				break;
			}
		}

		return list;

	}

	/**
	 * 为红球排序准备
	 */
	public List<Integer> sortList(List<Integer> list) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else {
			return list.stream().sorted().collect(Collectors.toList());
		}
	}

	/**
	 * list拼接成字符串彩票
	 *
	 * @param notContinueNum
	 * @return
	 */
	public List<Integer> getCaiPiao(int notContinueNum, boolean flag) {
		List<Integer> list;
		if(flag){
			list = sortList(this.getLeastRedBalls());
			list.add(this.calEachNumCount(null).get("blue").get(0).getKey());
		}else {
			do {
				list = listComposedRed();
				list = sortList(list);
				//			list.add(composeBlue());
				list.add(this.calEachNumCount(null).get("blue").get(0).getKey());
			} while (continueRed(list, notContinueNum) || hasOldData(list));
		}
		return list;
	}

	private List<Integer> getLeastRedBalls(){
		List<KeyValue> redList = this.calEachNumCount(null).get("red");
		return Arrays.asList(redList.get(0).getKey(), redList.get(1).getKey(), redList.get(2).getKey(), redList.get(3).getKey(), redList.get(4).getKey(), redList.get(5).getKey());
	}

	/**
	 * 连续数字不能超过规定个数
	 *
	 * @param list
	 * @param continueNum
	 * @return
	 */
	public boolean continueRed(List<Integer> list, int continueNum) {
		if (continueNum <= 1) {
			return true;
		}
		int max = continueRed(list);
		if (max >= continueNum) {
			return true;
		}
		return false;
	}

	public int continueRed(List<Integer> list){
		int max = continueMap(list).entrySet().parallelStream().sorted((e1, e2)-> e2.getValue()-e1.getValue()).findFirst().get().getValue();
		return max;
	}

	public Map<Integer, Integer> continueMap(List<Integer> list){
		Map<Integer, Integer> continueMap = new HashMap<>();
		continueMap.put(0, 1);
		for (int i = list.size() - 1; i >= 1; i--) {
			if (list.get(i) - list.get(i-1) == 1) {
				if(continueMap.containsKey(i+1)){
					continueMap.put(i, continueMap.get(i+1) + 1);
					continueMap.remove(i+1);
				}else{
					continueMap.put(i, 2);
				}
			}
		}
		return continueMap;
	}

	/**
	 * 是否生成的是老数据
	 *
	 * @param balls
	 * @return
	 */
	public boolean hasOldData(List<Integer> balls) {
		Wrapper<SsqLottery> wrapper = new EntityWrapper<>();
		Map<String, Object> params = new HashMap<>();
		params.put("red_ball_1", balls.get(0));
		params.put("red_ball_2", balls.get(1));
		params.put("red_ball_3", balls.get(2));
		params.put("red_ball_4", balls.get(3));
		params.put("red_ball_5", balls.get(4));
		params.put("red_ball_6", balls.get(5));
		params.put("blue_ball_1", balls.get(6));
		wrapper.allEq(params);
		List<SsqLottery> ssqLotteries = lotteryService.selectList(wrapper);
		if(CollectionUtils.isNotEmpty(ssqLotteries)){
			return true;
		}
		return false;
	}

	public int composeRed() {
		Random random = new Random();
		return random.nextInt(33) + 1;
	}

	public int composeBlue() {
		Random random = new Random();
		return random.nextInt(16) + 1;
	}

	public static void main(String[] args) {
//		ballPicMap.entrySet().parallelStream().sorted(Comparator.comparingInt(Map.Entry::getValue)).collect(Collectors.toList()).forEach(entry-> System.out.println("ballPicMap.put(\"" + entry.getKey() + "\"," + entry.getValue() + ");"));
		CaipiaoServiceImp caipiaoServiceImp = new CaipiaoServiceImp();
//		int total = caipiaoServiceImp.getCaiPiaoWinRate();
//		System.out.println("total: " + total);
//		int threeTotal = (33-3) * 30 * 29 * 28/3/2 * 16;
//		System.out.println("threeTotal: " + threeTotal);
//		int twoTotal = (33-2) * 31 * 30 * 29 * 28/4/3/2*16;
//		System.out.println("twoTotal: " + twoTotal);
//		System.out.println("remove threeTotal:" + (total-threeTotal));
//		System.out.println("remove twoTotal:" + (total-twoTotal));
		caipiaoServiceImp.continueMap(Arrays.asList(1,2,3,4,5,6,7,8,10,11,12)).entrySet().forEach(System.out::println);
		System.out.println(caipiaoServiceImp.continueRed(Arrays.asList(1,2,3,4,5,6,7,8,10,11,12)));
	}

}