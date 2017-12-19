<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<my:pagetemplate title="Reservation's details">
    <jsp:attribute name="body">
        <table class="table">
            <c:if test="${authenticatedUser.isAdmin}">
            <tr>
                <td><f:message key="reservation.detail.customer"/></td>
                <td><a href="${pageContext.request.contextPath}/user/detail/${reservation.user.id}">
                            <c:out value="${reservation.user.name} ${reservation.user.surname}"/>
                </a>
                </td>
            </tr>
            </c:if>
            <tr>
                <td><f:message key="reservation.detail.trip"/></td>
                <td><a href="${pageContext.request.contextPath}/trip/detail/${reservation.trip.id}">
                            <c:out value="${reservation.trip.name}"/>
                </a></td>
            </tr>
            <tr>
                <td><f:message key="reservation.detail.price"/></td>
                <td><c:out value="${reservation.totalPrice} CZK"/></td>
            </tr>
             <tr>
                <td><f:message key="reservation.detail.state"/></td>
                <td><c:out value="${reservation.paymentState}"/></td>
            </tr>
            <c:if test="${empty reservation.excursions}">
                <tr>
                    <td><f:message key="reservation.detail.excursions"/></td>
                    <td><f:message key="reservation.detail.noexcursions"/></td>
                </tr>
            </c:if>
                <c:if test="${not empty reservation.excursions}">
                <tr>
                    <td><f:message key="reservation.detail.excursions"/></td>
                    <td>    
                    <c:forEach items="${reservation.excursions}" var="excursion">
                            <a href="${pageContext.request.contextPath}/excursion/detail/${excursion.id}">
                                ${excursion.destination}
                            </a> <c:out value="${excursion.price} "></c:out><strong><f:message key="reservation.list.currency"/></strong><br>
                            </c:forEach>
                     </td>   
                </tr>
                </c:if>    
        </table>
        <c:if test="${authenticatedUser.isAdmin}">
            <a href="${pageContext.request.contextPath}/reservation/delete/${reservation.id}" 
                class="btn btn-danger"><f:message key="reservation.list.delete.button"/></a>           
        </c:if> 
                <a href="${pageContext.request.contextPath}/reservation/edit/${reservation.id}" 
                class="btn btn-success"><f:message key="reservation.list.edit.button"/></a>               
             
    </jsp:attribute>
</my:pagetemplate>
