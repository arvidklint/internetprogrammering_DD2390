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
				$host = 'localhost';
				$user = 'root';
				$pw = 'root';
				$dbname = 'Lab4';

				$db = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $user, $pw);
				$query = "SELECT county FROM residence GROUP BY county;";

				try {
					$stmt = $db->prepare($query);
					$stmt->execute();
				} catch(PDOException $e) {
					echo "Query Error: " . $e;
				}
				echo "<option value='all'> – </option>";
				while($line = $stmt->fetch(PDO::FETCH_ASSOC)){ 
					$county = $line['county'];
					echo "<option value='$county'>$county</option>";
				}
			?>
			</select>
			Bostadstyp:
			<select id="residence-type" oninput="search()">
				<?php
					$host = 'localhost';
					$user = 'root';
					$pw = 'root';
					$dbname = 'Lab4';

					$db = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $user, $pw);
					$query = "SELECT objectType FROM residence GROUP BY objectType;";

					try {
						$stmt = $db->prepare($query);
						$stmt->execute();
					} catch(PDOException $e) {
						echo "Query Error: " . $e;
					}
					echo "<option value='all'> – </option>";
					while($line = $stmt->fetch(PDO::FETCH_ASSOC)){ 
						$type = $line['objectType'];
						echo "<option value='$type'>$type</option>";
					}
				?>
			</select>
		</div>

		<div class="slider" id="room-container">
			<div id="room-amount">Rooms:</div>
			<div id="slider-rooms"></div>
		</div>

		<div class="slider" id="price-container">
			<div id="price-amount">Price range:</div>
			<div id="slider-price"></div>
		</div>

		<div class="slider" id="area-container">
			<div id="area-amount">Area:</div>
			<div id="slider-area"></div>
		</div>

		<div class="slider" id="rent-container">
			<div id="rent-amount">Rent:</div>
			<div id="slider-rent"></div>
		</div>

		<table id="table">
			
		</table>

		<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
		<script src="http://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
		<script src="js/index.js"></script>

	</body>
</html>

