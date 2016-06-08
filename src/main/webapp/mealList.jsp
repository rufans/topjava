<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meal List</title>
</head>
<body>
<h2><a href="index.html">Home</a></h2>
<h2>Meal list</h2>

<table>
    <th>ID</th>
    <th>First name</th>
    <th>Last name</th>
    <th>Year level</th>
    <c:forEach var="student" items="${studentList}" >
        <tr>
            <td>${student.studentId}</td>
            <td>${student.firstname}</td>
            <td>${student.lastname}</td>
            <td>${student.yearLevel}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
