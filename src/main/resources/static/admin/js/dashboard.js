layui.use(['layer', 'jquery', 'table'], function () {

    var layer = layui.layer;
    var $ = layui.$;
    var table = layui.table;

    var parameter1 = {
        id: 'all-table',
        elem: '#all-table',
        url: '/admin/dashboard/visit/list',
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
                data: ['访问量']
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
            }]
        };
        flowAjax(option, flow);
    }

    function initCountTable() {
        countAjax();
    }

    function flowAjax(option, chart) {
        $.ajax({
            url: "/admin/dashboard/visit/chart",
            type: "get",
            dataType: "json",
            success: function (response) {
                if (response.status === 0) {
                    option.xAxis.data = response.data.dateList;
                    option.series[0].data = response.data.visitList;
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
            url: "/admin/dashboard/visit/count",
            type: "get",
            dataType: "json",
            success: function (response) {
                if (response.status === 0) {
                    $('#todayVisit').text(response.data.todayVisit);
                    $('#totalVisit').text(response.data.totalVisit);
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