package android;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class Wines
 */
@WebServlet("/Wines")
public class Wines extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Wines() {
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
            
            String query = "SELECT * FROM wines";
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
        		    String grape =resultSet.getString(3);
        		    String q =resultSet.getString(4);
        		    String time =resultSet.getString(5);
        		    
        		    JSONObject jObj = new JSONObject();
        		    jObj.put("id", id);
        		    jObj.put("name", name);
        		    jObj.put("grape", grape);
        		    jObj.put("q", q);
        		    jObj.put("time", time);
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
        	case "create":
        	{
        		String[] arrData = {"name", "grape", "q"};
        		if(isTheseParametersAvailable(request, arrData))
        		{
        			String name = request.getParameter("name");
        	        String grape = request.getParameter("grape");
        	        String q = request.getParameter("q");
        	        
        	        
        	        try 
        	        {
        	            Class.forName("com.mysql.jdbc.Driver");
        	            Connection con = DriverManager.getConnection
        	            		(
        	                    "jdbc:mysql://localhost:3306/test", "root", ""
        	                    );
        	            
        	            
        	            
        	            ResultSet r = null;
	            		PreparedStatement p = null;
	            		
	            		p = con.prepareStatement("SELECT COUNT(*) AS rowcount FROM wines WHERE name = ? AND grape = ? AND q = ?");
	            		p.setString(1, name);
	            		p.setString(2, grape);
	            		p.setString(3, q);
	            		r = p.executeQuery();
        	            
        	            r.next();
        	            int count = r.getInt("rowcount");
        	            r.close();
        	            
        	            
        	            String id = null, n = null, g = null, qu = null, time = null, message = null;
        	            boolean err=true;
        	            
        	            if(count == 0) // ако няма такъв запис
        	            {
	        	            PreparedStatement ps = con.prepareStatement("INSERT INTO wines (name, grape, q) VALUES (?, ?, ?)");
	        	 
	        	            ps.setString(1, name);
	        	            ps.setString(2, grape);
	        	            ps.setString(3, q);
	        	            
	        	            
	        	 
	        	            int i = ps.executeUpdate();
	        	            if (i > 0)
	        	            {
	        	            	String query = "SELECT * FROM wines WHERE name = ? AND grape = ? AND q = ?";
	        	            	try 
	    	            		{
	        	            		ResultSet resultSet = null;
	        	            		PreparedStatement preparedStatement = null;
	        	            		
	        	            		preparedStatement = con.prepareStatement(query);
	        	            		preparedStatement.setString(1, name);
	        	            		preparedStatement.setString(2, grape);
	        	            		preparedStatement.setString(3, q);
	        	            		resultSet = preparedStatement.executeQuery();
	
	
	        	            		while(resultSet.next()) 
	        	            		{
	        	            			id = resultSet.getString(1);
	        	            			n = resultSet.getString(2);
	        	            			g = resultSet.getString(3);
	        	            			qu = resultSet.getString(4);
	        	            			time = resultSet.getString(5);
	        	            		}
	        	            		
	        	            		err = false;
	        	            		message = n+" was successfuly added!";
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
        	    		
        	    		JSONObject wine = new JSONObject();  
        	    		wine.put("id",id);
        	    		wine.put("name",n);
        	    		wine.put("grape",g);
        	    		wine.put("q",qu);
        	    		wine.put("time",time);
        	    		
        	    		jo.put("wine", wine);

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
        		String[] arrData = {"id", "name", "grape", "q"};
        		if(isTheseParametersAvailable(request, arrData))
        		{
        			String _id = request.getParameter("id");
        			String name = request.getParameter("name");
        	        String grape = request.getParameter("grape");
        	        String q = request.getParameter("q");
        	        
        	        
        	        try 
        	        {
        	            Class.forName("com.mysql.jdbc.Driver");
        	            Connection con = DriverManager.getConnection
        	            		(
        	                    "jdbc:mysql://localhost:3306/test", "root", ""
        	                    );
        	            
        	            
        	            
        	            
        	            String id = null, n = null, g = null, qu = null, time = null, message = null;
        	            boolean err=true;
        	            
        	            String query = "SELECT * FROM wines WHERE id = ?";
    	            	try 
	            		{
    	            		ResultSet resultSet = null;
    	            		PreparedStatement preparedStatement = null;
    	            		
    	            		preparedStatement = con.prepareStatement(query);
    	            		preparedStatement.setString(1, _id);
    	            		resultSet = preparedStatement.executeQuery();

    	            		int br=0;

    	            		while(resultSet.next()) 
    	            		{
    	            			id = resultSet.getString(1);
    	            			n = resultSet.getString(2);
    	            			g = resultSet.getString(3);
    	            			qu = resultSet.getString(4);
    	            			time = resultSet.getString(5);
    	            			br++;
    	            		}
    	            		
    	            		if(br>0)
    	            		{
    	            				PreparedStatement ps = con.prepareStatement("UPDATE wines SET name = ?, grape = ?, q = ? WHERE id = ?");
       	           	        	 
    		        	            ps.setString(1, name);
    		        	            ps.setString(2, grape);
    		        	            ps.setString(3, q);
    		        	            ps.setString(4, _id);
    		        	            
    		        	 
    		        	            int i = ps.executeUpdate();
    		        	            if (i > 0)
    		        	            {
    		        	            	String query2 = "SELECT * FROM wines WHERE id = ?";
    		        	            	try 
    		    	            		{
    		        	            		ResultSet resultSet2 = null;
    		        	            		PreparedStatement preparedStatement2 = null;
    		        	            		
    		        	            		preparedStatement2 = con.prepareStatement(query2);
    		        	            		preparedStatement2.setString(1, _id);
    		        	            		resultSet2 = preparedStatement2.executeQuery();
    		
    		
    		        	            		while(resultSet2.next()) 
    		        	            		{
    		        	            			id = resultSet2.getString(1);
    		        	            			n = resultSet2.getString(2);
    		        	            			g = resultSet2.getString(3);
    		        	            			qu = resultSet2.getString(4);
    		        	            			time = resultSet2.getString(5);
    		        	            		}
    		        	            		
    		        	            		err = false;
    		        	            		message = n+" was successfuly edited!";
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
        	            		message = n+" not in DataBase";
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
        	    		
        	    		JSONObject wine = new JSONObject();  
        	    		wine.put("id",id);
        	    		wine.put("name",n);
        	    		wine.put("grape",g);
        	    		wine.put("q",qu);
        	    		wine.put("time",time);
        	    		
        	    		jo.put("wine", wine);

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
        			String _id = request.getParameter("id");
        	        
        	        try 
        	        {
        	            Class.forName("com.mysql.jdbc.Driver");
        	            Connection con = DriverManager.getConnection
        	            		(
        	                    "jdbc:mysql://localhost:3306/test", "root", ""
        	                    );
        	            
        	            
        	            
        	            
        	            String /*id = null, */name = null/*, mail = null, pass = null, acc = null*/, message = null;
        	            boolean err=true;
        	            
        	            String query = "SELECT * FROM wines WHERE id = ?";
    	            	try 
	            		{
    	            		ResultSet resultSet = null;
    	            		PreparedStatement preparedStatement = null;
    	            		
    	            		preparedStatement = con.prepareStatement(query);
    	            		preparedStatement.setString(1, _id);
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
    	            			PreparedStatement ps = con.prepareStatement("DELETE FROM wines WHERE id = ?");
   	           	        	 
		        	            ps.setString(1, _id);
		        	            
		        	 
		        	            int i = ps.executeUpdate();
		        	            if (i > 0)
		        	            {
		        	            	String query2 = "SELECT * FROM wines WHERE id = ?";
		        	            	try 
		    	            		{
		        	            		ResultSet resultSet2 = null;
		        	            		PreparedStatement preparedStatement2 = null;
		        	            		
		        	            		preparedStatement2 = con.prepareStatement(query2);
		        	            		preparedStatement2.setString(1, _id);
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
        	            		message = _id+" not in DataBase";
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
        		String[] arrData = {"id", "name", "grape", "q"};
        		if(isTheseParametersAvailable(request, arrData))
        		{
        			String _id = request.getParameter("id");
        			String name = request.getParameter("name");
        	        String grape = request.getParameter("grape");
        	        String q = request.getParameter("q");
        	        
        	        
        	        try 
        	        {
        	            Class.forName("com.mysql.jdbc.Driver");
        	            Connection con = DriverManager.getConnection
        	            		(
        	                    "jdbc:mysql://localhost:3306/test", "root", ""
        	                    );
        	            
        	            
        	            
        	            
        	            String id = null, n = null, g = null, qu = null, time = null, message = null;
        	            boolean err=true;
        	            
        	            String query = "SELECT * FROM wines WHERE id = ?";
    	            	try 
	            		{
    	            		ResultSet resultSet = null;
    	            		PreparedStatement preparedStatement = null;
    	            		
    	            		preparedStatement = con.prepareStatement(query);
    	            		preparedStatement.setString(1, _id);
    	            		
    	            		resultSet = preparedStatement.executeQuery();

    	            		int br=0;

    	            		while(resultSet.next()) 
    	            		{
    	            			id = resultSet.getString(1);
    	            			n = resultSet.getString(2);
    	            			g = resultSet.getString(3);
    	            			qu = resultSet.getString(4);
    	            			time = resultSet.getString(5);
    	            			br++;
    	            		}
    	            		
    	            		if(br>0)
    	            		{
    	            			message = "Already in DB!";
	            			}
	            			else
	            			{
	            				
	            				PreparedStatement ps = con.prepareStatement("INSERT INTO wines (id, name, grape, q) VALUES (?, ?, ?, ?)");
   	           	        	 
	            				ps.setString(1, _id);
		        	            ps.setString(2, name);
		        	            ps.setString(3, grape);
		        	            ps.setString(4, q);
		        	            
		        	 
		        	            int i = ps.executeUpdate();
		        	            if (i > 0)
		        	            {
		        	            	String query2 = "SELECT * FROM wines WHERE id = ?";
		        	            	try 
		    	            		{
		        	            		ResultSet resultSet2 = null;
		        	            		PreparedStatement preparedStatement2 = null;
		        	            		
		        	            		preparedStatement2 = con.prepareStatement(query2);
		        	            		
		        	            		preparedStatement2.setString(1, _id);
		        	            		
		        	            		resultSet2 = preparedStatement2.executeQuery();
		
		        	            		while(resultSet2.next()) 
		        	            		{
		        	            			id = resultSet2.getString(1);
		        	            			n = resultSet2.getString(2);
		        	            			g = resultSet2.getString(3);
		        	            			qu = resultSet2.getString(4);
		        	            			time = resultSet2.getString(5);
		        	            		}
		        	            		
		        	            		
		        	            		err = false;
		        	            		message = n+" was successfuly UNDO-Deleted!";
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
        	    		
        	    		JSONObject wine = new JSONObject();  
        	    		wine.put("id",id);
        	    		wine.put("name",n);
        	    		wine.put("grape",g);
        	    		wine.put("q",qu);
        	    		wine.put("time",time);
        	    		
        	    		jo.put("wine", wine);

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
