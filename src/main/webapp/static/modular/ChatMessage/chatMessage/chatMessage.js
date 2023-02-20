/**
 * chatMessage管理初始化
 */
var ChatMessage = {
    id: "ChatMessageTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
ChatMessage.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: 'createBy', field: 'createBy', visible: true, align: 'center', valign: 'middle'},
            {title: 'updateBy', field: 'updateBy', visible: true, align: 'center', valign: 'middle'},
            {title: 'version', field: 'version', visible: true, align: 'center', valign: 'middle'},
            {title: 'message', field: 'message', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
ChatMessage.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        ChatMessage.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加chatMessage
 */
ChatMessage.openAddChatMessage = function () {
    var index = layer.open({
        type: 2,
        title: '添加chatMessage',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/chatMessage/chatMessage_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看chatMessage详情
 */
ChatMessage.openChatMessageDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: 'chatMessage详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/chatMessage/chatMessage_update/' + ChatMessage.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除chatMessage
 */
ChatMessage.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/chatMessage/delete", function (data) {
            Feng.success("删除成功!");
            ChatMessage.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("chatMessageId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询chatMessage列表
 */
ChatMessage.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    ChatMessage.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = ChatMessage.initColumn();
    var table = new BSTable(ChatMessage.id, "/chatMessage/list", defaultColunms);
    table.setPaginationType("client");
    ChatMessage.table = table.init();
});
