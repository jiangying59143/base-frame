/**
 * 管理初始化
 */
var Category = {
    id: "CategoryTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Category.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '主题名称', field: 'categoryName', visible: true, align: 'center', valign: 'middle'},
            {title: '未登录时默认', field: 'defaultTagDesc', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Category.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Category.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加
 */
Category.openAddCategory = function () {
    var index = layer.open({
        type: 2,
        title: '添加',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/category/category_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看详情
 */
Category.openCategoryDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/category/category_update/' + Category.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除
 */
Category.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/category/delete", function (data) {
            Feng.success("删除成功!");
            Category.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("categoryId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询列表
 */
Category.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Category.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Category.initColumn();
    var table = new BSTable(Category.id, "/category/list", defaultColunms);
    table.setPaginationType("client");
    Category.table = table.init();
});
