<%--
  User: Martin Sevcik <422365>
  Date: 17.12.2017
  Time: 23:37
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<my:pagetemplate>
    <jsp:attribute name="title">
        <fmt:message key="user.list.title"/>
    </jsp:attribute>
    <jsp:attribute name="body">
        <table class="table">
            <caption><fmt:message key="user.list.users"/></caption>
            <thead>
            <tr>
                <th><fmt:message key="id"/></th>
                <th><fmt:message key="surname"/></th>
                <th><fmt:message key="name"/></th>
                <th><fmt:message key="email"/></th>
                <th><fmt:message key="user.list.admin"/></th>
                <th><fmt:message key="reservations"/></th>
                <th><fmt:message key="user.list.detail"/></th>
                <th><fmt:message key="edit"/></th>
                <th><fmt:message key="user.list.remove"/></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="user" items="${users}">
                    <tr>
                        <td><c:out value="${user.id}" /></td>
                        <td><c:out value="${user.surname}" /></td>
                        <td><c:out value="${user.name}" /></td>
                        <td><c:out value="${user.email}" /></td>
                        <td>
                            <form method="POST" action="${pageContext.request.contextPath}/user/admin/${user.id}&${user.isAdmin}">
                                <c:choose>
                                    <c:when test="${user.isAdmin}">
                                        <button type="submit" class="btn btn-primary"><fmt:message key="user.list.unmake"/></button>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="submit" class="btn btn-primary"><fmt:message key="user.list.make"/></button>
                                    </c:otherwise>
                                </c:choose>
                            </form>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/reservation/list/${user.id}" class="btn btn-primary">Reservations</a>
                        </td>>
                        <td>
                            <a href="${pageContext.request.contextPath}/user/view/${user.id}" class="btn btn-primary">View</a>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/user/edit/${user.id}" class="btn btn-primary">Edit</a>
                        </td>
                        <td>
                            <form method="POST" action="${pageContext.request.contextPath}/user/remove/${user.id}">
                                <button type="submit" class="btn btn-primary"><fmt:message key="user.list.remove"/></button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

    </jsp:attribute>
</my:pagetemplate>
