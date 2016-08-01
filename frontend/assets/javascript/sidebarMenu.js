var switcher = [];
var btnWasClicked = false;
var barData = undefined;

function sidebarMenu(){
    // Holds the role of the user.
    var role = -1;
    // String we'll be searching on.
    var searchOn = "vopro7_role=";
    // Get cookie data.
    var cookieData = document.cookie;


    function loadSidebar(niv){


        // Will hold the text that will appear later when the button is pressed.
        switcher = [];

        var fillSidebar = function(json){
            // Get the location that will hold our sidebar menu and fill it in with an ul.
            $("#sidebar-wrapper").empty();
            var menu = $("#sidebar-wrapper").append('<ul  class = \"sidebar-nav\"></ul>').find('ul');
            for(var i= 0; i < json[niv].length; i++){
                //Put the text that will later appear next to the icons in this global var.
                switcher.push(json[niv][i].nameLink);
                if(json[niv][i].nameLink === "Meldingen"){
                    //Put the icons in order in li items and link them to their correct pages.
                    menu.append('<li id="menuEventsNotify"><a href = \" ' + json[niv][i].linksTo + '\" ><span class = \" ' + json[niv][i].icon + '\" ></span></a></li>');
                } else {
                    //Put the icons in order in li items and link them to their correct pages.
                    menu.append('<li><a href = \" ' + json[niv][i].linksTo + '\" ><span class = \" ' + json[niv][i].icon + '\" ></span></a></li>');
                }
            }

            //If the togglemenu was open than it fills in the textfields of the icons.
            if($("#menu-toggle span").attr("class").indexOf("left") > 0){
                var listItems = $(".sidebar-nav li a");
                var k = 0;
                angular.forEach(listItems, function(item){
                    item.innerHTML = item.innerHTML.concat(" " + switcher[k]);
                    k++;
                });
            }
        }

        if(barData){
            // Json already got loaded once. Just fill the bar-menu
            fillSidebar(barData);
        } else{
            // Asks the json file with our data about our menubar up and fill it in.
            $.getJSON("assets/json/sidebarMenu.json",function(json){
                barData = json;
                fillSidebar(barData);
            });
        }

        //Unbind previous click functions!
        $("#menu-toggle").unbind();
        // Yup here we press that button we mentioned earlier.
        $("#menu-toggle").click(function(e){
            e.preventDefault();

            // Go get them links!
            var listItems = $(".sidebar-nav li a");

            // Toggle the css of the involved objects so that it slides in and out and looks all fancy.
            $("#main-menu").toggleClass("display");
            $("#main-content").toggleClass("display");

            //Switch icon in button.
            $("#menu-toggle span").toggleClass("glyphicon-triangle-left");
            $("#menu-toggle span").toggleClass("glyphicon-triangle-right");

            // Add/remove the text.
            if(listItems[0].textContent.length === 0){
                btnWasClicked = !btnWasClicked;
                // Delay the filling of the box with text because else it looks ugly.
                setTimeout(function() {
                    if(btnWasClicked){
                        // Add the text next to our icons.
                        for (var i = 0, length = listItems.length; i < length; i++){
                            listItems[i].innerHTML = listItems[i].innerHTML.concat(" " + switcher[i]);
                        }
                        btnWasClicked = !btnWasClicked;
                    }
                },800);


            } else {
                //No delay here because it should happen instantly, the disappearing of the text.
                for (var i = 0, length = listItems.length; i < length; i++) {
                    // Remove the text from next to our icons once more.
                    listItems[i].innerHTML = listItems[i].innerHTML.replace(listItems[i].textContent, "");
                }
            }
        });
    }

    // Check if we have cookies.
    if(cookieData.length != 0) {

        // indexOf returns the first index of where the substring matches in the string,
        // thus we must add the length of what we searched on to find the index at which the value we want starts.
        var startIndex = cookieData.indexOf(searchOn) + searchOn.length;
        // Check if we are logged in.
        if(startIndex > searchOn.length){
            var endIndex = cookieData.indexOf(";", startIndex);
            // Get authorisation level of the logged in user.
            role = cookieData.substring(startIndex, endIndex);
        }
    }

    loadSidebar(role);
}

sidebarMenu();

var originalColor = undefined;
var alertUserForEvents = function(number){
    var notifier = $("#menuEventsNotify a");
    originalColor = notifier.css("color");
    notifier.css("color", "#e30513");//#eb5c3f
    /*if(number){
        notifier.append("(" + number + ")");
    }*/
};

var clearAlertForEvents = function(){
    if(originalColor){
        $("#menuEventsNotify a").css("color", originalColor);
    }
};

