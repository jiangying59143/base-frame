package com.yjiang.base.modular.system.service.impl;

import com.yjiang.base.modular.system.dao.CommentCountMapper;
import com.yjiang.base.modular.system.model.CommentCount;
import com.yjiang.base.modular.system.service.ICommentCountService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-11-12
 */
@Service
public class CommentCountServiceImpl extends ServiceImpl<CommentCountMapper, CommentCount> implements ICommentCountService {

}
