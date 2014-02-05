package leavemanagement.spectrum.net.in;

import org.json.JSONArray;
import org.json.JSONObject;

public class Leave_Type_Json_Parse {
	static String[] Leave_Type;
	static int ParseLength;
	public static int Parse_LeaveType(String Datta1){
		try {
			System.out.println("the length is 123..............");
			JSONObject jobj1=new JSONObject(Datta1);
			JSONArray typearray=jobj1.getJSONArray("LEAVE TYPES");
			ParseLength=typearray.length();
			Leave_Type=new String[ParseLength];
			System.out.println("the length is "+ParseLength);
			for (int i = 0;i <ParseLength; i++) {
				Leave_Type[i]= typearray.getJSONArray(i).getString(0);
				System.out.println("the leaave type is ..........."+Leave_Type[i]);
				
			}
		} catch (Exception e) {
		}
		return ParseLength;
	}
	  public static String[] getLeave_type()
	    {
	        return    Leave_Type ;
	        
	    }
}
