$(function () {
    try {
        var charts = $("#runningInfo").html();
        var parseData = JSON.parse(charts);
        if (parseData == null) {
            $("#chart").html("服务端响应数据是空的!");
            return;
        }
    } catch (e) {
        $("#chart").html("无法加载数据！响应数据内容：<br>" + charts);
    }
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
            if (parseData['used.heap.size'] != null){
                var option = {
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: ['UsedHeap'],
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
                            axisLabel : {
                                formatter: '{value} M'
                            }
                        }
                    ],
                    series: [
                        {
                            name: 'UsedHeap',
                            type: 'line',
                            stack: '总量',
                            itemStyle: {normal: {areaStyle: {type: 'default'}}},
                            data: parseData['used.heap.size'],
                            symbol: 'none'
                        }
                    ]
                };
                var echart = ec.init(document.getElementById('UsedHeap'));
                echart.setOption(option);
            }

            if (parseData['used.nonHeap.size'] != null){
                var option = {
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: ['UsedNonHeap'],
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
                            axisLabel : {
                                formatter: '{value} M'
                            }
                        }
                    ],
                    series: [
                        {
                            name: 'UsedNonHeap',
                            type: 'line',
                            stack: '总量',
                            itemStyle: {normal: {areaStyle: {type: 'default'}}},
                            data: parseData['used.nonHeap.size'],
                            symbol: 'none'
                        }
                    ]
                };
                var echart = ec.init(document.getElementById('UsedNonHeap'));
                echart.setOption(option);
            }

            if (parseData['thread.count'] != null){
                var option = {
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: ['thread.current.count'],
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
                            type: 'value'
                        }
                    ],
                    series: [
                        {
                            name: 'thread.current.count',
                            type: 'line',
                            stack: '总量',
                            itemStyle: {normal: {areaStyle: {type: 'default'}}},
                            data: parseData['thread.count'],
                            symbol: 'none'
                        }
                    ]
                };
                var echart = ec.init(document.getElementById('Thread'));
                echart.setOption(option);
            }

            if (parseData['gc.count'] != null){
                var option = {
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: ['gc.total.count'],
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
                            type: 'value'
                        }
                    ],
                    series: [
                        {
                            name: 'gc.total.count',
                            type: 'line',
                            stack: '总量',
                            itemStyle: {normal: {areaStyle: {type: 'default'}}},
                            data: parseData['gc.count'],
                            symbol: 'none'
                        }
                    ]
                };
                var echart = ec.init(document.getElementById('GCCount'));
                echart.setOption(option);
            }

            if (parseData['gc.time'] != null){
                var option = {
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: ['gc.total.time'],
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
                            axisLabel : {
                                formatter: '{value} ms'
                            }
                        }
                    ],
                    series: [
                        {
                            name: 'gc.total.time',
                            type: 'line',
                            stack: '总量',
                            itemStyle: {normal: {areaStyle: {type: 'default'}}},
                            data: parseData['gc.time'],
                            symbol: 'none'
                        }
                    ]
                };
                var echart = ec.init(document.getElementById('GCTime'));
                echart.setOption(option);
            }
        }
    );

    $("#hostname").html(parseData['hostname']);

})

