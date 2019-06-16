<%--
  Created by IntelliJ IDEA.
  User: Танюшка
  Date: 10.06.2019
  Time: 22:51
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title> Meals</title>
</head>

<body>
<h3><a href="index.jsp">Home</a></h3>
<hr>
<h2>Meals</h2>
<table border=1>
    <thead>
        <tr>
            <th>Id_meal</th>
            <th>DateTime</th>
            <th>Description</th>
            <th>Calories</th>
            <th colspan=2>Action</th>
        </tr>
    </thead>
    <tbody>
    <jsp:useBean id="mealToList" scope="request" type="java.util.List"/>
    <c:forEach var="mealTo" items="${mealToList}">
        <tr style="color: ${mealTo.excess ? 'limegreen' : 'red'}">
            <td><c:out value="${mealTo.id}"/></td>
            <td>
            <fmt:parseDate value="${mealTo.dateTime}" pattern="yyyy-MM-dd'T'HH:mm"
                           var="parsedDateTime" type="both"/>
            <fmt:formatDate value="${parsedDateTime}" pattern="dd.MM.yyyy HH:mm"/>
        </td>
            <td><c:out value="${mealTo.description}"/></td>
            <td><c:out value="${mealTo.calories}"/></td>
            <td><a href="meals?action=edit&id_meal=<c:out value="${mealTo.id}"/>">Update</a></td>
            <td><a href="meals?action=delete&id_meal=<c:out value="${mealTo.id}"/>">Delete</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<p><a href="meals?action=insert">Add Meal</a></p>
</body>
</html>
