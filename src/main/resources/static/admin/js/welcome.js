layui.use(['layer', 'jquery', 'table'], function () {

    var layer = layui.layer;
    var $ = layui.$;
    var table = layui.table;

    var parameter1 = {
        id: 'all-table',
        elem: '#all-table',
        url: '/api/admin/visit/list',
        method: 'get',
        page: true,
        toolbar: '#toolbar-head',
        parseData: function (response) {
            return {
                code: response.status,
                message: response.message,
                count: response.data.count,
                data: response.data.items
            }
        },
        cols: [[
            {field: 'ip', title: 'IP'},
            {field: 'url', title: 'URL'},
            {field: 'referer', title: 'Referer'},
            {field: 'status', title: '状态码'},
            {field: 'agent', title: '客户端'},
            {fixed: 'right', field: 'date', title: '日期'},
        ]]
    };

    initChart('flow');

    initCountTable();

    table.render(parameter1);

    function initChart(id) {
        var flow = echarts.init(document.getElementById(id));
        var option = {
            itemStyle: {
                color: '#3FA7DC'
            },
            title: {
                text: '30日内流量分析'
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    label: {
                        backgroundColor: '#6a7985'
                    }
                }
            },
            legend: {
                data: ['访问量', '访客量']
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: []
            },
            yAxis: {
                type: 'value'
            },
            series: [{
                name: '访问量',
                type: 'line',
                data: [],
                areaStyle: {}
            }, {
                name: '访客量',
                type: 'line',
                data: [],
                areaStyle: {}
            }]
        };
        flowAjax(option, flow);
    }

    function initCountTable() {
        countAjax();
    }

    function flowAjax(option, chart) {
        $.ajax({
            url: "/api/admin/visit/chart",
            type: "get",
            dataType: "json",
            success: function (response) {
                if (response.status === 0) {
                    option.xAxis.data = response.data.dateList;
                    option.series[0].data = response.data.visitList;
                    option.series[1].data = response.data.visitorList;
                    chart.setOption(option);
                } else {
                    layer.open({
                        type: 0,
                        content: response.message
                    });
                }
            },
            error: function (response) {
                layer.open({
                    type: 0,
                    content: "服务器错误"
                });
            }
        });
    }

    function countAjax() {
        $.ajax({
            url: "/api/admin/visit/count",
            type: "get",
            dataType: "json",
            success: function (response) {
                if (response.status === 0) {
                    $('#todayVisit').text(response.data.todayVisit);
                    $('#todayVisitor').text(response.data.todayVisitor);
                    $('#totalVisit').text(response.data.totalVisit);
                    $('#totalVisitor').text(response.data.totalVisitor);
                } else {
                    layer.open({
                        type: 0,
                        content: response.message
                    });
                }
            },
            error: function (response) {
                layer.open({
                    type: 0,
                    content: "服务器错误"
                });
            }
        });
    }

});