package net.bce.nielsbwashere;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
public class ClientLog extends JFrame{
	private static final long serialVersionUID = -3504835749666396332L;
	JTextArea jta = new JTextArea();
public ClientLog() {
	super("Buttery Cheese Eggs (C) Nielsbwashere");
	this.setSize(300,200);
	this.setResizable(true);
	jta.setEditable(false);
	jta.setText("Starting connection...\n");
	add(new JScrollPane(jta));
	this.setVisible(true);
}
public void log(String s){
	jta.append("\n"+s);
	System.out.println(s);
}
}