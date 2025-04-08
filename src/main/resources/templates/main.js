function chooseall() {
    $(".box").prop({
        "checked" : true
    });
}

function fchoose() {
    $(".box").each(function() {
        if ($(this).is(":checked")) {
            $(this).prop({
                "checked" : false
            });
        } else {
            $(this).prop({
                "checked" : true
            });
        }
    });
}

function readed() {
    var choose = "";
    $(".box").each(function() {
        if ($(this).is(":checked")) {
            choose = choose + "-" + $(this).val();
        }
    });
    if (choose != "") {
        choose = choose.substring(1,choose.length);
        window.location.href = "msg_readedMessage?chooseBox="+choose+"&pu.currentpage="+curpage;
        setTimeout("tz()", 100);
//        $.ajax({
//            url : 'msg_readedMessage',
//            type : 'post',
//            datatype : 'json',
//            data : {
//                chooseBox : choose
//            },
//            success : function(data) {
//                
//            },
//            error : function() {
//                alert("异常！");
//            }
//        });
    }else{
        alert("请选择需要标记为已读的序号！");
    }
}

function deletem() {
    var choose = "";
    $(".box").each(function(){
        if($(this).is(":checked")){
            choose = choose + "-" + $(this).val();
        }
    });
    if(choose !=""){
        choose = choose.substring(1,choose.length);
        window.location.href = "msg_deleteMessage?chooseBox="+choose+"&pu.currentpage="+curpage;
        setTimeout("tz()", 100);
//        $.ajax({
//            url : 'msg_deleteMessage',
//            type : 'post',
//            datatype : 'json',
//            data : {
//                chooseBox : choose
//            },
//            success : function(data){
//                refresh();
//            },
//            error : function(){
//                alert("异常！");
//            }
//        });
    }else{
        alert("请选择需要删除的序号！");
    }
}

function tz(){
	window.location.href = "msg_list?pu.currentpage="+curpage;
}
