layui.use(['element', 'jquery'], function () {
    var element = layui.element;
    var $ = layui.$;

    $(".layui-nav a").click(function () {
        var id = $(this).attr('data-id');
        var title = $(this).attr('data-title');
        var url = $(this).attr('data-url');

        if (!url) {
            return;
        }

        var isActive = $('.layui-body .layui-tab-title').find("li[lay-id=" + id + "]");
        if (isActive.length > 0) {
            element.tabChange('tabs', id);
        } else {
            element.tabAdd('tabs', {
                title: title,
                content: '<iframe src="' + url + '" class="iframe" framborder="0" data-id="' + id + '" scrolling="auto" width="100%"  height="100%"></iframe>',
                id: id
            });
            element.tabChange('tabs', id);
        }
    });
});