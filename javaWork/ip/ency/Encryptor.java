package ency;

public class Encryptor {
	private static Encryptor encryptor=new Encryptor();
	private static Key key;
	public Encryptor() {
		key=GenericKey.getKey();
	}
	public Encryptor(Key key){
		this.key=key;
	}
	public static String encryptContent(String content) throws Throwable{
		if(key == null){
			throw new Exception("NoKeyDefined");
		}		
		return new Encryptor(key).encryptor(content);
	}
	private String encryptor(String content){
		String str=content;
		System.out.println(key.getEncryptKey());
		return str;
	}
}
