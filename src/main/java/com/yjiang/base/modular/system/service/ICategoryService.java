package com.yjiang.base.modular.system.service;

import com.yjiang.base.modular.system.model.Category;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-11-14
 */
public interface ICategoryService extends IService<Category> {
    public List<Category> list();

    public List<Category> getUserCategories();

    public List<Category> getNotUserCategories();
}
