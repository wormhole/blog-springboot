layui.use(['form', 'jquery'], function () {
    var $ = layui.$;
    var form = layui.form;

    $('#verify-img').click(function () {
        $(this).attr('src', $(this).attr('src') + '?' + Math.random());
    });
});

