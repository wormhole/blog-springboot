layui.use(['layer', 'jquery'], function () {

    var layer = layui.layer;
    var $ = layui.$;

    function updateSEOAjax(param) {
        $.ajax({
            url: "/admin/system/setting/update",
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
                        if (response.data['title'] !== undefined) {
                            layer.open({
                                type: 0,
                                content: response.data['title']
                            });
                        } else if (response.data['keywords'] !== undefined) {
                            layer.open({
                                type: 0,
                                content: response.data['keywords']
                            });
                        } else if (response.data['description'] !== undefined) {
                            layer.open({
                                type: 0,
                                content: response.data['description']
                            });
                        } else if (response.data['copyright'] !== undefined) {
                            layer.open({
                                type: 0,
                                content: response.data['copyright']
                            });
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

    function updateBaseAjax(param) {
        $.ajax({
            url: "/admin/system/setting/update",
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
                        if (response.data['name'] !== undefined) {
                            layer.open({
                                type: 0,
                                content: response.data['name']
                            });
                        } else if (response.data['signature'] !== undefined) {
                            layer.open({
                                type: 0,
                                content: response.data['signature']
                            });
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

    function updateLimitAjax(param) {
        $.ajax({
            url: "/admin/system/setting/update",
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
                        if (response.data['limit'] !== undefined) {
                            layer.open({
                                type: 0,
                                content: response.data['limit']
                            });
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

    function updateHeadAjax(formData) {
        $.ajax({
            url: "/admin/system/setting/head",
            type: 'POST',
            data: formData,
            cache: false,
            processData: false,
            contentType: false,
            dataType: "json",
            success: function (response) {
                if (response.status === 0) {
                    $('#head').attr('src', response.data.head);
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
            },
            error: function (response) {
                layer.open({
                    type: 0,
                    content: "服务器错误",
                });
            }
        });
    }

    function getObjectURL(file) {
        var url = null;
        if (window.createObjectURL != undefined) {
            url = window.createObjectURL(file);
        } else if (window.URL != undefined) {
            url = window.URL.createObjectURL(file);
        } else if (window.webkitURL != undefined) {
            url = window.webkitURL.createObjectURL(file);
        }
        return url;
    }

    $('#head').click(function () {
        $('input[type="file"]').click();
    });

    $('input[type="file"]').change(function () {
        var file = this.files[0];
        $('#head').attr('src', getObjectURL(file));
    });

    $('#seo-btn').click(function () {
        var title = $('#title').val();
        var keywords = $('#keywords').val();
        var description = $('#description').val();
        var copyright = $('#copyright').val();

        var data = [];
        data.push({name: 'title', value: title});
        data.push({name: 'keywords', value: keywords});
        data.push({name: 'description', value: description});
        data.push({name: 'copyright', value: copyright});

        var param = {
            data: {
                setting: data
            }
        };

        updateSEOAjax(param);
    });

    $('#base-btn').click(function () {
        var name = $('#name').val();
        var signature = $('#signature').val();

        var data = [];
        data.push({name: 'name', value: name});
        data.push({name: 'signature', value: signature});

        var param = {
            data: {
                setting: data
            }
        };

        updateBaseAjax(param);
    });

    $('#article-btn').click(function () {
        var limit = $('#limit').val();

        var data = [];
        data.push({name: 'limit', value: limit});

        var param = {
            data: {
                setting: data
            }
        };

        updateLimitAjax(param);
    });

    $('#head-btn').click(function () {
        var formData = new FormData();
        formData.append('headImg', $('#head-img').get(0).files[0]);
        updateHeadAjax(formData);
    });

});