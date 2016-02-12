"use strict";

var mode = 0; // 0 == prepare game | 1 == shoot ship | etc..

var unplacedShips = [2,3,3,4,5];
var defaultColor = "steelblue";
var highlightColor = "green";
var shipColor = "brown";
var vertical = false;
var shipObjects = [];
var width;
var height;

function generateGrid(){
	width = document.getElementById("width").value;
	height = document.getElementById("height").value;
	var playingBoard = document.getElementById("playingBoard");
	
	while( playingBoard.firstChild ){
		playingBoard.removeChild(playingBoard.firstChild);
	}

	for( var y = 0; y < height; y++ ){
		var row = document.createElement("div");
		row.setAttribute("class","boardRow");

		for( var x = 0; x < width; x++ ){
			var gridDiv = document.createElement("div");
			gridDiv.setAttribute("class","boardPiece");
			gridDiv.setAttribute("id", x + "_" + y);

			gridDiv.onmouseover = (function(){
				var xPos = x;
				var yPos = y;
				return function() {
					drawShip(highlightColor, unplacedShips[0], xPos, yPos);
				}
			})();

			gridDiv.onmouseout = (function(){
				var xPos = x;
				var yPos = y;
				return function() {
					drawShip(defaultColor, unplacedShips[0], xPos, yPos);
				}
			})();

			gridDiv.onclick = (function(){
				var xPos = x;
				var yPos = y;
				return function() {
					placeShip(unplacedShips[0], xPos, yPos);
				}
			})();


			row.appendChild(gridDiv);
		}

		playingBoard.appendChild(row);
	}
}

var generateButton = document.getElementById("generateButton");
generateButton.onclick = function(){
	generateGrid();
}

function drawShip(_color, _length, _xPos, _yPos){ // rotation
	if(mode === 0){
		var tempShipPieces = [];
		var spotAvailable = true;
		for(var i = 0; i < _length; i++){
			
			var nextPiece = document.getElementById(_xPos + (vertical ? 0 : i) + "_" + (_yPos + (vertical ? i : 0)));
			tempShipPieces.push(nextPiece);

			if(nextPiece === null || nextPiece.className.indexOf("shipPiece") > -1 ){
				spotAvailable = false;
			}
		}
		for( var index in tempShipPieces  ){
			if(spotAvailable){
				tempShipPieces[index].style.backgroundColor = _color;
			}
		}
	}
}

function placeShip(_length, _xPos, _yPos){
	if(mode === 0){
		var shipObject = {"length" : _length, "xPos" : _xPos, "yPos" : _yPos, "vertical" : vertical };
		var tempShipPieces = [];
		var spotAvailable = true;
		shipObjects.push(shipObject);
		unplacedShips.shift();

		for( var i = 0; i<_length; i++){
			var nextPiece = document.getElementById(_xPos + (vertical ? 0 : i) + "_" + (_yPos + (vertical ? i : 0)));
			tempShipPieces.push(nextPiece);

			if(nextPiece === null){
				spotAvailable = false;
			}
		}
		for( var index in tempShipPieces  ){
			if(spotAvailable){
				tempShipPieces[index].className += " shipPiece";
				tempShipPieces[index].style.backgroundColor = shipColor;
			}
		}
		if(unplacedShips.length === 0){
			mode = 1;
		}
	}
}

function changeMode(_modeNumber){
	mode = _modeNumber;
}

document.onkeydown = function(e){
	if( e.keyCode === 82 ){	
		vertical = !vertical;
		for( var y = 0; y < height; y++ ){
			for( var x = 0; x < width; x++ ){
				var piece = document.getElementById(x + "_" + y)
				if( piece.className.indexOf("shipPiece") <= -1){
					piece.style.backgroundColor = defaultColor;
				}
			}
		}
	}
}


generateGrid();