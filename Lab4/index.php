<?php
echo 'Hello World';

$host = 'localhost';
$user = 'root';
$pw = 'root';
$db = 'Lab4';

$link = mysql_connect($host, $user, $pw);

mysql_select_db($db) or die(mysql_error());

if( !$link ){
	echo 'Connection Failed';
}

$query = 'SELECT * FROM residence;';

if( ($result = mysql_query($query, $link)) === false ){
	echo 'Query Failed: ' . mysql_error();
	exit();
}

while( $line = mysql_fetch_assoc($result)){
	$county = $line['county'];
	$
	$
	$
	$
	echo $county;
}
?>