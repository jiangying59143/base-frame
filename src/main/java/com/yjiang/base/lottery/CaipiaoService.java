package com.yjiang.base.lottery;

import java.util.List;
import java.util.Map;

public interface CaipiaoService {

	void init();

	Map<String, List<CaipiaoServiceImp.KeyValue>> calEachNumCount(Integer limit);
	
	int getCaiPiaoWinRate();

	boolean continueRed(List<Integer> list, int continueNum);

	List<Integer> getCaiPiao(int notContinueNum, boolean flag);

}
