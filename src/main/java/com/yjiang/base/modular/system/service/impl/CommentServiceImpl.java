package com.yjiang.base.modular.system.service.impl;

import com.yjiang.base.modular.system.dao.CommentMapper;
import com.yjiang.base.modular.system.model.Comment;
import com.yjiang.base.modular.system.service.ICommentService;
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
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

}
