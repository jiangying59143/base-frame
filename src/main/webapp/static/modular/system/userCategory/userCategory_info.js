/**
 * 初始化详情对话框
 */
var UserCategoryInfoDlg = {
    userCategoryInfoData : {}
};

/**
 * 清除数据
 */
UserCategoryInfoDlg.clearData = function() {
    this.userCategoryInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserCategoryInfoDlg.set = function(key, val) {
    if("multiselect_to"==key){
        var ret = new Array();
        $("#"+key+" option").each(function(){
            //遍历所有option
            var value = $(this).val();   //获取option值
            ret.push(value);
        });
        this.userCategoryInfoData[key] = ret;
    }else{
        this.userCategoryInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    }
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserCategoryInfoDlg.get = function(key) {
    if("multiselect_to"==key){
        var ret = new Array();
        $("#"+key+" option").each(function(){
            //遍历所有option
            var value = $(this).val();   //获取option值
            ret.push(value);
        });
        return ret;
    }
    return $("#" + key).val();
}

/**
 * 收集数据
 */
UserCategoryInfoDlg.collectData = function() {
    this
    .set('categoryIds')
}

/**
 * 提交修改
 */
UserCategoryInfoDlg.editSubmit = function() {
    var ret = new Array();
    $("#multiselect_to option").each(function(){
        //遍历所有option
        var value = $(this).val();   //获取option值
        ret.push(value);
    });
    $("#categoryIds").val(ret);

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/userCategory/update", function(data){
        Feng.success("修改成功!");
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.userCategoryInfoData);
    ajax.start();
}

$(function() {

});
