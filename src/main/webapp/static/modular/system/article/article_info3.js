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
    if(key=="files"){
        var filenames = "";
        $(".filename").each(function(index,element){
            var value = $(this).val();
            if(index == $(".filename").size()-1){
                filenames = filenames + value;
            }else {
                filenames = filenames + value + ",";
            }
        });
        this.articleInfoData["files"] = filenames;
        var fileComments = "";
        $(".fileComment").each(function(index,element){
            var value = $(this).val();
            if(index == $(".fileComment").size()-1){
                fileComments += value;
            }else {
                fileComments += value + ",";
            }
        });
        this.articleInfoData["fileComments"] = fileComments;
    }else {
        this.articleInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    }
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
    this
        .set('id')
        .set('title')
        .set('privilege')
        .set("categoryId")
        .set("articleType")
        .set("files")
        .set("latitude").set("longitude").set("cityDesc");
    ;
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
    var ajax = new $ax(Feng.ctxPath + "/article/add2", function (data) {
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
});
