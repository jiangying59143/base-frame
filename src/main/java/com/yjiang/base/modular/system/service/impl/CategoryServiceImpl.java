package com.yjiang.base.modular.system.service.impl;

import com.yjiang.base.modular.system.dao.CategoryMapper;
import com.yjiang.base.modular.system.model.Category;
import com.yjiang.base.modular.system.service.IArticleCategoryService;
import com.yjiang.base.modular.system.service.ICategoryService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-11-14
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private IArticleCategoryService articleCategoryService;

    @Override
    public List<Category> list(){
        Map<String, Object> map = new HashMap<>();
        Wrapper<Category> articleImageWrapper = new EntityWrapper<Category>();
        articleImageWrapper.notIn("id", Arrays.asList(1,2));
        return this.selectList(articleImageWrapper);
    }

    @Override
    public List<Category> getUserCategories() {
        return null;
    }

    @Override
    public List<Category> getNotUserCategories() {
        return null;
    }
}
