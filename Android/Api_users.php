<?php 

	require_once 'DbConnect.php';
	
	$response = array();
	
	if(isset($_GET['apicall'])){
		
		switch($_GET['apicall']){
			
			case 'undodelete':
				if(isTheseParametersAvailable(array('id','username','email','password','access'))){
					$id = $_POST['id']; 
					$username = $_POST['username']; 
					$email = $_POST['email']; 
					$password = $_POST['password'];
					$access = $_POST['access']; 
					
					$stmt = $conn->prepare("SELECT * FROM users WHERE username = ? OR email = ?");
					$stmt->bind_param("ss", $username, $email);
					$stmt->execute();
					$stmt->store_result();
					
					if($stmt->num_rows > 0)
					{
						$response['error'] = true;
						$response['message'] = $username.' is already in database!';
						$stmt->close();
					}
					else
					{
						$stmt = $conn->prepare("INSERT INTO users (id, username, email, password, access) VALUES (?, ?, ?, ?, ?)");
						$stmt->bind_param("sssss", $id, $username, $email, $password, $access);

						if($stmt->execute())
						{
							$stmt = $conn->prepare("SELECT * FROM users WHERE id = ?"); 
							$stmt->bind_param("s",$id);
							$stmt->execute();
							$stmt->bind_result($id, $username, $email, $password, $access);
							$stmt->fetch();
							
							$user = array(
								'id'=>$id, 
								'username'=>$username, 
								'email'=>$email,
								'password'=>$password,
								'access'=>$access
							);
							
							$stmt->close();
							
							$response['error'] = false; 
							$response['message'] = $username.' undo deleted successfully - '.$access; 
							$response['user'] = $user; 
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

					$stmt = $conn->prepare("SELECT * FROM users WHERE id = ?");
					$stmt->bind_param("s", $id);
					$stmt->execute();
					$stmt->store_result();
					
					if($stmt->num_rows > 0)
					{
						$stmt = $conn->prepare("DELETE FROM users WHERE id = ?");
						$stmt->bind_param("s", $id);

						if($stmt->execute())
						{
							$stmt = $conn->prepare("SELECT * FROM users WHERE id = ?"); 
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
						$response['message'] = $username.' is NOT in database!';
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
				if(isTheseParametersAvailable(array('id','username','password','email','access')))
				{
					$id = $_POST['id']; 
					$username = $_POST['username']; 
					$email = $_POST['email']; 
					$password = $_POST['password'];
					$access = $_POST['access']; 
					
					$stmt = $conn->prepare("SELECT * FROM users WHERE id = ?");
					$stmt->bind_param("s", $id);
					$stmt->execute();
					$stmt->store_result();
					
					if($stmt->num_rows > 0)
					{
						if($password == "AD87109BFFF0765F4DD8CF4943B04D16A4070FEA") //empty
						{
							$stmt = $conn->prepare("UPDATE users SET username=?, email=?, access=? WHERE id = ?");
							$stmt->bind_param("ssss", $username, $email, $access, $id);
						}
						else
						{
							$stmt = $conn->prepare("UPDATE users SET username=?, email=?, password=?, access=? WHERE id = ?");
							$stmt->bind_param("sssss", $username, $email, $password, $access, $id);
						}

						if($stmt->execute())
						{
							$stmt = $conn->prepare("SELECT * FROM users WHERE id = ?"); 
							$stmt->bind_param("s",$id);
							$stmt->execute();
							$stmt->bind_result($id, $username, $email, $password, $access);
							$stmt->fetch();
							
							$user = array(
								'id'=>$id, 
								'username'=>$username, 
								'password'=>$password,
								'email'=>$email,
								'access'=>$access
							);
							
							$stmt->close();
							
							$response['error'] = false; 
							$response['message'] = $username.' edited successfully - '.$access; 
							$response['user'] = $user; 
						}
					}
					else
					{
						$response['error'] = true;
						$response['message'] = $username.' is NOT in database!';
						$stmt->close();
					}
					
				}
				else
				{
					$response['error'] = true; 
					$response['message'] = 'required parameters are not available'; 
				}
				
			break; 
			
			
			case 'signup':
				if(isTheseParametersAvailable(array('username','password','email','access'))){
					$username = $_POST['username']; 
					$email = $_POST['email']; 
					$password = $_POST['password'];
					$access = $_POST['access']; 
					
					$stmt = $conn->prepare("SELECT * FROM users WHERE username = ? OR email = ?");
					$stmt->bind_param("ss", $username, $email);
					$stmt->execute();
					$stmt->store_result();
					
					if($stmt->num_rows > 0){
						$response['error'] = true;
						$response['message'] = $username.' is already in database!';
						$stmt->close();
					}else{
						$stmt = $conn->prepare("INSERT INTO users (username, email, password, access) VALUES (?, ?, ?, ?)");
						$stmt->bind_param("ssss", $username, $email, $password, $access);

						if($stmt->execute()){
							$stmt = $conn->prepare("SELECT * FROM users WHERE username = ?"); 
							$stmt->bind_param("s",$username);
							$stmt->execute();
							$stmt->bind_result($id, $username, $email, $password, $access);
							$stmt->fetch();
							
							$user = array(
								'id'=>$id, 
								'username'=>$username, 
								'password'=>$password,
								'email'=>$email,
								'access'=>$access
							);
							
							$stmt->close();
							
							$response['error'] = false; 
							$response['message'] = $username.' created successfully - '.$access; 
							$response['user'] = $user; 
						}
					}
					
				}else{
					$response['error'] = true; 
					$response['message'] = 'required parameters are not available'; 
				}
				
			break; 
			
			case 'login':
				
				if(isTheseParametersAvailable(array('username', 'password'))){
					
					$username = $_POST['username'];
					$password = $_POST['password'];
					
					//$email = "";
					//$access = "";
					//$id = "";
					
					$stmt = $conn->prepare("SELECT * FROM users WHERE username = ? AND password = ?");
					$stmt->bind_param("ss",$username, $password);
					
					$stmt->execute();
					
					$stmt->store_result();
					
					if($stmt->num_rows > 0){
						
						$stmt->bind_result($id, $username, $password, $email, $access);
						$stmt->fetch();
						
						$user = array(
							'id'=>$id, 
							'username'=>$username, 
							'password'=>$password,
							'email'=>$email,
							'access'=>$access
						);
						
						$response['error'] = false; 
						$response['message'] = 'Login successfull'; 
						$response['user'] = $user; 
					}
					else
					{
						$response['error'] = true; 
						$response['message'] = 'Invalid username or password';
					}
				}
			break; 
			
			default: 
				$response['error'] = true; 
				$response['message'] = 'Invalid Operation Called';
		}
		
	}else{
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