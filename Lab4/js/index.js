var xhttp = new XMLHttpRequest();
var minRooms = 0;
var maxRooms = 6;

var minPrice = 0;
var maxPrice = 10000000;

var minArea = 0;
var maxArea = 500;

var minRent = 0;
var maxRent = 10000;

function search() {
	var county = document.getElementById("county").value;
	var residenceType = document.getElementById("residence-type").value;
	var searchValue = document.getElementById("search").value;

	xhttp.onreadystatechange = function() {
		if (xhttp.readyState == 4 && xhttp.status == 200) {
			document.getElementById("table").innerHTML = xhttp.responseText;
		}
	};

	xhttp.open("GET", "search.php?search=" + searchValue +
		"&county=" + county +
		"&residenceType=" + residenceType +
		"&minRooms=" + minRooms +
		"&maxRooms=" + maxRooms +
		"&minPrice=" + minPrice + 
		"&maxPrice=" + maxPrice +
		"&minArea=" + minArea + 
		"&maxArea=" + maxArea +
		"&minRent=" + minRent +
		"&maxRent=" + maxRent, true);
	xhttp.send();
}

$(function() {
	$( "#slider-rooms" ).slider({
		range: true,
		min: minRooms,
		max: maxRooms,
		values: [ minRooms, maxRooms ],
		slide: function( event, ui ) {
			$( "#room-amount" ).html( ui.values[ 0 ] + " - " + ui.values[ 1 ] + " rum");
			minRooms = ui.values[ 0 ];
			maxRooms = ui.values[ 1 ];
			search();
		}
	});

	$( "#slider-price" ).slider({
		range: true,
		min: minPrice,
		max: maxPrice,
		values: [ minPrice, maxPrice ],
		step : 100000,
		slide: function( event, ui ) {
			$( "#price-amount" ).html( ui.values[ 0 ] + " - " + ui.values[ 1 ] + " SEK");
			minPrice = ui.values[ 0 ];
			maxPrice = ui.values[ 1 ];
			search();
		}
	});

	$( "#slider-area" ).slider({
		range: true,
		min: minArea,
		max: maxArea,
		values: [ minArea, maxArea ],
		step : 5,
		slide: function( event, ui ) {
			$( "#area-amount" ).html( ui.values[ 0 ] + " - " + ui.values[ 1 ] + " m2");
			minArea = ui.values[ 0 ];
			maxArea = ui.values[ 1 ];
			search();
		}
	});

	$( "#slider-rent" ).slider({
		range: true,
		min: minRent,
		max: maxRent,
		values: [ minRent, maxRent ],
		step : 100,
		slide: function( event, ui ) {
			$( "#rent-amount" ).html( ui.values[ 0 ] + " - " + ui.values[ 1 ] + " SEK");
			minRent = ui.values[ 0 ];
			maxRent = ui.values[ 1 ];
			search();
		}
	});

	$( "#room-amount" ).html( $( "#slider-rooms" ).slider( "values", 0 ) + " - " + $( "#slider-rooms" ).slider( "values", 1 ) + " rum");
	$( "#price-amount" ).html( $( "#slider-price" ).slider( "values", 0 ) + " - " + $( "#slider-price" ).slider( "values", 1 ) + " SEK");
	$( "#area-amount" ).html( $( "#slider-area" ).slider( "values", 0 ) + " - " + $( "#slider-area" ).slider( "values", 1 ) + " m2");
	$( "#rent-amount" ).html( $( "#slider-rent" ).slider( "values", 0 ) + " - " + $( "#slider-rent" ).slider( "values", 1 ) + " SEK");
	search();
});

