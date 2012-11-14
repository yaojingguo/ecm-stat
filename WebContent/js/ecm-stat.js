var ECM_STAT ={
    fmt_map: {
      '7': {fmt: 'YYYY-MM', unit: 'months'},
      '10': {fmt: 'YYYY-MM-DD', unit: 'days'},
      '13': {fmt: 'YYYY-MM-DD HH', unit: 'hours'}
    }
};

function infoData(data) {
  console.log('x: ' + data.x + ', y: ' + data.y);
}

/*
 * Return a series dates between beginDate and endDate.
 */
function range(beginDateStr, endDateStr) {
  console.log('beginDate: ' + beginDateStr + ', endDateStr: ' + endDateStr);
  var len = beginDateStr.length;
  var mapVal = ECM_STAT.fmt_map['' + len];
  if (mapVal == undefined)
    throw new Error(dateStr + ' has a illegal length ' + len);
  
  var fmt = mapVal.fmt;
  var unit = mapVal.unit;

  var beginDate = moment(beginDateStr, fmt);
  var endDate = moment(endDateStr, fmt);
  var series = [];
  while (beginDate <= endDate) {
    //infoHour(beginDate);
    series.push(beginDate.clone());
    beginDate.add(unit, 1);
  }

  for (var i = 0; i < series.length; i++)
    series[i] = series[i].format(fmt);
  console.log('Date range: ' + series);
  return series;
}

/*
 * Fill the dates which does not have data in database.
 */
function fill(data) {
  if (data.x.length < 2) // No need to fill
    return data;
  var x = [];
  var y = [];
  var xRange = [];
  for (var i = 0; i < data.x.length-1; i++) {
    xRange = range(data.x[i], data.x[i+1]);
    for (var j = 0; j < xRange.length-1; j++) 
      x.push(xRange[j]);
    y.push(data.y[i]);
    for (var j = 0; j < xRange.length-2; j++) 
      y.push(0);
  }

  x.push(data.x[data.x.length-1]);
  y.push(data.y[data.x.length-1]);
  data.x = x;
  data.y = y;
  return data;
}

function verifyDate(dateStr) {
  var len = dateStr.length;
  var mapVal = ECM_STAT.fmt_map['' + len];
  if (mapVal == undefined)
    throw new Error(dateStr + ' has a illegal length ' + len);
  var fmt = mapVal.fmt;
  if (!moment(dateStr, fmt).isValid())
    throw new Error(dateStr + ' is not valid date string with format ' + fmt);
}

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

function getWidth(data) {
  var len = data.x.length;
  return len * 75;
}

$(document).ready(function() {
  $.jqplot.config.enablePlugins = true;
  
  $("#statQuery").submit(function(e) {
    e.preventDefault();
    var formId = 'statQuery';
    var query = $('#' + formId);
    var beginDate =  document.forms[formId]['beginDate'].value;
    var endDate = document.forms[formId]['endDate'].value;
    console.log('form: beginDate=' + beginDate + ', endDate= ' + endDate);
    verifyDate(beginDate);
    verifyDate(endDate);
    var dataString = query.serialize();
    $.ajax({
      type: "POST",
       url: "StatServlet",
      data: dataString,
      dataType: "json",
      success: function(data) {
        console.log('data: ' + JSON.stringify(data));
        // Client side fill is not used. Server side has filled data.
//        data = fill(data);
        /*
         * Re-draw the div.
         */
        var width = getWidth(data);
        $('#jsonServer').remove();
        $('#query').append('<div id="jsonServer" style="width: ' + width + 'px"></div>');
        load(data, 'jsonServer');
      }
    });
  });
  
//  testVerifyDate();
//  testRange();
//  testFill();
  
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

function testVerifyDate() {
  verifyDate('2012-01');
  verifyDate('2012-01-01');
  verifyDate('2012-01-01 00');
  
  verifyDate('2012-34');
}

function testRange() {
  // month
  console.log('============== Month');
  range('2012-01', '2012-01');
  range('2012-01', '2012-02');
  range('2012-01', '2012-12');
  range('2012-11', '2013-02');

  // day
  console.log('============== Day');
  range('2012-01-01', '2012-01-01');
  range('2012-01-01', '2012-01-03');
  range('2012-01-30', '2012-02-03');

  // hour
  console.log('============== Hour');
  range('2012-01-01 00', '2012-01-01 01');
  range('2012-01-01 00', '2012-01-01 23');

  //range('201-01-01 00', '2012-01-01 23');
}

function testFill() {
  // month
  console.log('============== Month');
  infoData(fill({x: ['2012-01'], y: [12]}));
  infoData(fill({x: ['2012-01', '2012-02'], y: [12, 20]}));
  infoData(fill({x: ['2012-01', '2012-03'], y: [12, 20]}));
  infoData(fill({x: ['2012-01', '2012-12'], y: [12, 20]}));
  infoData(fill({x: ['2012-01', '2012-03', '2012-07'], y: [12, 20, 100]}));

  // day
  console.log('============== Day');
  infoData(fill({x: ['2012-01-01'], y: [100]}));
  infoData(fill({x: ['2012-01-01', '2012-01-03'], y: [1, 7]}));
  infoData(fill({x: ['2012-01-30', '2012-02-03'], y: [7, 8]}));

  // hour
  console.log('============== Hour');
  infoData(fill({x: ['2012-01-01 00', '2012-01-01 01'], y: [12, 33]}));
  infoData(fill({x: ['2012-01-01 00', '2012-01-01 23'], y: [23, 14]}));
}

