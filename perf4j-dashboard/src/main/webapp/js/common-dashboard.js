$(function () {
    var clusterMap = JSON.parse($("#clusterMap").html());
    var mySource = [];
    for (var key in clusterMap) {
        var o = {};
        o['id'] = clusterMap[key];
        o['name'] = key;
        mySource.push(o)
    }
    $('#search').typeahead({
        source: mySource
    });

});