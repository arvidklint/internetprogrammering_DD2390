<!DOCTYPE html>
<html>
	<head>
		<title>Lab4</title>
		<meta charset="UTF-8"/>
		<link rel="stylesheet" href="http://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css"/>
		<link rel="stylesheet" type="text/css" href="css/index.css"/>
	</head>
	<body>
		<div>
			Adress: 
			<input type="text" id="search" oninput="search()"></input>
			Län: 
			<select id="county" oninput="search()">
			<?php
				$host = 'mysql­-vt2016.csc.kth.se';
				$user = 'emiwes_user';
				$pw = 'HXLZZ8Si';
				$dbname = 'emiwes';

				$db = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $user, $pw);
				$query = "SELECT county FROM residence GROUP BY county;";

				try {
					$stmt = $db->prepare($query);
					$stmt->execute();
				} catch(PDOException $e) {
					echo "Query Error: " . $e;
				}
				echo "<option id='allcounties' value='allcounties'> – </option>";
				while($line = $stmt->fetch(PDO::FETCH_ASSOC)){ 
					$county = $line['county'];
					echo "<option id='$county' value='$county'>$county</option>";
				}
			?>
			</select>
			Bostadstyp:
			<select id="residence-type" oninput="search()">
				<?php
					$host = 'mysql­-vt2016.csc.kth.se';
					$user = 'emiwes_user';
					$pw = 'HXLZZ8Si';
					$dbname = 'emiwes';

					$db = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $user, $pw);
					$query = "SELECT objectType FROM residence GROUP BY objectType;";

					try {
						$stmt = $db->prepare($query);
						$stmt->execute();
					} catch(PDOException $e) {
						echo "Query Error: " . $e;
					}
					echo "<option id='alltypes' value='alltypes'> – </option>";
					while($line = $stmt->fetch(PDO::FETCH_ASSOC)){ 
						$type = $line['objectType'];
						echo "<option value='$type'>$type</option>";
					}
				?>
			</select>
		</div>

		<div class="slider" id="room-container">
			<div>Rum: <span id="room-amount"></span></div>
			<div id="slider-rooms"></div>
		</div>

		<div class="slider" id="price-container">
			<div>Pris: <span id="price-amount"></span></div>
			<div id="slider-price"></div>
		</div>

		<div class="slider" id="area-container">
			<div>Area: <span id="area-amount"></span></div>
			<div id="slider-area"></div>
		</div>

		<div class="slider" id="rent-container">
			<div>Rent: <span id="rent-amount"></span></div>
			<div id="slider-rent"></div>
		</div>

		<table>
			<thead>
				<tr id="table-header">
					<th id="county">Län</th>
					<th id="objectType">Bostadstyp</th>
					<th id="address">Adress</th>
					<th id="area">Area [m2]</th>
					<th id="room">Antal rum</th>
					<th id="price">Pris [SEK]</th>
					<th id="rent">Hyra [SEK]</th>
				</tr>
			</thead>
			<tbody id="table">
				
			</tbody>
		</table>

		<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
		<script src="http://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
		<script src="js/index.js"></script>

	</body>
</html>

