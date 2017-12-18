<%--
  User: Martin Sevcik <422365>
  Date: 18.12.2017
  Time: 13:15
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:if test="${user != null}">
    <my:pagetemplate>
        <jsp:attribute name="title">
            <fmt:message key="user.edit.title" />
        </jsp:attribute>
        <jsp:attribute name="body">
            <form:form method="post" action="${pageContext.request.contextPath}/user/edit/${user.id}"
                       modelAttribute="userEdit">
                <table class="table">
                    <tr>
                        <td><label for="name" ><fmt:message key="user.edit.name"/></label></td>
                        <td><form:input id="name" name="name" path="name" type="text" value="${user.name}" /></td>
                    </tr>
                    <tr>
                        <td><label for="surname" ><fmt:message key="user.edit.surname"/></label></td>
                        <td><form:input required="true" id="surname" name="surname" path="surname" type="text" value="${user.surname}" /></td>
                    </tr>
                    <tr>
                        <td><label for="phoneNumber" ><fmt:message key="user.edit.phoneNumber"/></label></td>
                        <td><form:input id="phoneNumber" name="phoneNumber" path="phoneNumber" type="text" value="${user.phoneNumber}" pattern="^[0-9]{9}"/></td>
                    </tr>
                    <tr>
                        <td><label for="idCardNumber" ><fmt:message key="idCard"/></label></td>
                        <td><form:input id="idCardNumber" name="idCardNumber" path="idCardNumber" type="text" value="${user.idCardNumber}"/></td>
                    </tr>
                    <tr>
                        <td><label for="email" ><fmt:message key="email"/></label></td>
                        <td><form:input id="email" name="email" path="email" type="email" value="${user.email}"/></td>
                    </tr>
                    <tr>
                        <td>
                            <a href="${pageContext.request.contextPath}/reservation/list/${user.id}" class="btn btn-primary"><fmt:message key="reservations"/></a>
                        </td>
                    </tr>

                </table>
                <button class="btn btn-lg btn-primary" type="submit" ><fmt:message key="user.edit.save"/></button>
            </form:form>
        </jsp:attribute>
    </my:pagetemplate>
</c:if>

<c:if test="${user == null}">
    <my:pagetemplate>
        <jsp:attribute name="title">
            <fmt:message key="noFound"/>
        </jsp:attribute>
        <jsp:attribute name="body"/>
    </my:pagetemplate>
</c:if>
