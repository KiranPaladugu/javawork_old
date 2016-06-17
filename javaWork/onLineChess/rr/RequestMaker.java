package rr;
public class RequestMaker {
	public static String getLoginRequest(String username,String password){
		return "/Login*"+username+":"+password;
	}
	public static String getMyMoveDetails(){
		return null;
	}
	public static String getLogoutRequest(){
		return "/Logout";
	}
}
