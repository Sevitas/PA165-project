<%-- 
    Document   : list
    Created on : 18.12.2017, 17:18:50
    Author     : (name = "Nermin Jukan", UCO = "<473370>")
--%>


<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="false" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<my:pagetemplate>
<jsp:attribute name="title"><f:message key="excursions"/></jsp:attribute>
<jsp:attribute name="body">

    <my:a href="/excursion/new" class="btn btn-primary">
        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
        <f:message key="excursion.new"/>
    </my:a>

    <table class="table">
        <thead>
        <tr>
            <th><f:message key="excursion.id"/></th>
            <th><f:message key="excursion.excursionDate"/></th>
            <th><f:message key="excursion.duration"/></th>
            <th><f:message key="excursion.destination"/></th>
            <th><f:message key="excursion.description"/></th>
            <th><f:message key="excursion.price"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${excursions}" var="excursion">
            <tr>
                <td><c:out value="${excursion.id}"/></td>
                <td><f:formatDate value="${excursion.excursionDate}" pattern="dd-MM-yyyy"/></td>
                <td><c:out value="${excursion.duration}"/></td>
                <td><c:out value="${excursion.destination}"/></td>
                <td><c:out value="${excursion.description}"/></td>
                <td><c:out value="${excursion.price} €"/></td>
                <td>
                    <my:a href="/excursion/view/${excursion.id}" class="btn btn-primary"><f:message key="view"/></my:a>
                </td>
                <td>
                    <form method="post" action="${pageContext.request.contextPath}/excursion/delete/${excursion.id}">
                        <button type="submit" class="btn btn-danger"><f:message key="delete"/></button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>


</jsp:attribute>
</my:pagetemplate>
