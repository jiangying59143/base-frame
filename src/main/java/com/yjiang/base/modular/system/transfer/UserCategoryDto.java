package com.yjiang.base.modular.system.transfer;

public class UserCategoryDto {

    private Long[] multiselect_to;

    private Long[] categoryIds;

    public Long[] getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(Long[] categoryIds) {
        this.categoryIds = categoryIds;
    }

    public Long[] getMultiselect_to() {
        return multiselect_to;
    }

    public void setMultiselect_to(Long[] multiselect_to) {
        this.multiselect_to = multiselect_to;
    }
}
