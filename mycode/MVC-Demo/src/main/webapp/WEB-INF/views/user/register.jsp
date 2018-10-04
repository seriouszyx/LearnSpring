<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: seriouszyx
  Date: 2018/10/4
  Time: 18:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register Page</title>
</head>
<body>
    <form method="post" action="<c:url value="/user.html" />">
        <table>
            <tr>
                <td>用户名：</td>
                <td><input type="text" name="userName"></td>
            </tr>
            <tr>
                <td>密码：</td>
                <td><input type="password" name="password"></td>
            </tr>
            <tr>
                <td>姓名：</td>
                <td><input type="text" name="realName"></td>
            </tr>
            <tr>
                <td colspan="2"><input type="submit" name="提交"></td>
            </tr>
        </table>
    </form>
</body>
</html>
