/**
 * 管理初始化
 */
var Location = {
    id: "LocationTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Location.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '经度', field: 'longitude', visible: true, align: 'center', valign: 'middle'},
            {title: '纬度', field: 'latitude', visible: true, align: 'center', valign: 'middle'},
            {title: '位置描述', field: 'location', visible: true, align: 'center', valign: 'middle'},
    ];
};

/**
 * 检查是否选中
 */
Location.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Location.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加
 */
Location.openAddLocation = function () {
    var index = layer.open({
        type: 2,
        title: '添加',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/location/location_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看详情
 */
Location.openLocationDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/location/location_update/' + Location.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除
 */
Location.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/location/delete", function (data) {
            Feng.success("删除成功!");
            Location.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("locationId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询列表
 */
Location.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Location.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Location.initColumn();
    var table = new BSTable(Location.id, "/location/list", defaultColunms);
    table.setPaginationType("client");
    Location.table = table.init();
});
