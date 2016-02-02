<?php



if ($_COOKIE['session_id'] == null) {
    session_start();
    $id = session_id();
    session_destroy();
} else {
	$id = $_COOKIE['session_id'];
}

$response = "";

if ($_GET['guess'] != null) {
	send($_GET["guess"], $id);
}
// $requestedDocument = "/index.html";



function send($guess, $id) {
	$PORT = 8081;
	$ADDRESS = 'localhost';
	global $response;

	if( !($socket = socket_create(AF_INET, SOCK_STREAM, getprotobyname('tcp'))) ){
		echo "Socket could not be created.\n";
	}

	if( !socket_connect($socket, $ADDRESS, $PORT) ){
		echo "Socket could not connect.\n";
	}

	$message = $id . " " . $guess ."\n";
	$messageLength = strlen($message);

	$socketStatus = socket_sendto($socket, $message, $messageLength, MSG_EOF, $ADDRESS, $PORT);

	if( $socketStatus !== FALSE ){
	// echo $response;
		$r = '';
		$nextResponse = '';
		while( $nextResponse = socket_read($socket, 4096) ){
			if(!$nextResponse == ""){
				$header .= $nextResponse;
		}

		$response = $r;
	}else{
		$response = "Socket could not send message to server.\n";
	}
}

?>

<!DOCTYPE html>
<html>
<head>
	<title>Guessing Game</title>
</head>
<body>
	<?php
		global $response;
		echo $response;
	?>
	<form>
		<input type="number" name="guess"/>
		<input type="submit" value="Guess"/>
	</form>
</body>
</html>