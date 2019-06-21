// 判断是否是整数
function isInteger(obj){
    return Math.round(obj)==obj;  //是整数，则返回true，否则返回false
}

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function(fmt)
{ //author: meizz
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}
//获取url中的参数
//例如 localhost:8088/serverestimate/files/groupDetail.html?type=edit&groupSn=efa7db1c395a44ef85366293f1dff858
//     request = getRequest();
//     globalGroupSn=request["groupSn"];
function getRequest() {
    var url = location.search; //获取url中"?"符后的字串
    var theRequest = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for(var i = 0; i < strs.length; i ++) {
            theRequest[strs[i].split("=")[0]]=(strs[i].split("=")[1]);
        }
    }
    return theRequest;
}

// 提示弹框
// 例如：$("#ddd").click(function () {
//     showInfoDialog("#dsss");
// })
showInfoDialog=function(div){
    div.show();
    div.dialog({
        height:200,
        width:400,
        resizable: false,
        buttons: {
            "确定": function() {
                div.hide();
                $( this ).dialog( "close" );
            }
        }
    });
}
removeclass=function(obj){
    $(obj).removeClass("mouse-over-row-color");
}
addclass=function(obj){
    $(obj).addClass("mouse-over-row-color");
}