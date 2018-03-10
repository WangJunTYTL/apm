$(function () {

    function getSubText(metric) {
        //本地化翻译
        var metricMap = {
            "Max": "最大响应时间(ms)",
            "Min": "最小响应时间(ms)",
            "Mean": "平均响应时间(ms)",
            "TPS": "每秒请求数",
            "Std": "响应标准方差",
            "GCCount": "GC次数累计",
            "GCTime": "GC时间累计(ms)",
            "HeapMemory": "堆内存使用情况(M)",
            "NonHeapMemory": "堆内存使用情况(M)",
            "ThreadCount": "线程创建次数累计"
        };
        return metricMap[metric];
    }

    function chartLine(myChart, graph, title) {
        var option = {
            title: {
                text: title,
                subtext: getSubText(title)
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: [getSubText(title)],
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
                    data: graph['series'],
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
                name: getSubText(title),
                type: 'line',
                data: graph['counts'],
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
        require.config(
            {
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
                $.post("/monitor/graph", data, function (data) {
                    try {
                        if (data.code != 0) {
                            alert(data.message);
                            return;
                        }
                    } catch (e) {
                        $("#chart").html("无法加载数据！响应数据内容:<br>" + data.toString());
                        return;
                    }
                    $("#chart").html("");
                    var metrics = data.data['metrics'];
                    var size = Object.keys(metrics).length;
                    var i = 1;
                    for (var key in metrics) {
                        if (size % 2 != 0 && i == size) {
                            var container = $("<div>").attr("class", "col-lg-12");
                        } else {
                            var container = $("<div>").attr("class", "col-lg-6");
                        }
                        var node = $("<div>").attr("id", "chart" + "_" + i)
                            .attr("style", "height:300px;");
                        $("#chart").append(container.append(node));
                        i++;

                        var myChart = ec.init(node.get(0));
                        var title = key;
                        var data = metrics[key];
                        chartLine(myChart, data, title.replace(/\d\d-/g, ""));
                    }
                });
            }
        );
    }

    $(document).on("click", "#submit", function () {
        var service = $("#service").val();
        var node = $("#node").val();
        var type = $("#Type").val();
        if (service == '') {
            alert("请选择要查看的服务");
            exit(1);
        }
        if (node == '') {
            alert("请选择要查看的服务节点");
            exit(1);
        }
        if (type == '') {
            alert("请选择要查看的监控指标");
            exit(1);
        }
        var startTime = $("input[name='from']").val();
        var endTime = $("input[name='to']").val();
        var data = {
            service: service,
            node: node,
            type: type,
            startTime: startTime,
            endTime: endTime
        }
        refreshChart(data);
    });
});

