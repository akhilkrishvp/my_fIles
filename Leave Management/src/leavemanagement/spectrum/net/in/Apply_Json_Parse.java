package leavemanagement.spectrum.net.in;

import org.json.JSONObject;

public class Apply_Json_Parse {

		static String result,reason,id;
		public static String parseApply(String Data){
			try {
				System.out.println(" the result is...122334");
					JSONObject jobj=new JSONObject(Data);
					System.out.println(" the result is...");
					id=jobj.getString("Id");
					System.out.println("The id is ........"+id);
					result=jobj.getString("Result");
					System.out.println(" the result is..."+result);
					if(result.equals("Application Failed"))
					{
						reason=jobj.getString("Reason").toString();
						System.out.println("the reason is "+reason);
					}
				
			} catch (Exception e) {
				
			}
			return Data;
			
		}
		public static String Get_Result() {
			return result;

		}
		public static String Get_Reason() {
			return reason;

		}
		public static String Get_Id() {
			return id;

		}
}
