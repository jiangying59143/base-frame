/**
 * 初始化详情对话框
 */
var LocationInfoDlg = {
    locationInfoData : {},
    validateFields: {
        latitude: {
            validators: {
                notEmpty: {
                    message: '纬度不能为空'
                }
            }
        },
        longitude: {
            validators: {
                notEmpty: {
                    message: '经度不能为空'
                }
            }
        },
        location: {
            validators: {
                notEmpty: {
                    message: '位置描述不能为空'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
LocationInfoDlg.clearData = function() {
    this.locationInfoData = {};
}

/**
 * 验证数据是否为空
 */
LocationInfoDlg.validate = function () {
    $('#locationInfoForm').data("bootstrapValidator").resetForm();
    $('#locationInfoForm').bootstrapValidator('validate');
    return $("#locationInfoForm").data('bootstrapValidator').isValid();
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
LocationInfoDlg.set = function(key, val) {
    this.locationInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
LocationInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
LocationInfoDlg.close = function() {
    parent.layer.close(window.parent.Location.layerIndex);
}

/**
 * 收集数据
 */
LocationInfoDlg.collectData = function() {
    this
    .set('id')
    .set('latitude')
    .set('location')
    .set('longitude')
}

/**
 * 提交添加
 */
LocationInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();
    if (!this.validate()) {
        return;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/location/add",
        function(data){
        Feng.success("添加成功!");
        window.parent.Location.table.refresh();
        LocationInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.locationInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
LocationInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();
    if (!this.validate()) {
        return;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/location/update", function(data){
        Feng.success("修改成功!");
        window.parent.Location.table.refresh();
        LocationInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.locationInfoData);
    ajax.start();
}

$("#location").autocomplete({
    minLength: 0,
    source: function (request, response) {
        var skeyword = $("#location").val();
        $.ajax({
            url: Feng.ctxPath + "/location/location_search",
            dataType: "json",
            data: {"skeyword": skeyword,"city" : $("#city").val()},
            success: function (data) {
                console.log(data);
                response( $.map( data, function( item ) {
                    var arr = new Array();
                    arr.push(item.longitude);
                    arr.push(item.latitude);
                    return {
                        label: item.name,
                        value: arr
                    }
                }));
            }
        });
    },
    focus: function (event, ui) {
        $("#location").val(ui.item.label);
        $("#longitude").val(ui.item.value[0]);
        $("#latitude").val(ui.item.value[1]);
        return false;
    },
    select: function (event, ui) {
        $("#location").val(ui.item.label);
        $("#longitude").val(ui.item.value[0]);
        $("#latitude").val(ui.item.value[1]);
        $("#map").html("<img src='https://restapi.amap.com/v3/staticmap?markers=mid,0xFF0000,A:" + ui.item.value[0] + "," + ui.item.value[1] +"&key=xxxxxx'/>");
        return false;
    }
});

var getLocationMap = function(longitude, latitude) {
    $.ajax({
        url: Feng.ctxPath + "/location/location_Map",
        dataType: "json",
        data: {"longitude": longitude, "latitude": latitude},
        success: function (data) {
            console.log(data);
            $("#map").innerHTML("<img src='"+data+"'/>");
        }
    });
}

$(function() {
    Feng.initValidator("locationInfoForm", LocationInfoDlg.validateFields);
});
