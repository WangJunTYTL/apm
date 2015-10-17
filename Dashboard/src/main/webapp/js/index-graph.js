$(function () {

    var charts = $(".chart");
    for (var n = 0; n < charts.length; n++) {
        var c = charts[n];
        var config = $(c).html();
        $(c).html("");
        var node = $("<div>").attr("id", "chart" + n).attr("style", "height:500px;");
        $(c).append(node);
        //alert(config)
        var parseData = JSON.parse(config);
        var myChart = echarts.init(document.getElementById("chart" + n));
        var series = []
        for (var tag in parseData.tagsToYData){
            var tagData = {
                name:tag,
                type:'line',
                data:parseData.tagsToYData[tag],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        //{type : 'min', name: '最小值'}
                    ]
                },
                markLine : {
                    data : [
                        {type : 'average', name: '平均值'}
                    ]
                }
            }
            series.push(tagData)
        }
        var option = {
            title : {
                //text: parseData.graphType,
                //subtext: '纯属虚构'
            },
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                data:parseData.tags,
            },
            toolbox: {
                show : true,
                feature : {
                    mark : {show: false},
                    dataView : {show: true, readOnly: false},
                    magicType : {show: true, type: ['line', 'bar']},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            calculable : true,
            xAxis : [
                {
                    type : 'category',
                    boundaryGap : false,
                    data : parseData.labels
                }
            ],
            yAxis : [
                {
                    type : 'value',
                    axisLabel : {
                        formatter: '{value} '
                    }
                }
            ],
            series : series
        };


        // 为echarts对象加载数据
        myChart.setOption(option);
    }

});

