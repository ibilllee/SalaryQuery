<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--防止后退可以看到退出前的数据--%>
<%
    response.setHeader("Cache-Control","no-cache,no-store");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader ("Expires", -1);
%>

<!DOCTYPE html>
<html lang="zh-CN">
    <head>
        <meta charset="utf-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <link href="../ico/xy.ico" rel="shortcut icon" type="image/x-icon"/>
        <title>账户修改</title>

        <!-- 1. 导入CSS的全局样式 -->
        <link href="../css/bootstrap.min.css" rel="stylesheet">
        <!-- 2. jQuery导入，建议使用1.9以上的版本 -->
        <script src="../js/jquery-3.3.1.js"></script>
        <!-- 3. 导入bootstrap的js文件 -->
        <script src="../js/bootstrap.min.js"></script>
        <!-- 4. 导入布局js，共享header和footer-->
        <script type="text/javascript" src="../js/userInclude.js"></script>

    </head>
    <body>
        <div id="header"></div>

        <div class="container">
            <h3 style="text-align: center;margin-top:50px;">系统说明(用户版)</h3>
            <h4>1.工资信息查询功能</h4>
            <p style="line-height: 30px;font-size: 17px">
                ·使用者可以在主页查看自己的工资信息，列表以工资流水号倒序排序。点击详情可以查看工资详情。
            <p>
            <h4>2.账号管理功能</h4>
            <p style="line-height: 30px;font-size: 17px">
                ·使用者可以在右上角用户中心中，修改账号密码界面对自己的密码进行修改。<br>
                ·若使用者忘记了自己的密码，请联系管理员，管理员将会把使用者的密码恢复成身份证的后六位。
                为保证账号的安全性与信息的隐私性，<font color="red">请在初始化密码后及时修改密码</font>。
            <p>
            <h4>3.注意事项</h4>
            <p style="line-height: 30px;font-size: 17px">
                ·请在退出系统时点击右上角用户中心，退出登录<br>
            </p>
                <br><br><br><br><br>
            <div align="center"> 如有任何系统问题，请联系管理员或系统开发者:ibilllee@qq.com</div>
        </div>
        <!--与页底距离-->
        <div style="margin-bottom: 35px"></div>
    </body>
</html>