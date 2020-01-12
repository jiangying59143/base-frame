package com.yjiang.base.modular.system.service.impl;

import com.yjiang.base.modular.system.dao.ArticleViewCountMapper;
import com.yjiang.base.modular.system.model.ArticleViewCount;
import com.yjiang.base.modular.system.service.IArticleViewCountService;
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
public class ArticleViewCountServiceImpl extends ServiceImpl<ArticleViewCountMapper, ArticleViewCount> implements IArticleViewCountService {

}
