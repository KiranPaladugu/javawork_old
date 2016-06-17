import java.util.Vector;

public class Stack {
	private Vector<Object> stack;
	private int size = -1;

	/**
	 * Constructs stack with no limit.
	 */
	public Stack() {
		stack = new Vector<Object>();
	}

	/**
	 * Constructs stack with limited capacity
	 * 
	 * @param capacity
	 */
	public Stack(int size) {
		stack = new Vector<Object>(size);
		this.size = size;
	}

	/**
	 * puts object into stack
	 * 
	 * @param obj
	 * @throws Exception
	 */
	public void put(Object obj) throws Exception {
		if (size != -1) {
			if (stack.size() <= size-1) {
				stack.add(obj);
			} else {
				throw new Exception("Stack OverFlow.");
			}
		} else {
			stack.add(obj);
		}
	}

	/**
	 * gets object from stack
	 * 
	 * @return
	 * @throws Exception
	 */
	public Object get() throws Exception {
		Object obj;
		if (stack.size() != 0) {
			obj = stack.get(stack.size()-1);
			stack.remove(stack.size()-1);
			return obj;
		} else {
			throw new Exception("Stack UnderFlow.");
		}
	}

	/**
	 * returns true if stack has objects
	 * 
	 * @return
	 */
	public boolean hasPrevious() {
		if (stack.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * returns true if not reached limit
	 * 
	 * @return
	 */
	public boolean canPut() {
		if ((size != -1) && (stack.size() == size)) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * Retruns String representation of objects.
	 * 
	 * @return
	 */
	public String toString(){
		String msg="[";
		for(int i=0 ; i<stack.size();i++){
			if(i!=0){
				msg+=",";
			}
			msg+=stack.get(i);			
		}
		msg+="]";
		return msg;
	}
}
