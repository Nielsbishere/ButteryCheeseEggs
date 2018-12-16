package net.bce.nielsbwashere;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
public class ServerA extends JFrame{
	private static final long serialVersionUID = -3504835749666396332L;
	private JTextArea jta = new JTextArea();
	private static int port = 10001;
	ServerSocket ss;
public ServerA(int port) {
	super("Buttery Cheese Eggs Server (C) Nielsbwashere (:" + port);
	ServerA.port = port;
	this.setSize(300,100);
	jta.setEditable(false);
	add(new JScrollPane(jta));
	this.setVisible(true);
	try{
		ss = new ServerSocket(port);
	}catch(Exception e){
		System.exit(0);
	}
	jta.append("Connection server has been started.. on port " + port + "\n");
}
public void run() throws Exception{
	if(ss==null){
		try{
			ss = new ServerSocket(port);
		}catch(Exception e){
			System.exit(0);
		}
	}
	jta.append("Waiting for clients...\n");
	Socket s1 = ss.accept();
	jta.append("Client one is accepting...\n");
	Socket s2 = ss.accept();
	jta.append("Client two is accepting...\n");
	ObjectOutputStream oos1 = new ObjectOutputStream(s1.getOutputStream());
	ObjectOutputStream oos2 = new ObjectOutputStream(s2.getOutputStream());
	jta.append("Created a connection between two clients!\n");
	randomlyGenerateBoard(s1,s2,oos1,oos2);
	new ClientTunnel(s1,s2,oos1,oos2,this);
}
private void randomlyGenerateBoard(Socket s1, Socket s2, ObjectOutputStream oos1, ObjectOutputStream oos2) throws Exception{
	String st = randomBoard();
	oos1.writeObject("X(0("+st);
	oos2.writeObject("O(1("+st);
	oos1.flush();
	oos2.flush();
}
private String randomBoard() {
	String[][] array = new String[5][5];
	for(int i=0;i<(2+(int)(Math.random()*3));i++){
		array[(int)(Math.random()*5)][(int)(Math.random()*5)]="/";
	}
	for(int i=0;i<5;i++){
		for(int j=0;j<5;j++){
			if(array[i][j]==null||array[i][j]=="")array[i][j]="0";
		}	
	}
	array[2][1]="/";
	array[1][2]="/";
	array[2][3]="/";
	array[3][2]="/";
	array[3][3]="/";
	array[2][2]="/";
	String s = "";
	boolean isFirst=true;
	for(String[] arr : array){
		if(!isFirst)s=s+".";
		for(String a : arr){
			s=s+a;
		}
		isFirst=false;
	}
	return s;
}
public void log(String string) {
	jta.append("\n" + string);
}
}