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
    <link rel="stylesheet" href="/static/css/index.css"/>
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
                <div class="name">${Application.setting.name?html}</div>
                <hr/>
                <div class="signature">${Application.setting.signature?html}</div>
                <div class="menu">
                    <div id="item" class="hidden">
                    <#list Application.menu as menu>
                        <a class="item btn <#if menu.url == select>select</#if>"
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
                ${header}
                </div>
                <div class="body">
                    <#list articleList as article>
                        <div class="article">
                            <div class="title">
                                <a href="${article.url}">${article.title}</a>
                            </div>
                            <div class="content">${article.preview}</div>
                            <div class="footer">
                                <div>
                                    <span class="oi oi-calendar" aria-hidden="true"></span>
                                    <span>${article.createDate?string('yyyy-MM-dd')}</span>
                                </div>
                                <div>
                                    <span class="oi oi-person" aria-hidden="true"></span>
                                    <span>${article.author}</span>
                                </div>
                                <div>
                                    <span class="oi oi-tags" aria-hidden="true"></span>
                                    <span>${article.categoryName}</span>
                                </div>
                                <div>
                                    <span class="oi oi-eye" aria-hidden="true"></span>
                                    <span>${article.hits}</span>
                                </div>
                                <div>
                                    <span class="oi oi-thumb-up" aria-hidden="true"></span>
                                    <span>${article.likes}</span>
                                </div>
                                <div>
                                    <span class="oi oi-chat" aria-hidden="true"></span>
                                    <span>${article.commentCount}</span>
                                </div>
                            </div>
                        </div>
                    </#list>
                </div>
            </div>
            <div class="bottom">
                <ul class="pagination justify-content-center">
                    <#if (page > 1)>
                        <li class="page-item"><a class="page-link" href="${path}?page=${page-1}">上一页</a></li>
                    <#else>
                        <li class="page-item disabled"><a class="page-link"">上一页</a></li>
                    </#if>
                    <#list start..end as i>
                        <li class="page-item  <#if (i == page)>active</#if>"><a class="page-link"
                                                                                href="${path}?page=${i}">${i}</a></li>
                    </#list>
                    <#if (page < pageCount)>
                        <li class="page-item"><a class="page-link" href="${path}?page=${page+1}">下一页</a></li>
                    <#else>
                        <li class="page-item disabled"><a class="page-link"">下一页</a></li>
                    </#if>
                </ul>
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