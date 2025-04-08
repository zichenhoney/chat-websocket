$(document).ready(function() {
	//判断用户名
    $("#name").blur(function() {
		if(this.value == "") {
			$("#cname").html("*用户名不能为空！");
			return;
		} else {
			$("#cname").html("");
		}
	});
  //判断密码非空
    $("#pwd").blur(function() {
		if(this.value == "") {
			$("#cpwd").html("*密码不能为空！");
			return;
		} else {
			$("#cpwd").html("");
		}
	});
    //异步处理用户名唯一性
    $("#name").blur(function() {
        var name = $("#name").val();
        if(name != ""){
        $.ajax({
            url : 'user_checkUname',
            type : 'post',
            data : {
                'username' : name
            },
            dataType :'json',
            success : function(data) {
                console.log(data.mess);
                var datajson = data.mess;
                if(datajson=="可用"){
                	$("#sub").removeAttr("disabled");
                }
                $("#cname").html(datajson);
            },
            error : function() {
                alert("异常！");
            }
        });
        }
    });
});

//判断密码和确认密码是否一致
function checkpass() {
    var flag = true;
    var pwd1 = document.getElementById("pwd").value;
    var pwd2 = document.getElementById("redpwd").value;
    if (pwd2 != pwd1) {
        alert("两次密码必须一致!");
        document.getElementById("renpass").innerHTML = "改吧";
        flag = false;
        ;
    } else {
        document.getElementById("renpass").innerHTML = "";
        flag = true;
    }
    return flag;
};

// 换一张验证码
function changecode() {
	$("#img1").attr("src","createImageAction.action?random="+ Math.random());
};


// 密码强度条形框
$(function() {
    var aStr = ["弱", "中", "强", "别忘了"];

    function checkStrong(val) {
        var modes = 0;
        if (val.length < 6) return 0;
        if (/\d/.test(val)) modes++; //数字
        if (/[a-z]/.test(val)) modes++; //小写
        if (/[A-Z]/.test(val)) modes++; //大写  
        if (/\W/.test(val)) modes++; //特殊字符
        if (val.length > 12) return 4;
        return modes;
    };
    
    
    $("#pwd").keyup(function() {
        var val = $(this).val();
        var num = checkStrong(val);
        switch (num) {
            case 0:
                break;
            case 1:
                $("#tips span").css('background', '').text('').eq(num - 1).css('background', '#CDCD00').text(aStr[num - 1]);
                break;
            case 2:
                $("#tips span").css('background', '').text('').eq(num - 1).css('background', 'skyblue').text(aStr[num - 1]);
                break;
            case 3:
                $("#tips span").css('background', '').text('').eq(num - 1).css('background', '#CD6090').text(aStr[num - 1]);
                break;
            case 4:
                $("#tips span").css('background', '').text('').eq(num - 1).css('background', '#9A32CD').text(aStr[num - 1]);
                break;
            default:
                break;
        }
    })
});


