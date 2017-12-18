<%--
User: Martin Sevcik <422365>
  Date: 18.12.2017
  Time: 12:40
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<my:pagetemplate title="Login">
<jsp:attribute name="body">

    <form method="POST" action="${pageContext.request.contextPath}/auth/login">
        <h4>Please login.</h4>

        <label for="email">Email:</label>
        <input type="text" name="email" placeholder="example: user@gmail.com" required/>

        <label for="password">Password:</label>
        <input type="text" name="password" placeholder="Password" required/>

        <button class="btn btn-lg btn-primary" type="submit" >Sign in</button>
    </form>

</jsp:attribute>
</my:pagetemplate>