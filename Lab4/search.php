<tr>
	<th>County</th>
	<th>Type</th>
	<th>Address</th>
	<th>Area [m2]</th>
	<th>Rooms</th>
	<th>Price [SEK]</th>
	<th>Rent [SEK]</th>
</tr>

<?php
$host = 'localhost';
$user = 'root';
$pw = 'root';
$dbname = 'Lab4';

$search = "%".$_GET['search']."%";
$county = $_GET['county'];
$residenceType = $_GET['residenceType'];
$minRooms = $_GET['minRooms'];
$maxRooms = $_GET['maxRooms'];
$minPrice = $_GET['minPrice'];
$maxPrice = $_GET['maxPrice'];
$minArea = $_GET['minArea'];
$maxArea = $_GET['maxArea'];
$minRent = $_GET['minRent'];
$maxRent = $_GET['maxRent'];

$objectString = ' objectType = :objectType AND ';
if($residenceType === 'all'){
	$objectString = '';
}

$countyString = ' county = :county AND ';
if($county === 'all'){
	$countyString = '';
}


$db = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $user, $pw);
$query = "SELECT * 	FROM residence WHERE $countyString $objectString address LIKE :address AND room >= :minRooms AND room <= :maxRooms AND price >= :minPrice AND price <= :maxPrice AND area >= :minArea AND area <= :maxArea AND rent >= :minRent AND rent <= :maxRent ORDER BY price ASC;";

try {
	$stmt = $db->prepare($query);
	if($county !== 'all'){
		$stmt->bindParam(':county', $county, PDO::PARAM_STR);
	}
	if($residenceType !== 'all'){
		$stmt->bindParam(':objectType', $residenceType, PDO::PARAM_STR);
	}
	$stmt->bindParam(':address', $search, PDO::PARAM_STR);
	$stmt->bindParam(':minRooms', $minRooms, PDO::PARAM_INT);
	$stmt->bindParam(':maxRooms', $maxRooms, PDO::PARAM_INT);
	$stmt->bindParam(':minPrice', $minPrice, PDO::PARAM_INT);
	$stmt->bindParam(':maxPrice', $maxPrice, PDO::PARAM_INT);
	$stmt->bindParam(':minArea', $minArea, PDO::PARAM_INT);
	$stmt->bindParam(':maxArea', $maxArea, PDO::PARAM_INT);
	$stmt->bindParam(':minRent', $minRent, PDO::PARAM_INT);
	$stmt->bindParam(':maxRent', $maxRent, PDO::PARAM_INT);

	$stmt->execute();
} catch(PDOException $e) {
	echo "Query Error: " . $e;
}

while($line = $stmt->fetch(PDO::FETCH_ASSOC)){
	$county = $line['county'];
	$type = $line['objectType'];
	$address = $line['address'];
	$area = $line['area'];
	$room = $line['room'];
	$price = $line['price'];
	$rent = $line['rent'];

	echo '<tr>';
	echo "<td>$county</td>";
	echo "<td>$type</td>";
	echo "<td>$address</td>";
	echo "<td>$area</td>";
	echo "<td>$room</td>";
	echo "<td>$price</td>";
	echo "<td>$rent</td>";
	echo '</tr>';


}
?>