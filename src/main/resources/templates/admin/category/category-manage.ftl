<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
    <link rel="stylesheet" href="/static/plugins/layui/css/layui.css"/>
    <link rel="stylesheet" href="/static/admin/css/category/category-manage.css"/>
    <script src="/static/plugins/layui/layui.js"></script>
    <meta name="keywords" content="${Application.setting.keywords}"/>
    <meta name="description" content="${Application.setting.description}"/>
    <link rel="icon" href="${Application.setting.head}"/>
    <title>${Application.setting.title}</title>
</head>
<body>
<div class="layui-card">
    <div class="layui-card-header">分类</div>
    <div class="layui-card-body">
        <table class="layui-hide" id="category-table" lay-filter="category-table-1"></table>
    </div>
</div>
<script type="text/html" id="toolbar-head">
    <div class="layui-inline" lay-event="add"><i class="layui-icon layui-icon-add-1"/></div>
</script>
<script type="text/html" id="toolbar-col">
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del"><i class="layui-icon layui-icon-delete"/>删除</a>
</script>
<script type="text/html" id="add-edit">
    <div style="margin:20px">
        <div class="layui-form-item">
            <label class="layui-form-label">分类名称</label>
            <div class="layui-input-inline">
                <input class="layui-input" type="text" required id="categoryName" value="">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">分类编码</label>
            <div class="layui-input-inline">
                <input class="layui-input" type="text" required id="categoryCode" value="">
            </div>
        </div>
    </div>
</script>
<script type="text/javascript" src="/static/admin/js/category/category-manage.js"></script>
</body>
</html>