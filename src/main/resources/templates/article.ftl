<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
    <link rel="stylesheet" href="/static/plugins/open-iconic/font/css/open-iconic-bootstrap.min.css"/>
    <link rel="stylesheet" href="/static/plugins/editor.md/css/editormd.min.css"/>
    <link rel="stylesheet" href="/static/plugins/editor.md/css/editormd.preview.min.css"/>
    <link rel="stylesheet" href="/static/plugins/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/static/css/left.css"/>
    <link rel="stylesheet" href="/static/css/middle.css"/>
    <link rel="stylesheet" href="/static/css/footer.css"/>
    <link rel="stylesheet" href="/static/css/like.css"/>
    <link rel="stylesheet" href="/static/css/comment.css"/>
    <link rel="stylesheet" href="/static/css/article.css"/>
    <script src="/static/plugins/jquery/jquery.min.js"></script>
    <script src="/static/plugins/bootstrap/js/bootstrap.min.js"></script>
    <script src="/static/plugins/editor.md/lib/marked.min.js"></script>
    <script src="/static/plugins/editor.md/lib/prettify.min.js"></script>
    <script src="/static/plugins/editor.md/editormd.min.js"></script>
    <script src="/static/plugins/knockout/knockout-3.4.2.js"></script>
    <script src="/static/plugins/layer/layer.js"></script>
    <meta name="keywords" content="${Application.setting.keywords}"/>
    <meta name="description" content="${Application.setting.description}"/>
    <link rel="icon" href="${Application.setting.head}"/>
    <title>${title}</title>
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
                <div class="article">
                    <div class="title">
                    ${article.title}
                    </div>
                    <div class="info">
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
                            <span id="likes">${article.likes}</span>
                        </div>
                        <div>
                            <span class="oi oi-chat" aria-hidden="true"></span>
                            <span>${article.commentCount}</span>
                        </div>
                    </div>
                    <div class="content" id="editormd-view">
                        <textarea style="display:none;">${article.articleMd}</textarea>
                    </div>
                </div>
                <div class="like">
                    <div class="heart" <#if (isLike == true)>style="background-position:right"
                         <#else>style="background-position:left"</#if> rel="${isLike?string("true","false")}"></div>
                </div>
                <div class="comment">
                    <div class="comment-title">
                        所有评论:
                    </div>
                    <ul class="comment-list">
                        <#list commentList as comment>
                            <li class="comment-item row">
                                <div class="comment-item-left">
                                    <img src="/static/image/head.jpeg" class="rounded-circle img-fluid">
                                </div>
                                <div class="comment-item-right">
                                    <div class="comment-item-info">
                                        <span>
                                            <a href="${comment.website}" class="name">${comment.nickname}</a>
                                        </span>
                                        <span class="time">${comment.date?string('yyyy-MM-dd HH:mm:ss')}</span>
                                        <span>
                                            <a href="javascript:;" class="reply">回复</a>
                                        </span>
                                    </div>
                                    <hr>
                                    <div class="comment-content">
                                        <#if (comment.replyTo)??>
                                            <a href="javascript:;">@${comment.replyTo}</a>
                                        </#if>
                                        <span>${comment.content}</span>
                                    </div>
                                </div>
                            </li>
                        </#list>
                    </ul>
                    <div class="comment-reply" data-bind="visible:reply">
                        <span>回复</span>
                        <span>
                            <a href="javascript:;" class="reply-to" data-bind="text:replyRef"></a>
                        </span>
                        <span>
                            <a href="javascript:;" class="cancel">
                                取消
                            </a>
                        </span>
                    </div>
                    <div class="comment-input">
                        <textarea class="comment-text form-control" placeholder="请开始你的表演..." id="content"></textarea>
                    </div>
                    <div class="comment-info">
                        <div class="row">
                            <div class="col-sm-3">
                                <input class="form-control" type="text" placeholder="昵称(必填)" id="nickname">
                            </div>
                            <div class="col-sm-3">
                                <input class="form-control" type="email" placeholder="邮箱(必填)" id="email">
                            </div>
                            <div class="col-sm-3">
                                <input class="form-control" type="url" placeholder="个人网址" id="website">
                            </div>
                            <div class="col-sm-3">
                                <input type="button" class="btn btn-info" value="评论" id="comment-btn">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="padding">
            </div>
        </div>
    </div>
</div>
<footer>${Application.setting.copyright}</footer>
<script src="/static/js/article.js"></script>
<script src="/static/js/left.js"></script>
</body>
</html>