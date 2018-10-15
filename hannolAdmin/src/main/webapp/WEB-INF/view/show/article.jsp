<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
   String cp = request.getContextPath();
%>

<script type="text/javascript">
function updateShow() {
	var f = document.showForm;
	
	if(!confirm('공연 정보를 수정하시겠습니까?'))
		return;
   	
	var url = '<%=cp%>/show/update?showCode=${dto.showCode}&tab=${tab}&pageNo=${pageNo}';
	location.href = url;
}

$(function() {
	var url = "<%=cp%>/show/showDetail";
	var query = "showCode=${dto.showCode}";
	ajaxHTML(url, "get", query, "showDetail");
});

function updateShowScheduleForm(schCode, showCode) {
	var url = '<%=cp%>/show/updateShowSchedule';
	var query = "schCode="+schCode + "&showCode=" + showCode;
	ajaxHTML(url, "get", query, "showCreatedForm");
}

function createdShowScheduleForm(showInfoCode) {
	var url = '<%=cp%>/show/insertShowSchedule';
	var query = "showInfoCode="+showInfoCode;
	ajaxHTML(url, "get", query, "showCreatedForm");
}

function createdShowScheduleSubmit(mode, showInfoCode) {
	var f = document.showSchedule;
	
	var str = f.screenDate.value;
	if(!str) {
		alert('상영 날짜를 입력하세요');
		return;
	}
	
	var flag = true;
	var oldScreenDate = $("form[name=showDetailForm][data-showInfoCode=" + showInfoCode + "]").find("input[name=screenDate]");
	oldScreenDate.each(function(i, e) {
		if(str == $(this).val()) {
			flag = false;
			return;
		}
	});	
	if(mode=='created' && !flag) {
		alert('이미 등록된 날짜는 다시 등록할 수 없습니다. 다른 날짜를 선택하세요');
		return;
	}
	
	// str이 날짜 범위 안에 있는지 비교
	var eStartDate = $("input[name=eStartDate][data-showInfoCode=" + showInfoCode + "]").val();
	var eEndDate = $("input[name=eEndDate][data-showInfoCode=" + showInfoCode + "]").val();
	var isInRange = stringToDate(str).getTime() >= stringToDate(eStartDate).getTime() && 
				stringToDate(str).getTime() <= stringToDate(eEndDate).getTime();
	if(!isInRange) {
		alert('상영 날짜는 시작일과 종료일 사이만 가능합니다.');
		return;
	}
	
	str = f.startTimeList;
//	alert(str.length);
	var cnt = 0;
	for(var i = 0; i < str.length; i++) {
//		alert(i + "::" + str[i].value);	// 입력을 안했을 경우 ''
		if(str[i].value=='')
			cnt++;
	}
//	alert(cnt);
	if(cnt >= 5) {
		alert('시작시간은 최소 1개 입력해야 합니다.');
		return;
	}
	
	f.action = "<%=cp%>/show/insertShowSchedule/" + mode;
	f.submit();
}


function createdShowInfoForm(showCode) {
	var icon = $("#infoIcon");
	if(icon.hasClass("addIcon")) {
		icon.removeClass("addIcon").addClass("minusIcon");
		icon.text("-");
		$("#showCreatedForm").show();
	} else if(icon.hasClass("minusIcon")) {
		icon.removeClass("minusIcon").addClass("addIcon");
		icon.text("+");
		$("#showCreatedForm").hide();
		return;
	} 
	
	var url = '<%=cp%>/show/insertShowDetail';
	var query = "showCode="+showCode;

	ajaxHTML(url, "get", query, "showCreatedForm");
}

function createdShowInfoSubmit(mode) {
	var f = document.showInfoCreated;
	var str;
	
	str = f.startDate.value;
	if(!str) {
		alert('시작날짜를 입력하세요.');
		f.startDate.focus();
		return;
	}
	
	str = f.endDate.value;
	if(!str) {
		alert('종료날짜를 입력하세요.');
		f.endDate.focus();
		return;
	}
	
	str = f.runningTime.value;
	if(!str) {
		alert('종료날짜를 입력하세요.');
		f.runningTime.focus();
		return;
	}

	str = f.facilityCode.value;
	if(!str) {
		alert('공연장소를 검색하여 입력하세요.');
		f.facilityName.focus();
		return;
	}
	
	// 가장 최근 endDate
	var enddate = $("#endDate").val(); // 2018-08-11
	if(enddate) {
		var startDate = f.startDate.value;
		var diff = getDiffDays(startDate, enddate);
		if(diff <= 0) {
			alert('일정이 진행중이므로 추가할 수 없습니다.');
			return;
		}
	} 
	
	f.action = "<%=cp%>/show/showInfo/"+mode;
	f.submit();
}

function updateShowInfoForm(showInfoCode) {
	var url = '<%=cp%>/show/updateShowDetail';
	var query = "showInfoCode="+showInfoCode;
	ajaxHTML(url, "get", query, "showCreatedForm");
}

//ajax 공통함수
function ajaxHTML(url, type, query, divId) {
	$.ajax({
		type:type,
		url:url,
		data:query,
		success:function(data){
			if($.trim(data)=="error"){
				listPage(1);
				
				return;
			}
			$("#"+divId).html(data);
		},
		beforeSend:function(jqXHR){
			jqXHR.setRequestHeader("AJAX", true);
		},
		error:function(jqXHR){
			if(jqXHR.status==403){
				location.href="<%=cp%>/member/login";
				return;
			}
			console.log(jqXHR.responseText);
		}
	});
}

