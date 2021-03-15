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
<html lang="zh-CN">
    <head>
        <meta charset="utf-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <!-- viewport视口：网页可以根据设置的宽度自动进行适配，在浏览器的内部虚拟一个容器，容器的宽度与设备的宽度相同。
        width: 默认宽度与设备的宽度相同
        initial-scale: 初始的缩放比，为1:1 -->
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="../ico/xy.ico" rel="shortcut icon" type="image/x-icon"/>
        <title>工资查询系统</title>

        <!-- 1. 导入CSS的全局样式 -->
        <link href="../css/bootstrap.min.css" rel="stylesheet">
        <!-- 2. jQuery导入，建议使用1.9以上的版本 -->
        <script src="../js/jquery-3.3.1.js"></script>
        <!-- 3. 导入bootstrap的js文件 -->
        <script src="../js/bootstrap.min.js"></script>
        <!-- 4. 导入布局js，共享header和footer-->
        <script type="text/javascript" src="../js/userInclude.js"></script>

        <style>
            td, th {
                text-align: center;
            }

        </style>

    </head>
    <body>
        <div id="header"></div>

        <div class="container-fluid">
            <h3 style="text-align:center">工资信息列表</h3>
            <div class="row">
                <div class="hidden-xs col-sm-1  col-lg-2"></div>
                <div class="col-xs-12 col-sm-10 col-lg-8">

                    <%--                    不可见的详细信息框--%>
                    <c:forEach items="${pb.list}" var="salary" varStatus="s">
                        <div id="content_${s.count}" style="display: none">
                                ${salary.data_cont}
                        </div>
                    </c:forEach>
                    <%--                        表格--%>
                    <table border="1" class="table table-bordered table-hover"
                           style="text-align: center;">
                        <tr class="success">
                            <th style="display:table-cell; vertical-align:middle">流水号</th>
                            <th style="display:table-cell; vertical-align:middle">姓名</th>
                            <th style="display:table-cell; vertical-align:middle">日期</th>
                            <th style="display:table-cell; vertical-align:middle">详情</th>
                        </tr>
                        <c:forEach items="${pb.list}" var="salary" varStatus="s">
                            <tr>
                                <td style="display:table-cell; vertical-align:middle">${salary.data_id}</td>
                                <td style="display:table-cell; vertical-align:middle">${salary.emp_name}</td>
                                <td style="display:table-cell; vertical-align:middle">${salary.data_date}</td>
                                <td style="display:table-cell; vertical-align:middle">
                                        <span class="btn btn-default btn-sm" data-toggle="modal"
                                              data-target="#myModal"
                                              onclick="setWindowContent(${s.count})"> 查看详情
                                        </span>
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


                    <%--页码--%>
                    <c:if test="${not empty pb}">
                        <nav aria-label="Page navigation" style="text-align: center">
                            <ul class="pagination">

                                    <%--首页--%>
                                <li>
                                    <a href="../userServlet/querySalaryInfoByPage?currentPage=1&rows=8">首页 </a>
                                </li>

                                    <%--前一页--%>
                                <c:if test="${pb.currentPage==1}">
                                    <li class="disabled">
                                        <span aria-label="Previous">
                                            <span aria-hidden="true">&laquo;</span>
                                        </span>
                                    </li>
                                </c:if>

                                <c:if test="${pb.currentPage!=1}">
                                    <li>
                                        <a href="../userServlet/querySalaryInfoByPage?currentPage=${pb.currentPage-1}&rows=8"
                                           aria-label="Previous">
                                            <span aria-hidden="true">&laquo;</span>
                                        </a>
                                    </li>
                                </c:if>


                                    <%--页码--%>
                                <c:forEach begin="${pb.currentPage-3> 0 ? pb.currentPage-3 : 1}"
                                           end="${pb.currentPage+3<=pb.totalPage?pb.currentPage+3:pb.totalPage}"
                                           var="i">
                                    <c:if test="${pb.currentPage==i}">
                                        <li class="active">
                                            <span href="../userServlet/querySalaryInfoByPage?currentPage=${i}&rows=8">${i}</span>
                                        </li>
                                    </c:if>
                                    <c:if test="${pb.currentPage!=i}">
                                        <li>
                                            <a href="../userServlet/querySalaryInfoByPage?currentPage=${i}&rows=8">${i}</a>
                                        </li>
                                    </c:if>
                                </c:forEach>

                                    <%--后一页--%>
                                <c:if test="${pb.currentPage==pb.totalPage}">
                                    <li class="disabled">
                                    <span aria-label="Next">
                                        <span aria-hidden="true">&raquo;</span>
                                    </span>
                                    </li>
                                </c:if>

                                <c:if test="${pb.currentPage!=pb.totalPage}">
                                    <li>
                                        <a href="../userServlet/querySalaryInfoByPage?currentPage=${pb.currentPage+1}&rows=8"
                                           aria-label="Next">
                                            <span aria-hidden="true">&raquo;</span>
                                        </a>
                                    </li>
                                </c:if>

                                    <%--末页--%>
                                <li>
                                    <a href="../userServlet/querySalaryInfoByPage?currentPage=${pb.totalPage}&rows=8">末页 </a>
                                </li>
                            </ul>
                        </nav>

                        <%--                        页数提示--%>
                        <div align="center">
                            <span class="btn btn btn-default" style="color: #337ab7;">
                                共有${pb.totalPage}页，${pb.totalCount}
                                条记录</span>
                        </div>
                    </c:if>

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
                    <div class="modal-body" id="windowContent">详情内容</div>
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