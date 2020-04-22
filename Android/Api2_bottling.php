<?php 

	require_once 'DbConnect.php';
	
	//creating a query
	$stmt = $conn->prepare("SELECT * FROM bottling;");
	
	//executing the query 
	$stmt->execute();
	
	//binding results to the query 
	$stmt->bind_result($id, $name, $wineid, $bottleid, $time);
	
	$bottling = array(); 
	
	//traversing through all the result 
	while($stmt->fetch())
	{
		$temp = array();
		$temp['id'] = $id; 
		$temp['name'] = $name; 
		$temp['wineid'] = $wineid; 
		$temp['bottleid'] = $bottleid; 
		$temp['time'] = $time; 
		array_push($bottling, $temp);
	}
	
	//displaying the result in json format 
	echo json_encode($bottling);