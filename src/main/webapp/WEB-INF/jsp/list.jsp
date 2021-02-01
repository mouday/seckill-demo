<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--引入jstl--%>
<%@include file="common/tag.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>秒杀列表页</title>
    <%--动态包含--%>
    <%@include file="common/header.jsp" %>
</head>
<body>

<div class="container">
<%--    ${pageContext.request.contextPath}--%>

    <div class="panel panel-default">
        <div class="panel-heading text-center">
            <h2>秒杀列表</h2>
        </div>

        <div class="panel-body">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>名称</th>
                        <th>库存</th>
                        <th>开始时间</th>
                        <th>结束时间</th>
                        <th>创建时间</th>
                        <th>详情页</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach var="item" items="${list}">
                        <tr>
                            <td>${item.name}</td>
                            <td>${item.number}</td>
                            <td>
                                <fmt:formatDate value="${item.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                            <td>
                                <fmt:formatDate value="${item.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                            <td>
                                <fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                            <td>
                                <a href="${item.id}/detail" target="_blank" class="btn btn-info">link</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>

            </table>
        </div>
    </div>
</div>


<%@include file="common/footer.jsp" %>

</body>
</html>