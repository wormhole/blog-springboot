layui.use(['jquery'], function () {
    var $ = layui.$;

    $('#backup').click(function () {
        window.location.href = "/api/admin/backup/sql";
    })
});