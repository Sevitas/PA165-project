<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<my:pagetemplate title="Reservations">
<jsp:attribute name="body">
    <%--<c:if test="${authenticatedUser.isAdmin()}">--%>
    <div class="row">
        <div class="btn">
            <form method="get" action="${pageContext.request.contextPath}/reservation/list/all">
            <button type="submit" class="btn btn-success"><f:message key="reservation.list.all"/></button>
            </form>
        </div>
        <div class="btn">
            <form method="get" action="${pageContext.request.contextPath}/reservation/list/sorted">
            <button type="submit" class="btn btn-success"><f:message key="reservation.list.sortbydate"/></button>
            </form>
        </div>
        <div class="btn">
            <form method="get" action="${pageContext.request.contextPath}/reservation/list/notpaid">
            <button type="submit" class="btn btn-success"><f:message key="reservation.list.sortnotpaid"/></button>
            </form>
        </div>   
    </div>     
    <%--</c:if>--%>   
    <table class="table">
        <thead>
        <tr>
            <c:if test="${authenticatedUser.isAdmin()}">
                <th><f:message key="reservation.list.id"/></th>
                <th><f:message key="reservation.list.user"/></th>
            </c:if>
            <th><f:message key="reservation.list.created"/></th>
            <th><f:message key="reservation.list.trip"/></th>
            <th><f:message key="reservation.list.excursions"/></th>
            <th><f:message key="reservation.list.totalprice"/></th>
            <th><f:message key="reservation.list.state"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${reservations}" var="reservation">
            <tr>
                <c:if test="${authenticatedUser.isAdmin()}">
                    <td>${reservation.id}</td>
                    <td>${reservation.user.getName()}</td>
                </c:if>    
                <td><f:formatDate value="${reservation.created}" pattern="yyyy-MM-dd"/></td>
                <td><a href="${pageContext.request.contextPath}/trip/detail/${reservation.trip.id}">${reservation.trip.name}</a>
                    
                </td>  
                <td>
                <c:forEach items="${reservation.excursions}" var="ex">
                    <a href="${pageContext.request.contextPath}/excursion/detail/${ex.id}">
                        ${ex.destination}<br>
                    </a>
                </c:forEach>
                </td>
                <td>${reservation.totalPrice}</td>
                <td>${reservation.paymentState}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/reservation/detail/${reservation.id}" class="btn btn-primary"><f:message key="reservation.list.detail.button"/></a>
                </td>
                <%--<c:if test="${authenticatedUser.isAdmin()}">--%>
                <td>
                    <a href="${pageContext.request.contextPath}/reservation/paid/${reservation.id}" class="btn btn-success"><f:message key="reservation.list.set.paid"/></a>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/reservation/notpaid/${reservation.id}" class="btn btn-success"><f:message key="reservation.list.set.notpaid"/></a>
                </td>
                <%--</c:if>--%>
                <td>
                    <a href="${pageContext.request.contextPath}/reservation/delete/${reservation.id}" class="btn btn-danger"><f:message key="reservation.list.delete.button"/></a>
                </td> 
            </tr>
        </c:forEach>
        </tbody>
    </table>

</jsp:attribute>
</my:pagetemplate>