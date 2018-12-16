package net.bce.nielsbwashere;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
public class ClientA extends JFrame{
	private static final long serialVersionUID = -3504835749666396332L;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	Socket s;
	JButton[][] jb = new JButton[6][5];
	JButton jbutton2 = new JButton("New game");
	JButton jbutton3 = new JButton("Exit");
	JButton jbutton4 = new JButton("");
	JButton jbutton5 = new JButton("");
	String currChar = "O";
	boolean canEdit=false;
	ClientLog cl;
	String ip;
	int host;
public ClientA(ClientLog cl, String ip, int host) {
	super("Buttery Cheese Eggs (C) Nielsbwashere");
	this.ip = ip;
	this.host = host;
	this.cl=cl;
	cl.jta.append("Started Buttery Cheese Eggs (C) Nielsbwashere");
	this.setSize(326,477);
	this.setResizable(false);
	for(int i = 0;i<5;i++){
		for(int j = 0;j<5;j++){
			jb[i][j] = new JButton("");
			jb[i][j].setActionCommand(i + "." + j);
			jb[i][j].addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(!canEdit)return;
					int x = Integer.parseInt(arg0.getActionCommand().split("\\.")[0]);
					int y = Integer.parseInt(arg0.getActionCommand().split("\\.")[1]);
					if(jb[x][y].getText()==""){
					jb[x][y].setText(currChar);
					try{
						String sent = send();
						oos.writeObject(sent);
						oos.flush();
						canEdit=false;
						keepUpWith(sent);
					}catch(Exception e){
						try{
						oos.close();
						ois.close();
						s.close();
						}catch(Exception ex){
						System.exit(0);
						}
					}
					}
				}});
			jb[i][j].setBounds(i*64,j*64,64,64);
			add(jb[i][j]);
		}	
	}
	jb[5][4]=new JButton("");
	jbutton2.setActionCommand("Restart:false");
	jbutton2.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!jbutton2.getActionCommand().equalsIgnoreCase("Restart:true")){
				
			}else{
				String s = sendNewBoard();
				keepUpWith(s);
				jbutton2.setActionCommand("Restart:false");
			}
		}
	});
	jbutton2.setBounds(0*64,6*64,128,64);
	add(jbutton2);
	jbutton3.setActionCommand("Exit");
	jbutton3.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!jbutton3.getActionCommand().equalsIgnoreCase("Exit")){
				
			}else{
				System.exit(0);
			}
		}
	});
	jbutton3.setBounds(1*128,6*64,64,64);
	add(jbutton3);
	jbutton4.setBounds(1*128,5*64,64,64);
	add(jbutton4);
	jbutton5.setBounds(1*64+128,6*64,128,64);
	add(jbutton5);
	add(jb[5][4]);
	remove(jb[5][4]);
	try {
		cl.log("Starting connection....");
		startConnection();
		cl.log("Connection started!");
		this.setVisible(true);
	} catch (Exception e) {
		e.printStackTrace();
		this.setVisible(false);
		this.dispose();
		cl.log("Connection ended!");
	}
}
protected void keepUpWith(String sent) {
	String newBoard = sent.split("\\(")[2];
	String charac = sent.split("\\(")[0].equalsIgnoreCase("X")?"O":"X";
	String canSent = sent.split("\\(")[1].equals("1")?"0":"1";
	receive(charac+"("+canSent+"("+newBoard);
}
protected String sendNewBoard() {
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
	String toSend = currChar+"(1("+s;
	try{
		oos.writeObject(toSend);
		oos.flush();
		canEdit=false;
	}catch(Exception e){}
	currChar=currChar.equalsIgnoreCase("O")?"X":"O";
	return toSend;
}
boolean initialized=false;
private void startConnection() throws Exception {
	s = new Socket(InetAddress.getByName(ip),host);
	oos=new ObjectOutputStream(s.getOutputStream());
	ois=new ObjectInputStream(s.getInputStream());
	initialized=true;
}
public void receive(String stri){
	String character = stri.split("\\(")[0];
	canEdit = stri.split("\\(")[1].equals("0")?false:stri.split("\\(")[1].equals("1")?true:null;
	String s = stri.split("\\(")[2];
	String[] buttons1 = s.split("\\.");
	int i = 0;
	int j = 0;
	for(String str : buttons1){
		String[] st1 = str.split("");
		for(String st : st1){
			if(st.equalsIgnoreCase("")||st==null)continue;
			jb[i][j].setText(st.equals("0")?"":st);
			i++;
		}
		i=0;
		j++;
	}
	currChar=character.equalsIgnoreCase("X")?"O":"X";
	jbutton4.setText(currChar);
	victory(jb,stri);
}
private void victory(JButton[][] jb,String s) {
	String victor = victor(jb,s);
	if(victor==currChar){
		canEdit=false;
		jbutton5.setText("You have won!");
		jbutton2.setActionCommand("Restart:true");
	}else if(victor==(currChar.equalsIgnoreCase("O")?"X":"O")){
		canEdit=false;
		jbutton5.setText("You have lost!");
	}else if(victor=="Nobody"){
		canEdit=false;
		jbutton5.setText("Nobody has won!");
		if(currChar.equalsIgnoreCase("X"))jbutton2.setActionCommand("Restart:true");
	}
	else{
		jbutton5.setText("");
	}
}
private String victor(JButton[][] jb, String hori) {
		String opponent = currChar.equalsIgnoreCase("O")?"X":"O";
		if(hori.contains(currChar+currChar+currChar)){
			return currChar;
		}else if(hori.contains(opponent+opponent+opponent)){
			return opponent;
		}
		String verti = vertical(hori.substring(4));
		if(verti.contains(currChar+currChar+currChar)){
			return currChar;
		}else if(verti.contains(opponent+opponent+opponent)){
			return opponent;
		}
		String[] diagonal = dia(hori.substring(4));
		for(String dia : diagonal){
			System.out.println(dia);
		if(dia.contains(currChar+currChar+currChar)){
			return currChar;
		}else if(dia.contains(opponent+opponent+opponent)){
			return opponent;
		}
		}
		if(isFull(jb)){
			return "Nobody";
		}
	return "";
}
private String[] dia(String horizontal) {
	String[] split = horizontal.split("\\.");
	String[] split1 = split[0].split("");
	String[] split2 = split[1].split("");
	String[] split3 = split[2].split("");
	String[] split4 = split[3].split("");
	String[] split5 = split[4].split("");
	String[] s = new String[10];
	s[0]=split1[0]+split2[1]+split3[2]+split4[3]+split5[4];
	s[1]=split2[0]+split3[1]+split4[2]+split5[3];
	s[2]=split1[1]+split2[2]+split3[3]+split4[4];
	s[3]=split3[0]+split4[1]+split5[2];
	s[4]=split1[2]+split2[3]+split3[4];
	s[5]=split5[0]+split4[1]+split3[2]+split2[3]+split1[4];
	s[6]=split4[0]+split3[1]+split2[2]+split1[3];
	s[7]=split5[1]+split4[2]+split3[3]+split2[4];
	s[8]=split3[0]+split2[1]+split1[2];
	s[9]=split5[2]+split4[3]+split3[4];
	return s;
}
private boolean isFull(JButton[][] jb){
	for(JButton[] jbutt : jb){
		for(JButton jbu : jbutt){
			if(jbu.getText()=="")return false;
		}
	}
	return true;
}
private String vertical(String horizontal) {
	String[] split = horizontal.split("\\.");
	String[] split1 = split[0].split("");
	String[] split2 = split[1].split("");
	String[] split3 = split[2].split("");
	String[] split4 = split[3].split("");
	String[] split5 = split[4].split("");
	String vert = "";
	boolean isF=true;
	for(int i=0;i<5;i++){
		if(!isF)vert=vert+".";
		vert=vert+split1[i]+split2[i]+split3[i]+split4[i]+split5[i];
		isF=false;
	}
	System.out.println(vert + " from " + horizontal);
	return vert;
}
public String send(){
	String s = currChar+"("+(canEdit?"1":"0")+"(";
	boolean isFirst=true;
	for(int i=0;i<5;i++){
		if(!isFirst)s=s+".";
		for(int j=0;j<5;j++){
			JButton jbut = jb[j][i];
			s=s+(jbut.getText().equals("")?"0":jbut.getText());
		}
		isFirst=false;
	}
	return s;
}
public void run() {
	try {
			receive((String)ois.readObject());
	} catch (Exception e) {
		if(!initialized){
			return;
		}
		if(this.isVisible()){
		this.setVisible(false);
		this.dispose();
		e.printStackTrace();
		cl.log("Connection ended!");
		try {
			oos.close();
			ois.close();
		} catch (IOException e1) {
			System.exit(0);
		}
		System.exit(0);
		}
	}
}
}