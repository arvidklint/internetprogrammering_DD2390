<?php

$guess = $_GET["guess"];
$requestedDocument = "index.html";

$PORT = 8081;
$ADDRESS = 'localhost';

if( !($socket = socket_create(AF_INET, SOCK_STREAM, getprotobyname('tcp'))) ){
	echo "Socket could not be created.\n";
}

if( !socket_connect($socket, $ADDRESS, $PORT) ){
	echo "Socket could not connect.\n";
}

$message = $guess ." " .$requestedDocument;
$messageLength = strlen($message);

$socketStatus = socket_sendto($socket, $message, $messageLength, MSG_EOF, $ADDRESS, $PORT);

if( $socketStatus !== FALSE ){
	$response = '';
	$nextResponse = '';
	while( $nextResponse = socket_read($socket, 4096) ){
		$response .= $nextResponse;
	}

	echo $response;
}else{
	echo "Socket could not send message to server.\n";
}

?>