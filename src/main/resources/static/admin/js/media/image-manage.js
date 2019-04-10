layui.use(['element', 'jquery', 'layer'], function () {
    var element = layui.element;
    var $ = layui.$;
    var layer = layui.layer;

    $('img').click(function () {
        $('img').removeClass('select');
        $(this).addClass('select');
    });

    $('button').click(function () {
        var url = $('.select').attr('src');
        if (url !== undefined) {
            layer.confirm('确认删除该图片吗', function (index) {
                deleteImage(url);
                layer.close(index);
            });
        }
    });

    function deleteImage(url) {
        $.ajax({
            url: "/api/admin/image/delete?url=" + url,
            type: "post",
            dataType: "json",
            success: function (response) {
                if (response.status === 0) {
                    layer.open({
                        type: 0,
                        content: response.message
                    });
                    $('.select').remove();
                } else {
                    layer.open({
                        type: 0,
                        content: response.message
                    });
                }
            },
            error: function (response) {
                layer.open({
                    type: 0,
                    content: "服务器错误"
                });
            }
        });
    }
});