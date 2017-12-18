<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<my:pagetemplate title="Reservation's edit">
    <jsp:attribute name="body">
        <h3><f:message key="reservation.list.edit.excursions"/>:</h3>
        <form action="${pageContext.request.contextPath}/reservation/update/${reservation.id}" method="get">
            <table class="table">
            
                <tr>
                    <td></td>
                    <td>
                        <f:message key="reservation.list.edit.destination"/>
                    </td>
                    <td>
                        <f:message key="reservation.list.edit.description"/>
                    </td>
                    <td>
                        <f:message key="reservation.detail.price"/>
                    </td>
                </tr>
                <c:forEach items="${excursions}" var="excursion">
                <tr>
                    <td>
                        <input type="checkbox" name="id" value="${excursion.id}">
                    </td>
                    <td>
                        ${excursion.destination}
                    </td>
                    <td>
                        ${excursion.description}
                    </td>
                    <td>
                        <c:out value="${excursion.price} "></c:out> <strong><f:message key="reservation.list.currency"/></strong>
                    </td>
                </tr>
            </c:forEach>
            </table>
            <input type="submit" value="Change and save" class="btn btn-success">
        </form>
           
    </jsp:attribute>
</my:pagetemplate>
