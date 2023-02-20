/**
 * 初始化chatMessage详情对话框
 */
var ChatMessageInfoDlg = {
    chatMessageInfoData : {}
};

/**
 * 清除数据
 */
ChatMessageInfoDlg.clearData = function() {
    this.chatMessageInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ChatMessageInfoDlg.set = function(key, val) {
    this.chatMessageInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ChatMessageInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ChatMessageInfoDlg.close = function() {
    parent.layer.close(window.parent.ChatMessage.layerIndex);
}

/**
 * 收集数据
 */
ChatMessageInfoDlg.collectData = function() {
    this.set('id')
    .set('createBy')
    .set('updateBy')
    .set('version')
    .set('message');
}

/**
 * 提交添加
 */
ChatMessageInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/chatMessage/add", function(data){
        Feng.success("添加成功!");
        window.parent.ChatMessage.table.refresh();
        ChatMessageInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.chatMessageInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
ChatMessageInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/chatMessage/update", function(data){
        Feng.success("修改成功!");
        window.parent.ChatMessage.table.refresh();
        ChatMessageInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.chatMessageInfoData);
    ajax.start();
}

$(function() {

});
