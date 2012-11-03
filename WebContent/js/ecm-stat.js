function load(data, div) {
  console.log(data.y);
  plot1 = $.jqplot(div, data.y, {
    seriesDefaults: {
      renderer: $.jqplot.BarRenderer,
      pointLabels: {
        show: true
      }
    },
    axes: {
      xaxis: {
        renderer: $.jqplot.CategoryAxisRenderer,
        ticks: data.x
      }
    },
    highlighter: {
      show: false
    }
  });
}

$(document).ready(function() {
  $.jqplot.config.enablePlugins = true;
  var data = {
    x: [ '1月1日', '1月2日', '1月3日', '1月4日', '1月5日', '1月6日', '1月7日', '1月8日' ],
    // x: ['1月1日', '2_0', '3', '4', '5', '6', '7', '8'],
    y: [ [ 10, 20, 23, 33, 10, 33, 80, 34 ] ]
  };
  load(data, 'stat_chart');
  
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