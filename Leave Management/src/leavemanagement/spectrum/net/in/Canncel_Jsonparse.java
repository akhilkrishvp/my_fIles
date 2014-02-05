package leavemanagement.spectrum.net.in;

import org.json.JSONObject;

public class Canncel_Jsonparse 
{
	  static boolean  status;
	   static String[] result;
	 
	  
	     static int parselen;
	    public static boolean parsecancelleave(String data) throws Exception
	     {
		   status=false;
		  
		  try
		  {
			  JSONObject cobj=new JSONObject(data);
			  status=cobj.getBoolean("status");
			  System.out.println("status="+status);
			 
			  result= new String[parselen];
			  
			  System.out.println("parselen====="+parselen);
			  
			  for (int i = 0; i<parselen;i++)
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
	      return parselen;
	      
	    }
	   public static String[] getresult()
	    {
		   return result;
	 }
	  
}
