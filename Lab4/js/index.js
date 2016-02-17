var xhttp = new XMLHttpRequest();

function search() {
	var county = document.getElementById("county").value;
	var searchValue = document.getElementById("search").value;
	xhttp.onreadystatechange = function() {
		if (xhttp.readyState == 4 && xhttp.status == 200) {
			document.getElementById("table").innerHTML = xhttp.responseText;
		}
	};
	xhttp.open("GET", "search.php?search=" + searchValue + "&county=" + county, true);
	xhttp.send();
}

search();