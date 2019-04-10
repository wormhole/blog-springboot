<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
    <link rel="stylesheet" href="/static/plugins/layui/css/layui.css"/>
    <link rel="stylesheet" href="/static/admin/css/article/article-manage.css"/>
    <script src="/static/plugins/layui/layui.js"></script>
    <meta name="keywords" content="${Application.setting.keywords}"/>
    <meta name="description" content="${Application.setting.description}"/>
    <link rel="icon" href="${Application.setting.head}"/>
    <title>${Application.setting.title}</title>
</head>
<body>
<div class="layui-card">
    <div class="layui-card-header">文章管理</div>
    <div class="layui-card-body">
        <table class="layui-hide" id="article-table" lay-filter="article-table-1"></table>
    </div>
</div>
<script type="text/html" id="toolbar-col">
    <a class="layui-btn layui-btn-xs" lay-event="show"><i class="layui-icon layui-icon-ok"/>显示</a>
    <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="hidden"><i class="layui-icon layui-icon-close"/>隐藏</a>
    <a class="layui-btn layui-btn-warm layui-btn-xs" lay-event="export"><i class="layui-icon layui-icon-file"/>导出</a>
    <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="edit"><i class="layui-icon layui-icon-edit"/>编辑</a>
</script>
<script type="text/javascript" src="/static/admin/js/article/article-manage.js"></script>
</body>
</html>