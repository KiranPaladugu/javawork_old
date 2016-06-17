package range;

public class Generator {
	public static final byte NUMBERS_ONLY = 0;
	public static final byte ALPHABETS_CAPS_ONLY = 1;
	public static final byte ALPHABETS_SMALL_ONLY = 2;
	public static final byte ALPHABETS = 3;
	public static final byte ALPHA_NUMERIC = 4;
	private byte num_min=48;
	private byte num_max=57;
	private byte type;
	public Generator() {
		type = ALPHA_NUMERIC;
	}
	public Generator(byte type){
		this.type =type;
	}
}
