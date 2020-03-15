package com.yjiang.base.lottery;

import java.util.List;
import java.util.Map;

public interface CaipiaoService {

	void init();

	Map<String, Map<Integer, Integer>> calEachNumCount();
	
	int getCaiPiaoWinRate();

	boolean continueRed(List<Integer> list, int continueNum);

	List<Integer> getCaiPiao(int notContinueNum);
}
