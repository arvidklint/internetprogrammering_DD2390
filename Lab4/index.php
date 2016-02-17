<!DOCTYPE html>
<html>
	<head>
		<title>Lab4</title>
		<meta charset="UTF-8"/>
	</head>
	<body>
		<div>
			Sök: 
			<input type="text" id="search" oninput="search()"></input>
			<select id="county" oninput="search()">
			<?php
				$host = 'localhost';
				$user = 'root';
				$pw = 'root';
				$dbname = 'Lab4';

				$search = $_GET['search'];

				$db = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $user, $pw);
				$query = "SELECT county FROM residence GROUP BY county;";

				try {
					$stmt = $db->prepare($query);
					$stmt->execute();
				} catch(PDOException $e) {
					echo "Query Error: " . $e;
				}

				while($line = $stmt->fetch(PDO::FETCH_ASSOC)){ 
					$county = $line['county'];
					echo "<option value='$county'>$county</option>";
				}
			?>
			</select>
		</div>

		<table id="table">
			
		</table>

		<script src="js/index.js"></script>

	</body>
</html>

