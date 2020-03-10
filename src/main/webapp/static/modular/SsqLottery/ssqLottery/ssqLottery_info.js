/**
 * 初始化双色球详情对话框
 */
var SsqLotteryInfoDlg = {
    ssqLotteryInfoData : {}
};

/**
 * 清除数据
 */
SsqLotteryInfoDlg.clearData = function() {
    this.ssqLotteryInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SsqLotteryInfoDlg.set = function(key, val) {
    this.ssqLotteryInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SsqLotteryInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
SsqLotteryInfoDlg.close = function() {
    parent.layer.close(window.parent.SsqLottery.layerIndex);
}

/**
 * 收集数据
 */
SsqLotteryInfoDlg.collectData = function() {
    this
    .set('id')
    .set('createBy')
    .set('createDate')
    .set('updateBy')
    .set('updateDate')
    .set('version')
    .set('number')
    .set('openDate')
    .set('redBall1')
    .set('redBall2')
    .set('redBall3')
    .set('redBall4')
    .set('redBall5')
    .set('redBall6')
    .set('blueBall1')
    .set('totalSale')
    .set('prizePond')
    .set('firstCount')
    .set('secondCount')
    .set('thirdCount')
    .set('firstAmountMoney')
    .set('secondAmountMoney')
    .set('thirdAmountMoney');
}

/**
 * 提交添加
 */
SsqLotteryInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/ssqLottery/add", function(data){
        Feng.success("添加成功!");
        window.parent.SsqLottery.table.refresh();
        SsqLotteryInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.ssqLotteryInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
SsqLotteryInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/ssqLottery/update", function(data){
        Feng.success("修改成功!");
        window.parent.SsqLottery.table.refresh();
        SsqLotteryInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.ssqLotteryInfoData);
    ajax.start();
}

$(function() {

});
