package it.alwe.acronym.game;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import org.apache.commons.lang3.StringUtils;

public class Acronym {
	private JFrame mainFrame, optionsFrame, chooseFrame, connectFrame, errorFrame, infoFrame, waitFrame;
	private JLabel lblPlayer, lblHost, lblPort, lblImage, lblError, lblInfo, lblLoading;
	private JButton optClose, btnLocal, btnP2P, btnCreate, btnConnect, btnInfo, btnError, btnOk;
	private RoundJTextField txtHost, txtPort, txtAcronym;
	private JLabel[] optLabels = new JLabel[2], gameLabels = new JLabel[5];
	private JButton[] gameButtons = new JButton[4];
	private JSpinner secsSelector;
	private JCheckBox soundWarning, foreignLetters;
	public DragListener drag = new DragListener();
	public Integer counter = 40, port = 0, choose = 0;
	public Boolean soundOnOff = false, foreignOnOff = true, endCD = false;
	public Timer timer;
	public String hostname;
	public ServerSocket socket;
	public Socket client, server;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try { new Acronym().chooseFrame.setVisible(true); }
				catch (Exception e) { e.printStackTrace(); }
			}
		});
	}
	
	public Acronym() { initialize(); }
	
	private void initialize() {
		chooseFrame = new JFrame();
		baseFrame(chooseFrame, 250, 130);
		
		lblPlayer = new JLabel("Play in...", SwingConstants.CENTER);
		lblPlayer.setForeground(new Color(0x2dce98));
		lblPlayer.setBounds(15, 15, 230, 30);
		lblPlayer.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
		
		btnLocal = new JButton("Local"); btnP2P = new JButton("P2P");
		btnLocal.setBackground(new Color(0x2dce98)); btnP2P.setBackground(new Color(0x2dce98));
		btnLocal.setForeground(Color.BLACK); btnP2P.setForeground(Color.BLACK);
		btnLocal.setFont(new Font("Comic Sans MS", Font.BOLD, 18)); btnP2P.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		btnLocal.setBounds(130, 55, 110, 50); btnP2P.setBounds(10, 55, 110, 50);
		btnLocal.setUI(new ButtonStyles()); btnP2P.setUI(new ButtonStyles());
		
		btnLocal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				choose = 0;
				chooseFrame.dispose();
				GameFrame();
			}
		});
		
		btnP2P.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseFrame.dispose();
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
				txts[0].getDocument().addDocumentListener(new DocumentListener() {
					public void changedUpdate(DocumentEvent e) { hostname = txts[0].getText().length() >= 7 ? txts[0].getText().toString() : "127.0.0.1"; }
					public void insertUpdate(DocumentEvent e) { hostname = txts[0].getText().length() >= 7 ? txts[0].getText().toString() : "127.0.0.1"; }
					public void removeUpdate(DocumentEvent e) {	hostname = txts[0].getText().length() >= 7 ? txts[0].getText().toString() : "127.0.0.1"; }
				});
				txts[1].getDocument().addDocumentListener(new DocumentListener() {
					public void changedUpdate(DocumentEvent e) { port = txts[1].getText().length() >= 4 ? Integer.parseInt(txts[1].getText().toString()) : 2000; }
					public void insertUpdate(DocumentEvent e) { port = txts[1].getText().length() >= 4 ? Integer.parseInt(txts[1].getText().toString()) : 2000; }
					public void removeUpdate(DocumentEvent e) {	port = txts[1].getText().length() >= 4 ? Integer.parseInt(txts[1].getText().toString()) : 2000; }
				});
				((AbstractDocument) txts[0].getDocument()).setDocumentFilter(new FiltriDoc(0));
				((AbstractDocument) txts[1].getDocument()).setDocumentFilter(new FiltriDoc(1));
				
				btnCreate = new JButton("Create"); btnConnect = new JButton("Connect");
				btnCreate.setBackground(new Color(0x2dce98)); btnConnect.setBackground(new Color(0x2dce98));
				btnCreate.setForeground(Color.BLACK); btnConnect.setForeground(Color.BLACK);
				btnCreate.setFont(new Font("Comic Sans MS", Font.BOLD, 18)); btnConnect.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
				btnCreate.setBounds(15, 75, 110, 50); btnConnect.setBounds(145, 75, 110, 50);
				btnCreate.setUI(new ButtonStyles()); btnConnect.setUI(new ButtonStyles());
				
				btnCreate.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {	
						if (port >= 1024 && port <= 65535) {
							connectFrame.dispose();
							//waitFrame();
							try {
								socket = new ServerSocket(port);
								server = socket.accept();
								choose = 1;
								//waitFrame.dispose();
								GameFrame();
							} catch(IOException e1) { errorFrame(); }
						} else errorFrame();
					}
				});
				
				btnConnect.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {	
						if (port >= 1024 && port <= 65535) {
							connectFrame.dispose();
							try { 
								client = new Socket(hostname, port);
								choose = 2;
								GameFrame();
							} catch(IOException e1) { errorFrame(); }
						} else errorFrame();
					}
				});
				
				connectFrame.getContentPane().add(btnCreate); connectFrame.getContentPane().add(btnConnect);
			}
		});
		
		chooseFrame.getContentPane().add(lblPlayer);
		chooseFrame.getContentPane().add(btnP2P); chooseFrame.getContentPane().add(btnLocal);
	}
	
	public void GameFrame() {
		mainFrame = new JFrame();
		if (choose == 0) baseFrame(mainFrame, 400, 200);
		else baseFrame(mainFrame, 400, 265);
		
		String[] textLbl = {"Create a sentence with the following acronym :", "#####", counter + " seconds", "Game not started !",
			((choose != 0) ? "Connected on " + (choose == 1 ? server : client).getRemoteSocketAddress().toString().replace("/", "").replace("127.0.0.1", "localhost") : null)},
			btnText = {"Close", "Finish", "Start", "Options"};
		Integer[] bdsX = {280, 10, 10, 145}, bdsY = {10, 40, 70, 100, 170};
		for (int i = 0; i < ((choose != 0) ? gameLabels.length : gameLabels.length - 1); i++) {
			gameLabels[i] = new JLabel(textLbl[i], SwingConstants.CENTER);
			gameLabels[i].setForeground(new Color(0x2dce98));
			gameLabels[i].setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 16));
			gameLabels[i].setBounds(10, bdsY[i], 380, 25);
			mainFrame.getContentPane().add(gameLabels[i]);
		}
		
		txtAcronym = new RoundJTextField(0);
		txtAcronym.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
		txtAcronym.setBounds(10, 135, 380, 25);
		if (choose != 0) mainFrame.getContentPane().add(txtAcronym);
		
		for (int i = 0; i < gameButtons.length; i++) {
			gameButtons[i] = new JButton(btnText[i]);
			gameButtons[i].setForeground(Color.BLACK);
			gameButtons[i].setBackground(new Color(0x2dce98));
			gameButtons[i].setFont(new Font("Comic Sans MS", Font.BOLD, 18));
			gameButtons[i].setUI(new ButtonStyles());
			gameButtons[i].setBounds(bdsX[i], ((choose != 0) ? 205 : 140), 110, 50);
			mainFrame.getContentPane().add(gameButtons[i]);
		}
		
		if (choose == 1) {
			try {
				PrintWriter out = new PrintWriter(server.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
				out.println("Server connesso !");
				out.println("Attiva Timer");
				while (in.readLine() != null) { 
					System.out.println(in.readLine()); 
				}
			} catch (IOException e) { e.printStackTrace(); }
		}
		if (choose == 2) {
			gameButtons[2].setEnabled(false);
			gameButtons[3].setEnabled(false);
			try {
				PrintWriter out = new PrintWriter(client.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				out.println("Client connesso !");
				while (in.readLine() != null) { 
					if (in.readLine() == "Attiva Timer") System.out.println("Timer attivato !");
					System.out.println(in.readLine());
				}
			} catch (IOException e) { e.printStackTrace(); }
		}
		
		gameButtons[1].setVisible(false);
		gameButtons[0].addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				endCD = true; 
				mainFrame.dispose(); 
				try { 
					if (choose == 1) server.close();
					else if (choose == 2) client.close();
				} catch (IOException e1) { e1.printStackTrace(); }
			} 
		});
		gameButtons[1].addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { endCD = true; } });
		gameButtons[2].addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				Random r = new Random();
				Character[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
									   'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
				Character[] noestr = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'L', 'M',
						   			  'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'Z'};
				Character[] estr = {'J', 'K', 'W', 'X', 'Y'};
				String acronym = "";
				if (!foreignOnOff) {
					for (int i = 0; i < r.nextInt(3) + 3; i++) acronym = acronym + noestr[r.nextInt(20)] + ".";
					for (int i = 0; i < noestr.length; i++)
						if (StringUtils.countMatches(acronym, String.valueOf(noestr[i])) >= 3) 
							acronym.replace(noestr[i], noestr[r.nextInt(20)]);
				} else {
					for (int i = 0; i < r.nextInt(3) + 3; i++) acronym = acronym + letters[r.nextInt(25)] + ".";
					for (int i = 0; i < estr.length; i++)
						if (StringUtils.countMatches(acronym, String.valueOf(estr[i])) >= 2) 
							acronym.replace(estr[i], noestr[r.nextInt(20)]);
					for (int i = 0; i < letters.length; i++)
						if (StringUtils.countMatches(acronym, String.valueOf(letters[i])) >= 3) 
							acronym.replace(estr[i], noestr[r.nextInt(20)]);
				}
				gameLabels[1].setText(acronym);
				gameLabels[2].setText(counter + " seconds remaining !");
				gameLabels[3].setText("Game started !");
				gameButtons[1].setVisible(true);
				gameButtons[2].setVisible(false);
				gameButtons[3].setEnabled(false);
	        	if (soundOnOff) WarningSound();
	        	timer = new Timer(1000, new ActionListener() {
	        		int secs = counter;
					public void actionPerformed(ActionEvent e) {
						secs--;
						gameLabels[2].setText(secs + " seconds remaining !");
						if (endCD || secs == 0) {
							endCD = false;
							timer.stop();
							gameLabels[2].setText("The time is up !");
							gameLabels[3].setText("Game finished !");
							gameButtons[1].setVisible(false);
							gameButtons[2].setVisible(true);
							gameButtons[3].setEnabled(true);
						}
					}
	        	});
	        	timer.start();
			} 
		});
		gameButtons[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.setEnabled(false);
				
				optionsFrame = new JFrame();
				optionsFrame.getContentPane().setBackground(new Color(41, 41, 41));
				optionsFrame.setBounds(100, 100, 265, 200);
				optionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				optionsFrame.getContentPane().setLayout(null);
				optionsFrame.setUndecorated(true);
				optionsFrame.setLocationRelativeTo(null);
				optionsFrame.setResizable(false);
				optionsFrame.setShape(new RoundRectangle2D.Double(0, 0, optionsFrame.getWidth(), optionsFrame.getHeight(), 30, 30));
				optionsFrame.setVisible(true);
				optionsFrame.addMouseListener(drag);
				optionsFrame.addMouseMotionListener(drag);
				
				optClose = new JButton("Close");
				optClose.setBackground(new Color(0x2dce98));
				optClose.setForeground(Color.BLACK);
				optClose.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
				optClose.setBounds(145, 140, 110, 50);
				optClose.setUI(new ButtonStyles());
				optClose.addActionListener(new ActionListener() { 
					public void actionPerformed(ActionEvent e) { 
						optionsFrame.dispose(); 
						mainFrame.setEnabled(true);
						gameLabels[2].setText(counter + " seconds");
					} 
				});
				
				String[] optTexts = {"Available Time : ", "Created by zAlweNy26"};
				Integer[] optWidths = {135, 135}, optY = {10, 165}, fgs = {16, 11};
				for (int i = 0; i < optLabels.length; i++) {
					optLabels[i] = new JLabel(optTexts[i], SwingConstants.LEFT);
					optLabels[i].setForeground(new Color(0x2dce98));
					optLabels[i].setFont(new Font("Comic Sans MS", Font.BOLD, fgs[i]));
					optLabels[i].setBounds(10, optY[i], optWidths[i], 25);
					optionsFrame.getContentPane().add(optLabels[i]);
				}
				
			    secsSelector = new JSpinner(new SpinnerNumberModel((Number) counter, 5, 300, 1));   
			    secsSelector.setBounds(150, 10, 65, 25);
			    secsSelector.setBorder(BorderFactory.createLineBorder(new Color(0x2dce98)));
			    Component c = secsSelector.getEditor().getComponent(0);
			    c.setForeground(new Color(0x2dce98));
			    c.setBackground(new Color(41, 41, 41));
			    secsSelector.setUI(new JSpinnerLook());
			    secsSelector.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
			    secsSelector.addChangeListener(new ChangeListener() { public void stateChanged(ChangeEvent e) { counter = (Integer) ((JSpinner) e.getSource()).getValue(); } });
			    
			    soundWarning = new JCheckBox("Sound Warning : ");
			    soundWarning.setHorizontalTextPosition(SwingConstants.LEFT);
			    soundWarning.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
			    soundWarning.setForeground(new Color(0x2dce98));
			    soundWarning.setBackground(new Color(41, 41, 41));
			    soundWarning.setFocusPainted(false);
			    soundWarning.setIcon(new ImageIcon(this.getClass().getResource("CheckBoxUnchecked.png")));
			    soundWarning.setSelectedIcon(new ImageIcon(this.getClass().getResource("CheckBoxChecked.png")));
			    soundWarning.setBounds(5, 40, 170, 25);
			    if (soundOnOff) soundWarning.setSelected(true);
			    else soundWarning.setSelected(false);
			    soundWarning.addChangeListener(new ChangeListener() { 
			    	public void stateChanged(ChangeEvent e) { 
			    		if (((JCheckBox) e.getSource()).isSelected()) soundOnOff = true;
			    		else soundOnOff = false;
			    	} 
			    });
			    
			    foreignLetters = new JCheckBox("Foreign Letters : ");
			    foreignLetters.setHorizontalTextPosition(SwingConstants.LEFT);
			    foreignLetters.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
			    foreignLetters.setForeground(new Color(0x2dce98));
			    foreignLetters.setBackground(new Color(41, 41, 41));
			    foreignLetters.setFocusPainted(false);
			    foreignLetters.setIcon(new ImageIcon(this.getClass().getResource("CheckBoxUnchecked.png")));
			    foreignLetters.setSelectedIcon(new ImageIcon(this.getClass().getResource("CheckBoxChecked.png")));
			    foreignLetters.setBounds(5, 70, 180, 25);
			    if (foreignOnOff) foreignLetters.setSelected(true);
			    else foreignLetters.setSelected(false);
			    foreignLetters.addChangeListener(new ChangeListener() { 
			    	public void stateChanged(ChangeEvent e) { 
			    		if (((JCheckBox) e.getSource()).isSelected()) foreignOnOff = true;
			    		else foreignOnOff = false;
			    	} 
			    });
				
			    optionsFrame.getContentPane().add(secsSelector);
			    optionsFrame.getContentPane().add(soundWarning);
			    optionsFrame.getContentPane().add(foreignLetters);
				optionsFrame.getContentPane().add(optClose);
			}
		});
	}
	
	public void WarningSound() {
		URL url = this.getClass().getResource("WarningSound.wav");
		AudioClip clip = Applet.newAudioClip(url);
		clip.play();
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
	
	public void waitFrame() {
		waitFrame = new JFrame();
		baseFrame(waitFrame, 250, 130);
		connectFrame.dispose();
		
	    ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("loading.gif"));
	    lblLoading = new JLabel(imageIcon);
	    lblLoading.setBounds(10, 10, 300, 300);
	    imageIcon.setImageObserver(lblLoading);
	    
	    waitFrame.getContentPane().add(lblLoading);
	}
	
	public void baseFrame(JFrame baseFrame, int width, int height) {
		baseFrame.getContentPane().setBackground(new Color(41, 41, 41));
		baseFrame.setBounds(100, 100, width, height);
		baseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		baseFrame.setIconImage(new ImageIcon(this.getClass().getResource("AcronymIcon.png")).getImage());
		baseFrame.getContentPane().setLayout(null);
		baseFrame.setUndecorated(true);
		baseFrame.setLocationRelativeTo(null);
		baseFrame.setResizable(false);
		baseFrame.setVisible(true);
		baseFrame.setShape(new RoundRectangle2D.Double(0, 0, baseFrame.getWidth(), baseFrame.getHeight(), 30, 30));
		baseFrame.addMouseListener(drag);
		baseFrame.addMouseMotionListener(drag);
	}
}
