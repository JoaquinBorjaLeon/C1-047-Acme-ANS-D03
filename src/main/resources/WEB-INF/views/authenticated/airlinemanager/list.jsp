<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Flight List</title>
</head>
<body>
    <h1>Flight List</h1>
    <acme:list navigable="true" show="show">
        <acme:list-column path="tag" code="authenticated.airlinemanager.flight.list.label.tag" />
        <acme:list-column path="requiresSelfTransfer" code="authenticated.airlinemanager.flight.list.label.requiresSelfTransfer" />
        <acme:list-column path="cost" code="authenticated.airlinemanager.flight.list.label.cost" />
        <acme:list-column path="description" code="authenticated.airlinemanager.flight.list.label.description" />
        <acme:list-column path="draftMode" code="authenticated.airlinemanager.flight.list.label.draftMode" />
    </acme:list>
    <acme:button code="authenticated.airlinemanager.flight.list.button.create" action="/authenticated/airlinemanager/flight/create" />
</body>
</html>