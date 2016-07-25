$(function () {
    var currentCluster = $("#currentCluster").html();
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
            var refreshChart = function () {
                $.post("/graph/data", {currentCluster: currentCluster}, function (data) {
                    try {
                        var parseData = JSON.parse(data);
                        if (parseData.length == 0) {
                            $("#chart").html("没有实时数据，响应数据内容是：" + data + " <br>请确认是否配置实时数据展示或者先暂时查看历史数据");
                            return;
                        }
                    } catch (e) {
                        $("#chart").html("无法加载数据！响应数据内容:<br>" + data);
                        return;
                    }
                    $("#chart").html("");
                    for (var n = 0; n < parseData.length; n++) {
                        var graph = parseData[n];
                        var container = $("<div>").attr("class", "col-lg-6");
                        var node = $("<div>").attr("id", "chart" + n).attr("style", "height:300px;");
                        container.append(node);
                        $("#chart").append(container);
                        var myChart = ec.init(node.get(0));
                        var series = []
                        for (var tag in graph.tagsToYData) {
                            var tagData = {
                                name: tag,
                                type: 'line',
                                data: graph.tagsToYData[tag],
                                markPoint: {
                                    data: [
                                        {type: 'max', name: '最大值'},
                                    ]
                                },
                                markLine: {
                                    data: [
                                        {type: 'average', name: '平均值'}
                                    ]
                                }
                            }
                            series.push(tagData)
                        }
                        var option = {
                            title: {
                                text: graph.graphType,
                                //subtext: '纯属虚构'
                            },
                            tooltip: {
                                trigger: 'axis'
                            },
                            legend: {
                                data: graph.tags,
                                orient: 'horizontal', // 'vertical'
                                x: 'center', // 'center' | 'left' | {number},
                                y: 'top', // 'center' | 'bottom' | {number}
                                padding: [20, 5, 5, 5],    // [5, 10, 15, 20]
                            },
                            toolbox: {
                                show: true,
                                orient: 'vertical',
                                feature: {
                                    mark: {show: false},
                                    dataView: {show: true, readOnly: false},
                                    magicType: {show: true, type: ['line', 'bar']},
                                    saveAsImage: {show: true}
                                }
                            },
                            calculable: true,
                            xAxis: [
                                {
                                    type: 'category',
                                    boundaryGap: false,
                                    data: graph.labels,
                                    axisTick: {
                                        interval: 0

                                    }
                                }
                            ],
                            yAxis: [
                                {
                                    type: 'value',
                                    axisLabel: {
                                        formatter: '{value} '
                                    },
                                    axisTick: {
                                        interval: 0

                                    }
                                }
                            ],
                            series: series
                        };
                        myChart.setOption(option);
                    }
                });
            }
            refreshChart();
            var intervalId = setInterval(refreshChart, 60000);
            $(document).on("click", ".refreshMenu", function () {
                var v = $(this).children(0).html().split("s")[0];
                $(".refreshMenu").removeClass("active");
                $(this).addClass("active");
                window.clearInterval(intervalId);
                intervalId = setInterval(refreshChart, v * 1000)
            })
        }
    );
});

