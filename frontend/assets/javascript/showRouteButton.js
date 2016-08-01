// route button
function ShowRouteButton(controlDiv, object) {
  var controlUI = document.createElement('div');
  controlUI.style.backgroundColor = '#FFF';
  controlUI.style.borderRadius = '5px';
  controlUI.style.boxShadow = '0 2px 6px rgba(0,0,0,.3)';
  controlUI.style.cursor = 'pointer';
  controlUI.style.margin = '10px';
  controlUI.style.padding = '10px';
  // controlUI.style.display = 'none';
  controlDiv.appendChild(controlUI);

  var controlImg = document.createElement('div');
  controlImg.style.fontSize = '18px';
  controlImg.style.textAlign = 'center';
  controlImg.innerHTML = "Toon mijn route";
  controlImg.style.width = '200px';
  controlImg.style.height = '30px';
  controlImg.style.backgroundPosition = 'center';
  controlImg.style.padding = '4px';
  // controlImg.style.display = 'none';
  controlUI.appendChild(controlImg);
  controlUI.addEventListener('click', function(){
    object.renderRoute();
  });

}
