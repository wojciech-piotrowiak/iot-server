 <html>
  <head>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {

      var data = new google.visualization.DataTable();
              data.addColumn('string', 'Czas');
              data.addColumn('number', 'Temperatura');
                data.addRows([${temperature}]);

        var options = {
          title: 'Temperatura',
          curveType: 'function',
          legend: { position: 'bottom' }
        };

        var chart = new google.visualization.LineChart(document.getElementById('temp_chart'));

        chart.draw(data, options);
      }
    </script>
    <script type="text/javascript">
          google.charts.load('current', {'packages':['corechart']});
          google.charts.setOnLoadCallback(drawChart);

          function drawChart() {

          var data = new google.visualization.DataTable();
                  data.addColumn('string', 'Czas');
                  data.addColumn('number', 'Cisnienie');
                    data.addRows([${pressure}]);

            var options = {
              title: 'Cisnienie',
              curveType: 'function',
              legend: { position: 'bottom' }
            };

            var chart = new google.visualization.LineChart(document.getElementById('press_chart'));

            chart.draw(data, options);
          }
        </script>
  </head>
  <body>
    <div id="temp_chart" style="width: 900px; height: 500px"></div>
    <div id="press_chart" style="width: 900px; height: 500px"></div>
  </body>
</html>
