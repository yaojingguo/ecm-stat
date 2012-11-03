function load(data, div) {
  console.log(data.y);
  plot1 = $.jqplot(div, data.y, {
    seriesDefaults: {
      label: '访问人数', 
      renderer: $.jqplot.BarRenderer,
      pointLabels: {
        show: true
      }
    },
    axes: {
      xaxis: {
        renderer: $.jqplot.CategoryAxisRenderer,
        ticks: data.x,
        tickOptions: {
          angle: -30
        }
      }
    },
    highlighter: {
      show: false
    },
//    legend: {
//      location: 'n',
//      show: true
//    },
    title: '用户访问次数'
  });
}

$(document).ready(function() {
  $.jqplot.config.enablePlugins = true;
  var day_data = {
    x: [ '2012-1-2', '2012-1-2', '2012-1-3', '2012-1-4', '2012-1-4', '2012-1-5', '2012-1-6', '2012-1-7', '2012-1-8', '2012-1-9',  '2012-1-10',
         '2012-1-11', '2012-1-12', '2012-1-13', '2012-1-14', '2012-1-15', '2012-1-16', '2012-1-17', '2012-1-18', '2012-1-19', '2012-1-20'],
    // x: ['1月1日', '2_0', '3', '4', '5', '6', '7', '8'],
    y: [ [ 10, 20, 23, 33, 10, 33, 80, 34,  10, 20, 23, 33, 10, 33, 80, 34,  10, 20, 23, 33, 10, 33, 80, 34, 10, 90 ] ]
  };
  load(day_data, 'day_stat');
  
  // TEST
//  test_bar_chart();

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