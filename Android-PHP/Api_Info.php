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
					
					
					$stmt = $conn->prepare("SELECT * FROM grapes WHERE time between ? and ? AND type=0");
					$stmt->bind_param("ss", $time1, $time2);
					$stmt->execute();
					$stmt->store_result();
					$grapes_0 = $stmt->num_rows;
					$stmt->close();
					
					
					$stmt = $conn->prepare("SELECT * FROM grapes WHERE time between ? and ? AND type=1");
					$stmt->bind_param("ss", $time1, $time2);
					$stmt->execute();
					$stmt->store_result();
					$grapes_1 = $stmt->num_rows;
					$stmt->close();
					
					$stmt = $conn->prepare("SELECT * FROM grapes WHERE time between ? and ?");
					$stmt->bind_param("ss", $time1, $time2);
					$stmt->execute();
					$stmt->store_result();
					$all_grapes = $stmt->num_rows;
					$stmt->close();
					
					
					
					
					
					$stmt = $conn->prepare("SELECT * FROM bottles WHERE time between ? and ?");
					$stmt->bind_param("ss", $time1, $time2);
					$stmt->execute();
					$stmt->store_result();
					$all_bottles = $stmt->num_rows;
					$stmt->close();
					
					$stmt = $conn->prepare("SELECT * FROM bottles WHERE time between ? and ? AND type=0");
					$stmt->bind_param("ss", $time1, $time2);
					$stmt->execute();
					$stmt->store_result();
					$bottles_0 = $stmt->num_rows;
					$stmt->close();
					
					$stmt = $conn->prepare("SELECT * FROM bottles WHERE time between ? and ? AND type=0 AND ml=750");
					$stmt->bind_param("ss", $time1, $time2);
					$stmt->execute();
					$stmt->store_result();
					$bottles_0_750 = $stmt->num_rows;
					$stmt->close();

					$stmt = $conn->prepare("SELECT * FROM bottles WHERE time between ? and ? AND type=0 AND ml=375");
					$stmt->bind_param("ss", $time1, $time2);
					$stmt->execute();
					$stmt->store_result();
					$bottles_0_375 = $stmt->num_rows;
					$stmt->close();
					
					$stmt = $conn->prepare("SELECT * FROM bottles WHERE time between ? and ? AND type=0 AND ml=200");
					$stmt->bind_param("ss", $time1, $time2);
					$stmt->execute();
					$stmt->store_result();
					$bottles_0_200 = $stmt->num_rows;
					$stmt->close();
					
					$stmt = $conn->prepare("SELECT * FROM bottles WHERE time between ? and ? AND type=0 AND ml=187");
					$stmt->bind_param("ss", $time1, $time2);
					$stmt->execute();
					$stmt->store_result();
					$bottles_0_187 = $stmt->num_rows;
					$stmt->close();
					
					$stmt = $conn->prepare("SELECT * FROM bottles WHERE time between ? and ? AND type=1 AND ml=750");
					$stmt->bind_param("ss", $time1, $time2);
					$stmt->execute();
					$stmt->store_result();
					$bottles_1_750 = $stmt->num_rows;
					$stmt->close();

					$stmt = $conn->prepare("SELECT * FROM bottles WHERE time between ? and ? AND type=1 AND ml=375");
					$stmt->bind_param("ss", $time1, $time2);
					$stmt->execute();
					$stmt->store_result();
					$bottles_1_375 = $stmt->num_rows;
					$stmt->close();
					
					$stmt = $conn->prepare("SELECT * FROM bottles WHERE time between ? and ? AND type=1 AND ml=200");
					$stmt->bind_param("ss", $time1, $time2);
					$stmt->execute();
					$stmt->store_result();
					$bottles_1_200 = $stmt->num_rows;
					$stmt->close();
					
					$stmt = $conn->prepare("SELECT * FROM bottles WHERE time between ? and ? AND type=1 AND ml=187");
					$stmt->bind_param("ss", $time1, $time2);
					$stmt->execute();
					$stmt->store_result();
					$bottles_1_187 = $stmt->num_rows;
					$stmt->close();
					
					
					$stmt = $conn->prepare("SELECT * FROM bottles WHERE time between ? and ? AND type=1");
					$stmt->bind_param("ss", $time1, $time2);
					$stmt->execute();
					$stmt->store_result();
					$bottles_1 = $stmt->num_rows;
					$stmt->close();
					
					
					
					
					
					
					
					$stmt = $conn->prepare("SELECT * FROM bottling WHERE time between ? and ?");
					$stmt->bind_param("ss", $time1, $time2);
					$stmt->execute();
					$stmt->store_result();
					$bottling = $stmt->num_rows;
					$stmt->close();
					
					
					
					$response['error'] = false; 
					
					
					$grapes = 
					array(
							'all'=>$all_grapes, 
							'type0'=>$grapes_0, 
							'type1'=>$grapes_1
						);
					
					$response['grapes'] = $grapes;
					
					
					
					
					$bottles = 
					array(
							'all'=>$all_bottles, 
							'type0'=>$bottles_0, 
							'type1'=>$bottles_1,
							'1_750'=>$bottles_1_750,
							'1_375'=>$bottles_1_375,
							'1_200'=>$bottles_1_200,
							'1_187'=>$bottles_1_187,
							'0_750'=>$bottles_0_750,
							'0_375'=>$bottles_0_375,
							'0_200'=>$bottles_0_200,
							'0_187'=>$bottles_0_187
						);
					
					$response['bottles'] = $bottles;
					
					
					
					
					$response['bottling'] = $bottling; 
				}
				else
				{
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