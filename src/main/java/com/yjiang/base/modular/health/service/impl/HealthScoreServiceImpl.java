package com.yjiang.base.modular.health.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yjiang.base.modular.health.service.IHealthScoreService;
import com.yjiang.base.modular.system.dao.HealthScoreMapper;
import com.yjiang.base.modular.system.model.HealthScore;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yjiang
 * @since 2021-11-20
 */
@Service
public class HealthScoreServiceImpl extends ServiceImpl<HealthScoreMapper, HealthScore> implements IHealthScoreService {

}
