<?php 

	require_once 'DbConnect.php';
	
	$response = array();
	
	if(isset($_GET['apicall'])){
		
		switch($_GET['apicall']){
			
			case 'info':
				if(isTheseParametersAvailable(array('time1','time2')))
				{
					$time1 = $_POST['time1']; 
					$time2 = $_POST['time2']; 
					
					
					$stmt = $conn->prepare("SELECT * FROM grapes WHERE time between ? and ?");
					$stmt->bind_param("ss", $time1, $time2);
					$stmt->execute();
					$stmt->store_result();
					
					if($stmt->num_rows > 0)
					{
						$response['error'] = false; 
						$response['message'] = 'Found!'; 
						$response['count'] = $stmt->num_rows; 
						
						$stmt->close();
						
					}
					else
					{
						$response['error'] = true;
						$response['message'] = 'Not Found!';
						$stmt->close();
					}
					
				}
				else
				{
					$response['error'] = true; 
					$response['message'] = 'required parameters are not available'; 
				}
				
			break; 
			
			case 'undodelete':
				if(isTheseParametersAvailable(array('id','name','type','producer','q')))
				{
					$id = $_POST['id']; 
					$name = $_POST['name']; 
					$type = $_POST['type']; 
					$producer = $_POST['producer']; 
					$q = $_POST['q']; 
					$time = 0;
					
					$stmt = $conn->prepare("SELECT * FROM grapes WHERE id = ?");
					$stmt->bind_param("s", $id);
					$stmt->execute();
					$stmt->store_result();
					
					if($stmt->num_rows > 0)
					{
						$response['error'] = true;
						$response['message'] = $name.' is already in database!';
						$stmt->close();
					}
					else
					{
						$stmt = $conn->prepare("INSERT INTO grapes (id, name, type, producer, q) VALUES (?, ?, ?, ?, ?)");
						$stmt->bind_param("sssss", $id, $name, $type, $producer, $q);

						if($stmt->execute())
						{
							$stmt = $conn->prepare("SELECT * FROM grapes WHERE id = ?"); 
							$stmt->bind_param("s",$id);
							$stmt->execute();
							$stmt->bind_result($id, $name, $type, $producer, $q, $time);
							$stmt->fetch();
							
							$grape = array(
								'id'=>$id, 
								'name'=>$name, 
								'type'=>$type,
								'producer'=>$producer, 
								'q'=>$q,
								'time'=>$time
							);
							
							$stmt->close();
							
							$response['error'] = false; 
							$response['message'] = $name.' undo deleted successfully'; 
							$response['grape'] = $grape; 
						}
					}
					
				}else{
					$response['error'] = true; 
					$response['message'] = 'required parameters are not available'; 
				}
				
			break; 
			
			
			case 'delete':
				if(isTheseParametersAvailable(array('id')))
				{
					$id = $_POST['id']; 

					$stmt = $conn->prepare("SELECT * FROM grapes WHERE id = ?");
					$stmt->bind_param("s", $id);
					$stmt->execute();
					$stmt->store_result();
					
					if($stmt->num_rows > 0)
					{
						$stmt = $conn->prepare("DELETE FROM grapes WHERE id = ?");
						$stmt->bind_param("s", $id);

						if($stmt->execute())
						{
							$stmt = $conn->prepare("SELECT * FROM grapes WHERE id = ?"); 
							$stmt->bind_param("s",$id);
							$stmt->execute();
							$stmt->store_result();
							
							if($stmt->num_rows > 0)
							{
								$response['error'] = true;
								$response['message'] = $id.' IS NOT DELETED!';
							}
							else
							{
								$response['error'] = false;
								$response['message'] = $id.' Deleted!';
							}
							
							$stmt->close();
						}
					}
					else
					{
						$response['error'] = true;
						$response['message'] = $id.' is NOT in database!';
						$stmt->close();
					}
					
				}
				else
				{
					$response['error'] = true; 
					$response['message'] = 'required parameters are not available'; 
				}
				
			break; 
			
			
			case 'edit':
				if(isTheseParametersAvailable(array('id','name','type', 'producer','q')))
				{
					$id = $_POST['id']; 
					$name = $_POST['name']; 
					$type = $_POST['type']; 
					$producer = $_POST['producer']; 
					$q = $_POST['q'];
					$time = 0;
					
					$stmt = $conn->prepare("SELECT * FROM grapes WHERE id = ?");
					$stmt->bind_param("s", $id);
					$stmt->execute();
					$stmt->store_result();
					
					if($stmt->num_rows > 0)
					{
						$stmt = $conn->prepare("UPDATE grapes SET name=?, type=?, producer=?, q=? WHERE id = ?");
						$stmt->bind_param("sssss", $name, $type, $producer, $q, $id);

						if($stmt->execute())
						{
							$stmt = $conn->prepare("SELECT * FROM grapes WHERE id = ?"); 
							$stmt->bind_param("s",$id);
							$stmt->execute();
							$stmt->bind_result($id, $name, $type, $producer, $q, $time);
							$stmt->fetch();
							
							$grape = array(
								'id'=>$id, 
								'name'=>$name, 
								'type'=>$type,
								'producer'=>$producer, 
								'q'=>$q,
								'time'=>$time
							);
							
							$stmt->close();
							
							$response['error'] = false; 
							$response['message'] = $name.' edited successfully'; 
							$response['grape'] = $grape; 
						}
					}
					else
					{
						$response['error'] = true;
						$response['message'] = $name.' is NOT in database!';
						$stmt->close();
					}
					
				}
				else
				{
					$response['error'] = true; 
					$response['message'] = 'required parameters are not available'; 
				}
				
			break; 
			
			
			case 'create':
				if(isTheseParametersAvailable(array('name','type','producer','q'))){
					$name = $_POST['name']; 
					$type = $_POST['type']; 
					$producer = $_POST['producer']; 
					$q = $_POST['q'];
					$time = 0;
					
					$stmt = $conn->prepare("SELECT * FROM grapes WHERE name = ? AND type = ? AND producer = ? AND q = ?");
					$stmt->bind_param("ssss", $name, $type, $producer, $q);
					$stmt->execute();
					$stmt->store_result();
					
					if($stmt->num_rows > 0)
					{
						$response['error'] = true;
						$response['message'] = $name.' is already in database!';
						$stmt->close();
					}else{
						$stmt = $conn->prepare("INSERT INTO grapes (name, type, producer, q) VALUES (?, ?, ?, ?)");
						$stmt->bind_param("ssss", $name, $type, $producer, $q);

						if($stmt->execute())
						{
							$stmt = $conn->prepare("SELECT * FROM grapes WHERE name = ? AND type = ? AND producer = ? AND q = ?"); 
							$stmt->bind_param("ssss", $name, $type, $producer, $q);
							$stmt->execute();
							$stmt->bind_result($id, $name, $type, $producer, $q, $time);
							$stmt->fetch();
							
							$grape = array(
								'id'=>$id, 
								'name'=>$name, 
								'type'=>$type,
								'producer'=>$producer, 
								'q'=>$q,
								'time'=>$time
							);
							
							$stmt->close();
							
							$response['error'] = false; 
							$response['message'] = $name.' created successfully'; 
							$response['grape'] = $grape; 
						}
					}
					
				}else{
					$response['error'] = true; 
					$response['message'] = 'required parameters are not available'; 
				}
				
			break; 
			
			default: 
					$response['error'] = true; 
					$response['message'] = 'Invalid Operation Called';
			}
		}
	else
	{
		$response['error'] = true; 
		$response['message'] = 'Invalid API Call';
	}
	
	echo json_encode($response);
	
	function isTheseParametersAvailable($params){
		
		foreach($params as $param){
			if(!isset($_POST[$param])){
				return false; 
			}
		}
		return true; 
	}