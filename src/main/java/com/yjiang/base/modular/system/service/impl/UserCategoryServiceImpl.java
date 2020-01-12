package com.yjiang.base.modular.system.service.impl;

import com.yjiang.base.core.shiro.ShiroKit;
import com.yjiang.base.modular.system.dao.UserCategoryMapper;
import com.yjiang.base.modular.system.model.Category;
import com.yjiang.base.modular.system.model.UserCategory;
import com.yjiang.base.modular.system.service.ICategoryService;
import com.yjiang.base.modular.system.service.IUserCategoryService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiangying
 * @since 2018-11-20
 */
@Service
public class UserCategoryServiceImpl extends ServiceImpl<UserCategoryMapper, UserCategory> implements IUserCategoryService {

    @Autowired
    private ICategoryService categoryService;

    @Override
    public List<Category> getUserCategories() {
        List<UserCategory> list = this.getCurrentUserCategories(false);
        List<Long> categoryIds = list.stream().map(uc -> uc.getCategoryId()).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(categoryIds)) {
            return Collections.EMPTY_LIST;

        }else{
            List<Category> categories = categoryService.selectBatchIds(categoryIds);
            return categories;
        }
    }

    @Override
    public List<Category> getNotUserCategories() {
        List<UserCategory> list = this.getCurrentUserCategories(false);
        List<Long> categoryIds = list.stream().map(uc -> uc.getCategoryId()).collect(Collectors.toList());
        categoryIds.add(1L);
        categoryIds.add(2L);

        if(CollectionUtils.isEmpty(categoryIds)) {
            return Collections.EMPTY_LIST;

        }

        Wrapper<Category> categoryEntityWrapper = new EntityWrapper<Category>();
        categoryEntityWrapper.notIn("id", categoryIds);
        List<Category> categories = categoryService.selectList(categoryEntityWrapper);
        return categories;
    }

    @Override
    @Transactional
    public void update(Long[] categoryIds) {
        List<Long> categoryIdList = Arrays.asList(categoryIds);
        List<UserCategory> list = this.getCurrentUserCategories(true);
        List<Long> categoryIds1 = list.stream().map(uc -> uc.getCategoryId()).collect(Collectors.toList());
        for (Long categoryId : categoryIds1) {
            if(!categoryIdList.contains(categoryId)){
                Wrapper<UserCategory> userCategoryEntityWrapper = new EntityWrapper<UserCategory>();
                userCategoryEntityWrapper.eq("user_id", ShiroKit.getUser().getId());
                userCategoryEntityWrapper.eq("category_id", categoryId);
                this.delete(userCategoryEntityWrapper);
            }
        }
        for (Long categoryId : categoryIds) {
            if(!categoryIds1.contains(categoryId)){
                UserCategory userCategory = new UserCategory();
                userCategory.setCategoryId(categoryId);
                userCategory.setUserId(ShiroKit.getUser().getId());
                userCategory.setCreateBy(ShiroKit.getUser().getId());
                userCategory.setCreateDate(new Date());
                userCategory.setUpdateDate(new Date());
                userCategory.setUpdateBy(ShiroKit.getUser().getId());
                userCategory.setVersion(1);
                this.insert(userCategory);
            }
        }
    }

    private List<UserCategory> getCurrentUserCategories(boolean includeFlag){
        Wrapper<UserCategory> userCategoryEntityWrapper = new EntityWrapper<UserCategory>();
        userCategoryEntityWrapper.eq("user_id", ShiroKit.getUser().getId());
        if(!includeFlag) {
            userCategoryEntityWrapper.notIn("category_id", Arrays.asList(1, 2));
        }
        List<UserCategory> list = this.selectList(userCategoryEntityWrapper);
        return list;
    }

}
