<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
    <link rel="stylesheet" href="/static/plugins/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/static/plugins/open-iconic/font/css/open-iconic-bootstrap.min.css"/>
    <link rel="stylesheet" href="/static/css/left.css"/>
    <link rel="stylesheet" href="/static/css/middle.css"/>
    <link rel="stylesheet" href="/static/css/footer.css"/>
    <link rel="stylesheet" href="/static/css/error.css"/>
    <script src="/static/plugins/jquery/jquery.min.js"></script>
    <script src="/static/plugins/bootstrap/js/bootstrap.min.js"></script>
    <meta name="keywords" content="${Application.setting.keywords}"/>
    <meta name="description" content="${Application.setting.description}"/>
    <link rel="icon" href="${Application.setting.head}"/>
    <title>${Application.setting.title}</title>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-lg-3">
            <div class="left">
                <img src="${Application.setting.head}" class="rounded-circle">
                <div class="nickname">${Application.setting.nickname?html}</div>
                <hr/>
                <div class="signature">${Application.setting.signature?html}</div>
                <div class="menu">
                    <div id="item" class="hidden">
                    <#list Application.menu as menu>
                        <a class="item btn <#if menu.url == '/'>select</#if>"
                           href="${menu.url}">${menu.name?html}</a>
                    </#list>
                    </div>
                    <a class="btn-item btn">
                        <span class="oi oi-menu" aria-hidden="true"></span>
                    </a>
                </div>
            </div>
        </div>
        <div class="col-lg-9">
            <div class="middle">
                <div class="header">
                    500
                </div>
                <div class="body">
                    <img src="/static/image/500.jpeg" class="img-fluid img-error"/>
                </div>
            </div>
            <div class="padding">
            </div>
        </div>
    </div>
</div>
<footer>${Application.setting.copyright}</footer>
<script src="/static/js/left.js"></script>
</body>
</html>