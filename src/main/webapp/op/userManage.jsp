<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%--防止后退可以看到退出前的数据--%>
<%
    response.setHeader("Cache-Control","no-cache,no-store");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader ("Expires", -1);
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <link href="../ico/xy.ico" rel="shortcut icon" type="image/x-icon"/>
        <title>工资信息录入</title>

        <!-- 1. 导入CSS的全局样式 -->
        <link href="../css/bootstrap.min.css" rel="stylesheet">
        <!-- 3. jQuery导入，建议使用1.9以上的版本 -->
        <script src="../js/jquery-3.3.1.js"></script>
        <!-- 4. 导入bootstrap的js文件 -->
        <script src="../js/bootstrap.min.js"></script>
        <!-- 6. 导入布局js，共享header和footer-->
        <script type="text/javascript" src="../js/opInclude.js">
        </script>

        <style>
            th, td {
                text-align: center;
                vertical-align: middle;
            }
        </style>

        <script>
            window.onload = function () {
                <c:if test="${not empty opResult}">
                alert("${opResult}");
                </c:if>
            }
        </script>

    </head>
    <body>
        <div id="header"></div>


        <div class="container-fluid">

            <h3 style="text-align:center">员工账号管理表</h3>
            <h5 style="text-align: center;">（以姓名拼音排序，优先显示未激活账号，按Ctrl+F可对页面进行搜索）</h5>
            <div class="row">
                <div class="hidden-xs col-sm-1  col-lg-2"></div>
                <div class="col-xs-12 col-sm-10 col-lg-8">
                    <table border="1" class="table table-bordered table-hover" style="text-align: center;">
                        <tr class="success">
                            <th style="display:table-cell; vertical-align:middle">序号</th>
                            <th style="display:table-cell; vertical-align:middle">身份证号</th>
                            <th style="display:table-cell; vertical-align:middle">姓名</th>
                            <th style="display:table-cell; vertical-align:middle">激活状态</th>
                            <th style="display:table-cell; vertical-align:middle">激活账号</th>
                            <th style="display:table-cell; vertical-align:middle">恢复密码</th>
                            <th style="display:table-cell; vertical-align:middle">删除账号</th>
                        </tr>
                        <c:forEach items="${userList}" var="user" varStatus="i">
                            <tr>
                                <td style="display:table-cell; vertical-align:middle">${i.count}</td>
                                <td style="display:table-cell; vertical-align:middle">${user.emp_id}</td>
                                <td style="display:table-cell; vertical-align:middle">${user.emp_name}</td>
                                <td style="display:table-cell; vertical-align:middle">${user.emp_status=="Y"?"已经激活":"未激活"}</td>
                                <td style="display:table-cell; vertical-align:middle">
                                    <a href="javascript:if(confirm('确认激活 ${user.emp_name} 的账号？')) location='../opServlet/activate?emp_id=${user.emp_id}'"
                                       class="btn btn-sm btn-default">激活</a></td>
                                <td style="display:table-cell; vertical-align:middle">
                                    <a href="javascript:if(confirm('确认恢复 ${user.emp_name} 的账号密码为身份证号后六位？')) location='../opServlet/recoverPassword?emp_id=${user.emp_id}'"
                                       class="btn btn-sm btn-default">恢复</a></td>
                                <td style="display:table-cell; vertical-align:middle">
                                    <a href="javascript:if(confirm('确认删除 ${user.emp_name} 的账号？')) location='../opServlet/delUser?emp_id=${user.emp_id}'"
                                       class="btn btn-sm btn-default">删除</a></td>
                            </tr>
                        </c:forEach>
                    </table>

                    <!-- 显示的信息框 -->
                    <div class="alert alert-info alert-dismissible" role="alert" id="msg_block"
                         style="margin-top: 10px;display: none;" align="center">
                        <button type="button" class="close" data-dismiss="alert">
                            <span id="err_msg_block_x_y">&times;</span>
                        </button>
                        <strong>未添加数据</strong>
                    </div>


                </div>
            </div>
        </div>
        <!--与页底距离-->
        <div style="margin-bottom: 35px"></div>
    </body>
</html>