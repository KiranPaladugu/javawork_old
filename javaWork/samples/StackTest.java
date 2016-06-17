
public class StackTest {
	public static void main(String[] args) {
		Stack stack = new Stack(4);
		try{
			stack.put(new String("Kiran"));
			stack.put(new String("ONE"));
			stack.put(new String("TWO"));
			System.out.println(stack);
			stack.put(new String("Three"));
			System.out.println(stack);
			stack.put(new String("Four"));
			System.out.println(stack);
			System.out.println("PoP:"+stack.get());
			System.out.println(stack);
			System.out.println("PoP:"+stack.get());
			System.out.println(stack);
			System.out.println("PoP:"+stack.get());
			System.out.println(stack);
			stack.put(new String("Five"));
			stack.put(new String("six"));
			System.out.println(stack);
			System.out.println("PoP:"+stack.get());
			stack.put(new String("seven"));
			System.out.println(stack);
			System.out.println("PoP:"+stack.get());
			System.out.println(stack);
			System.out.println("PoP:"+stack.get());
			System.out.println(stack);
			System.out.println("PoP:"+stack.get());
			System.out.println(stack);
			System.out.println("PoP:"+stack.get());
			System.out.println(stack);
			System.out.println("PoP:"+stack.get());
			System.out.println(stack);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e);
		}
	}
}
