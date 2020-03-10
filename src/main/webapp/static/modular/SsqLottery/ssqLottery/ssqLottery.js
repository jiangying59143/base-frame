/**
 * 双色球管理初始化
 */
var SsqLottery = {
    id: "SsqLotteryTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
SsqLottery.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'createBy', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'createDate', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'updateBy', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'updateDate', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'version', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'number', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'openDate', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'redBall1', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'redBall2', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'redBall3', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'redBall4', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'redBall5', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'redBall6', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'blueBall1', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'totalSale', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'prizePond', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'firstCount', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'secondCount', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'thirdCount', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'firstAmountMoney', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'secondAmountMoney', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'thirdAmountMoney', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
SsqLottery.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        SsqLottery.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加双色球
 */
SsqLottery.openAddSsqLottery = function () {
    var index = layer.open({
        type: 2,
        title: '添加双色球',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/ssqLottery/ssqLottery_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看双色球详情
 */
SsqLottery.openSsqLotteryDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '双色球详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/ssqLottery/ssqLottery_update/' + SsqLottery.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除双色球
 */
SsqLottery.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/ssqLottery/delete", function (data) {
            Feng.success("删除成功!");
            SsqLottery.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("ssqLotteryId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询双色球列表
 */
SsqLottery.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    SsqLottery.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = SsqLottery.initColumn();
    var table = new BSTable(SsqLottery.id, "/ssqLottery/list", defaultColunms);
    table.setPaginationType("client");
    SsqLottery.table = table.init();
});
