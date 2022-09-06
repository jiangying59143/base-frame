/**
 * 初始化HealthUser详情对话框
 */
var HealthUsersInfoDlg = {
    healthUsersInfoData : {}
};

/**
 * 清除数据
 */
HealthUsersInfoDlg.clearData = function() {
    this.healthUsersInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
HealthUsersInfoDlg.set = function(key, val) {
    this.healthUsersInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
HealthUsersInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
HealthUsersInfoDlg.close = function() {
    parent.layer.close(window.parent.HealthUsers.layerIndex);
}

/**
 * 收集数据
 */
HealthUsersInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
    .set('age')
    .set('sex')
    .set('education')
    .set('job')
    .set('orgName')
    .set('count')
    .set('nutrition')
    .set('createDate')
    .set('updateDate');
}

/**
 * 提交添加
 */
HealthUsersInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/healthUsers/add", function(data){
        Feng.success("添加成功!");
        window.parent.HealthUsers.table.refresh();
        HealthUsersInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.healthUsersInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
HealthUsersInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/healthUsers/update", function(data){
        Feng.success("修改成功!");
        window.parent.HealthUsers.table.refresh();
        HealthUsersInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.healthUsersInfoData);
    ajax.start();
}

$(function() {

});
