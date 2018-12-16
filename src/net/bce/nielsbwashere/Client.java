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
public class Client {
	public static void main(String[] args) {
		final JFrame jf = new JFrame("ButteryCheeseEggs (C) Nielsbwashere - Choose ip");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setResizable(false);
		jf.setBounds(0,0,500,130);
		JPanel jp = new JPanel();
		jp.setLayout(new GridLayout(0,2));
		jp.add(new JButton("IP"));
		final JTextField tf = new JTextField("localhost");
		tf.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if((e.getKeyChar()<'0'||e.getKeyChar()>'9') && e.getKeyChar() != '.')e.consume();
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});
		jp.add(tf);
		jp.add(new JButton("Port"));
		final JTextField tf2 = new JTextField("10001");
		tf2.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar()<'0'||e.getKeyChar()>'9' || tf2.getText().length() > 4)e.consume();
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});
		jp.add(tf2);
		jf.add(jp,BorderLayout.NORTH);
		jp = new JPanel();
		JButton jb = new JButton("Choose IP");
		jb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				jf.setVisible(false);
				if(Integer.parseInt(tf2.getText())<65536)init(tf.getText(),Integer.parseInt(tf2.getText()));
				else init(tf.getText(),10001);
			}
		});
		jp.add(jb, BorderLayout.WEST);
		jb = new JButton("Choose default port");
		jb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				jf.setVisible(false);
				init(tf.getText(),10001);
			}
		});
		jp.add(jb, BorderLayout.EAST);
		jb = new JButton("Choose default IP");
		jb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				jf.setVisible(false);
				init("localhost",10001);
			}
		});
		jp.add(jb, BorderLayout.CENTER);
		jf.add(jp,BorderLayout.SOUTH);
		jf.setVisible(true);
	}
	public static void init(String ip, int host){
		if(host<0||host>65535)host = 10001;
		final ClientLog cl = new ClientLog();
		cl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final ClientA client = new ClientA(cl,ip,host);
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					while(true){
					client.run();}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}});	
		t.start();
	}
}