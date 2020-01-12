package com.yjiang.base.modular.system.service;

import com.yjiang.base.modular.system.model.Category;
import com.yjiang.base.modular.system.model.UserCategory;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jiangying
 * @since 2018-11-20
 */
public interface IUserCategoryService extends IService<UserCategory> {

    public List<Category> getUserCategories();

    public List<Category> getNotUserCategories();

    public void update(Long[] categoryIds);

}
