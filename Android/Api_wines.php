<?php 

	require_once 'DbConnect.php';
	
	$response = array();
	
	if(isset($_GET['apicall'])){
		
		switch($_GET['apicall']){
			
			case 'undodelete':
				if(isTheseParametersAvailable(array('id','name','grape','q')))
				{
					$id = $_POST['id']; 
					$name = $_POST['name']; 
					$grape = $_POST['grape']; 
					$q = $_POST['q']; 
					$time = 0;
					
					$stmt = $conn->prepare("SELECT * FROM wines WHERE id = ?");
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
						$stmt = $conn->prepare("INSERT INTO wines (id, name, grape, q) VALUES (?, ?, ?, ?)");
						$stmt->bind_param("ssss", $id, $name, $grape, $q);

						if($stmt->execute())
						{
							$stmt = $conn->prepare("SELECT * FROM wines WHERE id = ?"); 
							$stmt->bind_param("s",$id);
							$stmt->execute();
							$stmt->bind_result($id, $name, $grape, $q, $time);
							$stmt->fetch();
							
							$grape = array(
								'id'=>$id, 
								'name'=>$name, 
								'grape'=>$grape,
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

					$stmt = $conn->prepare("SELECT * FROM wines WHERE id = ?");
					$stmt->bind_param("s", $id);
					$stmt->execute();
					$stmt->store_result();
					
					if($stmt->num_rows > 0)
					{
						$stmt = $conn->prepare("DELETE FROM wines WHERE id = ?");
						$stmt->bind_param("s", $id);

						if($stmt->execute())
						{
							$stmt = $conn->prepare("SELECT * FROM wines WHERE id = ?"); 
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
				if(isTheseParametersAvailable(array('id','name','grape','q')))
				{
					$id = $_POST['id']; 
					$name = $_POST['name']; 
					$grape = $_POST['grape']; 
					$q = $_POST['q'];
					$time = 0;
					
					$stmt = $conn->prepare("SELECT * FROM wines WHERE id = ?");
					$stmt->bind_param("s", $id);
					$stmt->execute();
					$stmt->store_result();
					
					if($stmt->num_rows > 0)
					{
						$stmt = $conn->prepare("UPDATE wines SET name=?, grape=?, q=? WHERE id = ?");
						$stmt->bind_param("ssss", $name, $grape, $q, $id);

						if($stmt->execute())
						{
							$stmt = $conn->prepare("SELECT * FROM wines WHERE id = ?"); 
							$stmt->bind_param("s",$id);
							$stmt->execute();
							$stmt->bind_result($id, $name, $grape, $q, $time);
							$stmt->fetch();
							
							$grape = array(
								'id'=>$id, 
								'name'=>$name, 
								'grape'=>$grape,
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
				if(isTheseParametersAvailable(array('name','grape','q'))){
					$name = $_POST['name']; 
					$grape = $_POST['grape'];
					$q = $_POST['q'];
					$time = 0;
					
					$stmt = $conn->prepare("SELECT * FROM wines WHERE name = ? AND grape = ? AND q = ?");
					$stmt->bind_param("sss", $name, $grape, $q);
					$stmt->execute();
					$stmt->store_result();
					
					if($stmt->num_rows > 0)
					{
						$response['error'] = true;
						$response['message'] = $name.' is already in database!';
						$stmt->close();
					}else{
						$stmt = $conn->prepare("INSERT INTO wines (name, grape, q) VALUES (?, ?, ?)");
						$stmt->bind_param("sss", $name, $grape, $q);

						if($stmt->execute())
						{
							$stmt = $conn->prepare("SELECT * FROM wines WHERE name = ? AND grape = ? AND q = ?"); 
							$stmt->bind_param("sss", $name, $grape, $q);
							$stmt->execute();
							$stmt->bind_result($id, $name, $grape, $q, $time);
							$stmt->fetch();
							
							$grape = array(
								'id'=>$id, 
								'name'=>$name, 
								'grape'=>$grape,
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