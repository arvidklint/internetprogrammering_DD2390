<?php

if ($_COOKIE['searchValues']) {
	$values = unserialize($_COOKIE['searchValues']);
} else {
	$values = 0;
}

echo json_encode($values);

?>