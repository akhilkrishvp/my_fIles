package leavemanagement.spectrum.net.in;

import org.json.JSONObject;

public class Approve_Reject_Delete_jsonparse 
{
	static boolean  status;
	   static String[] result;
	 
	  
	     static int parselenARD;
	    public static boolean parseARD(String data) throws Exception
	     {
		   status=false;
		  
		  try
		  {
			  JSONObject cobj=new JSONObject(data);
			  status=cobj.getBoolean("status");
			  System.out.println("status="+status);
			 
			  result= new String[parselenARD];
			  
			  System.out.println("parselen====="+parselenARD);
			  
			  for (int i = 0; i<parselenARD;i++)
			  {
				  result[i]=cobj.getString("Result").toString();
	              
			  }
		  }
		  catch(Exception e)
	      {

	      }
	      return status;    

	  }
	   public static int parselength()
	     {
	      return parselenARD;
	      
	    }
	   public static String[] getresult()
	    {
		   return result;
	 }
}
