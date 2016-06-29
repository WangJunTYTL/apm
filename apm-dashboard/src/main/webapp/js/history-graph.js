$(function () {
    try {
        var charts = $("#runningInfo").html();
        var parseData = JSON.parse(charts);
    } catch (e) {
        $("#chart").html("无法加载数据！");
    }

    var tags = parseData['tags'];
    var currentTag = $("#currentTag").html();
    for (var n = 0; n < tags.length; n++) {
        var node = $("<option>").html(tags[n]);
        if (currentTag != null && tags[n] == currentTag) node.attr("selected", "true");
        $("#tag").append(node);
    }
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
                for (var i = 0; i < graph.tags.length; i++) {
                    var symbol = 'heart';
                    if (graph.tagsToYData.length > 168) {
                        symbol = "none";
                    }
                    var tagData = {
                        name: graph.tags[i],
                        type: 'line',
                        data: graph.tagsToYData,
                        symbol: symbol,
                        markPoint: {
                            data: [
                                {type: 'max', name: '最大值'},
                            ]
                        }
                    }
                    series.push(tagData)
                }
                var option = {
                    title: {
                        text: graph.graphType
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: graph.tags
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            mark: {show: true},
                            dataView: {show: true, readOnly: false},
                            magicType: {show: true, type: ['line', 'bar']},
                            restore: {show: true},
                            saveAsImage: {show: true}
                        }
                    },
                    calculable: true,
                    xAxis: [
                        {
                            type: 'category',
                            boundaryGap: false,
                            data: graph.labels
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            axisLabel: {
                                formatter: '{value}'
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

