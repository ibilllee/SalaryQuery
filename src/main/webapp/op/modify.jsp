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
        <script type="text/javascript" src="../js/opInclude.js"></script>

        <script>
            function displayErrMsg(data) {
                var err_msg_block = $("#err_msg_block");
                err_msg_block.css("display", "block");
                err_msg_block.html(data);
            }

            function clearErrMsg() {
                var err_msg_block = $("#err_msg_block");
                err_msg_block.css("display", "none");
                err_msg_block.html("状态提示");
            }

            function checkOpPasswordOld() {
                var password_old_obj = $("#op_password_old");
                var result = false;
                $.ajaxSettings.async = false;//同步提交表单
                $.post("../opServlet/checkOpPassword", {op_password: password_old_obj.val()}, function (data) {
                    if (!data.flag) {//验证失败
                        password_old_obj.css("border-color", "red");
                        displayErrMsg("旧密码错误");
                        result = false;
                    } else {
                        password_old_obj.css("border-color", "#cccccc");
                        clearErrMsg();
                        result = true;
                    }
                })
                $.ajaxSettings.async = true;//改回异步
                return result;
            }

            function checkOpPasswordConfirm() {
                var op_password_obj = $("#op_password_new");
                var op_password_conf_obj = $("#op_password_confirm");
                if (op_password_obj.val() !== op_password_conf_obj.val()) {
                    op_password_conf_obj.css("border-color", "red");
                    displayErrMsg("两次密码输入不一致");
                    return false;
                } else {
                    op_password_conf_obj.css("border-color", "#cccccc");
                    clearErrMsg();
                    return true;
                }
            }

            $(function () {
                $("#op_password_old").blur(checkOpPasswordOld);
                $("#op_password_confirm").blur(checkOpPasswordConfirm);

                $("#confirm_btn").click(function () {
                    var err_msg = "";
                    if (!checkOpPasswordOld()) err_msg += "旧密码错误<br>";
                    if (!checkOpPasswordConfirm()) err_msg += "两次密码输入不一致<br>";
                    if (err_msg !== "") {
                        err_msg += "修改失败！"
                        displayErrMsg(err_msg);
                    } else {
                        $.post("../opServlet/modifyOpPassword", {op_password: $("#op_password_new").val()}, function (data) {
                            var err_msg_block = $("#err_msg_block");
                            err_msg_block.css("display", "block");
                            err_msg_block.html(data.errorMsg);
                        })
                    }
                })
                $("#op_password_confirm").get(0).addEventListener("keyup", function (e) {
                    e.preventDefault()
                    if (e.keyCode === 13) {
                        $("#login_btn").click();
                    }
                })
            })

        </script>
    </head>
    <body>
        <div id="header"></div>

        <div class="container" style="width: 400px;">
            <h1 style="text-align: center;margin-top:50px;">管理员账户修改</h1>
            <form action="" id="password_form" method="post" accept-charset="utf-8">
                <div class="form-group">
                    <label for="op_password_old">旧密码：</label>
                    <input type="password" name="op_password_old" class="form-control" id="op_password_old"
                           placeholder="请输入旧密码"/>
                </div>

                <div class="form-group">
                    <label for="op_password_new">新密码：</label>
                    <input type="password" name="op_password_new" class="form-control" id="op_password_new"
                           placeholder="请输入新密码"/>
                </div>

                <div class="form-group">
                    <label for="op_password_confirm">确认新密码：</label>
                    <input type="password" name="op_password_confirm" class="form-control" id="op_password_confirm"
                           placeholder="请再次输入新密码"/>
                </div>

                <!-- 显示的信息框 -->
                <div class="alert alert-info alert-dismissible" role="alert" id="err_msg_block" style="display: none;">
                    <button type="button" class="close" data-dismiss="alert" id="err_msg_block_x">
                        <span id="err_msg_block_x_y">&times;</span>
                    </button>
                    <strong>状态提示</strong>
                </div>

                <div class="form-group" style="text-align: center;margin-top: 20px">
                    <input class="btn btn btn-primary" type="button" value="确认修改" id="confirm_btn">
                </div>
            </form>

        </div>

        <!--与页底距离-->
        <div style="margin-bottom: 35px"></div>
    </body>
</html>