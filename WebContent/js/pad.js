function testComparison() {
  var begin = moment('2012-12', 'YYYY-MM');
  var end = moment('2014-12', 'YYYY-MM');
  var result = end > begin;
  console.log('comparison: ' + result);
}

function test1()
{
  range('2011-01-09', '2011-03-10');
  range('2011-01', '2011-03');
  range('2011', '2011');
}

function test2() {
  var birth = new Date('2009-03');
  console.log('birth: ' + birth);
}

function info() {
  console.log('formatted date: ' + arguments[0].format('YYYY-MM-DD'));
}

function infoHour() {
  console.log('formatted date: ' + arguments[0].format('YYYY-MM-DD HH'));
}

function test3() {
  console.log('test month');
  var otherday = moment(new Date(2009, 2));
  console.log('birth: ' + otherday.format('YYYY-MM-DD'));  
  otherday.add('months', 1);
  console.log('format after 1: ' + otherday.format('YYYY-MM-DD'));
  otherday.add('months', 8);
  console.log('format after 8: ' + otherday.format('YYYY-MM-DD'));  
  otherday.add('months', 1);
  console.log('format after 1: ' + otherday.format('YYYY-MM-DD'));  

  console.log('test day');
  var day = moment(new Date(2009, 2, 12));
  info(day);
  day.add('days', 1);
  info(day);
  day.add('days', 18);
  info(day);
  day.add('days', 1);
  info(day);

  console.log('test hour');
  var hour = new moment(new Date(2009, 2, 13, 10));
  infoHour(hour);
  hour.add('hours', 1);
  infoHour(hour);
  hour.add('hours', 12);
  infoHour(hour);
  hour.add('hours', 1);
  infoHour(hour);
}


function testValidation() {
  console.log('YYYY-MM: ' + moment('2011-01', 'YYYY-MM').isValid());
  console.log('YYYY-MM-DD: ' + moment('2011-01-12', 'YYYY-MM-DD').isValid());
  console.log('YYYY-MM-DD HH: ' + moment('2011-01-12 00', 'YYYY-MM-DD HH').isValid());
  console.log('YYYY-MM-DD HH: ' + moment('2011-01-12 24', 'YYYY-MM-DD HH').isValid());
}

// T E S T
//test1();
//test2();
//test3();


// Implementation
function range(beginDateStr, endDateStr) 
{
  console.log('beginDate: ' + beginDateStr + ', endDateStr: ' + endDateStr);
  var fmt;
  var unit;
  var len = beginDateStr.length;
  switch (len) {
    case 7: 
      fmt = 'YYYY-MM';
      unit = 'months';
      break;
    case 10: 
      fmt = 'YYYY-MM-DD';
      unit = 'days';
      break;
    case 13:
      fmt = 'YYYY-MM-DD HH';
      unit = 'hours';
      break;
    default:
      throw new Error(beginDateStr + ' has a illegal length ' + len);
  }

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
}

function fill(data)
{
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
}

function testFill() {
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

testFill();
