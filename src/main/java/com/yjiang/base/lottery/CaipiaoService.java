package com.yjiang.base.lottery;

import java.util.List;
import java.util.Map;

public interface CaipiaoService {

	void init();

	Map<String, Map<Integer, Integer>> calEachNumCount();
	
	int getCaiPiaoWinRate();

	List<Integer> getCaiPiao(int notContinueNum);
}
