// WaypointAdder object
function WaypointAdder(controlDiv, mapString) {
  var controlUI = document.createElement('div');
  controlUI.style.backgroundColor = '#FFF';
  controlUI.style.borderRadius = '5px';
  controlUI.style.boxShadow = '0 2px 6px rgba(0,0,0,.3)';
  controlUI.style.cursor = 'pointer';
  controlUI.style.margin = '0px 10px 10px 0px';
  controlUI.style.padding = '10px';
  controlUI.title = 'Klik op het toevoegicoon om een waypointadres in te geven';
  controlDiv.appendChild(controlUI);

  var controlImg = document.createElement('div');
  controlImg.style.backgroundImage = 'url(assets/images/plus.svg)';
  controlImg.style.width = '20px';
  controlImg.style.height = '20px';
  controlImg.style.padding = '4px';
  controlUI.appendChild(controlImg);

  controlUI.addEventListener('click', function() {
    $(document).ready(function(){
      $("#addWaypoint").fadeTo(1000,1);
      $("#"+mapString).css('transition','1s');
      $("#"+mapString).css('-webkit-filter','blur(5px)');
    });
  });
}
