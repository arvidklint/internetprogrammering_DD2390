var xhttp = new XMLHttpRequest();

var county = document.getElementById("county").value;
var residenceType = document.getElementById("residence-type").value;
var searchValue = document.getElementById("search").value;
var countyDefault = county;
var residenceTypeDefault = residenceType;
var searchDefault = searchValue;

var minRooms = 0;
var maxRooms = 10;
var minRoomsDefault = minRooms;
var maxRoomsDefault = maxRooms;

var minPrice = 0;
var maxPrice = 10000000;
var minPriceDefault = minPrice;
var maxPriceDefault = maxPrice;

var minArea = 0;
var maxArea = 500;
var minAreaDefault = minArea;
var maxAreaDefault = maxArea;

var minRent = 0;
var maxRent = 10000;
var minRentDefault = minRent;
var maxRentDefault = maxRent;

var orderBy = "price";
var orderDirection = "asc";

function search() {
	county = document.getElementById("county").value;
	console.log("county: " + county);
	residenceType = document.getElementById("residence-type").value;
	searchValue = document.getElementById("search").value;

	xhttp.onreadystatechange = function() {
		if (xhttp.readyState == 4 && xhttp.status == 200) {
			var response = xhttp.responseText;
			if (response !== "") {
				document.getElementById("table").innerHTML = response;
			} else {
				document.getElementById("table").innerHTML = "Inga bostäder uppfyller dina kriterier...";
			}
		}
	};

	console.log("search.php?search=" + searchValue +
		"&county=" + county +
		"&residenceType=" + residenceType +
		"&minRooms=" + minRooms +
		"&maxRooms=" + maxRooms +
		"&minPrice=" + minPrice + 
		"&maxPrice=" + maxPrice +
		"&minArea=" + minArea + 
		"&maxArea=" + maxArea +
		"&minRent=" + minRent +
		"&maxRent=" + maxRent + 
		"&orderBy=" + orderBy + 
		"&orderDirection=" + orderDirection);

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
		"&maxRent=" + maxRent + 
		"&orderBy=" + orderBy + 
		"&orderDirection=" + orderDirection, true);
	xhttp.send();
}

$(function() {
	xhttp.onreadystatechange = function() {
		if (xhttp.readyState == 4 && xhttp.status == 200) {
			if (xhttp.responseText !== "0") {
				var values = JSON.parse(xhttp.responseText);
				console.log(values);
				countyDefault = values.county;
				console.log(values.county);
				searchDefault = values.search;
				residenceTypeDefault = values.objectType;
				minRoomsDefault = parseInt(values.minRooms);
				maxRoomsDefault = parseInt(values.maxRooms);
				minPriceDefault = parseInt(values.minPrice);
				maxPriceDefault = parseInt(values.maxPrice);
				minAreaDefault = parseInt(values.minArea);
				maxAreaDefault = parseInt(values.maxArea);
				minRentDefault = parseInt(values.minRent);
				maxRentDefault = parseInt(values.maxRent);
				orderBy = values.orderBy;
				orderDirection = values.orderDirection;
			}
			init();
			search();
		}
	};

	xhttp.open("GET", "getCookieInfo.php", true);
	xhttp.send();
});


function init() {
	$( "#slider-rooms" ).slider({
		range: true,
		min: minRooms,
		max: maxRooms,
		values: [ minRoomsDefault, maxRoomsDefault ],
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
		values: [ minPriceDefault, maxPriceDefault ],
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
		values: [ minAreaDefault, maxAreaDefault ],
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
		values: [ minRentDefault, maxRentDefault ],
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

	$("#table-header").children("th").each(function() {
		$(this).click(function() {
			if (orderBy === this.id) {
				if (orderDirection === 'asc') {
					orderDirection = 'desc';
				} else {
					orderDirection = 'asc';
				}
			} else {
				orderBy = this.id;
				orderDirection = "asc";
			}
			search();
		});
	});
	console.log(countyDefault);
	console.log($("#" + countyDefault));
	console.log($("#county").val(countyDefault));
	$("#county").children("option").each(function() {
		$(this).removeAttr("selected");
	});
	$("#county").val(countyDefault);
	$("#" + residenceTypeDefault).val(residenceTypeDefault);
	$("#" + residenceTypeDefault).attr("selected", "selected");
	$('#county option[value="' + countyDefault + '"]').attr("selected", "selected");
	$("#search").val(searchDefault);

	county = countyDefault;
	searchValue = searchDefault;
	residenceType = residenceTypeDefault;
	minRooms = minRoomsDefault;
	maxRooms = maxRoomsDefault;
	minPrice = minPriceDefault;
	maxPrice = maxPriceDefault;
	minArea = minAreaDefault;
	maxArea = maxAreaDefault;
	minRent = minRentDefault;
	maxRent = maxRentDefault;
}

