package com.yjiang.base.lottery;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.yjiang.base.core.shiro.ShiroKit;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
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
			SsqLottery ssqLottery = new SsqLottery();
			Elements tds = tr.getElementsByTag("td");
			if(!isExistedForRow(tds.get(0).text())){
				processSingleRow(tds, ssqLottery);
			}
		}
	}

	private void processSingleRow(Elements tds,SsqLottery ssqLottery) {
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
					ssqLottery.setTotalSale(new BigDecimal(td.text().substring(0, td.text().length()-1)));
					break;
				case 4:
					ssqLottery.setPrizePond(new BigDecimal(td.text().substring(0, td.text().length()-1)));
					break;
				case 5:
					ssqLottery.setFirstCount(Integer.parseInt(td.text().substring(0, td.text().length()-1)));
					break;
				case 6:
					ssqLottery.setFirstAmountMoney(new BigDecimal(td.text().substring(0, td.text().length()-1)));
					break;
				case 7:
					ssqLottery.setSecondCount(Integer.parseInt(td.text()));
					break;
				case 8:
					ssqLottery.setSecondAmountMoney(new BigDecimal(td.text().substring(0, td.text().length()-1)));
					break;
				case 9:
					ssqLottery.setThirdCount(Integer.parseInt(td.text()));
					break;
				case 10:
					ssqLottery.setThirdAmountMoney(new BigDecimal(td.text().substring(0, td.text().length()-1)));
					break;
			}
		}
		ssqLottery.setCreateBy(ShiroKit.getUser().getId());
		ssqLottery.setCreateDate(new Date());
		lotteryService.insert(ssqLottery);
	}

	private boolean isExistedForRow(String termNumber){
		Wrapper<SsqLottery> wrapper = new EntityWrapper<>();
		wrapper.eq("number", termNumber);
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

	public static void main(String[] args) {
		ballPicMap.entrySet().parallelStream().sorted(Comparator.comparingInt(Map.Entry::getValue)).collect(Collectors.toList()).forEach(entry-> System.out.println("ballPicMap.put(\"" + entry.getKey() + "\"," + entry.getValue() + ");"));
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
	public Map<String, Map<Integer, Integer>> calEachNumCount() {
		Map<Integer, Integer> redBallMap = new HashMap<>();
		Map<Integer, Integer> blueBallMap = new HashMap<>();
		List<SsqLottery> ssqLotteries = lotteryService.selectList(new EntityWrapper<>());
		for (SsqLottery ssqLottery : ssqLotteries) {
			redBallMap.put(ssqLottery.getRedBall1(), redBallMap.get(ssqLottery.getRedBall1()) == null ? 1 : redBallMap.get(ssqLottery.getRedBall1()) + 1);
			redBallMap.put(ssqLottery.getRedBall2(), redBallMap.get(ssqLottery.getRedBall2()) == null ? 1 : redBallMap.get(ssqLottery.getRedBall2()) + 1);
			redBallMap.put(ssqLottery.getRedBall3(), redBallMap.get(ssqLottery.getRedBall3()) == null ? 1 : redBallMap.get(ssqLottery.getRedBall3()) + 1);
			redBallMap.put(ssqLottery.getRedBall4(), redBallMap.get(ssqLottery.getRedBall4()) == null ? 1 : redBallMap.get(ssqLottery.getRedBall4()) + 1);
			redBallMap.put(ssqLottery.getRedBall5(), redBallMap.get(ssqLottery.getRedBall5()) == null ? 1 : redBallMap.get(ssqLottery.getRedBall5()) + 1);
			redBallMap.put(ssqLottery.getRedBall6(), redBallMap.get(ssqLottery.getRedBall6()) == null ? 1 : redBallMap.get(ssqLottery.getRedBall6()) + 1);
			blueBallMap.put(ssqLottery.getBlueBall1(), blueBallMap.get(ssqLottery.getBlueBall1()) == null ? 1 : blueBallMap.get(ssqLottery.getBlueBall1()) + 1);
		}
		Map<String, Map<Integer, Integer>> returnMap = new HashMap<>();
		returnMap.put("red", redBallMap);
		returnMap.put("blue", blueBallMap);
		return returnMap;
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
	public List<Integer> getCaiPiao(int notContinueNum) {
		List<Integer> list;
		do{
			list = listComposedRed();
			list = sortList(list);
			list.add(composeBlue());
		}while(continueRed(list, notContinueNum) || hasOldData(list));
		return list;
	}

	/**
	 * 连续数字不能超过规定个数
	 *
	 * @param list
	 * @param continueNum
	 * @return
	 */
	public boolean continueRed(List<Integer> list, int continueNum) {
		int k = 1;
		if (continueNum == 1) {
			return true;
		}
		for (int i = list.size() - 1; i >= 1; i--) {
			for (int j = list.size() - 2; j >= 0; j--) {
				if (list.get(i) - list.get(j) == 1) {
					k++;
				}
				if (k == continueNum) {
					return true;
				}
			}
		}
		return false;
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

}