package ency;

public class CharPrint {
	public static void main(String[] args) {
		String str="ENTER CURRENT PASSWORD";
		for(int i=0;i<str.length();i++){
			System.out.println((byte)str.charAt(i));
		}
	}
}
