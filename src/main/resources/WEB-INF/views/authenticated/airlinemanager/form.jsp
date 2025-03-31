<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Flight Details</title>
</head>
<body>
    <h1>Flight Details</h1>
    <acme:form readonly="false">
        <acme:input-textbox code="authenticated.airlinemanager.flight.form.label.tag" path="tag" />
        <acme:input-checkbox code="authenticated.airlinemanager.flight.form.label.requiresSelfTransfer" path="requiresSelfTransfer" />
        <acme:input-textbox code="authenticated.airlinemanager.flight.form.label.cost" path="cost" />
        <acme:input-textbox code="authenticated.airlinemanager.flight.form.label.description" path="description" />
        <acme:input-moment code="authenticated.airlinemanager.flight.form.label.scheduledDeparture" path="scheduledDeparture" />
        <acme:input-moment code="authenticated.airlinemanager.flight.form.label.scheduledArrival" path="scheduledArrival" />
        
        <!-- Button to view legs associated with this flight -->
        <acme:button code="authenticated.airlinemanager.flight.form.button.legs" action="/authenticated/airlinemanager/leg/list?flightId=${id}" />
        
        <!-- Publish button, only visible if the flight is still in draft mode -->
        <c:if test="${flight.draftMode}">
            <acme:submit code="authenticated.airlinemanager.flight.form.button.publish" action="/authenticated/airlinemanager/flight/publish" />
        </c:if>
        
        <!-- Conditional submit for update or create -->
        <jstl:choose>
            <jstl:when test="${acme:anyOf(_command, 'show|update')}">
                <acme:input-checkbox code="authenticated.airlinemanager.flight.form.label.confirmation" path="confirmation" />
                <acme:submit code="authenticated.airlinemanager.flight.form.button.update" action="/authenticated/airlinemanager/flight/update" />
            </jstl:when>
            <jstl:when test="${_command == 'create'}">
                <acme:input-checkbox code="authenticated.airlinemanager.flight.form.label.confirmation" path="confirmation" />
                <acme:submit code="authenticated.airlinemanager.flight.form.button.create" action="/authenticated/airlinemanager/flight/create" />
            </jstl:when>
        </jstl:choose>
    </acme:form>
</body>
</html>