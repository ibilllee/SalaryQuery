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
            }
        </style>
        <script>
            $(function () {


                //确认录入数据按钮操作
                $("#confirm_btn").click(function () {
                    $.post("../opServlet/insertSalaryInfo", function (data) {
                        var msg_block = $("#msg_block");
                        msg_block.css("display", "block");
                        msg_block.html(data.errorMsg);
                    })
                });

                //取消录入数据按钮操作
                $("#cancel_btn").click(function () {
                    $.post("../opServlet/cancelInsertSalaryInfo", function () {
                        location.reload();
                    });
                });
            })
        </script>
    </head>
    <body>
        <div id="header"></div>

        <div class="container-fluid">
            <div align="center">
                <form action="../opServlet/uploadSalaryFile" enctype="multipart/form-data" method="post"
                      accept-charset="utf-8">
                    <input class="btn btn btn-default" style="width: 300px"
                           type="file" name="file1" accept=".xlsx,.xls" id="choose_btn">
                    <input class="btn btn btn-success" style="align-items: center;margin-top: 10px;"
                           type="submit" value="提交选中文件" id="submit_btn">
                </form>

            </div>
            <h3 style="text-align:center">工资信息录入预览表</h3>
            <div class="row">
                <div class="hidden-xs col-sm-1  col-lg-2"></div>
                <div class="col-xs-12 col-sm-10 col-lg-8">
                    <c:forEach items="${insertSalaryList}" var="salary" varStatus="s">
                        <div id="content_${s.count}" style="display: none">
                                ${salary.data_cont}
                        </div>
                    </c:forEach>
                    <table border="1" class="table table-bordered table-hover" style="text-align: center;">
                        <tr class="success">
                            <th style="display:table-cell; vertical-align:middle">编号</th>
                            <th style="display:table-cell; vertical-align:middle">身份证号</th>
                            <th style="display:table-cell; vertical-align:middle">姓名</th>
                            <th style="display:table-cell; vertical-align:middle">日期</th>
                            <th style="display:table-cell; vertical-align:middle">详情</th>
                        </tr>
                        <c:forEach items="${insertSalaryList}" var="salary" varStatus="s">
                            <tr>
                                <td style="display:table-cell; vertical-align:middle">${s.count}</td>
                                <td style="display:table-cell; vertical-align:middle">${salary.emp_id}</td>
                                <td style="display:table-cell; vertical-align:middle">${salary.emp_name}</td>
                                <td style="display:table-cell; vertical-align:middle">${salary.data_date}</td>
                                <td style="display:table-cell; vertical-align:middle">
                                    <button class="btn btn-default btn-sm" data-toggle="modal" data-target="#myModal"
                                            onclick="setWindowContent(${s.count})"> 查看详情
                                    </button>
                                </td>
                                <script>
                                    function setWindowContent(num) {
                                        var content_html = document.getElementById("content_" + num).innerHTML;
                                        document.getElementById("windowContent").innerHTML = content_html;
                                    }
                                </script>
                            </tr>
                        </c:forEach>

                    </table>

                    <!-- 显示的信息框 -->

                    <c:if test="${not empty checkSalaryInfo}">
                        <div class="alert alert-info alert-dismissible" role="alert" id="msg_block"
                             style="margin-top: 10px;" align="center">
                            <button type="button" class="close" data-dismiss="alert">
                                <span>&times;</span>
                            </button>
                            <strong style="text-align: center;font-size: 16px">异常提醒</strong>
                            <div><br>${checkSalaryInfo}</div>
                        </div>
                    </c:if>

                    <c:if test="${empty checkSalaryInfo}">
                        <div class="alert alert-info alert-dismissible" role="alert" id="msg_block"
                             style="margin-top: 10px;display: none;" align="center">
                            <button type="button" class="close" data-dismiss="alert">
                                <span>&times;</span>
                            </button>
                            <strong>提示</strong>
                        </div>
                    </c:if>

                    <%--                    底部按钮--%>
                    <div align="center">
                        <input class="btn btn btn-danger" style="align-items: center;;margin-bottom: 20px"
                               type="button" value="取消录入" id="cancel_btn">
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <input class="btn btn btn-success" style="align-items: center;margin-bottom: 20px"
                               type="button" value="预览后确认录入" id="confirm_btn">
                    </div>


                </div>
            </div>
        </div>

        <%--        详细信息模态框--%>
        <style>
            @media screen and (max-width: 1000px) {
                　　#myModalDialog {
                    width: 200px;
                }
            }

            @media screen and (min-width: 1001px) {
                　　#myModalDialog {
                    width: 600px;
                }
            }
        </style>
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
             aria-hidden="true">
            <div class="modal-dialog" id="myModalDialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="myModalLabel" style="text-align: center">工资详情</h4>
                    </div>
                    <div class="modal-body" id="windowContent"></div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    </div>
                </div>
            </div>
        </div>

        <!--与页底距离-->
        <div style="margin-bottom: 35px"></div>
    </body>
</html>