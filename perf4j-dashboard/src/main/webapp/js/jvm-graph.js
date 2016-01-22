$(function () {
    try {
        var charts = $("#runningInfo").html();
        var parseData = JSON.parse(charts);
        for (var n = 0; n < parseData['jvmData'].length; n++) {
            var graph = parseData['jvmData'][n];
            var option = {
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['' + graph['tag']],
                    orient: 'horizontal', // 'vertical'
                    x: 'center', // 'center' | 'left' | {number},
                    y: 'top', // 'center' | 'bottom' | {number}
                    padding: [20, 5, 5, 5],    // [5, 10, 15, 20]
                },
                toolbox: {
                    show: true,
                    orient: 'vertical',
                    feature: {
                        mark: {show: true},
                        dataView: {show: true, readOnly: false},
                        magicType: {show: false, type: ['line', 'bar']},
                        saveAsImage: {show: true}
                    }
                },
                calculable: true,
                xAxis: [
                    {
                        type: 'category',
                        boundaryGap: false,
                        data: graph['labels']
                    }
                ],
                yAxis: [
                    {
                        type: 'value'
                    }
                ],
                series: [
                    {
                        name: graph['tag'],
                        type: 'line',
                        stack: '总量',
                        itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data: graph['counts'],
                        symbol: 'none'
                    }
                ]
            };
            var echart = echarts.init(document.getElementById(graph['tag']));
            echart.setOption(option);
        }

        $("#hostname").html(parseData['hostname']);
    } catch (e) {
        $("#chart").html("无法获取数据")
    }

})