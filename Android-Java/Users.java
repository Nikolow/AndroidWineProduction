package android;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class Users
 */
@WebServlet("/Users")
public class Users extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Users() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try 
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection
            		(
                    "jdbc:mysql://localhost:3306/test", "root", ""
                    );
            
            String query = "SELECT * FROM users";
        	try 
    		{
        		ResultSet resultSet = null;
        		PreparedStatement preparedStatement = null;
        		
        		preparedStatement = con.prepareStatement(query);
        		resultSet = preparedStatement.executeQuery();

        		JSONArray jArray = new JSONArray();
        		while(resultSet.next()) 
        		{
        			String id =resultSet.getString(1);
        		    String name =resultSet.getString(2);
        		    String mail =resultSet.getString(3);
        		    String pass =resultSet.getString(4);
        		    String acc =resultSet.getString(5);
        		    
        		    JSONObject jObj = new JSONObject();
        		    jObj.put("id", id);
        		    jObj.put("username", name);
        		    jObj.put("password", pass);
        		    jObj.put("email", mail);
        		    jObj.put("access", acc);
        		    jArray.put(jObj);
        		}
        		
        		con.close();  
        		out.print(jArray.toString());
        	}
    		catch (SQLException e) 
			{
				e.printStackTrace();
			}
        }
        catch (Exception e2) 
        {
            System.out.println(e2);
        }
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		//doGet(request, response);
		
		response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        String param1 = request.getParameter("apicall");
        
        
        switch(param1)
        {
        	case "signup":
        	{
        		String[] arrData = {"username", "password", "email", "access"};
        		if(isTheseParametersAvailable(request, arrData))
        		{
        			String username = request.getParameter("username");
        	        String password = request.getParameter("password");
        	        String email = request.getParameter("email");
        	        String access = request.getParameter("access");
        	        
        	        
        	        try 
        	        {
        	            Class.forName("com.mysql.jdbc.Driver");
        	            Connection con = DriverManager.getConnection
        	            		(
        	                    "jdbc:mysql://localhost:3306/test", "root", ""
        	                    );
        	            
        	            
        	            
        	            ResultSet r = null;
	            		PreparedStatement p = null;
	            		
	            		p = con.prepareStatement("SELECT COUNT(*) AS rowcount FROM users WHERE username = ? OR email = ?");
	            		p.setString(1, username);
	            		p.setString(2, email);
	            		r = p.executeQuery();
        	            
        	            //Statement s = con.createStatement();
        	            //ResultSet r = s.executeQuery("SELECT COUNT(*) AS rowcount FROM users WHERE username = '"+username+"' OR email = '"+email+"'");
        	            
        	            
        	            r.next();
        	            int count = r.getInt("rowcount");
        	            r.close();
        	            
        	            
        	            String id = null, name = null, mail = null, pass = null, acc = null, message = null;
        	            boolean err=true;
        	            
        	            if(count == 0) // ако няма такъв запис
        	            {
	        	            PreparedStatement ps = con.prepareStatement("INSERT INTO users (username, email, password, access) VALUES (?, ?, ?, ?)");
	        	 
	        	            ps.setString(1, username);
	        	            ps.setString(2, email);
	        	            ps.setString(3, password);
	        	            ps.setString(4, access);
	        	            
	        	            
	        	 
	        	            int i = ps.executeUpdate();
	        	            if (i > 0)
	        	            {
	        	            	String query = "SELECT * FROM users WHERE username = ?";
	        	            	try 
	    	            		{
	        	            		ResultSet resultSet = null;
	        	            		PreparedStatement preparedStatement = null;
	        	            		
	        	            		preparedStatement = con.prepareStatement(query);
	        	            		preparedStatement.setString(1, username);
	        	            		resultSet = preparedStatement.executeQuery();
	
	
	        	            		while(resultSet.next()) 
	        	            		{
	        	            			id = resultSet.getString(1);
	        	            			name = resultSet.getString(2);
	        	            			mail = resultSet.getString(3);
	        	            			pass = resultSet.getString(4);
	        	            			acc = resultSet.getString(5);
	        	            		}
	        	            		
	        	            		err = false;
	        	            		message = name+" was successfuly added!";
	        	            	}
	    	            		catch (SQLException e) 
		            			{
	    	            			message = "Select Failed";
		            				e.printStackTrace();
		            			}
	        	            }
        	            }
        	            else // ако има
        	            {
        	            	message = "Already in the db";
        	            }
        	            
        	            
        	            // answer
        	            JSONObject jo = new JSONObject();  
        	    		jo.put("error",err);
        	    		jo.put("message", message);
        	    		
        	    		JSONObject user = new JSONObject();  
        	    		user.put("id",id);
        	    		user.put("username",name);
        	    		user.put("password",pass);
        	    		user.put("email",mail);
        	    		user.put("access",acc);
        	    		
        	    		jo.put("user", user);

        	    		con.close();  
        	    		out.print(jo.toString());
        	        } 
        	        catch (Exception e2) 
        	        {
        	            System.out.println(e2);
        	        }
        		}
        		break;
        	}
        	
        	
        	case "login":
        	{
        		String[] arrData = {"username", "password"};
        		if(isTheseParametersAvailable(request, arrData))
        		{
        			String username = request.getParameter("username");
        	        String password = request.getParameter("password"); 

        	        try 
        	        {
        	            Class.forName("com.mysql.jdbc.Driver");
        	            Connection con = DriverManager.getConnection
        	            		(
        	                    "jdbc:mysql://localhost:3306/test", "root", ""
        	                    );
        	            
        	            
        	            
        	            
        	            String id = null, name = null, mail = null, pass = null, acc = null, message = null;
        	            boolean err=true;
        	            
        	            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
    	            	try 
	            		{
    	            		ResultSet resultSet = null;
    	            		PreparedStatement preparedStatement = null;
    	            		
    	            		preparedStatement = con.prepareStatement(query);
    	            		preparedStatement.setString(1, username);
    	            		preparedStatement.setString(2, password);
    	            		resultSet = preparedStatement.executeQuery();

    	            		int br=0;

    	            		while(resultSet.next()) 
    	            		{
    	            			id = resultSet.getString(1);
    	            			name = resultSet.getString(2);
    	            			mail = resultSet.getString(3);
    	            			pass = resultSet.getString(4);
    	            			acc = resultSet.getString(5);
    	            			br++;
    	            		}
    	            		
    	            		if(br>0)
    	            		{
	    	            		err = false;
	    	            		message = name+" Logged in!";
    	            		}
    	            		else
    	            		{
    	            			err = true;
        	            		message = username+" not in DataBase";
    	            		}
	            		}
	            		catch (SQLException e) 
            			{
	            			message = "Select Failed";
            				e.printStackTrace();
            			}
        	            
        	            // answer
        	            JSONObject jo = new JSONObject();  
        	    		jo.put("error",err);
        	    		jo.put("message", message);
        	    		
        	    		JSONObject user = new JSONObject();  
        	    		user.put("id",id);
        	    		user.put("username",name);
        	    		user.put("password",pass);
        	    		user.put("email",mail);
        	    		user.put("access",acc);
        	    		
        	    		jo.put("user", user);

        	    		con.close();  
        	    		out.print(jo.toString());
        	        } 
        	        catch (Exception e2) 
        	        {
        	            System.out.println(e2);
        	        }
        		}
        		break;
        	}
        	
        	
        	
        	
        	
        	case "edit":
        	{
        		String[] arrData = {"id", "username", "password", "email", "access"};
        		if(isTheseParametersAvailable(request, arrData))
        		{
        			String user_id = request.getParameter("id");
        			String username = request.getParameter("username");
        	        String password = request.getParameter("password");
        	        String email = request.getParameter("email");
        	        String access = request.getParameter("access");
        	        
        	        
        	        try 
        	        {
        	            Class.forName("com.mysql.jdbc.Driver");
        	            Connection con = DriverManager.getConnection
        	            		(
        	                    "jdbc:mysql://localhost:3306/test", "root", ""
        	                    );
        	            
        	            
        	            
        	            
        	            String id = null, name = null, mail = null, pass = null, acc = null, message = null;
        	            boolean err=true;
        	            
        	            String query = "SELECT * FROM users WHERE id = ?";
    	            	try 
	            		{
    	            		ResultSet resultSet = null;
    	            		PreparedStatement preparedStatement = null;
    	            		
    	            		preparedStatement = con.prepareStatement(query);
    	            		preparedStatement.setString(1, user_id);
    	            		resultSet = preparedStatement.executeQuery();

    	            		int br=0;

    	            		while(resultSet.next()) 
    	            		{
    	            			id = resultSet.getString(1);
    	            			name = resultSet.getString(2);
    	            			mail = resultSet.getString(3);
    	            			pass = resultSet.getString(4);
    	            			acc = resultSet.getString(5);
    	            			br++;
    	            		}
    	            		
    	            		if(br>0)
    	            		{
    	            			if(pass.contentEquals("AD87109BFFF0765F4DD8CF4943B04D16A4070FEA")) //update without pass !
    	            			{
    	            				
    	            				PreparedStatement ps = con.prepareStatement("UPDATE users SET username = ?, email = ?, access = ? WHERE id = ?");
    	           	        	 
    		        	            ps.setString(1, username);
    		        	            ps.setString(2, email);
    		        	            ps.setString(3, access);
    		        	            ps.setString(4, user_id);
    		        	            
    		        	 
    		        	            int i = ps.executeUpdate();
    		        	            if (i > 0)
    		        	            {
    		        	            	String query2 = "SELECT * FROM users WHERE id = ?";
    		        	            	try 
    		    	            		{
    		        	            		ResultSet resultSet2 = null;
    		        	            		PreparedStatement preparedStatement2 = null;
    		        	            		
    		        	            		preparedStatement2 = con.prepareStatement(query2);
    		        	            		preparedStatement2.setString(1, user_id);
    		        	            		resultSet2 = preparedStatement2.executeQuery();
    		
    		
    		        	            		while(resultSet2.next()) 
    		        	            		{
    		        	            			id = resultSet2.getString(1);
    		        	            			name = resultSet2.getString(2);
    		        	            			mail = resultSet2.getString(3);
    		        	            			pass = resultSet2.getString(4);
    		        	            			acc = resultSet2.getString(5);
    		        	            		}
    		        	            		
    		        	            		err = false;
    		        	            		message = username+" was successfuly edited (without pass)!";
    		        	            	}
    		    	            		catch (SQLException e) 
    			            			{
    		    	            			message = "Select Failed";
    			            				e.printStackTrace();
    			            			}
    		        	            }
    	            			}
    	            			else //update all
    	            			{
    	            				
    	            				PreparedStatement ps = con.prepareStatement("UPDATE users SET username = ?, email = ?, password = ?, access = ? WHERE id = ?");
       	           	        	 
    		        	            ps.setString(1, username);
    		        	            ps.setString(2, email);
    		        	            ps.setString(3, password);
    		        	            ps.setString(4, access);
    		        	            ps.setString(5, user_id);
    		        	            
    		        	 
    		        	            int i = ps.executeUpdate();
    		        	            if (i > 0)
    		        	            {
    		        	            	String query2 = "SELECT * FROM users WHERE id = ?";
    		        	            	try 
    		    	            		{
    		        	            		ResultSet resultSet2 = null;
    		        	            		PreparedStatement preparedStatement2 = null;
    		        	            		
    		        	            		preparedStatement2 = con.prepareStatement(query2);
    		        	            		preparedStatement2.setString(1, user_id);
    		        	            		resultSet2 = preparedStatement2.executeQuery();
    		
    		
    		        	            		while(resultSet2.next()) 
    		        	            		{
    		        	            			id = resultSet2.getString(1);
    		        	            			name = resultSet2.getString(2);
    		        	            			mail = resultSet2.getString(3);
    		        	            			pass = resultSet2.getString(4);
    		        	            			acc = resultSet2.getString(5);
    		        	            		}
    		        	            		
    		        	            		err = false;
    		        	            		message = username+" was successfuly edited (with new pass)!";
    		        	            	}
    		    	            		catch (SQLException e) 
    			            			{
    		    	            			message = "Select Failed";
    			            				e.printStackTrace();
    			            			}
    		        	            }
    	            			}
    	            		}
    	            		else
    	            		{
    	            			err = true;
        	            		message = username+" not in DataBase";
    	            		}
	            		}
	            		catch (SQLException e) 
            			{
	            			message = "Select Failed";
            				e.printStackTrace();
            			}
        	            
        	            // answer
        	            JSONObject jo = new JSONObject();  
        	    		jo.put("error",err);
        	    		jo.put("message", message);
        	    		
        	    		JSONObject user = new JSONObject();  
        	    		user.put("id",id);
        	    		user.put("username",name);
        	    		user.put("password",pass);
        	    		user.put("email",mail);
        	    		user.put("access",acc);
        	    		
        	    		jo.put("user", user);

        	    		con.close();  
        	    		out.print(jo.toString());
        	        } 
        	        catch (Exception e2) 
        	        {
        	            System.out.println(e2);
        	        }
        		}
        		break;
        	}
        	
        	
        	
        	
        	case "delete":
        	{
        		String[] arrData = {"id"};
        		if(isTheseParametersAvailable(request, arrData))
        		{
        			String user_id = request.getParameter("id");
        	        
        	        try 
        	        {
        	            Class.forName("com.mysql.jdbc.Driver");
        	            Connection con = DriverManager.getConnection
        	            		(
        	                    "jdbc:mysql://localhost:3306/test", "root", ""
        	                    );
        	            
        	            
        	            
        	            
        	            String /*id = null, */name = null/*, mail = null, pass = null, acc = null*/, message = null;
        	            boolean err=true;
        	            
        	            String query = "SELECT * FROM users WHERE id = ?";
    	            	try 
	            		{
    	            		ResultSet resultSet = null;
    	            		PreparedStatement preparedStatement = null;
    	            		
    	            		preparedStatement = con.prepareStatement(query);
    	            		preparedStatement.setString(1, user_id);
    	            		resultSet = preparedStatement.executeQuery();

    	            		int br=0;

    	            		while(resultSet.next()) 
    	            		{
    	            			//id = resultSet.getString(1);
    	            			name = resultSet.getString(2);
    	            			//mail = resultSet.getString(3);
    	            			//pass = resultSet.getString(4);
    	            			//acc = resultSet.getString(5);
    	            			br++;
    	            		}
    	            		
    	            		if(br>0)
    	            		{
    	            			PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE id = ?");
   	           	        	 
		        	            ps.setString(1, user_id);
		        	            
		        	 
		        	            int i = ps.executeUpdate();
		        	            if (i > 0)
		        	            {
		        	            	String query2 = "SELECT * FROM users WHERE id = ?";
		        	            	try 
		    	            		{
		        	            		ResultSet resultSet2 = null;
		        	            		PreparedStatement preparedStatement2 = null;
		        	            		
		        	            		preparedStatement2 = con.prepareStatement(query2);
		        	            		preparedStatement2.setString(1, user_id);
		        	            		resultSet2 = preparedStatement2.executeQuery();
		
		        	            		int br2=0;
		
		        	            		while(resultSet2.next()) 
		        	            		{
		        	            			br2++;
		        	            		}
		        	            		
		        	            		if(br2>0)
		        	            		{
		        	            			err = true;
			        	            		message = name+" is still in DB!";
		        	            		}
		        	            		else
		        	            		{
		        	            			err = false;
		        	            			message = name+" was successfuly DELETED!";
		        	            		}
		        	            	}
		    	            		catch (SQLException e) 
			            			{
		    	            			message = "Select Failed";
			            				e.printStackTrace();
			            			}
		        	            }
    	            		}
    	            		else
    	            		{
    	            			err = true;
        	            		message = user_id+" not in DataBase";
    	            		}
	            		}
	            		catch (SQLException e) 
            			{
	            			message = "Select Failed";
            				e.printStackTrace();
            			}
        	            
        	            // answer
        	            JSONObject jo = new JSONObject();  
        	    		jo.put("error",err);
        	    		jo.put("message", message);
        	    		
        	    		/*JSONObject user = new JSONObject();  
        	    		user.put("id",id);
        	    		user.put("username",name);
        	    		user.put("password",pass);
        	    		user.put("email",mail);
        	    		user.put("access",acc);
        	    		
        	    		jo.put("user", user);*/

        	    		con.close();  
        	    		out.print(jo.toString());
        	        } 
        	        catch (Exception e2) 
        	        {
        	            System.out.println(e2);
        	        }
        		}
        		break;
        	}
        	
        	
        	
        	case "undodelete":
        	{
        		String[] arrData = {"id", "username", "password", "email", "access"};
        		if(isTheseParametersAvailable(request, arrData))
        		{
        			String user_id = request.getParameter("id");
        			String username = request.getParameter("username");
        	        String password = request.getParameter("password");
        	        String email = request.getParameter("email");
        	        String access = request.getParameter("access");
        	        
        	        
        	        try 
        	        {
        	            Class.forName("com.mysql.jdbc.Driver");
        	            Connection con = DriverManager.getConnection
        	            		(
        	                    "jdbc:mysql://localhost:3306/test", "root", ""
        	                    );
        	            
        	            
        	            
        	            
        	            String id = null, name = null, mail = null, pass = null, acc = null, message = null;
        	            boolean err=true;
        	            
        	            String query = "SELECT * FROM users WHERE username = ? OR email = ?";
    	            	try 
	            		{
    	            		ResultSet resultSet = null;
    	            		PreparedStatement preparedStatement = null;
    	            		
    	            		preparedStatement = con.prepareStatement(query);
    	            		preparedStatement.setString(1, username);
    	            		preparedStatement.setString(2, email);
    	            		
    	            		resultSet = preparedStatement.executeQuery();

    	            		int br=0;

    	            		while(resultSet.next()) 
    	            		{
    	            			id = resultSet.getString(1);
    	            			name = resultSet.getString(2);
    	            			mail = resultSet.getString(3);
    	            			pass = resultSet.getString(4);
    	            			acc = resultSet.getString(5);
    	            			br++;
    	            		}
    	            		
    	            		if(br>0)
    	            		{
    	            			message = "Already in DB!";
	            			}
	            			else
	            			{
	            				
	            				PreparedStatement ps = con.prepareStatement("INSERT INTO users (id, username, email, password, access) VALUES (?, ?, ?, ?, ?)");
   	           	        	 
	            				ps.setString(1, user_id);
		        	            ps.setString(2, username);
		        	            ps.setString(3, email);
		        	            ps.setString(4, password);
		        	            ps.setString(5, access);
		        	            
		        	 
		        	            int i = ps.executeUpdate();
		        	            if (i > 0)
		        	            {
		        	            	String query2 = "SELECT * FROM users WHERE id = ?";
		        	            	try 
		    	            		{
		        	            		ResultSet resultSet2 = null;
		        	            		PreparedStatement preparedStatement2 = null;
		        	            		
		        	            		preparedStatement2 = con.prepareStatement(query2);
		        	            		
		        	            		preparedStatement2.setString(1, user_id);
		        	            		
		        	            		resultSet2 = preparedStatement2.executeQuery();
		
		        	            		while(resultSet2.next()) 
		        	            		{
		        	            			id = resultSet2.getString(1);
		        	            			name = resultSet2.getString(2);
		        	            			mail = resultSet2.getString(3);
		        	            			pass = resultSet2.getString(4);
		        	            			acc = resultSet2.getString(5);
		        	            		}
		        	            		
		        	            		
		        	            		err = false;
		        	            		message = username+" was successfuly UNDO-Deleted!";
		        	            	}
		    	            		catch (SQLException e) 
			            			{
		    	            			message = "Select Failed";
			            				e.printStackTrace();
			            			}
		        	            }
	            			}
	            		}
	            		catch (SQLException e) 
            			{
	            			message = "Select Failed";
            				e.printStackTrace();
            			}
        	            
        	            // answer
        	            JSONObject jo = new JSONObject();  
        	    		jo.put("error",err);
        	    		jo.put("message", message);
        	    		
        	    		JSONObject user = new JSONObject();  
        	    		user.put("id",id);
        	    		user.put("username",name);
        	    		user.put("password",pass);
        	    		user.put("email",mail);
        	    		user.put("access",acc);
        	    		
        	    		jo.put("user", user);

        	    		con.close();  
        	    		out.print(jo.toString());
        	        } 
        	        catch (Exception e2) 
        	        {
        	            System.out.println(e2);
        	        }
        		}
        		break;
        	}
        	
        	
        	default:
        	{
	            JSONObject jo = new JSONObject();  
	    		try 
	    		{
					jo.put("error",true);
					jo.put("message", "Unexpected URL Parameter");
		    		out.print(jo.toString());
				} 
	    		catch (JSONException e) 
	    		{
					e.printStackTrace();
				}
	    		
        	}
        }
 
        out.close();
		
	}
	
	boolean isTheseParametersAvailable(HttpServletRequest req, String[] arrData)
	{
		for (String strTemp : arrData)
		{
			if (! req.getParameterMap().containsKey(strTemp))
			{
				return false;
			}
		}
		
		return true;
	}

}
