$(function(){
	$("#publishBtn").click(publish);
	$("#backIndexBtn").click(backIndex);
});

function publish() {
	$("#publishModal").modal("hide");
	// 获取标题和内容
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	// 发送异步请求
	$.post(
		CONTEXT_PATH + "/discuss/add",
		{"title": title, "content": content},
		// 处理服务端返回的数据
		function (data) {
			// String -> Json 对象
			data = $.parseJSON(data);
			// 在提示框 hintBody 显示服务端返回的消息
			$("#hintBody").text(data.msg);
			// 显示提示框
			$("#hintModal").modal("show");
			// 2s 后自动隐藏提示框
			setTimeout(function(){
				$("#hintModal").modal("hide");
				// 操作完成后，跳转到首页
				if (data.code == 0) {
					location.href = CONTEXT_PATH + "/index";
				}
			}, 2000);

		}
	)
}

function backIndex() {
	location.href = CONTEXT_PATH + "/index";
}