package leavemanagement.spectrum.net.in;

import org.json.JSONArray;
import org.json.JSONObject;

public class ViewRequest_JsonParse {
	static String[] name, empId, empType, department, applyDate, noOfDays,
			leaveDates, leaveType, reason, emailId, phoneNumber, requestStatus,
			requestId;

	static int ldEmploysLen, ldTeamLeaderLen, totalLen;

	public static boolean parseViewReqst(String data, String currentUser)
			throws Exception {
		boolean status = false;
		String statusString = "Failed";
		JSONArray ldTeamLeadersJsonArray = null;

		try {
			JSONObject cobj = new JSONObject(data);
			statusString = cobj.getString("Status").toString();
			if (statusString.equals("Success")) {
				status = true;

				JSONArray ldEmploysJsonArray = cobj
						.getJSONArray("Leave Details Of Employs");
				ldEmploysLen = ldEmploysJsonArray.length();
				if (currentUser.equals("Admin")) {
					ldTeamLeadersJsonArray = cobj
							.getJSONArray("Leave Details Of Team Leaders");
					ldTeamLeaderLen = ldTeamLeadersJsonArray.length();
					totalLen = ldTeamLeaderLen + ldEmploysLen;
				} else {
					totalLen = ldEmploysLen;
				}

				name = new String[totalLen];
				empId = new String[totalLen];
				empType = new String[totalLen];
				department = new String[totalLen];
				applyDate = new String[totalLen];
				noOfDays = new String[totalLen];
				leaveDates = new String[totalLen];
				leaveType = new String[totalLen];
				reason = new String[totalLen];
				emailId = new String[totalLen];
				phoneNumber = new String[totalLen];
				requestStatus = new String[totalLen];
				requestId = new String[totalLen];
				System.out.println("parselen=====" + totalLen);

				for (int i = 0; i < ldEmploysLen; i++) {

					name[i] = ldEmploysJsonArray.getJSONObject(i)
							.getString("Name").toString();
					empId[i] = ldEmploysJsonArray.getJSONObject(i)
							.getString("Emp_Id").toString();
					empType[i] = ldEmploysJsonArray.getJSONObject(i)
							.getString("Type").toString();
					department[i] = ldEmploysJsonArray.getJSONObject(i)
							.getString("Department").toString();
					applyDate[i] = ldEmploysJsonArray.getJSONObject(i)
							.getString("Apply_Date").toString();
					noOfDays[i] = ldEmploysJsonArray.getJSONObject(i)
							.getString("No_of_Days").toString();
					leaveDates[i] = ldEmploysJsonArray.getJSONObject(i)
							.getString("Leave_Dates").toString();
					leaveType[i] = ldEmploysJsonArray.getJSONObject(i)
							.getString("Leave_Type").toString();
					reason[i] = ldEmploysJsonArray.getJSONObject(i)
							.getString("Reason").toString();
					emailId[i] = ldEmploysJsonArray.getJSONObject(i)
							.getString("Email_Id").toString();
					phoneNumber[i] = ldEmploysJsonArray.getJSONObject(i)
							.getString("Phone_No").toString();
					requestStatus[i] = ldEmploysJsonArray.getJSONObject(i)
							.getString("Request_Status").toString();
					requestId[i] = ldEmploysJsonArray.getJSONObject(i)
							.getString("Request_Id").toString();

				}
				if (currentUser.equals("Admin")) {
					for (int i = ldEmploysLen; i < totalLen; i++) {
						name[i] = ldTeamLeadersJsonArray.getJSONObject(i)
								.getString("Name").toString();
						empId[i] = ldTeamLeadersJsonArray.getJSONObject(i)
								.getString("Emp_Id").toString();
						empType[i] = ldTeamLeadersJsonArray.getJSONObject(i)
								.getString("Type").toString();
						department[i] = ldTeamLeadersJsonArray.getJSONObject(i)
								.getString("Department").toString();
						applyDate[i] = ldTeamLeadersJsonArray.getJSONObject(i)
								.getString("Apply_Date").toString();
						noOfDays[i] = ldTeamLeadersJsonArray.getJSONObject(i)
								.getString("No_of_Days").toString();
						leaveDates[i] = ldTeamLeadersJsonArray.getJSONObject(i)
								.getString("Leave_Dates").toString();
						leaveType[i] = ldTeamLeadersJsonArray.getJSONObject(i)
								.getString("Leave_Type").toString();
						reason[i] = ldTeamLeadersJsonArray.getJSONObject(i)
								.getString("Reason").toString();
						emailId[i] = ldTeamLeadersJsonArray.getJSONObject(i)
								.getString("Email_Id").toString();
						phoneNumber[i] = ldTeamLeadersJsonArray
								.getJSONObject(i).getString("Phone_No")
								.toString();
						requestStatus[i] = ldTeamLeadersJsonArray
								.getJSONObject(i).getString("Request_Status")
								.toString();
						requestId[i] = ldTeamLeadersJsonArray.getJSONObject(i)
								.getString("Request_Id").toString();

					}
				}

			}

		} catch (Exception e) {

		}
		return status;

	}

	public static int parselength() {
		return totalLen;

	}

	public static int EmploysLen() {
		return ldEmploysLen;
	}

	public static int TeamLeaderLen() {
		return ldTeamLeaderLen;
	}

	public static String[] getName() {
		return name;
	}

	public static String[] getEmpId() {
		return empId;
	}

	public static String[] getEmpType() {
		return empType;
	}

	public static String[] getDepartment() {
		return department;
	}

	public static String[] getApplyDates() {
		return applyDate;
	}

	public static String[] getNoOfDays() {
		return noOfDays;
	}

	public static String[] getLeaveDates() {
		return leaveDates;
	}

	public static String[] getLeaveType() {
		return leaveType;
	}

	public static String[] getReason() {
		return reason;
	}

	public static String[] getEmailId() {
		return emailId;
	}

	public static String[] getPhoneNumber() {
		return phoneNumber;
	}

	public static String[] getRequestStatus() {
		return requestStatus;
	}

	public static String[] getRequestId() {
		return requestId;
	}
}