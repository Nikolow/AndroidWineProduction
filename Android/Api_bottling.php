<?php 

	require_once 'DbConnect.php';
	
	$response = array();
	
	if(isset($_GET['apicall'])){
		
		switch($_GET['apicall']){
			
			case 'undodelete':
				if(isTheseParametersAvailable(array('id','name','wineid','bottleid')))
				{
					$id = $_POST['id']; 
					$name = $_POST['name']; 
					$wineid = $_POST['wineid']; 
					$bottleid = $_POST['bottleid']; 
					$time = 0;
					
					$stmt = $conn->prepare("SELECT * FROM bottling WHERE id = ?");
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
						$stmt = $conn->prepare("INSERT INTO bottling (id, name, wineid, bottleid) VALUES (?, ?, ?, ?)");
						$stmt->bind_param("ssss", $id, $name, $wineid, $bottleid);

						if($stmt->execute())
						{
							$stmt = $conn->prepare("SELECT * FROM bottling WHERE id = ?"); 
							$stmt->bind_param("s",$id);
							$stmt->execute();
							$stmt->bind_result($id, $name, $wineid, $bottleid, $time);
							$stmt->fetch();
							
							$bottling = array(
								'id'=>$id, 
								'name'=>$name, 
								'wineid'=>$wineid,
								'bottleid'=>$bottleid,
								'time'=>$time
							);
							
							$stmt->close();
							
							$response['error'] = false; 
							$response['message'] = $name.' undo deleted successfully'; 
							$response['bottling'] = $bottling; 
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

					$stmt = $conn->prepare("SELECT * FROM bottling WHERE id = ?");
					$stmt->bind_param("s", $id);
					$stmt->execute();
					$stmt->store_result();
					
					if($stmt->num_rows > 0)
					{
						$stmt = $conn->prepare("DELETE FROM bottling WHERE id = ?");
						$stmt->bind_param("s", $id);

						if($stmt->execute())
						{
							$stmt = $conn->prepare("SELECT * FROM bottling WHERE id = ?"); 
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
				if(isTheseParametersAvailable(array('id','name','wineid','bottleid')))
				{
					$id = $_POST['id']; 
					$name = $_POST['name']; 
					$wineid = $_POST['wineid']; 
					$bottleid = $_POST['bottleid'];
					$time = 0;
					
					$stmt = $conn->prepare("SELECT * FROM bottling WHERE id = ?");
					$stmt->bind_param("s", $id);
					$stmt->execute();
					$stmt->store_result();
					
					if($stmt->num_rows > 0)
					{
						$stmt = $conn->prepare("UPDATE bottling SET name=?, wineid=?, bottleid=? WHERE id = ?");
						$stmt->bind_param("ssss", $name, $wineid, $bottleid, $id);

						if($stmt->execute())
						{
							$stmt = $conn->prepare("SELECT * FROM bottling WHERE id = ?"); 
							$stmt->bind_param("s",$id);
							$stmt->execute();
							$stmt->bind_result($id, $name, $wineid, $bottleid, $time);
							$stmt->fetch();
							
							$bottling = array(
								'id'=>$id, 
								'name'=>$name, 
								'wineid'=>$wineid,
								'bottleid'=>$bottleid,
								'time'=>$time
							);
							
							$stmt->close();
							
							$response['error'] = false; 
							$response['message'] = $name.' edited successfully'; 
							$response['bottling'] = $bottling; 
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
				if(isTheseParametersAvailable(array('name','wineid','bottleid'))){
					$name = $_POST['name']; 
					$wineid = $_POST['wineid']; 
					$bottleid = $_POST['bottleid'];
					$time = 0;
					
					$stmt = $conn->prepare("SELECT * FROM bottling WHERE name = ? AND wineid = ? AND bottleid = ?");
					$stmt->bind_param("sss", $name, $wineid, $bottleid);
					$stmt->execute();
					$stmt->store_result();
					
					if($stmt->num_rows > 0)
					{
						$response['error'] = true;
						$response['message'] = $name.' is already in database!';
						$stmt->close();
					}else{
						$stmt = $conn->prepare("INSERT INTO bottling (name, wineid, bottleid) VALUES (?, ?, ?)");
						$stmt->bind_param("sss", $name, $wineid, $bottleid);

						if($stmt->execute())
						{
							$stmt = $conn->prepare("SELECT * FROM bottling WHERE name = ? AND wineid = ? AND bottleid = ?"); 
							$stmt->bind_param("sss", $name, $wineid, $bottleid);
							$stmt->execute();
							$stmt->bind_result($id, $name, $wineid, $bottleid, $time);
							$stmt->fetch();
							
							$bottling = array(
								'id'=>$id, 
								'name'=>$name, 
								'wineid'=>$wineid,
								'bottleid'=>$bottleid,
								'time'=>$time
							);
							
							$stmt->close();
							
							$response['error'] = false; 
							$response['message'] = $name.' created successfully'; 
							$response['bottling'] = $bottling; 
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