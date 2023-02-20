package com.yjiang.base.modular.HealthUsers.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yjiang.base.modular.HealthUsers.service.IHealthUsersService;
import com.yjiang.base.modular.system.dao.HealthUsersMapper;
import com.yjiang.base.modular.system.model.HealthUsers;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiangying
 * @since 2022-09-06
 */
@Service
public class HealthUsersServiceImpl extends ServiceImpl<HealthUsersMapper, HealthUsers> implements IHealthUsersService {

}
