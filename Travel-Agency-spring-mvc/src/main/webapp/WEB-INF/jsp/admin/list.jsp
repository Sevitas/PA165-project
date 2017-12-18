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

<my:pagetemplate title="User list">
    <jsp:attribute name="body">
        <table class="table">
            <caption>Users</caption>
            <thead>
                <tr>
                    <th>Id</th>
                    <th>Surname</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Detail</th>
                    <th>Edit</th>
                    <th>Delete</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td><c:out value="${user.id}" /></td>
                        <td><c:out value="${user.surname}" /></td>
                        <td><c:out value="${user.name}" /></td>
                        <td><c:out value="${user.email}" /></td>
                        <td><a href="${pageContext.request.contextPath}/admin/view/${user.id}" class="btn">View</a></td>
                        <td><a href="${pageContext.request.contextPath}/admin/edit/${user.id}" class="btn">Edit</a></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/delete/${user.id}" class="btn">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

    </jsp:attribute>
</my:pagetemplate>