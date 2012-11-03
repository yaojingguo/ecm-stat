function get_render(name) 
{
  switch (name) {
    case 'BarRenderer':
      return $.jqplot.BarRenderer;
    case undefined:
      return undefined;
    default:
      throw new Error('Unsupported renderer ' + name);
  }
}

function load(data, div) {
  var shape_render = get_render(data.render);

  
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

function loadFromJson(json_str, div) {
  var data = JSON.parse(json_str);
  console.log('data: ' + JSON.stringify(data));
  load(data, div);
}


$(document).ready(function() {
  $.jqplot.config.enablePlugins = true;
  
  var day_data = {
    x: [ '2012-1-2', '2012-1-2', '2012-1-3', '2012-1-4', '2012-1-4', '2012-1-5', '2012-1-6', '2012-1-7', '2012-1-8', '2012-1-9',  '2012-1-10',
         '2012-1-11', '2012-1-12', '2012-1-13', '2012-1-14', '2012-1-15', '2012-1-16', '2012-1-17', '2012-1-18', '2012-1-19', '2012-1-20'],
    // x: ['1月1日', '2_0', '3', '4', '5', '6', '7', '8'],
    y: [  10, 20, 23, 33, 10, 33, 80, 34,  10, 20, 23, 33, 10, 33, 80, 34,  10, 20, 23, 33, 10, 33, 80, 34, 10, 90 ],
    render: 'BarRenderer'
  };
  
  load(day_data, 'day_stat');
  
  var month_data = {
      x: ['2012-1', '2012-2', '2012-3', '2012-4', '2012-5'],
      y: [100, 200, 78, 100, 300]
  };
  load(month_data, 'month_stat');
  
  var json_str = '{"x": ["1", "2"], "y": [23, 89]}';
  loadFromJson(json_str, 'json_str');
  
  // TEST
// test_bar_chart();

});

// TEST
function test_bar_chart() {
  var s1 = [ 2, 6, 7, 10 ];
  var ticks = [ 'a', 'b', 'c', 'd' ];

  plot2 = $.jqplot('chart', [ s1 ], {
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