layui.use(['table', 'jquery', 'layer'], function () {
    var table = layui.table;
    var $ = layui.$;
    var layer = layui.layer;

    var parameter = {
        id: 'comment-table',
        elem: '#comment-table',
        url: '/admin/article/list_comment',
        method: 'get',
        page: true,
        toolbar: '#toolbar-head',
        parseData: function (response) {
            return {
                code: response.status,
                message: response.message,
                count: response.data.count,
                data: response.data.items
            }
        },
        cols: [[
            {field: 'date', title: '日期', sort: true},
            {field: 'nickname', title: '昵称', sort: true},
            {field: 'email', title: '邮箱', sort: true},
            {field: 'website', title: '个人主页', sort: true},
            {field: 'articleTitle', title: '文章标题', sort: true},
            {field: 'replyTo', title: '回复谁', sort: true},
            {field: 'content', title: '评论内容'},
            {field: 'reviewStr', title: '是否审核'},
            {fixed: 'right', width: 210, title: '操作', toolbar: '#toolbar-col'}
        ]]
    };

    var tableIns = table.render(parameter);

    table.on('tool(comment-table-1)', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'del') {
            layer.confirm('确认删除该评论吗', function (index) {
                var param = [];
                param.push(data.id);
                deleteAjax(param);
                layer.close(index);
            });
        } else if (layEvent === 'review') {
            var param = [];
            param.push(data.id);
            reviewAjax(param, 1);
        } else if (layEvent === 'unreview') {
            var param = [];
            param.push(data.id);
            reviewAjax(param, 0);
        }
    });

    function deleteAjax(param) {
        $.ajax({
            url: "/admin/article/delete_comment",
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
                    layer.open({
                        type: 0,
                        content: response.message
                    });
                }
                tableIns.reload(parameter);
            },
            error: function (response) {
                layer.open({
                    type: 0,
                    content: "服务器错误"
                });
            }
        });
    }

    function reviewAjax(param, review) {
        $.ajax({
            url: "/admin/article/review_comment?review=" + review,
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
                    layer.open({
                        type: 0,
                        content: response.message
                    });
                }
                tableIns.reload(parameter);
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