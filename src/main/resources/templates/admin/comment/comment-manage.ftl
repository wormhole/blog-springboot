<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
    <link rel="stylesheet" href="/static/plugins/layui/css/layui.css"/>
    <link rel="stylesheet" href="/static/admin/css/comment/comment-manage.css"/>
    <script src="/static/plugins/layui/layui.js"></script>
    <meta name="keywords" content="${Application.setting.keywords}"/>
    <meta name="description" content="${Application.setting.description}"/>
    <link rel="icon" href="${Application.setting.head}"/>
    <title>${Application.setting.title}</title>
</head>
<body>
<div class="layui-card">
    <div class="layui-card-header">评论管理</div>
    <div class="layui-card-body">
        <table class="layui-hide" id="comment-table" lay-filter="comment-table-1"></table>
    </div>
</div>
<script type="text/html" id="toolbar-col">
    <a class="layui-btn layui-btn-xs" lay-event="review"><i class="layui-icon layui-icon-vercode"/>审批</a>
    <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="unreview"><i class="layui-icon layui-icon-refresh"/>撤回</a>
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del"><i class="layui-icon layui-icon-delete"/>删除</a>
</script>
<script type="text/javascript" src="/static/admin/js/comment/comment-manage.js"></script>
</body>
</html>