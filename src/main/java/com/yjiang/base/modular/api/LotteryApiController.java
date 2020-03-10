package com.yjiang.base.modular.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.yjiang.base.lottery.CaipiaoService;
import com.yjiang.base.modular.SsqLottery.service.ISsqLotteryService;
import com.yjiang.base.modular.system.model.SsqLottery;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        return lotteryService.selectCount(wrapper);
    }
}
