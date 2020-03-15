package com.yjiang.base.system;

import com.yjiang.base.base.BaseJunit;
import com.yjiang.base.lottery.CaipiaoService;
import com.yjiang.base.modular.system.dao.SsqLotteryMapper;
import com.yjiang.base.modular.system.model.SsqLottery;
import org.junit.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class LotteryTest extends BaseJunit {

    @Resource
    private SsqLotteryMapper ssqLotteryMapper;

    @Resource
    private CaipiaoService caipiaoService;

    @Test
    public void testAll(){
        List<SsqLottery> ssqLotteryList = ssqLotteryMapper.selectByMap(new HashMap<>());
        BigDecimal total = new BigDecimal(ssqLotteryList.size());
        ssqLotteryList.parallelStream()
                .filter(ssq -> caipiaoService.continueRed(
                        Arrays.asList(ssq.getRedBall1(), ssq.getRedBall2(), ssq.getRedBall3(), ssq.getRedBall4(), ssq.getRedBall5(), ssq.getRedBall6()), 5))
                .forEach(System.out::println);
        System.out.println( "two : " +  (this.returnContinualCount(ssqLotteryList, 2).multiply(new BigDecimal(100))).divide(total, 2, BigDecimal.ROUND_HALF_UP) );
        System.out.println( "three : " +  (this.returnContinualCount(ssqLotteryList, 3).multiply(new BigDecimal(100))).divide(total, 2, BigDecimal.ROUND_HALF_UP) );
        System.out.println( "four : " +  (this.returnContinualCount(ssqLotteryList, 4).multiply(new BigDecimal(100))).divide(total, 2, BigDecimal.ROUND_HALF_UP) );
        System.out.println( "five : " +  (this.returnContinualCount(ssqLotteryList, 5).multiply(new BigDecimal(100))).divide(total, 2, BigDecimal.ROUND_HALF_UP) );
    }

    private BigDecimal returnContinualCount(List<SsqLottery> list, int count){
        return new BigDecimal(list.parallelStream()
                .filter(ssq -> caipiaoService.continueRed(
                        Arrays.asList(ssq.getRedBall1(), ssq.getRedBall2(), ssq.getRedBall3(), ssq.getRedBall4(), ssq.getRedBall5(), ssq.getRedBall6()), count))
                .collect(Collectors.toList()).size());
    }
}
