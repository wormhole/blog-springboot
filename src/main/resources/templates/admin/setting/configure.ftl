<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
    <link rel="stylesheet" href="/static/plugins/layui/css/layui.css"/>
    <link rel="stylesheet" href="/static/admin/css/setting/configure.css"/>
    <script src="/static/plugins/layui/layui.js"></script>
    <meta name="keywords" content="${Application.setting.keywords}"/>
    <meta name="description" content="${Application.setting.description}"/>
    <link rel="icon" href="${Application.setting.head}"/>
    <title>${Application.setting.title}</title>
</head>
<body>
<div class="layui-card" id="seo-setting">
    <div class="layui-card-header">SEO设置</div>
    <div class="layui-card-body">
        <div class="layui-form-item">
            <label class="layui-form-label">标题</label>
            <div class="layui-input-inline">
                <input name="title" class="layui-input" type="text" required id="title" value="${Application.setting.title}">
            </div>
            <div class="layui-form-mid layui-word-aux">网页标题，长度小于等于100</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">关键字</label>
            <div class="layui-input-inline">
                <input name="keywords" class="layui-input" type="text" required id="keywords" value="${Application.setting.keywords}">
            </div>
            <div class="layui-form-mid layui-word-aux">每个单词用英文逗号隔开，否则无效，长度小于等于100</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">描述</label>
            <div class="layui-input-inline">
                <textarea name="description" class="layui-textarea" id="description">${Application.setting.description}</textarea>
            </div>
            <div class="layui-form-mid layui-word-aux">长度小于等于100</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">版权信息</label>
            <div class="layui-input-inline">
                <textarea name="copyright" class="layui-textarea" id="copyright">${Application.setting.copyright}</textarea>
            </div>
            <div class="layui-form-mid layui-word-aux">页面底部的版权信息，一般为备案号等，长度小于等于100</div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn layui-btn-normal" id="seo-btn">保存修改</button>
            </div>
        </div>
    </div>
</div>
<div class="layui-card" id="base-setting">
    <div class="layui-card-header">常规设置</div>
    <div class="layui-card-body">
        <div class="layui-form-item">
            <label class="layui-form-label">昵称</label>
            <div class="layui-input-inline">
                <input name="nickname" class="layui-input" type="text" required id="nickname" value="${Application.setting.nickname}">
            </div>
            <div class="layui-form-mid layui-word-aux">页面左边栏显示昵称，建议与管理员昵称保持一致，长度小于等于100</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">签名</label>
            <div class="layui-input-inline">
                <textarea name="signature" class="layui-textarea" id="signature">${Application.setting.signature}</textarea>
            </div>
            <div class="layui-form-mid layui-word-aux">页面左边栏签名，长度小于等于100</div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn layui-btn-normal" id="base-btn">保存修改</button>
            </div>
        </div>
    </div>
</div>
<div class="layui-card" id="head-setting">
    <div class="layui-card-header">头像设置</div>
    <div class="layui-card-body">
        <div class="layui-row">
            <img src="${Application.setting.head}" class="layui-circle" id="head">
        </div>
        <input type="file" name="headImg" id="head-img" class="hidden">
        <div class="layui-row">
            <button class="layui-btn layui-btn-normal layui-btn-lg layui-show-lg-block" id="head-btn">
                保存头像
            </button>
        </div>
    </div>
</div>
<div class="layui-card" id="article-setting">
    <div class="layui-card-header">文章设置</div>
    <div class="layui-card-body">
        <div class="layui-form-item">
            <label class="layui-form-label">显示数量</label>
            <div class="layui-input-inline">
                <input name="limit" class="layui-input" type="number" required id="limit" value="${Application.setting.limit}">
            </div>
            <div class="layui-form-mid layui-word-aux">只能够填数字，建议为5</div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn layui-btn-normal" id="article-btn">保存修改</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="/static/admin/js/setting/configure.js"></script>
</body>
</html>