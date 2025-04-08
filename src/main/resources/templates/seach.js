$(document).ready(function() {
	$("#seach").click(function() {
		var keyword = $("#keyword").val();
		if(keyword == "") {
			window.location.href = "msg_list";
		} else {
			window.location.href = "msg_keylist?keyword=" + keyword;
		}
	});
});