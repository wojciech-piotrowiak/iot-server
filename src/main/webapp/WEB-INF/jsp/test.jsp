 <html>
  <head>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {

      var data = new google.visualization.DataTable();
              data.addColumn('string', 'Czas');
              data.addColumn('number', 'Cisnienie');
              data.addColumn('number', 'Temperatura');
                data.addRows([${message}]);

        var options = {
          title: 'Domowa pogoda',
          curveType: 'function',
          legend: { position: 'bottom' }
        };

        var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

        chart.draw(data, options);
      }
    </script>
  </head>
  <body>
    <h1>${message}</h1>
    <div id="curve_chart" style="width: 900px; height: 500px"></div>
  </body>
</html>
