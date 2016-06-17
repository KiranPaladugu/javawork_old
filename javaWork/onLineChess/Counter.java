public class Counter {
    
    private static int instances;
    
    public  Counter( ) {
    	synchronized (this) {
			instances++;
		}        
    }
    public final void finalize() {
    	System.out.println("Called finalize...");
    	synchronized (this) {
    		instances--;
		}
    }
    public static int getInstances( ) {
        return instances;
    }
    public static void main(String args[]) throws InterruptedException{
    	new Counter();
    	new Counter();
    	new Counter();
    	Thread.sleep(1000);
    	if(getInstances() >1){
    		System.out.println("More than one instance is running at this time "+getInstances());
    	}
    }
} 