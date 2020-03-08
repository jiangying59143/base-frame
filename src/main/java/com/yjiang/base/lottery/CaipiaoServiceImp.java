package com.yjiang.base.lottery;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.shiro.util.CollectionUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class CaipiaoServiceImp implements CaipiaoService {

	public void init() {
		String url = "https://www.cjcp.com.cn/kaijiang/ssqmingxi.html";
		int i = 1;
		while(true) {
			try {
				processSinglePage(url);
			} catch (IOException e) {
				break;
			}
			url = "https://www.cjcp.com.cn/kaijiang/ssqmingxi_" + (++i)  + ".html";
		}
	}

	private void processSinglePage(String url) throws IOException {
		Connection conn = Jsoup.connect(url)
				.header("Cookie", "Hm_lvt_78803024be030ae6c48f7d9d0f3b6f03=1583651059; Hm_lpvt_78803024be030ae6c48f7d9d0f3b6f03=1583651059; wzws_cid=b7ce79d7bde11f72105bca5c47e81582c4bfa6d412ada8c7055f5919c4479cdcbd7fa3dabb910641adad64a278d6866e4d96e4dd9615542637aa474310a6f88e1caf91fae18f1d0f7fdeba71c5e23b51")
				.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
		Document doc = conn.get();
		Element tableBody = doc.getElementById("kjnum");
		Elements trs = tableBody.getElementsByTag("tr");
		for (Element tr : trs) {
			Elements tds = tr.getElementsByTag("td");
			for (int i = 0; i < tds.size(); i++) {
				Element td = tds.get(i);
				switch (i) {
					case 0:
						System.out.print("期号:" + td.text());
						break;
					case 1:
						System.out.print("开奖时间:" + td.text());
						break;
					case 2:
						System.out.print("双色球开奖结果:" + td.text());
						break;
					case 3:
						System.out.print("总销售额:" + td.text());
						break;
					case 4:
						System.out.print("奖池:" + td.text());
						break;
					case 5:
						System.out.print("一等奖注数:" + td.text());
						break;
					case 6:
						System.out.print("一等奖金额:" + td.text());
						break;
					case 7:
						System.out.print("二等奖注数:" + td.text());
						break;
					case 8:
						System.out.print("二等奖金额:" + td.text());
						break;
					case 9:
						System.out.print("三等奖注数:" + td.text());
						break;
					case 10:
						System.out.print("三等奖金额:" + td.text());
						break;
				}
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		new CaipiaoServiceImp().init();
	}

		/**
	 * 获取彩票概率
	 *
	 * @return
	 */
	public int getCaiPiaoCatSums() {
		int a = 33 * 32 * 31 * 30 * 29 * 28 / 6 / 5 / 4 / 3 / 2 * 16;
		return a;
	}

	/**
	 * 每个数字往期出现的次数
	 */
	public void calEachNumCount() {
		for (List<Integer> lst : list) {
			for (int i = 0; i < lst.size(); i++) {
				if (i < 6) {
					redBallMap.put(lst.get(i), redBallMap.get(lst.get(i)) == null ? 1 : redBallMap.get(lst.get(i)) + 1);
				} else {
					blueBallMap.put(lst.get(i), blueBallMap.get(lst.get(i)) == null ? 1 : blueBallMap.get(lst.get(i)) + 1);
				}
			}
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
	 * @param continueNum
	 * @return
	 */
	public String getCaiPiao(int continueNum) {
		List<Integer> list = listComposedRed();
		while (true) {
			if (continueRed(list, continueNum) || hasOldData(list)) {
				list = listComposedRed();
			}
			break;
		}
		list = sortList(list);
		String s = "";
		for (Integer a : list) {
			s += a + ", ";
		}
		s += composeBlue();
		return s;
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
	 * @param list1
	 * @return
	 */
	public boolean hasOldData(List<Integer> list1) {
		for (int i = 0; i < list.size(); i++) {
			List<Integer> listtmp = list.get(i);
//			System.out.print(list.get(i));
			int k = 0;
			for (int j = 0; j < list1.size(); j++) {
				if (listtmp.get(j) == list1.get(j)) {
					k++;
				}
			}
			if (k == 7) {
				System.out.print(list.get(i));
				System.out.print(true);
				return true;
			}
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