/**
 * order管理初始化
 */
var AlipayOrder = {
    id: "AlipayOrderTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
AlipayOrder.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: 'orderNo', field: 'orderNo', visible: true, align: 'center', valign: 'middle'},
            {title: 'totalAmount', field: 'totalAmount', visible: true, align: 'center', valign: 'middle'},
            {title: 'subject', field: 'subject', visible: true, align: 'center', valign: 'middle'},
            {title: 'body', field: 'body', visible: true, align: 'center', valign: 'middle'},
            {title: 'status', field: 'status', visible: true, align: 'center', valign: 'middle'},
            {title: 'createTime', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: 'updateTime', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
AlipayOrder.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        AlipayOrder.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加order
 */
AlipayOrder.openAddAlipayOrder = function () {
    var index = layer.open({
        type: 2,
        title: '添加order',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/alipayOrder/alipayOrder_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看order详情
 */
AlipayOrder.openAlipayOrderDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: 'order详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/alipayOrder/alipayOrder_update/' + AlipayOrder.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除order
 */
AlipayOrder.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/alipayOrder/delete", function (data) {
            Feng.success("删除成功!");
            AlipayOrder.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("alipayOrderId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询order列表
 */
AlipayOrder.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    AlipayOrder.table.refresh({query: queryData});
};

AlipayOrder.generateQR = function(){
    $("#qrcode").html("loading.....");
    var subject = $("#subject").val();
    var totalAmount = $("#totalAmount").val();
    $.post(Feng.ctxPath + "/alipay/submitOrder",
        {
            "subject":subject,
            "amount":totalAmount
        },
        function(result) {
            // 将二维码数据设置到页面中
            if(result.code == 1){
                $("#qrcode").html("<img src='data:image/png;base64," + result.content + "'/>");
            }else{
                $("#qrcode").html(result.content);
            }
        }
    );
};

AlipayOrder.refund = function(){
    $("#refundResult").html("loading.....");
    var orderId = $("#orderId").val();
    var refundAmount = $("#refundAmount").val();
    var refundReason = $("#refundReason").val();
    $.post(Feng.ctxPath + "/alipay/submitRefundOrder",
        {
            "orderId":orderId,
            "refundAmount":refundAmount,
            "refundReason":refundReason
        },
        function(result) {
            // 将二维码数据设置到页面中
            $("#refundResult").html(result.content);
        }
    );
};

$(function () {
    var defaultColunms = AlipayOrder.initColumn();
    var table = new BSTable(AlipayOrder.id, "/alipayOrder/list", defaultColunms);
    table.setPaginationType("client");
    AlipayOrder.table = table.init();
});
