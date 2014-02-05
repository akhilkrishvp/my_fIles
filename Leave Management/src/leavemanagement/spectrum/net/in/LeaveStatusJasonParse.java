package leavemanagement.spectrum.net.in;

import org.json.JSONArray;
import org.json.JSONObject;

public class LeaveStatusJasonParse

{

	static String[] total;
	static String[] remaining;
	static String[] type;
	static int ldLen, olLen, totalLen;
	static String status;
	static String[] total1 = new String[2];
	static String[] total2 = new String[2];
	static String[] remain1 = new String[2];
	static String[] remain2 = new String[2];

	public static String parseLeavestatuspopup(String data) throws Exception {
		status = null;
		System.out.println("czokyo8yoiyh");
		try {
			JSONObject cobj = new JSONObject(data);
			status = cobj.getString("Status");
			System.out.println("podaisssssssssssssssssssssss=" + status);
			JSONArray ldArray = cobj.getJSONArray("LEAVE DETAILS");
			JSONArray olArray = cobj.getJSONArray("OTHER LEAVES");
			ldLen = ldArray.length();
			olLen = olArray.length();

			totalLen = ldLen + olLen;

			total = new String[totalLen];
			remaining = new String[totalLen];
			type = new String[totalLen];

			System.out.println("pppppppppppppppppppppppp" + totalLen);

			for (int i = 0; i < ldLen; i++) {

				total[i] = ldArray.getJSONObject(i).getString("Total")
						.toString();

				System.out.println("total issssssssssss 1" + ldLen + total[i]);
				// ........System.out.println("TOtal leave................"+total[i]);

				total1 = total[i].split(" ");
				total[i] = total1[0];

				System.out.println("TOtal leave.........." + total[0]);
				System.out.println("TOtal leave.........." + total[1]);

				remaining[i] = ldArray.getJSONObject(i).getString("Remaining")
						.toString();
				System.out.println("total issssssssssss 2" + ldLen
						+ remaining[i]);
				remain1 = remaining[i].split(" ");
				remaining[i] = remain1[0];
				System.out.println("remaining............" + remaining[0]);

				type[i] = ldArray.getJSONObject(i).getString("Leave_Type")
						.toString();
				System.out.println("total issssssssssss 3" + ldLen + type[i]);

			}

			for (int i = ldLen, j = 0; i < totalLen && j < olLen; i++, j++) {

				total[i] = olArray.getJSONObject(j).getString("Total")
						.toString();
				System.out.println("othertotal issssssssssss" + total[i]);

				total2 = total[i].split(" ");
				total[i] = total2[0];

				remaining[i] = olArray.getJSONObject(j).getString("Remaining")
						.toString();

				remain2 = remaining[i].split(" ");
				remaining[i] = remain2[0];

				type[i] = olArray.getJSONObject(j).getString("Leave Type")
						.toString();

			}

		} catch (Exception e) {

		}
		return status;
	}

	public static int parselength() {
		return totalLen;

	}

	public static String[] getleavetotal() {
		return total;

	}

	public static String[] getleaveremaing() {
		return remaining;
	}

	public static String[] gettype() {
		return type;
	}

}
