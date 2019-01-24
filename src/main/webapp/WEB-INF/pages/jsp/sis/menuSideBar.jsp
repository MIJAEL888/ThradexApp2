<%@ include file="/WEB-INF/pages/jsp/includes.jsp" %>
    
    
<!--BEGIN SIDEBAR-->
<aside class="page-sidebar sidebar-offcanvas">
    <section class="sidebar">
        <ul class="sidebar-menu">
         <c:forEach items="${listMenu}" var="menu">
<%--          	<li>${menu.rolString}</li> --%>
        	<sec:authorize access="${menu.rolString}">	
        	
            <li><a href="<c:url value="${menu.url}"/>"><i class="${menu.icon}"></i>
            <span class="sidebar-text">${menu.label}</span></a>
            
            <c:if test="${!empty menu.listChild}">
                <ul class="nav nav-second-level">
                <c:forEach items="${menu.listChild}" var="menu2">
		    	<sec:authorize access="${menu2.rolString}">
		    	
                    <li><a href="<c:url value="${menu2.url}"/>"><i class="${menu2.icon}"></i>
                     	<span class="sidebar-text">${menu2.label}</span></a>
                            
                        <c:if test="${!empty menu2.listChild}">
                            
                        <ul class="nav nav-third-level">
                        	<c:forEach items="${menu2.listChild}" var="menu3">
							<sec:authorize access="${menu3.rolString}">
                            <li><a href="<c:url value="${menu3.url}"/>"><i class="${menu3.icon}"></i>
                            <span class="sidebar-text">${menu3.label}</span></a>
                            </li>
                            </sec:authorize>
                            </c:forEach>
                        </ul>
                        </c:if>
                    </li>
                 
                </sec:authorize>
                </c:forEach>
                </ul>
                </c:if>
            </li>
            </sec:authorize>
        </c:forEach>
        </ul>
    </section>
</aside>
<!--END SIDERBAR-->

