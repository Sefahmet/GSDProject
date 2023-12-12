 var map;
 var coord1Input = document.getElementById("coord1");
 var coord2Input = document.getElementById("coord2");
 var marker1;
 var marker2;
 var line;

 var leftBottom =  ol.proj.transform([7.0390799173829555,  50.6616799499222], "EPSG:4326", "EPSG:3857");
 var rightTop = ol.proj.transform([7.172951,   50.78453], "EPSG:4326", "EPSG:3857");
 var minx = leftBottom[0];
 var miny = leftBottom[1];
 var maxx = rightTop[0];
 var maxy = rightTop[1];
 var weights = {
     wLength: 1,
     wSlope: 1,
     wMaxSpeed: 1,
     wTurnLeft: 1,
     wGreenary: 1
 };
tile_layer = new ol.layer.Tile({ source: new ol.source.OSM() });
var oldZoom = 3;

var map = new ol.Map({
	target: 'map',
	layers: [
		tile_layer
	],
	view: new ol.View({
		center: ol.proj.fromLonLat([7.086, 50.727]),
		zoom: oldZoom,
		maxZoom: 20,
		minZoom: 9,
		extent: [minx, miny, maxx, maxy],
	})
});
map.on("click", function (e) {
      var position = ol.proj.toLonLat(e.coordinate);
      if (!marker1) {
        marker1 = new ol.Feature({
          geometry: new ol.geom.Point(e.coordinate)
        });
        var markerLayer = new ol.layer.Vector({
          source: new ol.source.Vector({
            features: [marker1]
          })
        });
        map.addLayer(markerLayer);
        coord1Input.value = position[0].toFixed(7) + "," + position[1].toFixed(7);
      } else if (!marker2) {
        marker2 = new ol.Feature({
          geometry: new ol.geom.Point(e.coordinate)
        });
        var markerLayer = new ol.layer.Vector({
          source: new ol.source.Vector({
            features: [marker2]
          })
        });
        map.addLayer(markerLayer);
        coord2Input.value = position[0].toFixed(7) + "," + position[1].toFixed(7);
      } else {
        marker1.getGeometry().setCoordinates(e.coordinate);
        coord1Input.value = position[0].toFixed(7) + "," + position[1].toFixed(7);
      }
    });

  function findShortestPath() {
    console.log("this function is findShortestPath version 2")

    var coord1 = coord1Input.value.split(",");
    var coord2 = coord2Input.value.split(",");
    var lat1 = parseFloat(coord1[1]);
    var lon1 = parseFloat(coord1[0]);
    var lat2 = parseFloat(coord2[1]);
    var lon2 = parseFloat(coord2[0]);
      console.log(lat1,
                  lon1,
                  lat2,
                  lon2)

      wLength = weights.wLength
      wSlope = weights.wSlope
      wMaxSpeed = weights.wMaxSpeed
      wTurnLeft = weights.wTurnLeft
      wGreenary = weights.wGreenary




      const url = `http://131.220.71.188:8080/masterproject/Effortless/Coordinates?lat1=${lat1}&lon1=${lon1}&lat2=${lat2}&lon2=${lon2}&wLength=${wLength}&wSlope=${wSlope}&wMaxSpeed=${wMaxSpeed}&wTurnLeft=${wTurnLeft}&wGreenary=${wGreenary}`;     console.log(url)
    fetch(url)
            .then(response => {
              if (response.ok) {
                return response.json();
              } else {
                throw new Error('Connection is unsuccessful.');
              }
            })
            .then(data => {

              console.log('Server respond:', data);
              drawPath(data);
            })
            .catch(error => {
              console.error('Connection is unsuccessful.', error);
            });
  }

  function drawPath(path) {
    if (line) {
      map.removeLayer(line);
    }

    var points = [];
    for (var i = 0; i < path.length; i++) {
      var coord = path[i];
        var point = ol.proj.fromLonLat([coord.y, coord.x]);
        points.push(point);


    }
    console.log("pushed points", points);

    var lineString = new ol.geom.LineString(points);
      console.log("linestring", lineString);
    var lineFeature = new ol.Feature({
      geometry: lineString
    });

    var lineStyle = new ol.style.Style({
      stroke: new ol.style.Stroke({
        color: '#0022ff',
        width: 2,
        opacity: 1
      })
    });
    lineFeature.setStyle(lineStyle);

    var vectorSource = new ol.source.Vector({
      features: [lineFeature]
    });

    line = new ol.layer.Vector({
      source: vectorSource
    });
      console.log(line);
    map.addLayer(line);
    console.log("line added");
  }
 var range_el = document.querySelector('input[type=range]');

 range_el.addEventListener('input', function() {
     this.setAttribute('value', this.value);
 }, false);
 function handleSliderChange() {
     weights.wLength = document.getElementById("distanceRange").value;
     weights.wSlope = document.getElementById("slopeRange").value;
     weights.wMaxSpeed = document.getElementById("speedRange").value;
     weights.wTurnLeft = document.getElementById("turningCostRange").value;
     weights.wGreenary = document.getElementById("greenaryRange").value;

 }
 document.getElementById("distanceRange").addEventListener("input", handleSliderChange);
 document.getElementById("slopeRange").addEventListener("input", handleSliderChange);
 document.getElementById("speedRange").addEventListener("input", handleSliderChange);
 document.getElementById("turningCostRange").addEventListener("input", handleSliderChange);
 document.getElementById("greenaryRange").addEventListener("input", handleSliderChange);