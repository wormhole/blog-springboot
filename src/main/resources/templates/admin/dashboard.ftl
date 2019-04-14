<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
    <link rel="stylesheet" href="/static/plugins/layui/css/layui.css"/>
    <link rel="stylesheet" href="/static/admin/css/dashboard.css"/>
    <script src="/static/plugins/echarts/echarts.min.js"></script>
    <script src="/static/plugins/layui/layui.js"></script>
    <meta name="keywords" content="${Application.setting.keywords}"/>
    <meta name="description" content="${Application.setting.description}"/>
    <link rel="icon" href="${Application.setting.head}"/>
    <title>${Application.setting.title}</title>
</head>
<body>
<div class="layui-card">
    <div class="layui-card-header">流量分析</div>
    <div class="layui-card-body">
        <div id="flow">

        </div>
    </div>
</div>
<div class="layui-card">
    <div class="layui-card-header">所有访问</div>
    <div class="layui-card-body">
        <table class="layui-hide" id="all-table" lay-filter="all-table-1"></table>
    </div>
</div>
<div class="layui-card">
    <div class="layui-card-header">数据统计</div>
    <div class="layui-card-body">
        <table class="layui-table">
            <colgroup>
                <col width="200">
                <col width="150">
                <col>
            </colgroup>
            <thead>
            <tr>
                <th>项目</th>
                <th>统计</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>今日访问量</td>
                <td id="todayVisit"></td>
            </tr>
            <tr>
                <td>所有访问量</td>
                <td id="totalVisit"></td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
<script type="text/javascript" src="/static/admin/js/dashboard.js"></script>
</body>
</html>