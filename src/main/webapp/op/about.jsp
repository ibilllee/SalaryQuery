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

    </head>
    <body>
        <div id="header"></div>

        <div class="container" id="textContent">
            <h3 style="text-align: center;margin-top:50px;">系统说明(管理员版)</h3>
            <h4>1.工资信息管理功能</h4>
            <p style="line-height: 30px;font-size: 17px">
                ·管理员可以对已上传的工资信息进行<font color="red">关键字查询，查看工资详情，删除工资信息的操作</font>，
                其中关键字查询到的结果并非需要输入整个字段完整的信息，如姓名可仅对姓"张"查询
            </p>
            <h4>2.工资信息录入功能</h4>
            <p style="line-height: 30px;font-size: 17px">
                ·在工资信息录入页面，管理员需要先选择EXCEL文件(.xls/.xlsx)，确认后点击提交选文件。<br>
                ·注意！文件需要<font color="red">遵从一定的规范</font>：第一行是表头，表头第一列到第三列分别为姓名，身份证号，日期
                从第四列开始的若干列为应发工资的项目，之后一列为应发合计。从应发合计后一列开始的若干列为应扣工资的项目，之后一列为应扣合计。
                紧接最后一列为合计工资。<br>
                ·请保证整个文件是<font color="red">没有任何格式样式的文件</font>，否则可能造成行数、列数或者其他的识别问题！<br>
                <br>
                ·在点击确认提交文件后，网页会自动加载内容，若文件较大，上传与识别时间可能更长。
                加载完成后，会在下方的工资信息录入预览表显示加载的内容，若其中有工资信息所属身份证未录入系统，
                或工资所属身份证在系统中的名字与上传文件中的名字不匹配，系统会给出提示信息。<br>
                ·请在预览完信息后做出确认：若文件无误，请点击预览后确认录入，若文件有误，请点击取消录入。
                点击预览后确认录入后，系统会对操作结果做出反馈提示。
            </p>
            <h4>3.员工账号管理功能</h4>
            <p style="line-height: 30px;font-size: 17px">
            ·管理员可以在员工账号管理页面对账号进行管理。<br>
            ·激活：员工在注册账号之后，需要管理员确认信息，并且激活账号，注册人方可登陆系统。<br>
                ·恢复密码：员工在忘记密码时，可以联系管理员，管理员在此界面可以恢员工的账号密码为<font color="red">其身份证的后六位</font>。<br>
            ·删除账号：若因故需要删除账号，可以直接点击删除账号的按钮。<br>
            此页面中，会优先展示未激活的账号，再按姓名拼音排序。
            </p>
            <h4>4.管理员账号管理功能</h4>
            <p style="line-height: 30px;font-size: 17px">
                ·管理员可以在右上角管理员中心的修改账号密码页面中，对该管理员账号密码进行修改。<br>
            </p>
            <h4>5.注意事项</h4>
            <p style="line-height: 30px;font-size: 17px">
                ·请在退出系统时点击右上角管理员中心，退出登录<br>
            </p>
            <br><br>
            <div align="center"> 如有任何系统问题，请联系系统开发者:ibilllee@qq.com</div>
        </div>

        <!--与页底距离-->
        <div style="margin-bottom: 35px"></div>
    </body>
</html>