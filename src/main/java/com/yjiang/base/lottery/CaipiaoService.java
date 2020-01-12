package com.yjiang.base.lottery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CaipiaoService {

	int length = 0;
	List<List<Integer>> list = new ArrayList<List<Integer>>();
	Map<Integer, Integer> redBallMap = new HashMap<Integer,Integer>();
	Map<Integer, Integer> blueBallMap = new HashMap<Integer,Integer>();

	void init();

	void calEachNumCount();
	
	int getCaiPiaoCatSums();

	String getCaiPiao(int continueNum);
}
