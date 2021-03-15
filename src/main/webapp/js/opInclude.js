$(function () {
    $.get("/salary/op/header.html",function (data) {
        $("#header").html(data);
    });

    // $.get("footer.html",function (data) {
    //     $("#footer").html(data);
    // });
});