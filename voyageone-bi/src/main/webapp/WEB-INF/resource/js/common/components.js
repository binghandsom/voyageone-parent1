// 自定义组件
(function(bigdata) {
	
	// Ajax 提交组件
	// 用于提交及处理，Ajax 请求。
	
	function doAjaxPost(url, arg, success, fail, param){
		$.ajax({
			url: url,
			data: arg,
			type: "POST",
			dataType: "json",
			success: function(json){
				// 先进行超时判断
				if ($.isPlainObject(json)) {
					if (json.exception) {
						bigdata.alert(json.exception);
					} else {
						// 此处对reqResult进行转译到新的result属性
						json.valid = (json.reqResult == 1);
						if (json.valid) {
							success.call(this, json, param);
						} else if (json.result == "session_timeout") {
							$("#logoutBtn").click();
						} else if (json.reqResult == "0") {
							alert(json.reqResultInfo);
						}
					}
				} else {
					bigdata.alert("服务器返回的内容不正确。");
					if (window.console){
						console.log(json);
					}
				}
			},
			error: function () {
				(fail || function() {
					bigdata.alert("服务器出现未处理的错误。");
				}).apply(this, arguments);
			}
		});
	}

	function doAjaxDownload(url, arg){
		$.fileDownload(url, {
			data: arg,
			preparingMessageHtml: "We are preparing your report, please wait...",
			failMessageHtml: "There was a problem generating your report, please try again."
		});
	}
	
	this.bigdata = $.extend(bigdata, {
		post: doAjaxPost
	});

	this.bigdata = $.extend(bigdata, {
		download: doAjaxDownload
	});
	
})(window.bigdata || {});
