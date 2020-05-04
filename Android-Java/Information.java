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

import org.json.JSONException;
//import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class Information
 */
@WebServlet("/Information")
public class Information extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Information() {
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
            
            String query =  "SELECT * FROM grapes WHERE type = 0";
            String query2 = "SELECT * FROM grapes WHERE type = 1";
            String query3 = "SELECT * FROM bottles WHERE type = 1";
            String query4 = "SELECT * FROM bottles WHERE type = 1";
        	try 
    		{
        		ResultSet resultSet = null;
        		PreparedStatement preparedStatement = null;
        		
        		preparedStatement = con.prepareStatement(query);
        		resultSet = preparedStatement.executeQuery();

        		int br_grapes_0 = 0;
        		while(resultSet.next()) 
        		{
        			br_grapes_0++;
        		}
        		
        		
        		
        		resultSet = null;
        		preparedStatement = null;
        		
        		preparedStatement = con.prepareStatement(query2);
        		resultSet = preparedStatement.executeQuery();

        		int br_grapes_1 = 0;
        		while(resultSet.next()) 
        		{
        			br_grapes_1++;
        		}
        		
        		
        		
        		
        		
        		
        		
        		
        		resultSet = null;
        		preparedStatement = null;
        		
        		preparedStatement = con.prepareStatement(query3);
        		resultSet = preparedStatement.executeQuery();

        		int br_bottles_0 = 0;
        		while(resultSet.next()) 
        		{
        			br_bottles_0++;
        		}
        		
        		
        		
        		resultSet = null;
        		preparedStatement = null;
        		
        		preparedStatement = con.prepareStatement(query4);
        		resultSet = preparedStatement.executeQuery();

        		int br_bottles_1 = 0;
        		while(resultSet.next()) 
        		{
        			br_bottles_1++;
        		}
        		
        		
        		
        		
        		// answer
	            JSONObject jo = new JSONObject();  
	    		
	    		JSONObject grapes = new JSONObject();  
	    		grapes.put("type0",br_grapes_0);
	    		grapes.put("type1",br_grapes_1);
	    		
	    		jo.put("grapes", grapes);
	    		
	    		
	    		
	    		JSONObject bottles = new JSONObject();  
	    		bottles.put("type0",br_bottles_0);
	    		bottles.put("type1",br_bottles_1);
	    		
	    		jo.put("bottles", bottles);

	    		con.close();  
	    		out.print(jo.toString());
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
        	case "info":
        	{
        		String[] arrData = {"time1", "time2"};
        		if(isTheseParametersAvailable(request, arrData))
        		{
        			String time1 = request.getParameter("time1");
        	        String time2 = request.getParameter("time2");
        	        
        	        
        	        try 
        	        {
        	            Class.forName("com.mysql.jdbc.Driver");
        	            Connection con = DriverManager.getConnection
        	            		(
        	                    "jdbc:mysql://localhost:3306/test", "root", ""
        	                    );
        	            
        	            String query =  "SELECT * FROM grapes WHERE time between ? and ? AND type = 0";
        	            String query2 = "SELECT * FROM grapes WHERE time between ? and ? AND type = 1";
        	            String query3 = "SELECT * FROM grapes WHERE time between ? and ?";
        	            
        	            String query4 = "SELECT * FROM bottles WHERE time between ? and ?";
        	            String query5 = "SELECT * FROM bottles WHERE time between ? and ? AND type=0";
        	            String query6 = "SELECT * FROM bottles WHERE time between ? and ? AND type=0 AND ml=750";
        	            String query7 = "SELECT * FROM bottles WHERE time between ? and ? AND type=0 AND ml=375";
        	            String query8 = "SELECT * FROM bottles WHERE time between ? and ? AND type=0 AND ml=200";
        	            String query9 = "SELECT * FROM bottles WHERE time between ? and ? AND type=0 AND ml=187";
        	            String query10 = "SELECT * FROM bottles WHERE time between ? and ? AND type=1 AND ml=750";
        	            String query11 = "SELECT * FROM bottles WHERE time between ? and ? AND type=1 AND ml=375";
        	            String query12 = "SELECT * FROM bottles WHERE time between ? and ? AND type=1 AND ml=200";
        	            String query13 = "SELECT * FROM bottles WHERE time between ? and ? AND type=1 AND ml=187";
        	            String query14 = "SELECT * FROM bottles WHERE time between ? and ? AND type=1";
        	            String query15 = "SELECT * FROM bottling WHERE time between ? and ?";
        	        	try 
        	    		{
        	        		ResultSet resultSet = null;
        	        		PreparedStatement preparedStatement = null;
        	        		
        	        		preparedStatement = con.prepareStatement(query);
        	        		preparedStatement.setString(1, time1);
        	        		preparedStatement.setString(2, time2);
        	        		resultSet = preparedStatement.executeQuery();

        	        		int grapes_0 = 0;
        	        		while(resultSet.next()) 
        	        		{
        	        			grapes_0++;
        	        		}
        	        		
        	        		
        	        		resultSet = null;
        	        		preparedStatement = null;
        	        		
        	        		preparedStatement = con.prepareStatement(query2);
        	        		preparedStatement.setString(1, time1);
        	        		preparedStatement.setString(2, time2);
        	        		resultSet = preparedStatement.executeQuery();

        	        		int grapes_1 = 0;
        	        		while(resultSet.next()) 
        	        		{
        	        			grapes_1++;
        	        		}
        	        		
        	        		
        	        		resultSet = null;
        	        		preparedStatement = null;
        	        		
        	        		preparedStatement = con.prepareStatement(query3);
        	        		preparedStatement.setString(1, time1);
        	        		preparedStatement.setString(2, time2);
        	        		resultSet = preparedStatement.executeQuery();

        	        		int all_grapes = 0;
        	        		while(resultSet.next()) 
        	        		{
        	        			all_grapes++;
        	        		}
        	        		
        	        		
        	        		
        	        		
        	        		
        	        		
        	        		resultSet = null;
        	        		preparedStatement = null;
        	        		
        	        		preparedStatement = con.prepareStatement(query4);
        	        		preparedStatement.setString(1, time1);
        	        		preparedStatement.setString(2, time2);
        	        		resultSet = preparedStatement.executeQuery();

        	        		int all_bottles = 0;
        	        		while(resultSet.next()) 
        	        		{
        	        			all_bottles++;
        	        		}
        	        		
        	        		
        	        		resultSet = null;
        	        		preparedStatement = null;
        	        		
        	        		preparedStatement = con.prepareStatement(query5);
        	        		preparedStatement.setString(1, time1);
        	        		preparedStatement.setString(2, time2);
        	        		resultSet = preparedStatement.executeQuery();

        	        		int bottles_0 = 0;
        	        		while(resultSet.next()) 
        	        		{
        	        			bottles_0++;
        	        		}
        	        		
        	        		
        	        		resultSet = null;
        	        		preparedStatement = null;
        	        		
        	        		preparedStatement = con.prepareStatement(query6);
        	        		preparedStatement.setString(1, time1);
        	        		preparedStatement.setString(2, time2);
        	        		resultSet = preparedStatement.executeQuery();

        	        		int bottles_0_750 = 0;
        	        		while(resultSet.next()) 
        	        		{
        	        			bottles_0_750++;
        	        		}
        	        		

        	        		resultSet = null;
        	        		preparedStatement = null;
        	        		
        	        		preparedStatement = con.prepareStatement(query7);
        	        		preparedStatement.setString(1, time1);
        	        		preparedStatement.setString(2, time2);
        	        		resultSet = preparedStatement.executeQuery();

        	        		int bottles_0_375 = 0;
        	        		while(resultSet.next()) 
        	        		{
        	        			bottles_0_375++;
        	        		}
        	        		
        	        		
        	        		resultSet = null;
        	        		preparedStatement = null;
        	        		
        	        		preparedStatement = con.prepareStatement(query8);
        	        		preparedStatement.setString(1, time1);
        	        		preparedStatement.setString(2, time2);
        	        		resultSet = preparedStatement.executeQuery();

        	        		int bottles_0_200 = 0;
        	        		while(resultSet.next()) 
        	        		{
        	        			bottles_0_200++;
        	        		}
        	        		

        	        		resultSet = null;
        	        		preparedStatement = null;
        	        		
        	        		preparedStatement = con.prepareStatement(query9);
        	        		preparedStatement.setString(1, time1);
        	        		preparedStatement.setString(2, time2);
        	        		resultSet = preparedStatement.executeQuery();

        	        		int bottles_0_187 = 0;
        	        		while(resultSet.next()) 
        	        		{
        	        			bottles_0_187++;
        	        		}
        	        		
        	        		
        	        		resultSet = null;
        	        		preparedStatement = null;
        	        		
        	        		preparedStatement = con.prepareStatement(query10);
        	        		preparedStatement.setString(1, time1);
        	        		preparedStatement.setString(2, time2);
        	        		resultSet = preparedStatement.executeQuery();

        	        		int bottles_1_750 = 0;
        	        		while(resultSet.next()) 
        	        		{
        	        			bottles_1_750++;
        	        		}
        	        		
        	        		
        	        		resultSet = null;
        	        		preparedStatement = null;
        	        		
        	        		preparedStatement = con.prepareStatement(query11);
        	        		preparedStatement.setString(1, time1);
        	        		preparedStatement.setString(2, time2);
        	        		resultSet = preparedStatement.executeQuery();

        	        		int bottles_1_375 = 0;
        	        		while(resultSet.next()) 
        	        		{
        	        			bottles_1_375++;
        	        		}
        	        		
        	        		
        	        		resultSet = null;
        	        		preparedStatement = null;
        	        		
        	        		preparedStatement = con.prepareStatement(query12);
        	        		preparedStatement.setString(1, time1);
        	        		preparedStatement.setString(2, time2);
        	        		resultSet = preparedStatement.executeQuery();

        	        		int bottles_1_200 = 0;
        	        		while(resultSet.next()) 
        	        		{
        	        			bottles_1_200++;
        	        		}
        	        		
        	        		
        	        		resultSet = null;
        	        		preparedStatement = null;
        	        		
        	        		preparedStatement = con.prepareStatement(query13);
        	        		preparedStatement.setString(1, time1);
        	        		preparedStatement.setString(2, time2);
        	        		resultSet = preparedStatement.executeQuery();

        	        		int bottles_1_187 = 0;
        	        		while(resultSet.next()) 
        	        		{
        	        			bottles_1_187++;
        	        		}
        	        		
        	        		
        	        		resultSet = null;
        	        		preparedStatement = null;
        	        		
        	        		preparedStatement = con.prepareStatement(query14);
        	        		preparedStatement.setString(1, time1);
        	        		preparedStatement.setString(2, time2);
        	        		resultSet = preparedStatement.executeQuery();

        	        		int bottles_1 = 0;
        	        		while(resultSet.next()) 
        	        		{
        	        			bottles_1++;
        	        		}
        	        		
        	        		
        	        		
        	        		
        	        		
        	        		
        	        		resultSet = null;
        	        		preparedStatement = null;
        	        		
        	        		preparedStatement = con.prepareStatement(query15);
        	        		preparedStatement.setString(1, time1);
        	        		preparedStatement.setString(2, time2);
        	        		resultSet = preparedStatement.executeQuery();

        	        		int bottling = 0;
        	        		while(resultSet.next()) 
        	        		{
        	        			bottling++;
        	        		}
        	        		
        	        		
        	        		
        	        		
        	        		
        	        		
        	        		
        	        		// answer
        		            JSONObject jo = new JSONObject();  
        		            jo.put("error",false);
        		    		
        		    		JSONObject grapes = new JSONObject(); 
        		    		grapes.put("all", all_grapes);
        		    		grapes.put("type0",grapes_0);
        		    		grapes.put("type1",grapes_1);
        		    		
        		    		jo.put("grapes", grapes);
        		    		
        		    		
        		    		
        		    		JSONObject bottles = new JSONObject();  
        		    		bottles.put("all",all_bottles);
        		    		bottles.put("type0",bottles_0);
        		    		bottles.put("type1",bottles_1);
        		    		
        		    		bottles.put("1_750",bottles_1_750);
        		    		bottles.put("1_375",bottles_1_375);
        		    		bottles.put("1_200",bottles_1_200);
        		    		bottles.put("1_187",bottles_1_187);
        		    		bottles.put("0_750",bottles_0_750);
        		    		bottles.put("0_375",bottles_0_375);
        		    		bottles.put("0_200",bottles_0_200);
        		    		bottles.put("0_187",bottles_0_187);
        		    		
        		    		jo.put("bottles", bottles);
        		    		
        		    		jo.put("bottling",bottling);

        		    		con.close();  
        		    		out.print(jo.toString());
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
