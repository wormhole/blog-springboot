<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
    <link rel="stylesheet" href="/static/plugins/layui/css/layui.css"/>
    <link rel="stylesheet" href="/static/admin/css/user/personal.css"/>
    <script src="/static/plugins/layui/layui.js"></script>
    <meta name="keywords" content="${Application.setting.keywords}"/>
    <meta name="description" content="${Application.setting.description}"/>
    <link rel="icon" href="${Application.setting.head}"/>
    <title>${Application.setting.title}</title>
</head>
<body>
<div class="layui-card" id="base-conf">
    <div class="layui-card-header">基本信息</div>
    <div class="layui-card-body">
        <div class="layui-form-item">
            <label class="layui-form-label">邮箱</label>
            <div class="layui-input-inline">
                <input class="layui-input" type="email" required id="email" value="${Session.user.email}">
            </div>
            <div class="layui-form-mid layui-word-aux">邮箱长度在1到100之间</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">昵称</label>
            <div class="layui-input-inline">
                <input class="layui-input" type="text" required id="nickname" value="${Session.user.nickname}">
            </div>
            <div class="layui-form-mid layui-word-aux">昵称长度只能在1到100之间</div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn layui-btn-normal" id="save-base-btn">保存修改</button>
            </div>
        </div>
    </div>
</div>
<div class="layui-card" id="pwd-conf">
    <div class="layui-card-header">修改密码</div>
    <div class="layui-card-body">
        <div class="layui-form-item">
            <label class="layui-form-label">旧密码</label>
            <div class="layui-input-inline">
                <input class="layui-input" type="password" id="old-password">
            </div>
            <div class="layui-form-mid layui-word-aux">请输入旧密码</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">新密码</label>
            <div class="layui-input-inline">
                <input class="layui-input" type="password" id="new-password">
            </div>
            <div class="layui-form-mid layui-word-aux">密码只能是英文数字下划线，长度大于等于6</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">确认密码</label>
            <div class="layui-input-inline">
                <input class="layui-input" type="password" id="re-password">
            </div>
            <div class="layui-form-mid layui-word-aux">请重新输入新密码</div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn layui-btn-normal" id="save-pwd-btn">保存密码</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="/static/admin/js/user/personal.js"></script>
</body>
</html>