$(function () {
    try {
        var charts = $("#runningInfo").html();
        var parseData = JSON.parse(charts);
        if (parseData == null) {
            $("#chart").html("服务端响应数据是空的!");
            return;
        }
        var tags = parseData['allTags'];
        var currentTag = parseData['currentTag'];
        for (var n = 0; n < tags.length; n++) {
            var node = $("<option>").html(tags[n]);
            if (currentTag != null && tags[n] == currentTag) node.attr("selected", "true");
            $("#tag").append(node);
        }
        if (parseData['graph'] == null) {
            $("#chart").html("没有查询到数据!");
            return;
        }
    } catch (e) {
        $("#chart").html("无法加载数据！响应数据内容：" + charts);
        return;
    }

    // 开始渲染图表
    $("#chart").html("");
    require.config({
        paths: {
            echarts: '../js/echart'
        }
    });
    require(
        [
            'echarts',
            'echarts/chart/line',   // 按需加载所需图表，如需动态类型切换功能，别忘了同时加载相应图表
            'echarts/chart/bar'
        ],
        function (ec) {
            for (var n = 0; n < parseData['graph'].length; n++) {
                var graph = parseData['graph'][n];
                var container = $("<div>").attr("class", "col-md-12");
                var node = $("<div>").attr("id", "chart" + n).attr("style", "height:300px;");
                container.append(node);
                $("#chart").append(container);
                var myChart = ec.init(node.get(0));
                var series = []
                var symbol = 'auto';
                if (graph.tagsToYData.length > 168) {
                    symbol = "none";
                }
                var tagData = {
                    name: currentTag,
                    type: 'line',
                    data: graph['tagsToYData'],
                    symbol: symbol,
                    markPoint: {
                        data: [
                            {type: 'max', name: '最大值'},
                        ]
                    }
                }
                series.push(tagData)
                var option = {
                    title: {
                        text: graph['graphType'] == 'Count' ? 'TPS(per second)' : graph['graphType'],
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        show: graph['graphType'] == 'Count' ? true : false,
                        data: [currentTag],
                        orient: 'horizontal', // 'vertical'
                        x: 'center', // 'center' | 'left' | {number},
                        y: 'top', // 'center' | 'bottom' | {number}
                        padding: [20, 5, 5, 5],    // [5, 10, 15, 20]
                    },
                    toolbox: {
                        show: true,
                        orient: 'vertical',
                        feature: {
                            magicType: {show: true, type: ['line', 'bar']},
                        }
                    },
                    calculable: true,
                    xAxis: [
                        {
                            type: 'category',
                            boundaryGap: false,
                            data: parseData['label']
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            axisLabel: {
                                formatter: '{value}' + ((graph['graphType'] == 'Min' || graph['graphType'] == 'Max' || graph['graphType'] == 'Mean') ? ' ms' : '')
                            }
                        }
                    ],
                    series: series
                };
                myChart.setOption(option);
            }
        }
    );
});

