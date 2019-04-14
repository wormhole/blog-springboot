<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
    <link rel="stylesheet" href="/static/plugins/layui/css/layui.css"/>
    <link rel="stylesheet" href="/static/admin/css/index.css"/>
    <script src="/static/plugins/layui/layui.js"></script>
    <meta name="keywords" content="${Application.setting.keywords}"/>
    <meta name="description" content="${Application.setting.description}"/>
    <link rel="icon" href="${Application.setting.head}"/>
    <title>${Application.setting.title}</title>
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <!-- 头部区域（可配合layui已有的水平导航） -->
    <div class="layui-header">
        <div class="layui-logo">虫洞博客</div>
        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item">
                <a href="javascript:;"><i class="layui-icon layui-icon-notice"></i>&nbsp;&nbsp;消息</a>
            </li>
            <li class="layui-nav-item">
                <a href="javascript:;">
                    <img src="${Application.setting.head}" class="layui-nav-img layui-circle">
                ${Session.user.nickname?html}
                </a>
                <dl class="layui-nav-child">
                    <dd><a href="javascript:;" data-url="/admin/user/personal" data-title="个人信息"
                           data-id="personal">个人信息</a></dd>
                    <dd><a href="/logout">注销</a></dd>
                </dl>
            </li>
        </ul>
    </div>

    <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
    <div class="layui-side layui-bg-black">
        <div class="layui-side-scroll">
            <ul class="layui-nav layui-nav-tree">
                <li class="layui-nav-item">
                    <a href="javascript:;"><i class="layui-icon layui-icon-read"></i>&nbsp;&nbsp;文章</a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:;" data-url="/admin/article/article-manage" data-title="文章管理"
                               data-id="article-manage">文章管理</a></dd>
                        <dd><a href="javascript:;" data-url="/admin/article/article-edit" data-title="文章编辑"
                               data-id="article-edit">文章编辑</a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item">
                    <a href="javascript:;"><i class="layui-icon layui-icon-note"></i>&nbsp;&nbsp;分类</a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:;" data-url="/admin/category/category-manage" data-title="分类管理"
                               data-id="category-manage">分类管理</a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item">
                    <a href="javascript:;"><i class="layui-icon layui-icon-reply-fill"></i>&nbsp;&nbsp;评论</a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:;" data-url="/admin/comment/comment-manage" data-title="评论管理"
                               data-id="comment-manage">评论管理</a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item">
                    <a href="javascript:;"><i class="layui-icon layui-icon-user"></i>&nbsp;&nbsp;用户</a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:;" data-url="/admin/user/personal" data-title="个人中心" data-id="personal">个人信息</a>
                        </dd>
                    </dl>
                </li>
                <li class="layui-nav-item">
                    <a href="javascript:;"><i class="layui-icon layui-icon-picture"></i>&nbsp;&nbsp;媒体</a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:;" data-url="/admin/media/image" data-title="图片管理"
                               data-id="image">图片管理</a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item">
                    <a href="javascript:;"><i class="layui-icon layui-icon-set-fill"></i>&nbsp;&nbsp;系统</a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:;" data-url="/admin/system/setting" data-title="常规设置"
                               data-id="setting">常规设置</a></dd>
                        <dd><a href="javascript:;" data-url="/admin/system/menu" data-title="菜单管理"
                               data-id="menu">菜单管理</a>
                        </dd>
                        <dd><a href="javascript:;" data-url="/admin/system/backup" data-title="数据备份"
                               data-id="backup">数据备份</a></dd>
                    </dl>
                </li>
            </ul>
        </div>
    </div>

    <!-- 内容主体区域 -->
    <div class="layui-body">
        <div class="layui-tab layui-tab-brief" lay-filter="tabs" lay-allowClose="true">
            <ul class="layui-tab-title">
                <li class="layui-this"><span class="layui-icon layui-icon-home"></span>&nbsp;&nbsp;首页</li>
            </ul>
            <div class="layui-tab-content">
                <div class="layui-tab-item layui-show">
                    <iframe src="/admin/dashboard" width="100%" height="100%" name="iframe" scrolling="auto"
                            class="iframe" framborder="0"></iframe>
                </div>
            </div>
        </div>
    </div>

    <!-- 底部固定区域 -->
    <div class="layui-footer">
        <center>${Application.setting.copyright}</center>
    </div>
</div>
<script type="text/javascript" src="/static/admin/js/index.js"></script>
</body>
</html>