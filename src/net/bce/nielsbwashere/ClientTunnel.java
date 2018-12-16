package net.bce.nielsbwashere;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
public class ClientTunnel {
	public Socket s1, s2;
	public ObjectInputStream ois1,ois2;
	public ObjectOutputStream oos1,oos2;
	public ClientTunnel(Socket s3, Socket s4,ObjectOutputStream oos12, ObjectOutputStream oos22, final ServerA sA) throws Exception{
		this.s1=s3;
		this.s2=s4;
		ois1=new ObjectInputStream(s1.getInputStream());
		ois2=new ObjectInputStream(s2.getInputStream());
		oos1=oos12;
		oos2=oos22;
		Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				while(true){
					  String s;
					try {
						s = (String)ois1.readObject();
			            oos2.writeObject(s);
			            oos2.flush();
					} catch (Exception e) {
						try{
							if(oos1!=null){
								oos1.close();
								oos1=null;
									}
									if(oos2!=null){
								oos2.close();
								oos2=null;
									}
									if(ois1!=null){
								ois1.close();
								ois1=null;
									}
									if(ois2!=null){
								ois2.close();
								ois2=null;
									}
									if(s1!=null){
								s1.close();
								s1=null;
									}
									if(s2!=null){
								s2.close();
								s2=null;
									}
							break;
							}catch(Exception exc){
								
							}
					}
				}
			}});
		t.start();
		t = new Thread(new Runnable(){
			@Override
			public void run() {
				while(true){
				  String s;
				try {
					s = (String)ois2.readObject();
		            oos1.writeObject(s);
		            oos1.flush();
				} catch (Exception e) {
					try{
						if(oos1!=null){
					oos1.close();
					oos1=null;
						}
						if(oos2!=null){
					oos2.close();
					oos2=null;
						}
						if(ois1!=null){
					ois1.close();
					ois1=null;
						}
						if(ois2!=null){
					ois2.close();
					ois2=null;
						}
						if(s1!=null){
					s1.close();
					s1=null;
						}
						if(s2!=null){
					s2.close();
					s2=null;
						}
					break;
					}catch(Exception exc){
						
					}
				} 
				}
			}});
		t.start();
	}
}
