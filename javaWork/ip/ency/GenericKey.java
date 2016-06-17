package ency;

public abstract class GenericKey implements Key {
	private static class KeyImpl implements Key{
		private int key1=12345;
		private int key2=54321;
		private int key3=67890;
		private int key4=12378;
		private int key5=65432;
		Object object ;
		 public static Key getKey(){
			return new KeyImpl();
		}
		private KeyImpl() {
			object = key1+"-"+key2+"-"+key3+"-"+key4+"-"+key5;
		}
		public Object getEncryptKey() {
			return object;
		}
		public void setEncryptKey(Object key) {
			this.object=key;
		}		
	}
	public static Key getKey(){
		return KeyImpl.getKey();			
	}
}
