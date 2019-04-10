layui.use(['layer', 'jquery'], function () {

    var layer = layui.layer;
    var $ = layui.$;

    function updateBaseInfoAjax(param) {
        $.ajax({
            url: "/api/admin/user/update?type=base",
            type: "post",
            data: JSON.stringify(param),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                if (response.status === 0) {
                    layer.open({
                        type: 0,
                        content: response.message
                    });
                } else {
                    if (response.data !== null) {
                        for (var attr in response.data) {
                            layer.open({
                                type: 0,
                                content: response.data[attr]
                            });
                            break;
                        }
                    } else {
                        layer.open({
                            type: 0,
                            content: response.message
                        });
                    }
                }
            },
            error: function (response) {
                layer.open({
                    type: 0,
                    content: "服务器错误",
                });
            }
        });
    }

    function updatePasswordAjax(param) {
        $.ajax({
            url: "/api/admin/user/update?type=password",
            type: "post",
            data: JSON.stringify(param),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                if (response.status === 0) {
                    layer.open({
                        type: 0,
                        content: response.message
                    });
                } else {
                    if (response.data !== null) {
                        for (var attr in response.data) {
                            layer.open({
                                type: 0,
                                content: response.data[attr]
                            });
                            break;
                        }
                    } else {
                        layer.open({
                            type: 0,
                            content: response.message
                        });
                    }
                }
            },
            error: function (response) {
                layer.open({
                    type: 0,
                    content: "服务器错误",
                });
            }
        });
    }

    $('#save-base-btn').click(function () {

        var email = $('#email').val();
        var nickname = $('#nickname').val();

        var data = {};
        data['email'] = email;
        data['nickname'] = nickname;

        var param = {
            data: {
                user: [data]
            }
        };
        updateBaseInfoAjax(param);
    });

    $('#save-pwd-btn').click(function () {

        var oldPassword = $('#old-password').val();
        var newPassword = $('#new-password').val();
        var rePassword = $('#re-password').val();

        if (newPassword !== rePassword) {
            $('#re-password').val('');
            layer.open({
                type: 0,
                content: "两次密码不匹配"
            });
            return;
        }

        var data = {};
        data['oldPassword'] = oldPassword;
        data['password'] = newPassword;

        var param = {
            data: {
                user: [data]
            }
        };

        updatePasswordAjax(param);
    });

})
;