var CONTEXT_PATH = "";

//V2 static
String.format = function () {
    if (arguments.length == 0) {
        return null;
    }
    let str = arguments[0];
    for (let i = 1; i < arguments.length; i++) {
        let re = new RegExp('\\{' + i + '\\}', 'gm');
        str = str.replace(re, arguments[i]);
    }
    return str;
};

$.fn.parseForm=function(){
	let serializeObj={};
	let array=this.serializeArray();
	let str=this.serialize();
	$(array).each(function(){
		if(serializeObj[this.name]){
			if($.isArray(serializeObj[this.name])){
				serializeObj[this.name].push(this.value);
			}else{
				serializeObj[this.name]=[serializeObj[this.name],this.value];
			}
		}else{
			serializeObj[this.name]=this.value;
		}
	});
	return serializeObj;
};


$(function(){
    $("#resetPwd").click(function(){
        if (check_data()) {
            $.ajax({
                type : "post",
                url : '/resetPwd',
                data : $('#resetPwdForm').parseForm(),
                success : function (result) {
                    if (result) {
                        if (result.status === '1') {
                            console.log('重置密码失败');
                            console.log(result.errMsg);
                        } else if (result.status === '0') {
                            console.log('重置密码成功');
                            if (result.msg) {
                                alert(result.msg);
                                location.href='/login';
                            }
                        } else {
                            console.log(result);
                        }
                    }
                }
            })
        } else {
            $("input").focus(clear_error);
        }
    });
});

function check_data() {
	if (!$("#password").val()) {
		$("#password").addClass("is-invalid");
		return false;
	}
	if (!$("#confirm-password").val().trim()) {
		$("#confirm-password").addClass("is-invalid");
		return false;
	}
	if (!$("#username").val()) {
		$("#username").addClass("is-invalid");
		return false;
	}
	if (!$("#kaptchaCode").val().trim()) {
		$("#kaptchaCode").addClass("is-invalid");
		return false;
	}
	if (!$("#emailVerifyCode").val().trim()) {
		$("#emailVerifyCode").addClass("is-invalid");
		return false;
	}
	var pwd1 = $("#password").val();
	var pwd2 = $("#confirm-password").val();
	if(pwd1 !== pwd2) {
		$("#confirm-password").addClass("is-invalid");
		return false;
	}
	return true;
}

function clear_error() {
	$(this).removeClass("is-invalid");
}

function refresh_kaptcha() {
    var path = CONTEXT_PATH + "/kaptcha?p=" + Math.random();
    $("#kaptcha").attr("src", path);
}

// 发送邮箱验证码功能
function sendEmailCodeForResetPwd() {
    var kaptchaCode = ($('#kaptchaCode').val() || '').trim();
    var username = ($('#username').val() || '').trim();
    if (kaptchaCode && username) {
        $.ajax({
            type: 'POST',
            url:"/sendEmailCodeForResetPwd",
            data: {
                username: username,
                kaptcha: kaptchaCode
            },
            success:function(result){
                if (result) {
                    if (result.status === '1') {
                        console.log('发送邮箱验证码失败');
                        alert(result.errMsg);
                    } else if (result.status === '0') {
                        console.log('发送邮箱验证码成功');
                        var div = $($('#hideDiv')[0]);
                        var clear = disableForAWhile(div, div);
                        alert(result.msg);
                    } else {
                        console.log(result);
                    }
                }
            }
        });
    } else {
        console.log('请输入完整的请求数据')
    }
}

// 倒计时
// hideDiv  和 secondTextDiv 选择器, 仅仅支持jquery选择器的第一个元素
function disableForAWhile(hideDiv, secondTextDiv, time, str, style) {
    // 备份
    let tmpHtml = hideDiv[0].outerHTML;
    let secondHtml = secondTextDiv[0].outerHTML;
    time = time || 60;
    str = str || '重新发送({1})';
    style = style || {'pointer-events': 'none', 'cursor': 'not-allowed'};
    hideDiv.css(style);
    // clear function
    secondTextDiv.text(String.format(str, time--));
    let clear = function (id) {
        clearInterval(id);
        hideDiv[0].outerHTML = tmpHtml;
        secondTextDiv[0].outerHTML = secondHtml;
    };
    let innerId = setInterval(() => {
        secondTextDiv.text(String.format(str, time--));
        if (time <= 0) {
            clear(innerId);
        }
    }, 1000);
    return {
        clear: () => {
            clear(innerId)
        }
    };
}
