<?php 

	require_once 'DbConnect.php';
	
	//creating a query
	$stmt = $conn->prepare("SELECT * FROM bottles;");
	
	//executing the query 
	$stmt->execute();
	
	//binding results to the query 
	$stmt->bind_result($id, $name, $type, $ml, $time);
	
	$bottles = array(); 
	
	//traversing through all the result 
	while($stmt->fetch())
	{
		$temp = array();
		$temp['id'] = $id; 
		$temp['name'] = $name; 
		$temp['type'] = $type; 
		$temp['ml'] = $ml; 
		$temp['time'] = $time; 
		array_push($bottles, $temp);
	}
	
	//displaying the result in json format 
	echo json_encode($bottles);