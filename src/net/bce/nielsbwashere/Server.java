package net.bce.nielsbwashere;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
public class Server {
	public static void main(String[] args) {
		final JFrame jf = new JFrame("ButteryCheeseEggs (C) Nielsbwashere - Choose port");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setResizable(false);
		jf.setBounds(0,0,300,100);
		JPanel jp = new JPanel();
		jp.setLayout(new GridLayout(0,2));
		jp.add(new JButton("Port"));
		final JTextField tf = new JTextField("");
		tf.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar()<'0'||e.getKeyChar()>'9' || tf.getText().length() > 4)e.consume();
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});
		jp.add(tf);
		jf.add(jp,BorderLayout.NORTH);
		jp = new JPanel();
		JButton jb = new JButton("Choose port");
		jb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(Integer.parseInt(tf.getText())<65536)init(Integer.parseInt(tf.getText()));
				else init(10001);
				jf.setVisible(false);
			}
		});
		jp.add(jb, BorderLayout.WEST);
		jb = new JButton("Choose default port");
		jb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				init(10001);
				jf.setVisible(false);
			}
		});
		jp.add(jb, BorderLayout.EAST);
		jf.add(jp,BorderLayout.SOUTH);
		jf.setVisible(true);
	}
	public static void init(int port){
		final ServerA server = new ServerA(port);
		server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					while(true){
					server.run();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}});
		t.start();
	}
}