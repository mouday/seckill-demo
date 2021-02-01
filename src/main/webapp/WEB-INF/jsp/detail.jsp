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
    <div class="panel panel-default text-center">

        <div class="panel-head">
            <h2>${seckill.name}</h2>
        </div>

        <div class="panel-body">
            <h2 class="text-danger">
                <%-- 显示计时图标--%>
                <span class="glyphicon glyphicon-time"></span>
                <%-- 倒计时--%>
                <span class="glyphicon" id="seckill-box"></span>
            </h2>
        </div>
    </div>
</div>

<!-- 模态框（Modal） -->
<div class="modal fade" id="seckill-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <%--<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>--%>
                <h4 class="modal-title" id="myModalLabel">输入手机号</h4>
            </div>

            <div class="modal-body">
                <input id="inputPhone" type="text" class="form-control" name="phone" placeholder="填写手机号">
                <div id="message"></div>
            </div>

            <div class="modal-footer">
                <button id="submit" type="button" class="btn btn-primary">提交更改</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

<%@include file="common/footer.jsp" %>

<script type="text/javascript">
    $(function () {
        // El表达式传入参数
        seckill.detail.init({
            seckillId: ${seckill.id},
            startTime: ${seckill.startTime.time}, // 毫秒
            endTime: ${seckill.endTime.time}
        })
    })
</script>
</body>
</html>