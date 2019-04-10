var viewModel;

$(function () {
    editormd.markdownToHTML("editormd-view", {});
    viewModel = new ViewModel();
    ko.applyBindings(viewModel);

    $('.heart').click(function () {
        if ($(this).attr("rel") === "true") {
            return;
        } else {
            likeAjax(window.location.pathname);
        }
    });

    $('.reply').click(function () {
        viewModel.reply(true);
        var replyRef = $(this).closest('li').find('.name').text();
        viewModel.replyRef(replyRef);
    });

    $('.cancel').click(function () {
        viewModel.reply(false);
    });

    $('#comment-btn').click(function () {
        var data = {
            nickname: $('#nickname').val(),
            email: $('#email').val(),
            content: $('#content').val(),
            url: window.location.pathname
        };

        if ($('#website').val() !== '') {
            data['website'] = $('#website').val();
        }

        if (viewModel.reply() === true) {
            data['replyTo'] = viewModel.replyRef();
        }

        var param = {
            data: {
                comment: [data]
            }
        };

        commentAjax(param);
    });
});

function ViewModel() {
    var self = this;

    self.reply = ko.observable(false);
    self.replyRef = ko.observable('');
}

function commentAjax(param) {
    $.ajax({
        url: "/api/comment",
        type: "post",
        data: JSON.stringify(param),
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function (response) {
            if (response.status === 0) {
                layer.open({
                    type: 0,
                    content: '评论成功,待管理员审核后显示'
                });
                $('#content').val('');
                $('#email').val('');
                $('#nickname').val('');
                $('#website').val('');
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
                content: '服务器错误'
            });
        }
    });
}

function likeAjax(url) {
    var data = {
        url: url
    };
    var param = {
        data: {
            article: [data]
        }
    };
    $.ajax({
        url: "/api/like",
        type: "post",
        data: JSON.stringify(param),
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function (response) {
            if (response.status === 0) {
                $('#likes').text(response.data);
                $('.heart').css("background-position", "right");
                $('.heart').addClass("heartAnimation");
                $('.heart').attr('rel', 'true');
            }
        },
        error: function (response) {

        }
    });
}



