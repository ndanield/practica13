<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Práctica 13 - Broker de mensajería</title>

    <script src="js/jquery-3.2.1.min.js"></script>
    <#--Google charts-->
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

    <#--Bootstrap-->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"
            integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy"
            crossorigin="anonymous"></script>
</head>
<body>
    <input type="hidden" id="measures" value='${measures}'>
    <div id="graph1" style="width: 90%; height: 500px;" class="mx-auto my-1"></div>
    <div id="graph2" style="width: 90%; height: 500px;" class="mx-auto my-1"></div>

    <script>
        var webSocket;
        var updateTimeInterval = 3000;

        var datos = JSON.parse($("#measures").val());
        var sensor1Data = [['FechaGeneracion', 'Temperatura', 'Humedad']];
        var sensor2Data = [['FechaGeneracion', 'Temperatura', 'Humedad']];

        for (var i = 0; i < datos.length; i++) {
            if (datos[i].idDevice === 1) {
                sensor1Data.push([
                    datos[i].generationDate,
                    datos[i].temperature,
                    datos[i].humidity
                ]);
            }

            if (datos[i].idDevice === 2) {
                sensor2Data.push([
                    datos[i].generationDate,
                    datos[i].temperature,
                    datos[i].humidity
                ]);
            }
        }

        google.charts.load('current', {'packages': ['corechart']});

        if (sensor1Data.length > 0) {
            google.charts.setOnLoadCallback(drawGraph.bind(this, sensor1Data, 'Sensor #1', document.getElementById('graph1')));
        } else {
            document.getElementById('graph1').innerHTML = '<p>No hay datos registrados para este sensor aún</p>';
        }
        if (sensor2Data.length > 0) {
            google.charts.setOnLoadCallback(drawGraph.bind(this, sensor2Data, 'Sensor #2', document.getElementById('graph2')));
        } else {
            document.getElementById('graph2').innerHTML = '<p>No hay datos registrados para este sensor aún</p>';
        }

        function getUpdateDataFromServer(mensaje) {
            var dato = JSON.parse(mensaje.data);

            if (dato.idDevice === 1) {
                google.charts.load('current', {'packages': ['corechart']});
                google.charts.setOnLoadCallback(drawGraph.bind(this, sensor1Data, 'Sensor #1', document.getElementById('graph1')));

                sensor1Data.push([
                    dato.generationDate,
                    dato.temperature,
                    dato.humidity
                ]);
            } else if (dato.idDevice === 2) {
                google.charts.load('current', {'packages': ['corechart']});
                google.charts.setOnLoadCallback(drawGraph.bind(this, sensor2Data, 'Sensor #2', document.getElementById('graph2')));

                sensor2Data.push([
                    dato.generationDate,
                    dato.temperature,
                    dato.humidity
                ]);
            }
        }

        function drawGraph(data, title, domElement) {
            var datos = google.visualization.arrayToDataTable(data);

            var opciones = {
                title: title,
                curveType: 'function',
                legend: {position: 'bottom'}
            };

            var grafica = new google.visualization.LineChart(domElement);
            grafica.draw(datos, opciones);
        }

        function dibujarGrafica1() {
            var datos = google.visualization.arrayToDataTable(sensor1Data);

            var opciones = {
                title: 'Sensor 1',
                curveType: 'function',
                legend: {position: 'bottom'}
            };

            var grafica = new google.visualization.LineChart(document.getElementById('graficaLinea1'));
            grafica.draw(datos, opciones);
        }

        function dibujarGrafica2() {
            var datos = google.visualization.arrayToDataTable(sensor2Data);

            var opciones = {
                title: 'Sensor 2',
                curveType: 'function',
                legend: {position: 'bottom'}
            };

            var grafica = new google.visualization.LineChart(document.getElementById('graficaLinea2'));
            grafica.draw(datos, opciones);
        }

        function connect() {
            webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/sensorsUpdate");
            webSocket.onmessage = function (datos) {
                getUpdateDataFromServer(datos);
            };
        }

        function checkConnection() {
            if (!webSocket || webSocket.readyState == 3) {
                connect();
            }
        }

        setInterval(checkConnection, updateTimeInterval);
    </script>
</body>
</html>
