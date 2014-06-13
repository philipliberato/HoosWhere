package edu.virginia.cs.hooswhere;

public class VariableContainer {
	private static String userName="";
	private static String userId="";
	
	
	public static String getUserName() {
		return userName;
	}
	public static void setUserName(String myUserName) {
		userName = myUserName;
	}
	public static String getUserId() {
		return userId;
	}
	public static void setUserId(String myUserId) {
		userId = myUserId;
	}
	
	

}
