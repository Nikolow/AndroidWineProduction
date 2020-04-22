<?php 

	require_once 'DbConnect.php';
	
	//creating a query
	$stmt = $conn->prepare("SELECT * FROM wines;");
	
	//executing the query 
	$stmt->execute();
	
	//binding results to the query 
	$stmt->bind_result($id, $name, $grape, $q, $time);
	
	$wines = array(); 
	
	//traversing through all the result 
	while($stmt->fetch())
	{
		$temp = array();
		$temp['id'] = $id; 
		$temp['name'] = $name; 
		$temp['grape'] = $grape; 
		$temp['q'] = $q; 
		$temp['time'] = $time; 
		array_push($wines, $temp);
	}
	
	//displaying the result in json format 
	echo json_encode($wines);