$(function() {
	// 시간 추가
	$("#showDetail").on("click", ".addTime", function() {
		var spbut = $(this).parent().find("#showTimeList");
		var cnt = spbut.children().size();
	    if(cnt==5) {
		      alert("추가 가능한 시간 개수는 5개 입니다.");
		      return;
		}
	    
	    spbut.prepend("<input type='text' name='startTime' size='4' class='boxTF' placeholder='11:30'/>");
	    
	});
	
	// 날짜 추가 - 상영날짜 개수 : 종료날짜 - 시작날짜
	$("#showDetail").on("click", ".addDate", function() {
		var spbut = $(this).parent().parent().parent(); // table
		var cnt = spbut.children().size()-2; 			// 첫 행, 마지막 행 제외

	    var startDate = $("input[name='startDate']").val();
	    var endDate = $("input[name='endDate']").val();
	    var diff = getDiffDays(startDate, endDate);		// 날짜 추가 제한 개수
	    
	    if(cnt==diff) {
		      alert("추가 가능한 날짜 수는 (종료날짜 - 시작날짜)개 입니다.");
		      return;
		}

	    var htmlCode  = "<tr align='center' height='30em' style='border-bottom: 1px solid #cccccc;'>"; 
	    	htmlCode += "<td width='20%'><input name='screenDate' type='text' placeholder='2018-08-01' value='' size='15'</td>";
	        htmlCode += "<td width='80%' align='left' style='padding-left: 1em; padding-right: 1em;'>";
	        htmlCode += "<span id='showTimeList' style='text-align: left; margin-left: 15px;'>"; 
	        htmlCode += "<input type='text' name='startTime' size='4' class='boxTF' placeholder='11:30'></span> ";
	        htmlCode += "<input type='text' name='startTime' size='4' class='boxTF' placeholder='11:30'></span> ";
	        htmlCode += "<input type='text' name='startTime' size='4' class='boxTF' placeholder='11:30'></span> ";
	        htmlCode += "<input type='text' name='startTime' size='4' class='boxTF' placeholder='11:30'></span> ";
	        htmlCode += "<input type='text' name='startTime' size='4' class='boxTF' placeholder='11:30'></span> ";
	        htmlCode += "<button class='btn btn-default btn-info' type='button' onclick='searchList()'>등록</button>";
		    htmlCode += "</td></tr>";
	    $(this).parent().parent().before(htmlCode);
	});
	

});

function selectFacility(facilityCode, facilityName) {
	$("#facilityCode").val(facilityCode);
	$("#facilityName").val(facilityName);
}
 
function facilityList(){	
	// 시작일, 종료일을 입력해야 공연장 검색이 가능
	var startDate = $("input[name='startDate']").val();
	var endDate = $("input[name='endDate']").val();
	if(!startDate || !endDate) {
		alert('시작일과 종료일을 입력 후 공연장 검색이 가능합니다.');
		return;
	}
	
	var url = '<%=cp%>/show/facilityList';
	var query = 'startDate=' + startDate + "&endDate=" + endDate;
	
	// ajax -> facilityList.jsp
	$.ajax({
		type:"get",
		url:url,
		data:query,
		success:function(data){
			if($.trim(data)=="error"){
				listPage(1);
				return;
			}
			$("#facilityList").html(data);
			$("#facilityModal").modal('show');
		},
		beforeSend:function(jqXHR){
			jqXHR.setRequestHeader("AJAX", true);
		},
		error:function(jqXHR){
			if(jqXHR.status==403){
				location.href="<%=cp%>/member/login";
				return;
			}
			console.log(jqXHR.responseText);
		}
	});
} 
</script>

<div class="sub-container" style="width: 960px;">
    <div class="sub-title">
     <h3>공연정보</h3>
   </div>
    
    <form role="form" name="showForm" method="post" enctype="multipart/form-data"> 
      <div class="form-group"> 
         <div class="col-sm-2">사진</div>
         <div class="col-sm-10">
	         <c:if test="${empty dto.saveFilename}"><img src="<%=cp%>/resource/images/noimage.png"  style="padding: 5px;" onerror="this.src='<%=cp%>/resource/images/noimage.png'"></c:if>
			 <c:if test="${not empty dto.saveFilename}"><img src="<%=cp%>/uploads/show/${dto.saveFilename}" style="padding: 5px; width: 600px;" onerror="this.src='<%=cp%>/resource/images/noimage.png'"></c:if>
		</div> 
      </div><br>
      <div class="form-group">
	     <div class="col-sm-2">구분</div> 
         <div class="col-sm-10">${dto.gubunName}</div>  
      </div><br>
      <div class="form-group"> 
      	<div class="col-sm-2">공연명</div>
        <div class="col-sm-10">${dto.showName}</div> 
      </div><br> 
      <div class="form-group"> 
      	<div class="col-sm-2">내용</div>
         <div class="col-sm-10">${dto.memo}</div>  
      </div>
      <br><br><br>
      <div>
	       <table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
	           <tr height="45"> 
	            <td align="center" >
	            	<c:if test="${sessionScope.staff.authority == 'ROLE_ADMIN'}">
			              <button type="button" class="btn" onclick="updateShow();">공연정보 수정</button>
	            	</c:if>
	              <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/show/manage?tab=${tab}&pageNo=${pageNo}';">리스트</button>
	               <c:if test="${mode=='update'}">
	                   <input type="hidden" name="num" value="">
	                  <input type="hidden" name="page" value="">
	              </c:if>
	            </td>
	          </tr>
	      </table>
      </div>
   </form>
   
   <!-- 공연 상세정보 -->
   <div id="showDetail" style="padding-top: 100px;"></div>
</div>