package it.alwe.games.forza4;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Forza4 {
	public JFrame frame, chooseFrame, playerFrame, connectFrame, errorFrame, infoFrame;
	private JButton btnRestart, btnChiudi, btnPlayer, btnBot, btnP2P, btnLocal, btnConnect, btnError, btnCreate, btnInfo, btnOk;
	private JLabel lblChoose, lblPoints1, lblPoints2, lblPlayer, lblHost, lblPort, lblError, lblImage, lblInfo;
	private JPanel panelBtns;
	private RoundJTextField txtHost, txtPort;
	public JLabel[] lbls = new JLabel[3], lblCircles = new JLabel[42];
	public ImageIcon FrameIcon = new ImageIcon(this.getClass().getResource("icon.png"));
	public DragListener drag = new DragListener();
	public String hostname;
	public Font base = new Font("Comic Sans MS", Font.BOLD, 18);
	public ServerSocket server;
	public Socket connessione;
	public Integer[][] tbl = new Integer[6][7];
	public int choose = 0, points1 = 0, points2 = 0, n = 0, ok = 0, port = 0;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Forza4 window = new Forza4();
					window.chooseFrame.setVisible(true);
				} catch (Exception e) { e.printStackTrace(); }
			}
		});
	}

	public Forza4() { initialize(); }
	
	private void initialize() {
		chooseFrame = new JFrame();
		baseFrame(chooseFrame, 250, 130);
		
		lblChoose = new JLabel("Play against a...", SwingConstants.CENTER);
		lblChoose.setForeground(new Color(0x2dce98));
		lblChoose.setBounds(15, 15, 230, 30);
		lblChoose.setFont(new Font("Comic Sans MS", Font.BOLD, 17));
		
		btnBot = new JButton("Computer"); btnPlayer = new JButton("Player"); 
		btnBot.setBackground(new Color(0x2dce98)); btnPlayer.setBackground(new Color(0x2dce98));
		btnBot.setForeground(Color.BLACK); btnPlayer.setForeground(Color.BLACK);
		btnBot.setFont(base); btnPlayer.setFont(base);
		btnBot.setBounds(130, 55, 110, 50); btnPlayer.setBounds(10, 55, 110, 50);
		btnBot.setUI(new ButtonStyles()); btnPlayer.setUI(new ButtonStyles());
		
		btnBot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				choose = 2;
				chooseFrame.dispose();
				MainFrame();
			}
		});
		
		btnPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseFrame.dispose();
				playerFrame = new JFrame();
				baseFrame(playerFrame, 250, 130);
				
				lblPlayer = new JLabel("Play in...", SwingConstants.CENTER);
				lblPlayer.setForeground(new Color(0x2dce98));
				lblPlayer.setBounds(15, 15, 230, 30);
				lblPlayer.setFont(new Font("Comic Sans MS", Font.BOLD, 17));
				
				btnLocal = new JButton("Local"); btnP2P = new JButton("P2P");
				btnLocal.setBackground(new Color(0x2dce98)); btnP2P.setBackground(new Color(0x2dce98));
				btnLocal.setForeground(Color.BLACK); btnP2P.setForeground(Color.BLACK);
				btnLocal.setFont(base); btnP2P.setFont(base);
				btnLocal.setBounds(130, 55, 110, 50); btnP2P.setBounds(10, 55, 110, 50);
				btnLocal.setUI(new ButtonStyles());btnP2P.setUI(new ButtonStyles());
				
				btnLocal.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						choose = 1;
						playerFrame.dispose();
						MainFrame();
					}
				});
				
				btnP2P.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						playerFrame.dispose();
						connectFrame = new JFrame();
						baseFrame(connectFrame, 270, 140);
						
						JLabel[] lbls = {lblHost, lblPort};
						String[] texts = {"Hostname :", "Port to open :"};
						Integer[] x = {15, 145}; Integer[] widths = {110, 110};
						for(int i = 0; i < 2; i++) {
							lbls[i] = new JLabel(texts[i], SwingConstants.CENTER);
							lbls[i].setBounds(x[i], 10, widths[i], 20);
							lbls[i].setForeground(new Color(0x2dce98));
							lbls[i].setFont(new Font("Comic Sans MS", Font.BOLD, 14));
							connectFrame.getContentPane().add(lbls[i]);
						}
						
						RoundJTextField[] txts = {txtHost, txtPort};
						for(int i = 0, j = 15; i < 2; i++, j = j + 130) {
							txts[i] = new RoundJTextField(0);
							txts[i].setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
							txts[i].setBounds(j, 40, 110, 20);
							connectFrame.getContentPane().add(txts[i]);
						}
						
						btnCreate = new JButton("Create"); btnConnect = new JButton("Connect");
						btnCreate.setBackground(new Color(0x2dce98)); btnConnect.setBackground(new Color(0x2dce98));
						btnCreate.setForeground(Color.BLACK); btnConnect.setForeground(Color.BLACK);
						btnCreate.setFont(base); btnConnect.setFont(base);
						btnCreate.setBounds(15, 75, 110, 50); btnConnect.setBounds(145, 75, 110, 50);
						btnCreate.setUI(new ButtonStyles()); btnConnect.setUI(new ButtonStyles());
						
						//port = Integer.parseInt(txtPort.getText());
						port = 2000;
						//hostname = txtHost.getText().toString();
						hostname = "127.0.0.1";
						
						btnCreate.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {	
								if (port >= 1024 && port <= 65535) {
									n = 0;
									choose = 3;
									connectFrame.dispose();
									try {
										server = new ServerSocket(port, 1);
										connessione = server.accept();
									} catch(IOException e1) { e1.printStackTrace(); }
									MainFrame();
								} else errorFrame();
							}
						});
						
						btnConnect.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {	
								if (port >= 1024 && port <= 65535) {
									n = 1;
									choose = 3;
									connectFrame.dispose();
									try { connessione = new Socket(hostname, port); }
									catch(IOException e1) { e1.printStackTrace(); }
									MainFrame();
								} else errorFrame();
							}
						});
						
						connectFrame.getContentPane().add(btnCreate);
						connectFrame.getContentPane().add(btnConnect);
					}
				});
				
				playerFrame.getContentPane().add(lblPlayer);
				playerFrame.getContentPane().add(btnP2P);
				playerFrame.getContentPane().add(btnLocal);
			}
		});
		
		chooseFrame.getContentPane().add(lblChoose);
		chooseFrame.getContentPane().add(btnPlayer); chooseFrame.getContentPane().add(btnBot);
	}
	
	private void MainFrame() {
		frame = new JFrame();
		baseFrame(frame, 430, 510);
		
		String pl1 = null, pl2 = null;
		Integer[] bds3 = {15, 275, 150}, bds4 = {15, 15, 455}, fgs = {18, 18, 10};
		if (choose == 1) { pl1 = "Red Player"; pl2 = "Yellow Player"; }
		if (choose == 2) { pl1 = "You"; pl2 = "Computer"; } 
		String[] texts = {pl1, pl2, "Force 4 by zAlweNy26"};
		for (int i = 0; i < 3 ; i++) {
			lbls[i] = new JLabel(texts[i], SwingConstants.CENTER);
			lbls[i].setBounds(bds3[i], bds4[i], 130, 30);
			lbls[i].setForeground(new Color(0x2dce98));
			lbls[i].setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, fgs[i]));
			frame.getContentPane().add(lbls[i]);
		}
		
		lblPoints1 = new JLabel(Integer.toString(points1), SwingConstants.CENTER);
		lblPoints2 = new JLabel(Integer.toString(points2), SwingConstants.CENTER);
        lblPoints1.setBounds(20, 45, 110, 20); lblPoints2.setBounds(290, 45, 110, 20);
        lblPoints1.setForeground(new Color(0x2dce98)); lblPoints2.setForeground(new Color(0x2dce98));
        lblPoints1.setFont(base); lblPoints2.setFont(base);
        
        panelBtns = new JPanel();
        panelBtns.setLayout(new GridLayout(6, 7));
        panelBtns.setBackground(new Color(41, 41, 41));
        panelBtns.setBounds(10, 80, 410, 350);
        
        for (int i = 0; i < lblCircles.length; i++) {
        	lblCircles[i] = new JLabel(new ImageIcon(this.getClass().getResource("vuoto.png")));
        	lblCircles[i].setName("vuota");
        	lblCircles[i].addMouseListener(new MouseAdapter() {  
        	    public void mouseClicked(MouseEvent e) {
        	    	for (int i = 0; i < lblCircles.length; i++)  {
        	    		if (e.getSource() == lblCircles[i]) {
        	    			int r = i / 7, c = i % 7;
        	    			if (r == 5) putImage(lblCircles[i], r, c);
        	    			else if (tbl[r+1][c] != null) putImage(lblCircles[i], r, c);
        	    		}
        	    	}
        	    }
        	});
        	panelBtns.add(lblCircles[i]);
        }
        
        btnRestart = new JButton("Restart"); btnChiudi = new JButton("Close");
        btnRestart.setBackground(new Color(0x2dce98)); btnChiudi.setBackground(new Color(0x2dce98));
        btnRestart.setBounds(15, 445, 110, 50); btnChiudi.setBounds(305, 445, 110, 50);
        btnRestart.setForeground(Color.BLACK); btnChiudi.setForeground(Color.BLACK);
        btnRestart.setFont(base); btnChiudi.setFont(base);
        btnRestart.setUI(new ButtonStyles()); btnChiudi.setUI(new ButtonStyles());
        
        btnRestart.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { ok = 1; clearAll(); } });
		btnChiudi.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { frame.dispose(); } });
		
		frame.getContentPane().add(panelBtns);
		frame.getContentPane().add(lblPoints1); frame.getContentPane().add(lblPoints2);
		frame.getContentPane().add(btnRestart); frame.getContentPane().add(btnChiudi);
	}
	
	public void putImage(JLabel lbl, int r, int c) {
		if (choose == 1) {
			if (n == 0 && "vuota".equals(lbl.getName())) {
				n = 1;
				lbl.setIcon(new ImageIcon(this.getClass().getResource("red.png")));
				lbl.setName("piena");
				tbl[r][c] = 1;
			} else if (n == 1 && "vuota".equals(lbl.getName())) {
				n = 0;
				lbl.setIcon(new ImageIcon(this.getClass().getResource("yellow.png")));
				lbl.setName("piena");
				tbl[r][c] = 2;
			}
		} else if (choose == 2) {
			if (n == 0 && "vuota".equals(lbl.getName())) {
				n = 1;
				lbl.setIcon(new ImageIcon(this.getClass().getResource("red.png")));
				lbl.setName("piena");
				tbl[r][c] = 1;
			} else if (n == 1 && "vuota".equals(lbl.getName())) {
				/*int x, y;
				do {
					x = new Random().nextInt(6);
					y = new Random().nextInt(7);
					if (x == 5 && tbl[x][y] != 1 && tbl[x][y] != 2) {
						tbl[x][y] = 2;
						lblCircles[(x*7)+y].setIcon(new ImageIcon(this.getClass().getResource("yellow.png")));
						lblCircles[(x*7)+y].setName("piena");
					} else if (tbl[x+1][y] != null && tbl[x][y] != 1 && tbl[x][y] != 2) {
						tbl[x][y] = 2;
						lblCircles[(x*7)+y].setIcon(new ImageIcon(this.getClass().getResource("yellow.png")));
						lblCircles[(x*7)+y].setName("piena");
					}
				} while (tbl[x][y] == 1 && tbl[x][y] == 2 && tbl[x][y] == null);*/
				n = 0;
			}
		} else if (choose == 3) {
			if (n == 0 && "vuota".equals(lbl.getName())) {
				n = 1;
			} else if (n == 1 && "vuota".equals(lbl.getName())) {
				n = 0;
			}
		}
		winCond();
	}
	
	public void clearAll() {
		n = 0;
		for (int i = 0; i < 42; i++) {
			lblCircles[i].setIcon(new ImageIcon(this.getClass().getResource("vuoto.png")));
			lblCircles[i].setName("vuota");
		}
		for (int x = 0; x < 6; x++) for(int y = 0; y < 7; y++) tbl[x][y] = null;
		if (ok == 1) {
			ok = 0;
			points1 = 0;
			points2 = 0;
			lblPoints1.setText(Integer.toString(points1));
			lblPoints2.setText(Integer.toString(points2));
		}
	}
	
	public void winCond() {
		for (int k = 1; k < 3; k++) {
			for (int j = 0; j < 4; j++){
		        for (int i = 0; i < 6; i++){
		        	if (tbl[i][j] != null && tbl[i][j+1] != null && tbl[i][j+2] != null && tbl[i][j+3] != null)
		        		if (tbl[i][j] == k && tbl[i][j+1] == k && tbl[i][j+2] == k && tbl[i][j+3] == k) winLocalBot(k);     
		        }
			}
			for (int i = 0; i < 3; i++){
			    for (int j = 0; j < 7; j++){
			    	if (tbl[i][j] != null && tbl[i+1][j] != null && tbl[i+2][j] != null && tbl[i+3][j] != null)
			    		if (tbl[i][j] == k && tbl[i+1][j] == k && tbl[i+2][j] == k && tbl[i+3][j] == k) winLocalBot(k);    
			    }
			}
			for (int i = 3; i < 6; i++){
			    for (int j = 0; j < 4; j++){
			    	if (tbl[i][j] != null && tbl[i-1][j+1] != null && tbl[i-2][j+2] != null && tbl[i-3][j+3] != null)
			    		if (tbl[i][j] == k && tbl[i-1][j+1] == k && tbl[i-2][j+2] == k && tbl[i-3][j+3] == k) winLocalBot(k);
			    }
			}
			for (int i = 3; i < 6; i++){
			    for (int j = 3; j < 7; j++){
			    	if (tbl[i][j] != null && tbl[i-1][j-1] != null && tbl[i-2][j-2] != null && tbl[i-3][j-3] != null)
			    		if (tbl[i][j] == k && tbl[i-1][j-1] == k && tbl[i-2][j-2] == k && tbl[i-3][j-3] == k) winLocalBot(k);
			    }
			}
		}
	}
	
	public void winLocalBot(int i) {
		if (i == 1) points1++;
		else if (i == 2) points2++;
		lblPoints1.setText(Integer.toString(points1)); 
		lblPoints2.setText(Integer.toString(points2));
		clearAll();
	}
	
	public void winP2P(int i) {
		
	}
	
	public void baseFrame(JFrame baseFrame, int width, int height) {
		baseFrame.getContentPane().setBackground(new Color(41, 41, 41));
		baseFrame.setBounds(100, 100, width, height);
		baseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		baseFrame.setIconImage(FrameIcon.getImage());
		baseFrame.getContentPane().setLayout(null);
		baseFrame.setUndecorated(true);
		baseFrame.setLocationRelativeTo(null);
		baseFrame.setResizable(false);
		baseFrame.setVisible(true);
		baseFrame.setShape(new RoundRectangle2D.Double(0, 0, baseFrame.getWidth(), baseFrame.getHeight(), 30, 30));
		baseFrame.addMouseListener(drag);
		baseFrame.addMouseMotionListener(drag);
	}
	
	public void errorFrame() {
		errorFrame = new JFrame();
		baseFrame(errorFrame, 160, 250);
		btnConnect.setEnabled(false);
		btnCreate.setEnabled(false);
		
		lblImage = new JLabel();
		lblImage.setIcon(new ImageIcon(this.getClass().getResource("ExPoint.png")));
		lblImage.setBounds(74, 20, 12, 64);
		
		lblError = new JLabel("<html><center>Input errors<br>or<br>missing data</center></html>", SwingConstants.CENTER);
		lblError.setForeground(new Color(216, 0, 0));
		lblError.setBounds(10, 80, 140, 100);
		lblError.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		
		btnError = new JButton("Close"); btnInfo = new JButton();
		btnError.setBackground(new Color(216, 0, 0)); btnInfo.setBackground(new Color(41, 41, 41));
		btnError.setBounds(25, 185, 110, 50); btnInfo.setBounds(118, 10, 32, 36);
		btnError.setUI(new ButtonStyles()); btnInfo.setUI(new ButtonStyles());
		btnError.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		btnError.setForeground(Color.BLACK);
		btnInfo.setIcon(new ImageIcon(this.getClass().getResource("Info.png")));
		
		btnError.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				errorFrame.dispose(); 
				btnConnect.setEnabled(true);
				btnCreate.setEnabled(true);
			} 
		});
		
		btnInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				infoFrame = new JFrame();
				baseFrame(infoFrame, 235, 200);
				btnError.setEnabled(false);
				btnInfo.setEnabled(false);
				
				lblInfo = new JLabel("<html><center>Check if you have correctly<br>"
						+ "entered the IP address<br>and the port to open !<br>"
						+ "(it can vary between<br> 1024 and 65535)</center></html>");
				lblInfo.setForeground(new Color(0x2dce98));
				lblInfo.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
				lblInfo.setBounds(10, 11, 215, 115);
				
				btnOk = new JButton("Ok");
				btnOk.setBackground(new Color(0x2dce98));
				btnOk.setForeground(Color.BLACK);
				btnOk.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
				btnOk.setBounds(60, 137, 110, 50);
				btnOk.setUI(new ButtonStyles());
				btnOk.addActionListener(new ActionListener() { 
					public void actionPerformed(ActionEvent e) { 
						infoFrame.dispose();
						btnError.setEnabled(true);
						btnInfo.setEnabled(true);
					} 
				});
				
				infoFrame.getContentPane().add(lblInfo);
				infoFrame.getContentPane().add(btnOk);
			}
		});
		
		errorFrame.getContentPane().add(btnInfo); errorFrame.getContentPane().add(btnError);
		errorFrame.getContentPane().add(lblError); errorFrame.getContentPane().add(lblImage);
    }
}