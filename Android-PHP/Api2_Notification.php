<?php 

	require_once 'DbConnect.php';
	
	$stmt = $conn->prepare("SELECT * FROM grapes WHERE type=0");
	$stmt->execute();
	$stmt->store_result();
	$grapes_0 = $stmt->num_rows;
	$stmt->close();
	
	
	$stmt = $conn->prepare("SELECT * FROM grapes WHERE type=1");
	$stmt->execute();
	$stmt->store_result();
	$grapes_1 = $stmt->num_rows;
	$stmt->close();
	
	
	



	$stmt = $conn->prepare("SELECT * FROM bottles WHERE type=0");
	$stmt->execute();
	$stmt->store_result();
	$bottles_0 = $stmt->num_rows;
	$stmt->close();
	
	$stmt = $conn->prepare("SELECT * FROM bottles WHERE type=1");
	$stmt->execute();
	$stmt->store_result();
	$bottles_1 = $stmt->num_rows;
	$stmt->close();
	
	
	
	
	
	$grapes = 
	array(
			'type0'=>$grapes_0, 
			'type1'=>$grapes_1
		);
	
	$response['grapes'] = $grapes;
	
	
	
	
	$bottles = 
	array(
			'type0'=>$bottles_0, 
			'type1'=>$bottles_1
		);
	
	$response['bottles'] = $bottles;

	
	//displaying the result in json format 
	echo json_encode($response);