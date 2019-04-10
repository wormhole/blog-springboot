layui.use(['table', 'jquery', 'layer'], function () {
    var table = layui.table;
    var $ = layui.$;
    var layer = layui.layer;

    var parameter = {
        id: 'category-table',
        elem: '#category-table',
        url: '/api/admin/category/list',
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
            {field: 'id', title: 'ID'},
            {field: 'name', title: '分类名', sort: true, edit: 'text'},
            {field: 'code', title: '编码', sort: true, edit: 'text'},
            {field: 'deleteTag', title: '是否能删除'},
            {fixed: 'right', width: 80, title: '操作', toolbar: '#toolbar-col'}
        ]]
    };

    var tableIns = table.render(parameter);

    table.on('toolbar(category-table-1)', function (obj) {
        if (obj.event === 'add') {
            layer.open({
                type: 1,
                title: '新增',
                skin: 'layui-layer-rim',
                area: ['420px', '240px'],
                btn: ['新增'],
                content: $('#add-edit').text(),
                yes: function (index, layero) {
                    var categoryName = $('#categoryName').val();
                    var categoryCode = $('#categoryCode').val();

                    var data = {};
                    data['code'] = categoryCode;
                    data['name'] = categoryName;
                    var param = {
                        data: {
                            category: [data]
                        }
                    };
                    insertCategoryAjax(param);
                    layer.close(index);

                }
            });
        }
    });

    table.on('tool(category-table-1)', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'del') {
            layer.confirm('确认删除该分类吗', function (index) {
                var param = {
                    data: {
                        category: [{id: data.id}]
                    }
                };
                deleteCategoryAjax(param);
                layer.close(index);
            });
        }
    });

    table.on('edit(category-table-1)', function (obj) {

        var data = {
            id: obj.data.id,
            code: obj.data.code,
            name: obj.data.name
        };
        var param = {
            data: {
                category: [data]
            }
        };
        updateCategoryAjax(param);

    });

    function updateCategoryAjax(param) {
        $.ajax({
            url: "/api/admin/category/update",
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

    function deleteCategoryAjax(param) {
        $.ajax({
            url: "/api/admin/category/delete",
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

    function insertCategoryAjax(param) {
        $.ajax({
            url: "/api/admin/category/insert",
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