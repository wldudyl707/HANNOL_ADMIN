﻿<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
   String cp = request.getContextPath();
%>
<style>
	/**table**/
	.table{table-layout:fixed;}
	.table th,
	.table td{text-align:center;}
	.table td{text-overflow:ellipsis; overflow:hidden; white-space:nowrap;}
	.table th:nth-child(2),
	.table td:nth-child(2),
	.table th:nth-child(3),
	.table td:nth-child(3),
	.table td:nth-child(4){text-align:left;}
	
	.listData_no{text-align:center;}
	
	.noLine{border:0 none;}
	.btfTel.noLine{width:36px; padding-left:0; padding-right:0;}
	
	
	
	/**modal**/
	.modalTable{width:100%; margin-top:20px; color:#444444;}
	.modalTable th,
	.modalTable td{padding:10px 0;} 
	.modalTable th{width:110px; padding-top:13px; padding-right:20px; text-align:right; vertical-align:top;}
	
	.boxTF,
	.boxTA{width:280px; vertical-align:middle;}
	.boxTA[disabled]{background-color:#f3f3f3; padding:10px 15px;}
	.selectField{padding:5px; vertical-align:middle;}
	.boxTF.btfName{width:120px;}
	.btfTel{width:60px; text-align:center;}
	
	
	.btnBox{margin:40px 0; text-align:center;}
	.btnBox .btn{margin:0 3px;}
	
	
	/**dialog new style**/
	.ui-widget{font-family:"Nanum Gothic";}
	.ui-widget input, 
	.ui-widget select, 
	.ui-widget textarea, 
	.ui-widget button{font-family:"Nanum Gothic"; font-size:13px;}
	.ui-widget-header{color:#ffffff; background:#4c4c4c;}
	.ui-dialog .ui-dialog-title{font-size:16px;}
	.ui-dialog .ui-dialog-titlebar{padding:11px 1em;}
	.ui-dialog{padding:0;}
	.ui-draggable .ui-dialog-titlebar{border-bottom-left-radius:0; border-bottom-right-radius:0;}
	
</style>
<script type="text/javascript">

$(function() {
	 $(document).on("click","button[name=infoBtn]",function(){
		
		var usersCode = $(this).closest("tr").children().eq(1).text();
		var careerCode = $(this).closest("tr").children().eq(3).text();
		var epCode = $(this).closest("tr").children().eq(4).text();
	 	var query = "page="+${page}+"&usersCode="+usersCode+"&careerCode="+careerCode;
	 	$("#code").html(usersCode);
	 	
		var url = "<%=cp%>/career/info?"+query;
	 	
	 	location.href = url;
	}); 
	 
});

function searchList(){
	
	var usersCode = $("#searchValue").val();
	var temp = ${usersCode};
	
	if(temp=="" && usersCode =="")
		return;
	else if(usersCode=="")
		usersCode = temp;
	
	var url = "<%=cp%>/career/getlist";
	var query = "page="+${page}+"&usersCode="+usersCode;
	
	$.ajax({
		type:"post"
		,url:url	//서버의 주소
		,data:query	//서버로 보내는 값
		,success:function(data){
			$("#listLayout").html(data);
			$("#codeLayout").html(usersCode);
		}
		,error:function(e){
			console.log(e.responseText);
			$("#resultLayout").html("갱신에러발생!");	
		}
	}); 
}
$(function(data){
	searchList();
});
function createForm(){
	var usersCode = $("#codeLayout").text();
	
	location.href = "<%=cp%>/career/create?usersCode="+usersCode; 
}

$(function(){
	$(document).on("click","button[name=updateBtn]",function(){
		
		if(document.getElementById("isStaff") != null){
			var temp = document.getElementById("isStaff").innerHTML;
			return false;
		}
		
		$("#themeModal").dialog({
			title:"테마",
			width:300,
			height:250, 
			modal:true,
			show:"clip",
			hide:"clip"
		});
	});
});

function sendTheme(){
	var url = "<%=cp%>/career/themeUpdate";
	var usersCode = ${usersCode};
	var themeCode = $("#themeCode").val();
	
 	var query = "page="+${page}+"&usersCode="+usersCode+"&themeCode="+themeCode;
 	
	// AJAX-POST
	$.ajax({
		type:"post"
		,url:url	//서버의 주소
		,data:query	//서버로 보내는 값
		,success:function(data){
			$("#resultLayout").html("수정완료!");
			$("#themeLayout").html("테마배치 : "+data.themeName);
		}
		,error:function(e){
			console.log(e.responseText);
			$("#resultLayout").html("에러발생!");	
		}
	}); 
}
</script>
<div class="sub-container">
    
    <div class="sub-title">
	  <h3>직원 발령 <small>이력관리</small></h3>
	</div> 
    
    <div>
  		<form name="searchForm" method="post" action="<%=cp%>/notice/list">
          	<div class="col-xs-8 col-xs-offset-2">
			  		<div class="input-group">
			            <input type="hidden" name="searchKey" value="all">         
			            <input type="text" class="form-control" name="searchValue" id="searchValue" placeholder="검색할 직원의 사번을 입력해 주세요...">
			            <span class="input-group-btn">
			                <button class="btn btn-default btn-info" type="button" onclick="searchList()"><span class="glyphicon glyphicon-search"></span></button>
			            </span>
			        </div>
			</div>
		</form>
  	</div>
  	
  	<br><br>
		<div><button type="button" name="updateBtn" class="btn btn-default">테마변경</button> 
		 <button class="btn btn-default" onclick="createForm();">발령 입력</button></div>
    
    <div>

		검색결과 : <span id="codeLayout"></span>
		<div id="listLayout"></div>
		<div id="themeLayout"></div>
		
		<div id="resultLayout"></div>

    </div>
    

</div>