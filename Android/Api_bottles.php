<?php 

	require_once 'DbConnect.php';
	
	$response = array();
	
	if(isset($_GET['apicall'])){
		
		switch($_GET['apicall']){
			
			case 'undodelete':
				if(isTheseParametersAvailable(array('id','name','type','ml')))
				{
					$id = $_POST['id']; 
					$name = $_POST['name']; 
					$type = $_POST['type']; 
					$ml = $_POST['ml']; 
					$time = 0;
					
					$stmt = $conn->prepare("SELECT * FROM bottles WHERE id = ?");
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
						$stmt = $conn->prepare("INSERT INTO bottles (id, name, type, ml) VALUES (?, ?, ?, ?)");
						$stmt->bind_param("ssss", $id, $name, $type, $ml);

						if($stmt->execute())
						{
							$stmt = $conn->prepare("SELECT * FROM bottles WHERE id = ?"); 
							$stmt->bind_param("s",$id);
							$stmt->execute();
							$stmt->bind_result($id, $name, $type, $ml, $time);
							$stmt->fetch();
							
							$bottle = array(
								'id'=>$id, 
								'name'=>$name, 
								'type'=>$type,
								'ml'=>$ml,
								'time'=>$time
							);
							
							$stmt->close();
							
							$response['error'] = false; 
							$response['message'] = $name.' undo deleted successfully'; 
							$response['bottle'] = $bottle; 
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

					$stmt = $conn->prepare("SELECT * FROM bottles WHERE id = ?");
					$stmt->bind_param("s", $id);
					$stmt->execute();
					$stmt->store_result();
					
					if($stmt->num_rows > 0)
					{
						$stmt = $conn->prepare("DELETE FROM bottles WHERE id = ?");
						$stmt->bind_param("s", $id);

						if($stmt->execute())
						{
							$stmt = $conn->prepare("SELECT * FROM bottles WHERE id = ?"); 
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
				if(isTheseParametersAvailable(array('id','name','type','ml')))
				{
					$id = $_POST['id']; 
					$name = $_POST['name']; 
					$type = $_POST['type']; 
					$ml = $_POST['ml'];
					$time = 0;
					
					$stmt = $conn->prepare("SELECT * FROM bottles WHERE id = ?");
					$stmt->bind_param("s", $id);
					$stmt->execute();
					$stmt->store_result();
					
					if($stmt->num_rows > 0)
					{
						$stmt = $conn->prepare("UPDATE bottles SET name=?, type=?, ml=? WHERE id = ?");
						$stmt->bind_param("ssss", $name, $type, $ml, $id);

						if($stmt->execute())
						{
							$stmt = $conn->prepare("SELECT * FROM bottles WHERE id = ?"); 
							$stmt->bind_param("s",$id);
							$stmt->execute();
							$stmt->bind_result($id, $name, $type, $ml, $time);
							$stmt->fetch();
							
							$bottle = array(
								'id'=>$id, 
								'name'=>$name, 
								'type'=>$type,
								'ml'=>$ml,
								'time'=>$time
							);
							
							$stmt->close();
							
							$response['error'] = false; 
							$response['message'] = $name.' edited successfully'; 
							$response['bottle'] = $bottle; 
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
				if(isTheseParametersAvailable(array('name','type','ml'))){
					$name = $_POST['name']; 
					$type = $_POST['type']; 
					$ml = $_POST['ml'];
					$time = 0;
					
					$stmt = $conn->prepare("SELECT * FROM bottles WHERE name = ? AND type = ? AND ml = ?");
					$stmt->bind_param("sss", $name, $type, $ml);
					$stmt->execute();
					$stmt->store_result();
					
					if($stmt->num_rows > 0)
					{
						$response['error'] = true;
						$response['message'] = $name.' is already in database!';
						$stmt->close();
					}else{
						$stmt = $conn->prepare("INSERT INTO bottles (name, type, ml) VALUES (?, ?, ?)");
						$stmt->bind_param("sss", $name, $type, $ml);

						if($stmt->execute())
						{
							$stmt = $conn->prepare("SELECT * FROM bottles WHERE name = ? AND type = ? AND ml = ?"); 
							$stmt->bind_param("sss", $name, $type, $ml);
							$stmt->execute();
							$stmt->bind_result($id, $name, $type, $ml, $time);
							$stmt->fetch();
							
							$bottle = array(
								'id'=>$id, 
								'name'=>$name, 
								'type'=>$type,
								'ml'=>$ml,
								'time'=>$time
							);
							
							$stmt->close();
							
							$response['error'] = false; 
							$response['message'] = $name.' created successfully'; 
							$response['bottle'] = $bottle; 
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