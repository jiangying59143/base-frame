package com.yjiang.base.modular.api;

import cn.stylefeng.roses.core.reqres.response.SuccessResponseData;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.yjiang.base.lottery.CaipiaoService;
import com.yjiang.base.modular.SsqLottery.service.ISsqLotteryService;
import com.yjiang.base.modular.system.model.SsqLottery;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/lotteryApi")
public class LotteryApiController {

    @Autowired
    private CaipiaoService caipiaoService;

    @Autowired
    private ISsqLotteryService lotteryService;


    @ApiOperation(value="初始化并且获取总数", notes="初始化并且获取总数")
    @RequestMapping(value="/count", method = RequestMethod.GET)
    public Object Count(){
        caipiaoService.init();
        Wrapper<SsqLottery> wrapper = new EntityWrapper<>();
        return new SuccessResponseData(200, "成功", lotteryService.selectCount(wrapper));
    }

    @ApiOperation(value="计算每个号码出现次数", notes="计算每个号码出现次数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "令牌(Bearer )", required = true, dataType = "String", paramType = "header")
    })
    @RequestMapping(value="/ballTimesSummary", method = RequestMethod.GET)
    public Object calEachNumCount(int count){
        return new SuccessResponseData(200, "成功", caipiaoService.calEachNumCount(count));
    }

    @ApiOperation(value="获取彩票中奖率", notes="获取彩票中奖率")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "令牌(Bearer )", required = true, dataType = "String", paramType = "header")
    })
    @RequestMapping(value="/getWinRatio", method = RequestMethod.GET)
    public Object getCaiPiaoWinRate(){
        return new SuccessResponseData(200, "成功", caipiaoService.getCaiPiaoWinRate());
    }

    @ApiOperation(value="获取彩票号码", notes="获取彩票号码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "令牌(Bearer )", required = true, dataType = "String", paramType = "header")
    })
    @RequestMapping(value="/getNumbers", method = RequestMethod.GET)
    public Object getNumbers(int count, int notContinueNumber){
        List<List<Integer>> lotteries = new ArrayList<>();
        List<Integer> l = caipiaoService.getCaiPiao(notContinueNumber, true);
        lotteries.add(l);
        for (int i = 1; i < count; i++) {
            List<Integer> lottery = caipiaoService.getCaiPiao(notContinueNumber, false);
            lotteries.add(lottery);
        }
        return new SuccessResponseData(200, "成功", lotteries);
    }
}
