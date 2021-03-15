$(function () {
    $.get("/salary/user/header.jsp",function (data) {
        $("#header").html(data);
    });

    // $.get("footer.html",function (data) {
    //     $("#footer").html(data);
    // });
});