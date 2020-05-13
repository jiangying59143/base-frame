package com.yjiang.base.modular.health.service.impl;

import com.yjiang.base.modular.system.model.Health;
import com.yjiang.base.modular.system.dao.HealthMapper;
import com.yjiang.base.modular.health.service.IHealthService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiangying
 * @since 2020-05-13
 */
@Service
public class HealthServiceImpl extends ServiceImpl<HealthMapper, Health> implements IHealthService {

}
