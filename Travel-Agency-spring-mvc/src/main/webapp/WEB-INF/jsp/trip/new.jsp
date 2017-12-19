<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="false" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<my:pagetemplate>
<jsp:attribute name="title"><f:message key="trip.new"/></jsp:attribute>
<jsp:attribute name="body">

    <form:form method="post" action="${pageContext.request.contextPath}/trip/create"
               modelAttribute="tripCreate" cssClass="form-horizontal">
        <div class="form-group ${name_error?'has-error':''}">
            <form:label path="name" cssClass="col-sm-2 control-label"><f:message key="trip.name"/></form:label>
            <div class="col-sm-10">
                <form:input path="name" cssClass="form-control"/>
                <form:errors path="name" cssClass="help-block"/>
            </div>
        </div>
        <div class="form-group ${dateFrom_error?'has-error':''}">
            <form:label path="dateFrom" cssClass="col-sm-2 control-label"><f:message key="trip.from"/></form:label>
            <div class="col-sm-10">
                <form:input path="dateFrom" cssClass="form-control"/>
                <form:label path="dateFrom" cssClass="help-block"><f:message key="date.format"/></form:label>
                <form:errors path="dateFrom" cssClass="help-block"/>
            </div>
        </div>
        <div class="form-group ${dateTo_error?'has-error':''}">
            <form:label path="dateTo" cssClass="col-sm-2 control-label"><f:message key="trip.to"/></form:label>
            <div class="col-sm-10">
                <form:input path="dateTo" cssClass="form-control"/>
                <form:label path="dateTo" cssClass="help-block"><f:message key="date.format"/></form:label>
                <form:errors path="dateTo" cssClass="help-block"/>
            </div>
        </div>
        <div class="form-group ${destination_error?'has-error':''}">
            <form:label path="destination" cssClass="col-sm-2 control-label"><f:message key="trip.destination"/></form:label>
            <div class="col-sm-10">
                <form:input path="destination" cssClass="form-control"/>
                <form:errors path="destination" cssClass="help-block"/>
            </div>
        </div>
        <div class="form-group ${spots_error?'has-error':''}">
            <form:label path="availableSpots" cssClass="col-sm-2 control-label"><f:message key="trip.availableSpots"/></form:label>
            <div class="col-sm-10">
                <form:input path="availableSpots" cssClass="form-control"/>
                <form:errors path="availableSpots" cssClass="help-block"/>
            </div>
        </div>
        <div class="form-group ${price_error?'has-error':''}" >
            <form:label path="price" cssClass="col-sm-2 control-label"><f:message key="trip.price"/></form:label>
            <div class="col-sm-10">
                <form:input path="price" cssClass="form-control"/>
                <form:errors path="price" cssClass="help-block"/>
            </div>
        </div>


        <button class="btn btn-primary" type="submit"><f:message key="trip.create"/></button>
    </form:form>

</jsp:attribute>
</my:pagetemplate>