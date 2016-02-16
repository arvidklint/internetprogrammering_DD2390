/*jslint node: true */
/*jslint browser:true */
"use strict";

var mode = 0; // 0 == prepare game | 1 == shoot ship | etc..

var unplacedShips = [2,3,3,4,5];
var destroyedShips = [];
var defaultColor = "steelblue";
var highlightColor = "green";
var shipColor = "brown";
var vertical = false;
var shipObjects = [];
var width;
var height;
var totalShotsFired = 0;
var gameWon = false;

function generateGrid(){
	updateScoreBoardView();
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
				};
			})();

			gridDiv.onmouseout = (function(){
				var xPos = x;
				var yPos = y;
				return function() {
					drawShip(defaultColor, unplacedShips[0], xPos, yPos);
				};
			})();

			gridDiv.onclick = (function(){
				var xPos = x;
				var yPos = y;
				return function() {
					if( document.getElementById(xPos + "_" + yPos).className.indexOf("shipPiece") <= -1){
						placeShip(unplacedShips[0], xPos, yPos);
					}
				};
			})();

			row.appendChild(gridDiv);
		}

		playingBoard.appendChild(row);
	}
	var winningOverlay = document.getElementById("winningOverlay");
	winningOverlay.style.width = playingBoard.offsetWidth + "px";
	winningOverlay.style.height = playingBoard.offsetHeight + "px";
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
	if(mode === 0 && unplacedShips.length !== 0 ){
		var shipObject = {"length" : _length, "xPos" : _xPos, "yPos" : _yPos, "vertical" : vertical, "hits" : 0, "destroyed" : false };
		var tempShipPieces = [];
		var spotAvailable = true;
		shipObjects.push(shipObject);
		unplacedShips.shift();
		updateScoreBoardView();

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

				tempShipPieces[index].ondblclick = function(){
					removeShip(_length, _xPos, _yPos);
				};
			}
		}
		if(unplacedShips.length === 0){
			console.log("All ships have been placed");
			console.log(shipObjects);
			// mode = 1;
		}
	}
}

function removeShip(_length, _xPos, _yPos){
	if(mode === 0){
		for(var j = 0; j < shipObjects.length; j++){
			if( (shipObjects[j].xPos === _xPos) && (shipObjects[j].yPos === _yPos) && (shipObjects[j].length !== undefined) ){
				var orientation = shipObjects[j].vertical;
				shipObjects.splice(j, 1);
				console.log(shipObjects);
			}
		}

		for( var i = 0; i<_length; i++ ){
			var nextPiece = document.getElementById(_xPos + (orientation ? 0 : i) + "_" + (_yPos + (orientation ? i : 0)));
			nextPiece.className = nextPiece.className.replace(/\bshipPiece\b/, '');
			nextPiece.style.backgroundColor = defaultColor;
		}

		unplacedShips.push(_length);
		updateScoreBoardView();
	}
}

function hideShips(){
	for( var y = 0; y < height; y++ ){
		for( var x = 0; x < width; x++ ){
			var gridDiv = document.getElementById(x + "_" + y);
			gridDiv.style.backgroundColor = defaultColor;

			gridDiv.onclick = (function(){
				var xPos = x;
				var yPos = y;
				return function() {
					if(gameWon === false){
						totalShotsFired++;
						updateScoreBoardView();
					}
					if(this.className.indexOf("shipPiece") > -1){
						this.style.backgroundColor = 'yellow';	
						checkHit(xPos, yPos);
						if(destroyedShips.length === shipObjects.length){
							gameWon = true;
							document.getElementById("winningOverlay").style.display = "block";
						}
					}			
					else{
						this.style.backgroundColor = "red";
					}
				};
			})();
		}
	}
}

function checkHit(_xPos, _yPos){
	console.log(_xPos + '_' + _yPos);
	for(var i = 0; i < shipObjects.length; i ++){
		var ship = shipObjects[i];
		for(var j = 0; j < ship.length; j++){
			if( ship.xPos + (ship.vertical ? 0 : j) === _xPos && ship.yPos + (ship.vertical ? j : 0) === _yPos){
				ship.hits ++;
				if(ship.hits === ship.length){
					ship.destroyed = true;
					for(var k = 0; k < ship.length; k++ ){
						var nextPiece = document.getElementById(ship.xPos + (ship.vertical ? 0 : k) + "_" + (ship.yPos + (ship.vertical ? k : 0)));
						nextPiece.style.backgroundColor = "green";
					}
					destroyedShips.push(ship);
					console.log("ship has been destroyed!");
				}
				console.log("Hits on ship: " + ship.hits);
			}
		}
	}
}

function updateScoreBoardView(){
	document.getElementById("shipsPlaced").innerHTML = shipObjects.length;
	document.getElementById("shipsDestroyed").innerHTML = destroyedShips.length;
	document.getElementById("shotsFired").innerHTML = totalShotsFired;
}

function changeMode(_modeNumber){
	mode = _modeNumber;
}

function resetGame(){
	document.getElementById("winningOverlay").style.display = "none";
	unplacedShips = [2,3,3,4,5];
	destroyedShips = [];
	defaultColor = "steelblue";
	highlightColor = "green";
	shipColor = "brown";
	vertical = false;
	shipObjects = [];
	totalShotsFired = 0;
	gameWon = false;
	console.log("Changing mode to '0'. Reseting game.")
	changeMode(0);
	generateGrid();
}

document.onkeydown = function(e){
	if( e.keyCode === 82 ){	// keypress on 'R'.
		vertical = !vertical;
		for( var y = 0; y < height; y++ ){
			for( var x = 0; x < width; x++ ){
				var piece = document.getElementById(x + "_" + y);
				if( piece.className.indexOf("shipPiece") <= -1){
					piece.style.backgroundColor = defaultColor;
				}
			}
		}
	}
};

var generateButton = document.getElementById("generateButton");
generateButton.onclick = function(){
	resetGame();
	generateGrid();
};

var startGameButton = document.getElementById("startGameButton");
startGameButton.onclick = function(){
	console.log("Changing mode to '1'. Starting game.");
	changeMode(1);
	updateScoreBoardView();
	console.log("Hiding Ships.");
	hideShips();
};

var resetGameButton = document.getElementById("resetGameButton");
resetGameButton.onclick = function(){
	resetGame();
};


generateGrid();


