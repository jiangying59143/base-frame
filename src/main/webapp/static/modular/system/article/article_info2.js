/**
 * 初始化通知详情对话框
 */
var ArticleInfoDlg = {
    articleInfoData: {},
    editor: null,
    validateFields: {
        title: {
            validators: {
                notEmpty: {
                    message: '标题不能为空'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
ArticleInfoDlg.clearData = function () {
    this.articleInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ArticleInfoDlg.set = function (key, value) {
    this.articleInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ArticleInfoDlg.get = function (key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ArticleInfoDlg.close = function () {
    parent.layer.close(window.parent.Article.layerIndex);
}

/**
 * 收集数据
 */
ArticleInfoDlg.collectData = function () {
    this.articleInfoData['content'] = ArticleInfoDlg.editor.txt.html();
    this.set('id').set('title').set('privilege').set("categoryId").set("articleType").set("files").set("latitude").set("longitude").set("cityDesc");
}

/**
 * 验证数据是否为空
 */
ArticleInfoDlg.validate = function () {
    $('#articleInfoForm').data("bootstrapValidator").resetForm();
    $('#articleInfoForm').bootstrapValidator('validate');
    return $("#articleInfoForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加
 */
ArticleInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/article/add", function (data) {
        Feng.success("添加成功!");
        window.parent.Article.table.refresh();
        ArticleInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.articleInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
ArticleInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/article/update", function (data) {
        Feng.success("修改成功!");
        window.parent.Article.table.refresh();
        ArticleInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.articleInfoData);
    ajax.start();
}

$(function () {
    Feng.initValidator("articleInfoForm", ArticleInfoDlg.validateFields);

    //初始化编辑器
    var E = window.wangEditor;
    var editor = new E('#editor');


    editor.customConfig.linkCheck = function (text, link) {
        return true // 返回 true 表示校验成功
        // return '验证失败' // 返回字符串，即校验失败的提示信息
    };

    editor.customConfig.linkImgCallback = function (url) {
        console.log(url) // url 即插入图片的地址
    };
    editor.customConfig.zIndex = 1;
    editor.customConfig.fontNames = [
        '宋体',
        '微软雅黑',
        'Arial',
        'Tahoma',
        'Verdana'
    ];
    editor.customConfig.colors = [
        '#000000',
        '#eeece0',
        '#1c487f',
        '#4d80bf',
        '#c24f4a',
        '#8baa4a',
        '#7b5ba1',
        '#46acc8',
        '#f9963b',
        '#ffffff'
    ];
    editor.customConfig.emotions = [
        {
            // tab 的标题
            title: 'emoji',
            // type -> 'emoji' / 'image'
            type: 'emoji',
            // content -> 数组
            content: ["😄","😃","😀","😊","😉","😍","😘","😚","😗","😙", "😜", "😝", "😛", "😳", "😁", "😔", "😌", "😒", "😞","😣", "😢", "😂", "😭", "😪", "😥", "😰",
                "😅", "😓", "😩", "😫", "😨", "😱", "😠",
                "😡", "😤", "😖", "😆", "😋", "😷",
                "😎", "😴", "😵", "😲", "😟", "😦", "😧",
                "😈", "👿", "😮", "😬", "😐", "😕", "😯",
                "😶", "😇", "😏", "😑"]
        }];

    // 图片上传相关  将图片大小限制为 5M

    editor.customConfig.uploadImgMaxSize = 5 * 1024 * 1024
    editor.customConfig.uploadImgServer = Feng.ctxPath + "/article/uploadImg"
    editor.customConfig.uploadFileName = 'file'

    // 上传图片到服务器
    // 可以自定义token
    // editor.customConfig.uploadImgParams = {
    //         token: 'abcdef12345'
    // }
    // 参数是否需要拼接到 url 中
    // editor.customConfig.uploadImgParamsWithUrl = true
    // 自定义 fileName
    // editor.customConfig.uploadFileName = 'yourFileName'
    // 自定义 header
    // editor.customConfig.uploadImgHeaders = {
    // 'Accept': 'text/x-json'
    // }
    // 或者 var editor = new E( document.getElementById('editor') )

    editor.create();
    editor.txt.html($("#contentVal").val());
    ArticleInfoDlg.editor = editor;

});
