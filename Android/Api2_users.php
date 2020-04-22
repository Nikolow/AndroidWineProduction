<?php 

	require_once 'DbConnect.php';
	
	//creating a query
	$stmt = $conn->prepare("SELECT * FROM users;");
	
	//executing the query 
	$stmt->execute();
	
	//binding results to the query 
	$stmt->bind_result($id, $username, $password, $email, $access);
	
	$users = array(); 
	
	//traversing through all the result 
	while($stmt->fetch()){
		$temp = array();
		$temp['id'] = $id; 
		$temp['username'] = $username; 
		$temp['password'] = $password; 
		$temp['email'] = $email; 
		$temp['access'] = $access; 
		array_push($users, $temp);
	}
	
	//displaying the result in json format 
	echo json_encode($users);