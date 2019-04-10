layui.use(['jquery'], function () {
    var $ = layui.$;

    $('#verify-img').click(function () {
        $(this).attr('src', $(this).attr('src') + '?' + Math.random());
    });

    $('#register-btn').click(function () {
        var email = $('#email').val();
        var nickname = $('#nickname').val();
        var password = $('#password').val();
        var rePassword = $('#re-password').val();
        var vcode = $('#vcode').val();

        if (password !== rePassword) {
            $('#re-password').val('');
            $('blockquote').html('两次密码不一致');
            $('blockquote').removeClass('hidden');
            $('#verify-img').attr('src', '/api/vcode' + '?' + Math.random());
            return;
        }

        var user = {};
        user['email'] = email;
        user['nickname'] = nickname;
        user['password'] = password;
        user['vcode'] = vcode;
        var param = {'data': {"user": [user]}};

        registerAjax(param);
    });

    function registerAjax(param) {
        $.ajax({
            url: "/api/register",
            type: "post",
            data: JSON.stringify(param),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                if (response.status === 0) {
                    $('blockquote').html('<a href="/login">点击前往登陆页面</a>');
                } else {
                    if (response.data !== null) {
                        for (var attr in response.data) {
                            $('blockquote').html(response.data[attr]);
                            break;
                        }
                    } else {
                        $('blockquote').html(response.message);
                    }
                }
                $('blockquote').removeClass('hidden');
                $('#verify-img').attr('src', '/api/vcode' + '?' + Math.random());
            },
            error: function (response) {
                $('blockquote').html('服务器错误');
                $('blockquote').removeClass('hidden');
                $('#verif-img').attr('src', '/api/vcode' + '?' + Math.random());
            }
        });
    }
});