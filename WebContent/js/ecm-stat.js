function getRender(name) {
  switch (name) {
    case 0:
      return $.jqplot.BarRenderer;
    case 1:
      return undefined; // means a line renderer.
    default:
      throw new Error('Unsupported renderer ' + name);
  }
}

function load(data, div) {
  var shape_render = getRender(data.chartType);

  plot1 = $.jqplot(div, [data.y], {
    seriesDefaults: {
      label: '访问人数', 
      renderer: shape_render,
      pointLabels: {
        show: true
      }
    },
    axes: {
      xaxis: {
        renderer: $.jqplot.CategoryAxisRenderer,
        labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
        tickRenderer: $.jqplot.CanvasAxisTickRenderer,
        ticks: data.x,
        tickOptions: {
          angle: -30
        }
      }
    },
    highlighter: {
      show: false
    },
    title: '用户访问次数'
  });
}

function loadFromJson(jsonStr, div) {
  var data = JSON.parse(jsonStr);
  console.log('data: ' + JSON.stringify(data));
  load(data, div);
}

$(document).ready(function() {
  $.jqplot.config.enablePlugins = true;
  
  $("#statQuery").submit(function(e) {
    e.preventDefault();
    var dataString = $("#statQuery").serialize();
    $.ajax({
      type: "POST",
       url: "StatServlet",
      data: dataString,
      dataType: "json",
      success: function(data) {
        $('#jsonServer').remove();
        $('#query').append('<div id="jsonServer"></div>');
        console.log('data: ' + JSON.stringify(data));
        load(data, 'jsonServer');
      }
    });
  });
 
  
  ////////////////////////////////////////////////////////////////////
  // Sample Charts
  ////////////////////////////////////////////////////////////////////
  var dayData = {
      x: [ '2012-1-2', '2012-1-2', '2012-1-3', '2012-1-4', '2012-1-4', '2012-1-5', '2012-1-6', '2012-1-7', '2012-1-8', '2012-1-9',  '2012-1-10',
           '2012-1-11', '2012-1-12', '2012-1-13', '2012-1-14', '2012-1-15', '2012-1-16', '2012-1-17', '2012-1-18', '2012-1-19', '2012-1-20'],
      // x: ['1月1日', '2_0', '3', '4', '5', '6', '7', '8'],
      y: [  10, 20, 23, 33, 10, 33, 80, 34,  10, 20, 23, 33, 10, 33, 80, 34,  10, 20, 23, 33, 10, 33, 80, 34, 10, 90 ],
      chartType: 0
    };
    
    load(dayData, 'dayStat');
    
    var monthData = {
        x: ['2012-1', '2012-2', '2012-3', '2012-4', '2012-5'],
        y: [100, 200, 78, 100, 300],
        chartType: 1
    };
    load(monthData, 'monthStat');
    
    var jsonStr = '{"y":[100,200,78,23,300],"x":["2012-1","2012-2","2012-3","2012-4","2012-5"], "chartType": 1}';
    loadFromJson(jsonStr, 'jsonStr');
    testBarChart();

});

function testBarChart() {
  var s1 = [ 2, 6, 7, 10 ];
  var ticks = [ 'a', 'b', 'c', 'd' ];

  plot2 = $.jqplot('chart1', [ s1 ], {
    // Only animate if we're not using excanvas (not in IE 7 or IE 8)..
    animate: !$.jqplot.use_excanvas,
    seriesDefaults: {
      renderer: $.jqplot.BarRenderer,
      pointLabels: {
        show: true
      }
    },
    axes: {
      xaxis: {
        renderer: $.jqplot.CategoryAxisRenderer,
        ticks: ticks
      }
    },
    highlighter: {
      show: false
    }
  });
}
