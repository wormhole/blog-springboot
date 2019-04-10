<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
    <link rel="stylesheet" href="/static/plugins/layui/css/layui.css"/>
    <link rel="stylesheet" href="/static/admin/css/media/image-manage.css"/>
    <script src="/static/plugins/layui/layui.js"></script>
    <meta name="keywords" content="${Application.setting.keywords}"/>
    <meta name="description" content="${Application.setting.description}"/>
    <link rel="icon" href="${Application.setting.head}"/>
    <title>${Application.setting.title}</title>
</head>
<body>
<button class="layui-btn layui-btn-lg layui-btn-normal"><i class="layui-icon layui-icon-delete"></i>删除图片</button>
<div class="layui-collapse">
    <#list map?keys as key>
    <div class="layui-colla-item">
        <h2 class="layui-colla-title">${key}</h2>
        <div class="layui-colla-content layui-show">
            <#list map[key] as url>
                <img src="${url}"/>
            </#list>
        </div>
    </div>
    </#list>
</div>
<script type="text/javascript" src="/static/admin/js/media/image-manage.js"></script>
</body>
</html>