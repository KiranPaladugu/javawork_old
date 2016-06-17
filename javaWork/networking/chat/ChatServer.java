package chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ChatServer {
	ArrayList<PrintWriter> clientOutputStream;
	public class ClientHandler implements Runnable{
		BufferedReader reader;
		Socket sock;
		public ClientHandler(Socket clientSocket){
			try{
				sock = clientSocket;
				InputStreamReader isReader= new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		public void run(){
			String message;
			try{
				while((message=reader.readLine())!=null){
					System.out.println(sock.getInetAddress()+" : "+message);
					tellEveryone(message);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}		
	}
	public static void main(String args[]){
		new ChatServer().go();
	}
	public void go(){
		clientOutputStream = new ArrayList<PrintWriter>();
		ServerSocket server = null;
		try{
			server = new ServerSocket(5000);
			while(true){
				Socket clientSocket = server.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clientOutputStream.add(writer);
				Thread t=new Thread(new ClientHandler(clientSocket));
				t.start();
				System.out.println("Got a Connecti0n...");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		    if(server!=null){
		        try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
		    }
		}
	}
	public void tellEveryone(String message){
		Iterator<PrintWriter> itr=clientOutputStream.iterator();
		while(itr.hasNext()){
			try{
				PrintWriter writer = itr.next();
				writer.print(message+"\n");
				writer.flush();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
