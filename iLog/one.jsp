<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
	<c:when test="${param['inicio'] == 'true'}">		
		<jsp:forward page="/pub/paginas/login.jsf"></jsp:forward>				
	</c:when>
	<c:otherwise>		
		<c:redirect url="/pages/index.jsf?inicio=true"></c:redirect>
	</c:otherwise>
</c:choose>