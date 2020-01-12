package com.yjiang.base.lottery;

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
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sf.format(new Date());
		String url = "http://baidu.lecai.com/lottery/draw/list/50/?type=range_date&start=2003-01-01&end=" + date;
		try {
			System.out.println("开始");
			Connection conn = Jsoup.connect(url);

			Document doc = conn.get();
			Elements links = doc.getElementsByClass("balls");
			for (Element ae : links) {
				String hrefText = ae.text().replace(" ", "");
				List<Integer> lst = new ArrayList<Integer>();
				for (int i = 0; i < hrefText.length() / 2; i++) {
					String metadata = hrefText.substring(i * 2, i * 2 + 2);
					lst.add(Integer.parseInt(metadata));
				}
				list.add(lst);
			}
			System.out.println("结束");
		} catch (Exception e) {
			e.printStackTrace();
		}
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
//				System.out.print(red + " ");
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