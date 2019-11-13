package it.alwe.triswb;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;

public class Tris implements ActionListener {
	private JFrame mainFrame, chooseFrame, playerFrame, connectFrame, errorFrame, infoFrame, restartFrame, winFrame, waitFrame;
	private JButton btnRestart, btnChiudi, btnPlayer, btnBot, btnP2P, btnLocal, btnConnect, btnError, btnCreate, btnInfo, btnOk, btnYes, btnNo, btnExit;
	private JLabel lblChoose, lblPoints1, lblPoints2, lblPlayer, lblHost, lblPort, lblError, lblImage, lblInfo, lblRestart, lblWin;
	private RoundJTextField txtHost, txtPort;
	public JButton[] btns = new JButton[9];
	public JLabel[] lbls = new JLabel[4];
	public Integer[] xy1 = {0, 0, 0, 1, 1, 1, 2, 2, 2}, xy2 = {0, 1, 2, 0, 1, 2, 0, 1, 2};
	public ImageIcon orosso = new ImageIcon(this.getClass().getResource("O_Rosso.png"));
	public ImageIcon xblu = new ImageIcon(this.getClass().getResource("X_Blu.png"));
	public DragListener drag = new DragListener();
	public String hostname;
	public Font Font17 = new Font("Comic Sans MS", Font.BOLD, 17);
	public ServerSocket server;
	public Socket connessione;
	public Timer pause;
	public char[][] tbl = new char[3][3];
	public int n = 0, xya = 0, xyb = 0, choose = 0, tc = 0, points1 = 0, points2 = 0, ok = 0, port = 0, seconds = 0, max = 30;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tris window = new Tris();
					window.chooseFrame.setVisible(true);
				} catch (Exception e) { e.printStackTrace(); }
			}
		});
	}
	
	public Tris() { initialize(); }
	
	private void initialize() {
		chooseFrame = new JFrame();
		baseFrame(chooseFrame, 250, 130);
		
		lblChoose = new JLabel("Play against a...", SwingConstants.CENTER);
		lblChoose.setForeground(new Color(0x2dce98));
		lblChoose.setBounds(15, 15, 230, 30);
		lblChoose.setFont(Font17);
		
		btnBot = new JButton("Computer"); btnPlayer = new JButton("Player"); 
		btnBot.setBackground(new Color(0x2dce98)); btnPlayer.setBackground(new Color(0x2dce98));
		btnBot.setForeground(Color.BLACK); btnPlayer.setForeground(Color.BLACK);
		btnBot.setFont(Font17); btnPlayer.setFont(Font17);
		btnBot.setBounds(130, 55, 110, 50); btnPlayer.setBounds(10, 55, 110, 50);
		btnBot.setUI(new ButtonStyles()); btnPlayer.setUI(new ButtonStyles());
		
		btnBot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				choose = 2;
				chooseFrame.dispose();
				mainFrame();
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
				lblPlayer.setFont(Font17);
				
				btnLocal = new JButton("Local"); btnP2P = new JButton("P2P");
				btnLocal.setBackground(new Color(0x2dce98)); btnP2P.setBackground(new Color(0x2dce98));
				btnLocal.setForeground(Color.BLACK); btnP2P.setForeground(Color.BLACK);
				btnLocal.setFont(Font17); btnP2P.setFont(Font17);
				btnLocal.setBounds(130, 55, 110, 50); btnP2P.setBounds(10, 55, 110, 50);
				btnLocal.setUI(new ButtonStyles()); btnP2P.setUI(new ButtonStyles());
				
				btnLocal.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						choose = 1;
						playerFrame.dispose();
						mainFrame();
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
						btnCreate.setFont(Font17); btnConnect.setFont(Font17);
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
									waitFrame();
									try {
										server = new ServerSocket(port);
										connessione = server.accept();
									} catch(IOException e1) { errorFrame(); }
								} else errorFrame();
							}
						});
						
						btnConnect.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {	
								if (port >= 1024 && port <= 65535) {
									n = 1;
									choose = 3;
									connectFrame.dispose();
									try { 
										connessione = new Socket(hostname, port);
										mainFrame();
									} catch(IOException e1) { errorFrame(); }
								} else errorFrame();
							}
						});
						
						connectFrame.getContentPane().add(btnCreate); connectFrame.getContentPane().add(btnConnect);
					}
				});
				
				playerFrame.getContentPane().add(lblPlayer);
				playerFrame.getContentPane().add(btnP2P); 
				playerFrame.getContentPane().add(btnLocal);
			}
		});
		
		chooseFrame.getContentPane().add(lblChoose);
		chooseFrame.getContentPane().add(btnPlayer); 
		chooseFrame.getContentPane().add(btnBot);
	}
	
	public void mainFrame() {
		mainFrame = new JFrame();
		if (choose != 3) baseFrame(mainFrame, 250, 390);
		else baseFrame(mainFrame, 250, 425);
		
	    Integer[] bds1 = {10, 90, 170, 10, 90, 170, 10, 90, 170}, bds2 = {65, 65, 65, 145, 145, 145, 225, 225, 225};
		for (int i = 0; i < 9 ; i++) {
			btns[i] = new JButton();
			btns[i].setBounds(bds1[i], bds2[i], 70, 70);
			btns[i].setName("vuoto");
			btns[i].setUI(new ButtonStyles());
			btns[i].addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		        	for (int i = 0; i < 9 ; i++) { 
		        		if ((JButton) e.getSource() == btns[i] && choose == 1) btnClickedPlayer(btns[i], xy1[i], xy2[i]);
		        		if ((JButton) e.getSource() == btns[i] && choose == 2) btnClickedBot(btns[i], xy1[i], xy2[i]);
		        		if ((JButton) e.getSource() == btns[i] && choose == 3) btnClickedP2P(btns[i], xy1[i], xy2[i]);
		        	}
					if (btns[0].getIcon() != null && btns[1].getIcon() != null && btns[2].getIcon() != null &&
							btns[3].getIcon() != null && btns[4].getIcon() != null && btns[5].getIcon() != null &&
							btns[6].getIcon() != null && btns[7].getIcon() != null && btns[8].getIcon() != null) clearAll();
		        }
		    });
			mainFrame.getContentPane().add(btns[i]);
		}
		
		int txts = 3;
		if (choose == 3) txts = 4;
		String pl1 = null, pl2 = null, portip = null;
		Integer[] bds3 = {10, 130, 70, 65}, bds4 = {10, 10, 360, 385}, fgs = {16, 16, 10, 12}, hs = {20, 20, 20, 30};
		if (choose == 1) { 
			pl1 = "Player X"; 
			pl2 = "Player O"; 
		} else if (choose == 2) { 
			pl1 = "You"; 
			pl2 = "Computer"; 
		} else if (choose == 3) { 
			pl1 = "You"; 
			pl2 = "Your friend"; 
			portip = "<html><center>Connected on<br>" + connessione.getRemoteSocketAddress().toString().replace("/", "").replace("127.0.0.1", "localhost") + "</center></html>"; 
		}
		String[] texts = {pl1, pl2, "Tris by zAlweNy26", portip};
		for (int i = 0; i < txts; i++) {
			lbls[i] = new JLabel(texts[i], SwingConstants.CENTER);
			lbls[i].setBounds(bds3[i], bds4[i], 110, hs[i]);
			lbls[i].setForeground(new Color(0x2dce98));
			lbls[i].setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, fgs[i]));
			mainFrame.getContentPane().add(lbls[i]);
		}
		
		btnRestart = new JButton("Restart"); btnChiudi = new JButton("Close");
		lblPoints1 = new JLabel(Integer.toString(points1), SwingConstants.CENTER);
		lblPoints2 = new JLabel(Integer.toString(points2), SwingConstants.CENTER);
		
		btnRestart.setBackground(new Color(0x2dce98)); btnChiudi.setBackground(new Color(0x2dce98));
		
		btnRestart.setForeground(Color.BLACK); btnChiudi.setForeground(Color.BLACK);
		lblPoints1.setForeground(new Color(0x2dce98)); lblPoints2.setForeground(new Color(0x2dce98));
		
		btnRestart.setFont(new Font("Comic Sans MS", Font.BOLD, 18)); btnChiudi.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		lblPoints1.setFont(new Font("Comic Sans MS", Font.BOLD, 16)); lblPoints2.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
		
		btnRestart.setBounds(10, 305, 110, 50); btnChiudi.setBounds(130, 305, 110, 50);
		lblPoints1.setBounds(10, 35, 110, 20); lblPoints2.setBounds(130, 35, 110, 20);
		
		btnRestart.setUI(new ButtonStyles()); btnChiudi.setUI(new ButtonStyles());
		
		btnRestart.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				if (choose != 3) {
					ok = 1; 
					clearAll(); 
				} else {
					// AVVIARE IL FRAME NELL'INSTANZA DELL'AVVERSARIO
					//restartFrame();
				}
			} 
		});
		btnChiudi.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				mainFrame.dispose();
				if (choose == 3) try { connessione.close(); } catch (IOException e1) { e1.printStackTrace(); }
			} 
		});
		
		mainFrame.getContentPane().add(btnRestart); mainFrame.getContentPane().add(btnChiudi);
		mainFrame.getContentPane().add(lblPoints1); mainFrame.getContentPane().add(lblPoints2);
	}
	
	public void baseFrame(JFrame baseFrame, int width, int height) {
		baseFrame.getContentPane().setBackground(new Color(41, 41, 41));
		baseFrame.setBounds(100, 100, width, height);
		baseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		baseFrame.setIconImage(new ImageIcon(this.getClass().getResource("TrisIcon.png")).getImage());
		baseFrame.getContentPane().setLayout(null);
		baseFrame.setUndecorated(true);
		baseFrame.setLocationRelativeTo(null);
		baseFrame.setResizable(false);
		baseFrame.setVisible(true);
		baseFrame.setShape(new RoundRectangle2D.Double(0, 0, baseFrame.getWidth(), baseFrame.getHeight(), 30, 30));
		baseFrame.addMouseListener(drag);
		baseFrame.addMouseMotionListener(drag);
	}

	public void clearAll() {
		if (choose != 3) {
			n = 0;
			for (int i = 0; i < 9 ; i++) {
				btns[i].setIcon(null);
				btns[i].setName("vuoto");
			}
			for (int x = 0; x < 3; x++) for(int y = 0; y < 3; y++) tbl[x][y] = '\0';
			if (ok == 1) {
				points1 = 0;
				points2 = 0;
				lblPoints1.setText(Integer.toString(points1));
				lblPoints2.setText(Integer.toString(points2));
				ok = 0;
			}
		} else {
			// FARE LA PARTE DEL CLEARALL PER LA CONNESSION P2P
		}
	}
	
	public void btnClickedPlayer(JButton btnXO, int r, int c) {
		if (n == 0 && "vuoto".equals(btnXO.getName())) {
			n = 1;
			btnXO.setIcon(xblu);
			btnXO.setName("pieno");
			tbl[r][c] = 'X';
		} else if (n == 1 && "vuoto".equals(btnXO.getName())) {
			n = 0;
			btnXO.setIcon(orosso);
			btnXO.setName("pieno");
			tbl[r][c] = 'O';
		}
		winCond();
	}
	
	public void btnClickedBot(JButton btnXO, int r, int c) {
		int bt1, bt2, vb = 0, i = 0;
		if (tbl[r][c] != 'X' && tbl[r][c] != 'O') {
			btnXO.setIcon(xblu);
			btnXO.setName("pieno");
			tbl[r][c] = 'X';
			vb++;
		}
		if (vb == 1) {
			do {
				bt1 = new Random().nextInt(3);
				bt2 = new Random().nextInt(3);
				if (tbl[bt1][bt2] != 'X' && tbl[bt1][bt2] != 'O') {
					tbl[bt1][bt2] = 'O';
					if (bt1 == 0 && bt2 == 0) { btns[0].setIcon(orosso); btns[0].setName("pieno"); }
					if (bt1 == 0 && bt2 == 1) { btns[1].setIcon(orosso); btns[1].setName("pieno"); }
					if (bt1 == 0 && bt2 == 2) { btns[2].setIcon(orosso); btns[2].setName("pieno"); }
					if (bt1 == 1 && bt2 == 0) { btns[3].setIcon(orosso); btns[3].setName("pieno"); }
					if (bt1 == 1 && bt2 == 1) { btns[4].setIcon(orosso); btns[4].setName("pieno"); }
					if (bt1 == 1 && bt2 == 2) { btns[5].setIcon(orosso); btns[5].setName("pieno"); }
					if (bt1 == 2 && bt2 == 0) { btns[6].setIcon(orosso); btns[6].setName("pieno"); }
					if (bt1 == 2 && bt2 == 1) { btns[7].setIcon(orosso); btns[7].setName("pieno"); }
					if (bt1 == 2 && bt2 == 2) { btns[8].setIcon(orosso); btns[8].setName("pieno"); }
					i++;
				}
				for (int j = 0, k = 0; j < 9; j++) {
					if (btns[j].getName() == "pieno") k++;
					if (k == 9) i++;
				}
			} while(i < 1);
			i = 0;
			vb = 0;
		}
		winCond();
	}
	
	public void btnClickedP2P(JButton btnXO, int r, int c) {
		try {
			int r1 = 0, c1 = 0;
			DataOutputStream dOut = new DataOutputStream(connessione.getOutputStream());
			DataInputStream dIn = new DataInputStream(connessione.getInputStream());
			/*dOut.writeByte(1);
			dOut.write(r);
			dOut.flush();
			dOut.writeByte(2);
			dOut.write(c);
			dOut.flush();
			dOut.writeByte(-1);
			dOut.flush();*/
			dOut.close();
			int intNum = dIn.read();
			if (intNum == 1) r1 = dIn.readInt();
			if (intNum == 2) c1 = dIn.readInt();
			if (n == 0 && "vuoto".equals(btnXO.getName())) {
				n = 1;
				btnXO.setIcon(xblu);
				btnXO.setName("pieno");
				tbl[r1][c1] = 'X';
			} else if (n == 1 && "vuoto".equals(btnXO.getName())) {
				n = 0;
				btnXO.setIcon(orosso);
				btnXO.setName("pieno");
				tbl[r1][c1] = 'O';
			}
			dIn.close();
			winCond();
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public void winCond() {
		Character[] XO = {'X', 'O'};
		for (int j = 0; j < 2; j++) {
			if (choose == 1 || choose == 2) {
				if (tbl[0][0] == XO[j] && tbl[0][0] == tbl[1][1] && tbl[1][1] == tbl[2][2]) winLocalBot(j);
				if (tbl[2][0] == XO[j] && tbl[2][0] == tbl[1][1] && tbl[1][1] == tbl[0][2]) winLocalBot(j);
				if (tbl[0][0] == XO[j] && tbl[0][0] == tbl[1][0] && tbl[1][0] == tbl[2][0]) winLocalBot(j);
				if (tbl[0][1] == XO[j] && tbl[0][1] == tbl[1][1] && tbl[1][1] == tbl[2][1]) winLocalBot(j);
				if (tbl[0][2] == XO[j] && tbl[0][2] == tbl[1][2] && tbl[1][2] == tbl[2][2]) winLocalBot(j);
				if (tbl[0][0] == XO[j] && tbl[0][0] == tbl[0][1] && tbl[0][1] == tbl[0][2]) winLocalBot(j);
				if (tbl[1][0] == XO[j] && tbl[1][0] == tbl[1][1] && tbl[1][1] == tbl[1][2]) winLocalBot(j);
				if (tbl[2][0] == XO[j] && tbl[2][0] == tbl[2][1] && tbl[2][1] == tbl[2][2]) winLocalBot(j);
			} else if (choose == 3) {
				if (tbl[0][0] == XO[j] && tbl[0][0] == tbl[1][1] && tbl[1][1] == tbl[2][2]) winP2P(j);
				if (tbl[2][0] == XO[j] && tbl[2][0] == tbl[1][1] && tbl[1][1] == tbl[0][2]) winP2P(j);
				if (tbl[0][0] == XO[j] && tbl[0][0] == tbl[1][0] && tbl[1][0] == tbl[2][0]) winP2P(j);
				if (tbl[0][1] == XO[j] && tbl[0][1] == tbl[1][1] && tbl[1][1] == tbl[2][1]) winP2P(j);
				if (tbl[0][2] == XO[j] && tbl[0][2] == tbl[1][2] && tbl[1][2] == tbl[2][2]) winP2P(j);
				if (tbl[0][0] == XO[j] && tbl[0][0] == tbl[0][1] && tbl[0][1] == tbl[0][2]) winP2P(j);
				if (tbl[1][0] == XO[j] && tbl[1][0] == tbl[1][1] && tbl[1][1] == tbl[1][2]) winP2P(j);
				if (tbl[2][0] == XO[j] && tbl[2][0] == tbl[2][1] && tbl[2][1] == tbl[2][2]) winP2P(j);
			}
		}
	}
	
	public void winLocalBot(int i) {
		if (i == 0) points1++;
		else if (i == 1) points2++;
		winFrame(i);
	}
	
	public void winP2P(int i) {
		if (i == 0) points1++;
		else if (i == 1) points2++;
		lblPoints1.setText(Integer.toString(points1)); 
		lblPoints2.setText(Integer.toString(points2));
		clearAll();
	}
	
	public void winFrame(int win) {
		winFrame = new JFrame();
		baseFrame(winFrame, 250, 120);
		mainFrame.setEnabled(false);
		pause = new Timer(2000, this);
		pause.start();
		
		String winner = null;
		if (lbls[win].getText() == "You") winner = "You have won the match !";
		else winner = lbls[win].getText() + " has won the match !";
		
		lblWin = new JLabel("<html><center>" + winner + "</center></html>", SwingConstants.CENTER);
		lblWin.setForeground(new Color(0x2dce98));
		lblWin.setBounds(10, 5, 230, 50);
		lblWin.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		
		btnOk = new JButton("Ok !");
		btnOk.setBackground(new Color(0x2dce98));
		btnOk.setForeground(Color.BLACK);
		btnOk.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		btnOk.setBounds(70, 55, 110, 50);
		btnOk.setUI(new ButtonStyles());
		btnOk.addActionListener(this);
		
		winFrame.getContentPane().add(lblWin);
		winFrame.getContentPane().add(btnOk);
	}
	
	public void waitFrame() {
		waitFrame = new JFrame();
		baseFrame(waitFrame, 250, 130);
		
		btnExit = new JButton("Exit");
		btnExit.setBackground(new Color(0x2dce98));
		btnExit.setForeground(Color.BLACK);
		btnExit.setFont(Font17);
		btnExit.setBounds(10, 65, 110, 50);
		btnExit.setUI(new ButtonStyles());
		
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					connessione.close();
					server.close();
				} catch (IOException e1) { e1.printStackTrace(); }
				waitFrame.dispose();
			}
		});
		
		waitFrame.getContentPane().add(btnExit);
	}
	
	public void restartFrame() {
		restartFrame = new JFrame();
		baseFrame(restartFrame, 250, 130);
		mainFrame.setEnabled(false);
		
		lblRestart = new JLabel("<html><center>The opponent asks to restart,<br>do you accept ?</center></html>", SwingConstants.CENTER);
		lblRestart.setForeground(new Color(0x2dce98));
		lblRestart.setBounds(10, 5, 230, 50);
		lblRestart.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		
		btnYes = new JButton("Yes"); btnNo = new JButton("No");
		btnYes.setBackground(new Color(0x2dce98)); btnNo.setBackground(new Color(0x2dce98));
		btnYes.setForeground(Color.BLACK); btnNo.setForeground(Color.BLACK);
		btnYes.setFont(Font17); btnNo.setFont(Font17);
		btnYes.setBounds(10, 65, 110, 50); btnNo.setBounds(130, 65, 110, 50);
		btnYes.setUI(new ButtonStyles()); btnNo.setUI(new ButtonStyles());
		
		btnYes.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				ok = 1;
				restartFrame.dispose();
				clearAll();
				mainFrame.setEnabled(true);
			} 
		});
		
		btnNo.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				ok = 0;
				restartFrame.dispose();
				mainFrame.setEnabled(true);
				// FAR APPARIRE UN FRAME CHE DICE A QUELLO CHE HA CHIESTO IL RIAVVIO, CHE LA RICHIESTA E' STATA RIFIUTATA
			} 
		});
		
		restartFrame.getContentPane().add(lblRestart);
		restartFrame.getContentPane().add(btnYes); 
		restartFrame.getContentPane().add(btnNo);
	}
	
	public void errorFrame() {
		errorFrame = new JFrame();
		baseFrame(errorFrame, 160, 250);
		connectFrame.setEnabled(false);
		
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
				connectFrame.setEnabled(true);
			} 
		});
		
		btnInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				infoFrame = new JFrame();
				baseFrame(infoFrame, 235, 200);
				errorFrame.setEnabled(false);
				
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
						errorFrame.setEnabled(true);
					} 
				});
				
				infoFrame.getContentPane().add(lblInfo);
				infoFrame.getContentPane().add(btnOk);
			}
		});
		
		errorFrame.getContentPane().add(btnInfo); errorFrame.getContentPane().add(btnError);
		errorFrame.getContentPane().add(lblError); errorFrame.getContentPane().add(lblImage);
    }

	
	@Override
	public void actionPerformed(ActionEvent e) {
		lblPoints1.setText(Integer.toString(points1)); 
		lblPoints2.setText(Integer.toString(points2));
		clearAll();
		winFrame.dispose();
		mainFrame.setEnabled(true);
		pause.stop();
	}
}