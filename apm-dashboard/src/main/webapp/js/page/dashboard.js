$(function () {

    function chartLine(myChart, graph, title, data, subtext) {
        var option = {
            title: {
                text: title,
                subtext: subtext
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: [graph.tag],
                orient: 'horizontal', // 'vertical'
                x: 'center', // 'center' | 'left' | {number},
                y: 'top', // 'center' | 'bottom' | {number}
                padding: [5, 10, 15, 20],    // [5, 10, 15, 20]
            },
            toolbox: {
                show: true,
                orient: 'vertical',
                feature: {
                    mark: {show: false},
                    dataView: {show: false, readOnly: false},
                    magicType: {show: true, type: ['line', 'bar']},
                    saveAsImage: {show: true}
                }
            },
            calculable: true,
            xAxis: [
                {
                    type: 'category',
                    boundaryGap: false,
                    data: graph.series,
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
            series: [{
                name: graph.tag,
                type: 'line',
                data: data,
                symbol: "none",
                markPoint: {
                    data: [
                        {type: 'max', name: '最大值'},
                    ]
                }
            }]
        };
        myChart.setOption(option);
    }

    function refreshChart(data) {
        require.config({
            paths: {
                echarts: '/js/echart'
            }
        });
        require(
            [
                'echarts',
                'echarts/chart/line',   // 按需加载所需图表，如需动态类型切换功能，别忘了同时加载相应图表
                'echarts/chart/bar'
            ],
            function (ec) {
                $.post("/dashboard/graph", data, function (data) {
                    try {
                        if (data.code == 1) {
                            $("#chart").html("<h3 class='text-warning'>Not Fount Data</h3>");
                            return;
                        }
                    } catch (e) {
                        $("#chart").html("无法加载数据！响应数据内容:<br>" + data);
                        return;
                    }
                    $("#chart").html("");
                    var graph = data.data;
                    if (checkSpecialData(graph, ec)) {
                        return;
                    }
                    for (var i = 1; i <= 6; i++) {
                        switch (i) {
                            case 1:
                                var container = $("<div>").attr("class", "col-lg-12");
                                break;
                            case 2:
                                var container = $("<div>").attr("class", "col-lg-12");
                                break;
                            default:
                                var container = $("<div>").attr("class", "col-lg-6");
                                break;
                        }
                        var node = $("<div>").attr("id", "chart" + "_" + i).attr("style", "height:300px;");
                        $("#chart").append(container.append(node));
                        var myChart = ec.init(node.get(0));
                        switch (i) {
                            case 1:
                                var title = "TPS";
                                var data = graph.tpss;
                                chartLine(myChart, graph, title, data, "每秒事务数")
                                break;
                            case 2:
                                var title = "Mean";
                                var data = graph.means;
                                chartLine(myChart, graph, title, data, "平均响应时间：ms")
                                break;
                            case 3:
                                var title = "Max";
                                var data = graph.maxs;
                                chartLine(myChart, graph, title, data, "最大响应时间：ms")
                                break;
                            case 4:
                                var title = "Min";
                                var data = graph.mins;
                                chartLine(myChart, graph, title, data, "最小响应时间：ms")
                                break;
                            case 5:
                                var title = "Count";
                                var data = graph.counts;
                                chartLine(myChart, graph, title, data, "总事务数")
                                break;
                            case 6:
                                var title = "Std";
                                var data = graph.stds;
                                chartLine(myChart, graph, title, data, "标准方差")
                                break;
                        }
                    }
                });
            }
        );
    }

    function checkSpecialData(graph, ec) {
        if (graph.tag == "jvm.gc" || graph.tag == "jvm.thread.count" || graph.tag == "jvm.used.non.heap" || graph.tag == "jvm.used.heap") {
            for (var i = 1; i <= 2; i++) {
                var container = $("<div>").attr("class", "col-lg-12");
                var node = $("<div>").attr("id", "chart" + "_" + i).attr("style", "height:300px;");
                $("#chart").append(container.append(node));
                var myChart = ec.init(node.get(0));
                switch (i) {
                    case 1:
                        var title = graph.tag;
                        var data = graph.counts;
                        var subTitle = "总GC次数";
                        if (graph.tag == "jvm.used.non.heap" || graph.tag == "jvm.used.heap") {
                            subTitle = "单位：M"
                        } else if (graph.tag == "jvm.thread.count") {
                            subTitle = "";
                        }
                        chartLine(myChart, graph, title, data, subTitle);
                        break;
                    case 2:
                        var data = graph.means;
                        chartLine(myChart, graph, title, data, "GC总时间：ms");
                        break;
                }
                if (graph.tag != "jvm.gc") {
                    break;
                }
            }
            return true;
        }
        return false;
    }

    $(document).on("click", "#submit", function () {
        var service = $("input[name='service']").val();
        var hostname = $("input[name='node']").val();
        var tag = $("#tag").val();
        var startTime = $("input[name='from']").val();
        var endTime = $("input[name='to']").val();
        var data = {
            service: service,
            hostname: hostname,
            tag: tag,
            startTime: startTime,
            endTime: endTime
        }
        refreshChart(data);
    });


});

