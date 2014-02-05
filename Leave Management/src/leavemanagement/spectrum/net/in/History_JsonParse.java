package leavemanagement.spectrum.net.in;

import org.json.JSONArray;
import org.json.JSONObject;

public class History_JsonParse {

	static String[] dateApplied, noOfLeave, leaveStatusArray, leaveDates, type,
			reason, leaveId, ldLeaveType, ldTotal, ldTaken, ldRemaining;

	static int appliedLeavesLen, leaveDetailsLen, otherLeavesLen,
			leaveTotalLen;

	public static boolean parseHistory(String data) throws Exception {
		boolean status = false;
		String statusString = "Failed";
		try {
			JSONObject cobj = new JSONObject(data);
			statusString = cobj.getString("Status");

			System.out.println("history status=" + statusString);
			if (statusString.equals("Success")) {

				// Applied leaves array
				JSONArray appliedLeavesJsonArray = cobj
						.getJSONArray("APPLIED LEAVES");
				appliedLeavesLen = appliedLeavesJsonArray.length();

				dateApplied = new String[appliedLeavesLen];
				noOfLeave = new String[appliedLeavesLen];
				leaveStatusArray = new String[appliedLeavesLen];
				leaveDates = new String[appliedLeavesLen];
				reason = new String[appliedLeavesLen];
				leaveId = new String[appliedLeavesLen];
				type = new String[appliedLeavesLen];

				System.out.println("historyLen=====" + appliedLeavesLen);
				for (int i = 0; i < appliedLeavesLen; i++) {

					dateApplied[i] = appliedLeavesJsonArray.getJSONObject(i)
							.getString("Apply_Date").toString();
					noOfLeave[i] = appliedLeavesJsonArray.getJSONObject(i)
							.getString("No_of_Days").toString();
					leaveStatusArray[i] = appliedLeavesJsonArray
							.getJSONObject(i).getString("Request_Status")
							.toString();
					leaveDates[i] = appliedLeavesJsonArray.getJSONObject(i)
							.getString("Leave_Dates").toString();
					reason[i] = appliedLeavesJsonArray.getJSONObject(i)
							.getString("Reason").toString();
					leaveId[i] = appliedLeavesJsonArray.getJSONObject(i)
							.getString("Request_Id").toString();
					type[i] = appliedLeavesJsonArray.getJSONObject(i)
							.getString("Type").toString();
				}

				// Leave Details Array
				JSONArray leaveDetailsJsonArray = cobj
						.getJSONArray("LEAVE DETAILS");
				leaveDetailsLen = leaveDetailsJsonArray.length();

				JSONArray otherLeavesJsonArray = cobj
						.getJSONArray("OTHER LEAVES");
				otherLeavesLen = otherLeavesJsonArray.length();

				leaveTotalLen = leaveDetailsLen + otherLeavesLen;

				ldLeaveType = new String[leaveTotalLen];
				ldRemaining = new String[leaveTotalLen];
				ldTaken = new String[leaveTotalLen];
				ldTotal = new String[leaveTotalLen];
				for (int i = 0; i < leaveDetailsLen; i++) {

					ldLeaveType[i] = leaveDetailsJsonArray.getJSONObject(i)
							.getString("Leave_Type").toString();
					ldRemaining[i] = leaveDetailsJsonArray.getJSONObject(i)
							.getString("Remaining").toString();
					ldTaken[i] = leaveDetailsJsonArray.getJSONObject(i)
							.getString("Taken").toString();
					ldTotal[i] = leaveDetailsJsonArray.getJSONObject(i)
							.getString("Total").toString();
				}
				if (otherLeavesLen != 0) {

					for (int i = leaveDetailsLen; i < leaveTotalLen; i++) {

						ldLeaveType[i] = leaveDetailsJsonArray.getJSONObject(i)
								.getString("Leave Type").toString();
						ldRemaining[i] = leaveDetailsJsonArray.getJSONObject(i)
								.getString("Remaining").toString();
						ldTaken[i] = leaveDetailsJsonArray.getJSONObject(i)
								.getString("Taken").toString();
						ldTotal[i] = leaveDetailsJsonArray.getJSONObject(i)
								.getString("Total").toString();
					}
				}
			}

		} catch (Exception e) {

		}
		return status;

	}

	public static int appliedLeavesLength() {
		return appliedLeavesLen;
	}

	public static String[] getDateApplied() {
		return dateApplied;
	}

	public static String[] getNoOfLeaves() {
		return noOfLeave;
	}

	public static String[] getLeaveStatus() {
		return leaveStatusArray;
	}

	public static String[] getLeaveDates() {
		return leaveDates;
	}

	public static String[] getReason() {
		return reason;
	}

	public static String[] getLeaveId() {
		return leaveId;
	}

	public static String[] getType() {
		return type;
	}

}
