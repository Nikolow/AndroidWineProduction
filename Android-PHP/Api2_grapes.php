<?php 

	require_once 'DbConnect.php';
	
	//creating a query
	$stmt = $conn->prepare("SELECT * FROM grapes;");
	
	//executing the query 
	$stmt->execute();
	
	//binding results to the query 
	$stmt->bind_result($id, $name, $type, $producer, $q, $time);
	
	$grapes = array(); 
	
	//traversing through all the result 
	while($stmt->fetch())
	{
		$temp = array();
		$temp['id'] = $id; 
		$temp['name'] = $name; 
		$temp['type'] = $type; 
		$temp['producer'] = $producer; 
		$temp['q'] = $q; 
		$temp['time'] = $time; 
		array_push($grapes, $temp);
	}
	
	//displaying the result in json format 
	echo json_encode($grapes);