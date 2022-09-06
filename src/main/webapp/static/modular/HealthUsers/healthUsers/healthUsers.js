/**
 * HealthUser管理初始化
 */
var HealthUsers = {
    id: "HealthUsersTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
HealthUsers.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'age', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'sex', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'education', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'job', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'orgName', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'count', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'nutrition', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'createDate', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'updateDate', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
HealthUsers.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        HealthUsers.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加HealthUser
 */
HealthUsers.openAddHealthUsers = function () {
    var index = layer.open({
        type: 2,
        title: '添加HealthUser',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/healthUsers/healthUsers_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看HealthUser详情
 */
HealthUsers.openHealthUsersDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: 'HealthUser详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/healthUsers/healthUsers_update/' + HealthUsers.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除HealthUser
 */
HealthUsers.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/healthUsers/delete", function (data) {
            Feng.success("删除成功!");
            HealthUsers.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("healthUsersId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询HealthUser列表
 */
HealthUsers.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    HealthUsers.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = HealthUsers.initColumn();
    var table = new BSTable(HealthUsers.id, "/healthUsers/list", defaultColunms);
    table.setPaginationType("client");
    HealthUsers.table = table.init();
});
