<%-- 
    Document   : view
    Created on : 18.12.2017, 17:19:21
    Author     : (name = "Nermin Jukan", UCO = "<473370>")
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="false" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<my:pagetemplate>
<jsp:attribute name="title"><f:message key="excursion.admin"/></jsp:attribute>
<jsp:attribute name="body">

    <form method="post" action="${pageContext.request.contextPath}/excursion/delete/${excursion.id}">
        <button type="submit" class="btn btn-primary"><f:message key="delete"/></button>
    </form>


    <form:form method="post" action="${pageContext.request.contextPath}/excursion/edit/${excursion.id}"
                   modelAttribute="excursion" cssClass="form-horizontal">
            <div class="form-group ${destination_error?'has-error':''}">
                <form:label path="destination" cssClass="col-sm-2 control-label"><f:message key="excursion.destination"/></form:label>
                <div class="col-sm-10">
                    <form:input path="destination" cssClass="form-control"/>
                    <form:errors path="destination" cssClass="help-block"/>
                </div>
            </div>
            <div class="form-group ${description_error?'has-error':''}">
                <form:label path="description" cssClass="col-sm-2 control-label"><f:message key="excursion.description"/></form:label>
                <div class="col-sm-10">
                    <form:input path="description" cssClass="form-control"/>
                    <form:errors path="description" cssClass="help-block"/>
                </div>
            </div>
            <div class="form-group ${duration_error?'has-error':''}">
                <form:label path="duration" cssClass="col-sm-2 control-label"><f:message key="excursion.duration"/></form:label>
                <div class="col-sm-10">
                    <form:input path="duration" cssClass="form-control"/>
                    <form:errors path="duration" cssClass="help-block"/>
                </div>
            </div>
            <div class="form-group ${excursionDate_error?'has-error':''}">
                <form:label path="excursionDate" cssClass="col-sm-2 control-label"><f:message key="excursion.excursionDate"/></form:label>
                <div class="col-sm-10">
                    <form:input path="excursionDate" cssClass="form-control"/>
                    <form:label path="excursionDate" cssClass="help-block"><f:message key="date.format"/></form:label>
                    <form:errors path="excursionDate" cssClass="help-block"/>
                </div>
            </div>
            <div class="form-group ${price_error?'has-error':''}" >
                <form:label path="price" cssClass="col-sm-2 control-label"><f:message key="excursion.price"/></form:label>
                <div class="col-sm-10">
                    <form:input path="price" cssClass="form-control"/>
                    <form:errors path="price" cssClass="help-block"/>
                </div>
            </div>


            <button class="btn btn-primary" type="submit"><f:message key="excursion.edit"/></button>
        </form:form>

    <table class="table">
            <thead>
            <tr>
                <th><f:message key="trip.id"/></th>
                <th><f:message key="trip.name"/></th>
                <th><f:message key="trip.from"/></th>
                <th><f:message key="trip.to"/></th>
                <th><f:message key="trip.destination"/></th>
                <th><f:message key="trip.availableSpots"/></th>
                <th><f:message key="trip.price"/></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${excursion.trips}" var="trip">
                <tr>
                    <td><c:out value="${trip.id}"/></td>
                    <td><c:out value="${trip.name}"/></td>
                    <td><f:formatDate value="${trip.dateFrom}" pattern="yyyy-MM-dd"/></td>
                    <td><f:formatDate value="${trip.dateTo}" pattern="yyyy-MM-dd"/></td>
                    <td><c:out value="${trip.destination}"/></td>
                    <td><c:out value="${trip.availableSpots}"/></td>
                    <td><c:out value="${trip.price}"/></td>
                    <td>
                        <my:a href="/trip/view/${trip.id}" class="btn btn-primary"><f:message key="view"/></my:a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

    <!--<form method="post" action="${pageContext.request.contextPath}/excursion/resetTrips/${excursion.id}">-->
        <!--<button type="submit" class="btn btn-primary"><f:message key="excursion.reset.trips"/></button>-->
    <!--</form>-->
</jsp:attribute>
</my:pagetemplate>