$(document).ready(function(){
    $(".tab-button").click(function(){
        // console.log($(this).data("tab"));
       var tabId = $(this).data("tab");
        
       //分頁按鈕處理
        $(".tab-button").removeClass("active");
        $(this).addClass("active");

        //分頁內容處理
        $(".tab-pane").removeClass("active");
        $("#" + tabId).addClass("active");
    });
    
    
    $(".btn_yes").click(function() {
        $("input[type='checkbox']:checked").each(function() {
	        console.log($(this).val());
	        selectedIds.push($(this).val());
            $(this).closest("tr").find("td.status").text("審核通過").attr("value", "1");
        });
        $("#selectedIds").val(selectedIds); // 设置选中的请假记录 ID 到隐藏域
        $("#batchActionForm").attr("action", "/leave/update"); // 设置表单提交的目标地址
        $("#batchActionForm").submit(); // 提交表单

    });

    $(".btn_no").click(function() {
        $("input[type='checkbox']:checked").each(function() {
        console.log($(this).val());
            $(this).closest("tr").find("td.status").text("審核未通過")
        });
    });

		



});