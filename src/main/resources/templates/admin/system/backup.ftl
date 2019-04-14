<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
    <link rel="stylesheet" href="/static/plugins/layui/css/layui.css"/>
    <link rel="stylesheet" href="/static/admin/css/system/backup.css"/>
    <script src="/static/plugins/layui/layui.js"></script>
    <meta name="keywords" content="${Application.setting.keywords}"/>
    <meta name="description" content="${Application.setting.description}"/>
    <link rel="icon" href="${Application.setting.head}"/>
    <title>${Application.setting.title}</title>
</head>
<body>
<div class="layui-card">
    <div class="layui-card-header">数据库备份</div>
    <div class="layui-card-body">
        <button type="button" class="layui-btn" id="backup">
            <i class="layui-icon layui-icon-upload"></i>导出备份文件
        </button>
    </div>
</div>
<script type="text/javascript" src="/static/admin/js/system/backup.js"></script>
</body>
</html>