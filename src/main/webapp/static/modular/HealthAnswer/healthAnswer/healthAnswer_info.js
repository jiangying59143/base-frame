/**
 * 初始化healthAnswer详情对话框
 */
var HealthAnswerInfoDlg = {
    healthAnswerInfoData : {}
};

/**
 * 清除数据
 */
HealthAnswerInfoDlg.clearData = function() {
    this.healthAnswerInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
HealthAnswerInfoDlg.set = function(key, val) {
    this.healthAnswerInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
HealthAnswerInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
HealthAnswerInfoDlg.close = function() {
    parent.layer.close(window.parent.HealthAnswer.layerIndex);
}

/**
 * 收集数据
 */
HealthAnswerInfoDlg.collectData = function() {
    this.set('id')
    .set('title')
    .set('answers');
}

/**
 * 提交添加
 */
HealthAnswerInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/healthAnswer/add", function(data){
        Feng.success("添加成功!");
        window.parent.HealthAnswer.table.refresh();
        HealthAnswerInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.healthAnswerInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
HealthAnswerInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/healthAnswer/update", function(data){
        Feng.success("修改成功!");
        window.parent.HealthAnswer.table.refresh();
        HealthAnswerInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.healthAnswerInfoData);
    ajax.start();
}

$(function() {

});
