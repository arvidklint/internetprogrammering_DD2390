<tr>
	<th>County</th>
	<th>Type</th>
	<th>Address</th>
	<th>Area</th>
	<th>Rooms</th>
	<th>Price [SEK]</th>
	<th>Rent [SEK]</th>
</tr>

<?php
$host = 'localhost';
$user = 'root';
$pw = 'root';
$dbname = 'Lab4';

$search = $_GET['search'];
$county = $_GET['county'];

$db = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $user, $pw);
$query = "SELECT * FROM residence WHERE county = '$county' AND address LIKE '%$search%' ORDER BY price ASC;";

try {
	$stmt = $db->prepare($query);
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