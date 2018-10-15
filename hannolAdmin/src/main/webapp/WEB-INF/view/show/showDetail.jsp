<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String cp = request.getContextPath();
%>
<script>
// 아래 스크립트 article.jsp 로 가져가면 페이지 처음 로딩 시 에는 없는 객체이므로 이벤트 적용이 안된다.!!!!!!!!
$(function() {
	$(".toggle").click(function() {
		var showInfoCode = $(this).attr("data-showInfoCode");
		var state = $("#moreScheduleLayout"+showInfoCode).css('display');
		
		if(state == 'none'){
			$("#moreScheduleLayout"+showInfoCode).show();
			$("#toggleButton"+showInfoCode).text("일정 보기 △");
		} else {
			$("#moreScheduleLayout"+showInfoCode).hide();
			$("#toggleButton"+showInfoCode).text("일정 보기 ▼"); 
		}
	});
});
</script>
<div>
<hr> 
	<div class="sub-title">
	     <div class="col-sm-11" style="display: inline-block;"><h3>상세 정보</h3></div>
	     <c:if test="${sessionScope.staff.authority == 'ROLE_ADMIN'}">
		     <button type="button" id="infoIcon" class="btn addIcon" onclick="createdShowInfoForm('${showCode}');">+</button>
		 </c:if>
		 <c:if test="${sessionScope.staff.authority == 'ROLE_STAFF'}">
			 &nbsp;
		 </c:if>
    </div>
    <br>
    <div id="showCreatedForm"></div>
	<!-- 상세정보 개수만큼 반복 구간 -->
	<c:forEach items="${list}" var="dto" varStatus="i">
		<div>	
			<div class="form-group">
				<div class="col-sm-2">시작  ~ 종료</div>
				<div class="col-sm-3">
					<input type="text" data-showInfoCode="${dto.showInfoCode}" name="eStartDate" value="${dto.startDate}" style="border: none; background-color: tomato; " size="10" readonly="readonly"> 
					~ 
					<input type="text" data-showInfoCode="${dto.showInfoCode}" name="eEndDate" value="${dto.endDate}" style="border: none; background-color: tomato; " size="10" readonly="readonly"></div> 
				<div class="col-sm-2">상영시간</div>
				<div class="col-sm-3"><mark>${dto.runningTime}분</mark></div>
				<div class="col-sm-1">
					<c:if test="${sessionScope.staff.authority == 'ROLE_ADMIN'}">
						<button type="button" class="btn" onclick="updateShowInfoForm('${dto.showInfoCode}')">수정</button>
		      		</c:if>
		      		<c:if test="${sessionScope.staff.authority == 'ROLE_STAFF'}">
				      		&nbsp;
		      		</c:if>
				</div>
			</div><br>
			<div class="form-group">
				<div class="col-sm-2">공연장소</div>
				<div class="col-sm-3"><mark>${dto.name}</mark></div>
				<div class="col-sm-2">시작시간</div>
				<div class="col-sm-3">
					<mark>
						<c:forEach items="${dto.showTime}" var="st" varStatus="status">
							${st}&nbsp;${(status.last) ? "" : " , "}
						</c:forEach>
					</mark>
				</div>
			</div><br>
			<form action="" name="showDetailForm" data-showInfoCode="${dto.showInfoCode}">
				<div class="form-group">
					<div class="col-sm-2">&nbsp;</div>
					<div class="col-sm-10 toggle" id="toggleButton${dto.showInfoCode}" data-showInfoCode="${dto.showInfoCode}">일정 보기 ▼</div><br>
					<div class="col-sm-10" id="moreScheduleLayout${dto.showInfoCode}" style="display: none;">
						<table  style="width: 80%; margin: 0px auto; border-spacing: 0px; border-collapse: collapse; border-top: 2px solid #005dab;">
							<tr align="center" height="30em" style="border-bottom: 1px solid #cccccc;"> 
						      <td width="30%">상영날짜</td>
						      <td width="60%" align="left" style="padding-left: 1em; padding-right: 1em;">시작시간</td> 
						      <td width="10%"></td>
							</tr>
							<c:forEach items="${dto.showScheduleList}" var="vo"> <!-- ShowSchedule의 list -->
								  <tr align="center" height="30em" style="border-bottom: 1px solid #cccccc;"> 
								      <td width="30%"><input name="screenDate" readonly="readonly" style="border: none;" value="${vo.screenDate}"></td>
								      <td width="60%" align="left" style="padding-left: 1em; padding-right: 1em;">
								      	<c:forEach items="${vo.startTime}" var="sst" varStatus="status">
								      		${sst.startTime}&nbsp;${(status.last) ? "" : " , "}
								      	</c:forEach>
								      </td>
								      <td width="10%">
								      		<c:if test="${sessionScope.staff.authority == 'ROLE_ADMIN'}">
										      		<button type="button" class="btn" onclick="updateShowScheduleForm('${vo.schCode}', '${showCode}');">수정</button>
								      		</c:if>
								      		<c:if test="${sessionScope.staff.authority == 'ROLE_STAFF'}">
										      		&nbsp;
								      		</c:if>
								      </td>
								  </tr>
							</c:forEach>
							<c:if test="${empty dto.showScheduleList}">
								<tr align="left" height="30em" style="border-bottom: 1px solid #cccccc;">
									<td></td>
									<td>등록된 일정이 없습니다.</td>
									<td></td>
								</tr> 
							</c:if>
				      		<c:if test="${sessionScope.staff.authority == 'ROLE_ADMIN'}">
								<tr align="center" height="30em" style="border-bottom: 1px solid #cccccc;"> 
							      <td width="30%"></td>
							      <td width="60%" align="left" style="padding-left: 1em; padding-right: 1em;">
							      			<button type="button" id="" class="btn" onclick="createdShowScheduleForm('${dto.showInfoCode}')">+</button>
							      </td> 
							      <td width="10%">&nbsp;</td>
								</tr>
				      		</c:if>
						</table>
					</div>
				</div>
			</form>
			
			<br>
		</div>
		<br><br><br><br><br><br>
	</c:forEach>	
	
	<c:if test="${empty list}">
		<table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			<tr height="100">
				<td align="center">
					등록된 상세정보가 없습니다.
				</td>
			</tr>
		</table>
	</c:if>
</div>

