/**
 * 初始化order详情对话框
 */
var AlipayOrderInfoDlg = {
    alipayOrderInfoData : {}
};

/**
 * 清除数据
 */
AlipayOrderInfoDlg.clearData = function() {
    this.alipayOrderInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AlipayOrderInfoDlg.set = function(key, val) {
    this.alipayOrderInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AlipayOrderInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
AlipayOrderInfoDlg.close = function() {
    parent.layer.close(window.parent.AlipayOrder.layerIndex);
}

/**
 * 收集数据
 */
AlipayOrderInfoDlg.collectData = function() {
    this.set('id')
    .set('orderNo')
    .set('totalAmount')
    .set('subject')
    .set('body')
    .set('status')
    .set('createTime')
    .set('updateTime');
}

/**
 * 提交添加
 */
AlipayOrderInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/alipayOrder/add", function(data){
        Feng.success("添加成功!");
        window.parent.AlipayOrder.table.refresh();
        AlipayOrderInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.alipayOrderInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
AlipayOrderInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/alipayOrder/update", function(data){
        Feng.success("修改成功!");
        window.parent.AlipayOrder.table.refresh();
        AlipayOrderInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.alipayOrderInfoData);
    ajax.start();
}

$(function() {

});
