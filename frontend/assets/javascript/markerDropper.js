// MarkerDropper object
function MarkerDropper(controlDiv,$scope, map, MARKERICON) {
  var controlUI = document.createElement('div');
  controlUI.style.backgroundColor = '#FFF';
  controlUI.style.borderRadius = '5px';
  controlUI.style.boxShadow = '0 2px 6px rgba(0,0,0,.3)';
  controlUI.style.cursor = 'pointer';
  controlUI.style.margin = '10px';
  controlUI.style.padding = '10px';
  controlUI.title = 'Sleep een marker op de map om een waypoint in te voegen';
  controlDiv.appendChild(controlUI);

  var controlImg = document.createElement('div');
  controlImg.style.backgroundImage = 'url(assets/images/map-marker.svg)';
  controlImg.style.width = '50px';
  controlImg.style.height = '50px';
  controlImg.style.padding = '4px';
  controlUI.appendChild(controlImg);

  controlUI.addEventListener('click', function() {
    $scope.addNewMarker(map.center, MARKERICON, map);
  });
}
