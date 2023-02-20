/**
 * healthAnswer管理初始化
 */
var HealthAnswer = {
    id: "HealthAnswerTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
HealthAnswer.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: 'title', field: 'title', visible: true, align: 'center', valign: 'middle'},
            {title: 'answers', field: 'answers', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
HealthAnswer.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        HealthAnswer.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加healthAnswer
 */
HealthAnswer.openAddHealthAnswer = function () {
    var index = layer.open({
        type: 2,
        title: '添加healthAnswer',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/healthAnswer/healthAnswer_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看healthAnswer详情
 */
HealthAnswer.openHealthAnswerDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: 'healthAnswer详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/healthAnswer/healthAnswer_update/' + HealthAnswer.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除healthAnswer
 */
HealthAnswer.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/healthAnswer/delete", function (data) {
            Feng.success("删除成功!");
            HealthAnswer.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("healthAnswerId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询healthAnswer列表
 */
HealthAnswer.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    HealthAnswer.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = HealthAnswer.initColumn();
    var table = new BSTable(HealthAnswer.id, "/healthAnswer/list", defaultColunms);
    table.setPaginationType("client");
    HealthAnswer.table = table.init();
});
