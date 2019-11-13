package it.alwe.codfiswb;

import java.util.*;
import java.util.List;
import java.io.*;
import java.net.MalformedURLException;
import org.json.simple.*;
import javax.swing.text.*;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.awt.geom.*;
import com.google.gson.*;
import java.text.ParseException;
import javax.swing.text.MaskFormatter;
import org.json.simple.parser.JSONParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

public class CodFis implements FocusListener {
	private JFrame frmMain;
	private JTextField fieldDDN, fieldCodFis;
	private JTextField[] fields = new JTextField[3];
	private JRadioButton[] rbtns = new JRadioButton[2];
	private JLabel lblSesso, lblDDS, lblProvincia, lblImage, lblMe;
	private JButton btnCalcolo, btnClose, btnSave;
	public DragListener drag = new DragListener();
	public Integer okPR = 0;
	public Map<List<Character>,String> accwords = new HashMap<List<Character>,String>() {
    	private static final long serialVersionUID = 1L; {
            put(new ArrayList<Character>(Arrays.asList('á', 'à', 'â', 'ă', 'ā', 'ã','ą')), "a"); 
            put(new ArrayList<Character>(Arrays.asList('å')), "aa"); 
            put(new ArrayList<Character>(Arrays.asList('ä', 'æ')), "ae");
            put(new ArrayList<Character>(Arrays.asList('ć', 'ċ', 'ĉ', 'č', 'ç')), "c");
            put(new ArrayList<Character>(Arrays.asList('ď', 'đ', 'ð', 'ɖ')), "d");
            put(new ArrayList<Character>(Arrays.asList('é', 'è', 'ė', 'ê', 'ë', 'ě', 'ĕ', 'ē', 'ę')), "e");
            put(new ArrayList<Character>(Arrays.asList('ġ', 'ĝ', 'ğ', 'ģ')), "g");
            put(new ArrayList<Character>(Arrays.asList('ĥ', 'ħ')), "h");
            put(new ArrayList<Character>(Arrays.asList('ı', 'í', 'ì', 'î', 'ï', 'ĭ', 'ī', 'ĩ', 'į')), "i");
            put(new ArrayList<Character>(Arrays.asList('ĵ')), "j"); 
            put(new ArrayList<Character>(Arrays.asList('ķ')), "k");
            put(new ArrayList<Character>(Arrays.asList('ĺ', '·', 'ľ', 'ļ', 'ł')), "l");
            put(new ArrayList<Character>(Arrays.asList('ń', 'ň', 'ñ', 'ņ', 'ŋ')), "n");
            put(new ArrayList<Character>(Arrays.asList('ó', 'ò', 'ô', 'ŏ', 'ō', 'õ', 'ő')), "o"); 
            put(new ArrayList<Character>(Arrays.asList('ö', 'ø', 'œ', 'œ')), "oe");
            put(new ArrayList<Character>(Arrays.asList('ŕ', 'ř', 'ŗ')), "r");
            put(new ArrayList<Character>(Arrays.asList('ś', 'ŝ', 'š', 'š', 'ş')), "s");
            put(new ArrayList<Character>(Arrays.asList('ß')), "ss");
            put(new ArrayList<Character>(Arrays.asList('ť', 'ţ', 'ŧ')), "t");
            put(new ArrayList<Character>(Arrays.asList('þ')), "th");
            put(new ArrayList<Character>(Arrays.asList('ú', 'ù', 'û', 'ŭ', 'ū', 'ũ', 'ů', 'ų', 'ű')), "u");
            put(new ArrayList<Character>(Arrays.asList('ü')), "ue");
            put(new ArrayList<Character>(Arrays.asList('ŵ')), "w");
            put(new ArrayList<Character>(Arrays.asList('ý', 'ŷ', 'ÿ')), "y");
            put(new ArrayList<Character>(Arrays.asList('ź', 'ż', 'ž')), "z");
    	}
    };
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CodFis window = new CodFis();
					window.frmMain.setVisible(true);
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) { e.printStackTrace(); }
			}
		});
	}
	
	public CodFis() throws ParseException { initialize(); }
	
	private void initialize() {
		frmMain = new JFrame();
		frmMain.setBounds(100, 100, 440, 277);
		frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMain.setResizable(false);
		frmMain.setIconImage(new ImageIcon(this.getClass().getResource("iconcf.png")).getImage());
		frmMain.getContentPane().setLayout(null);
		frmMain.setUndecorated(true);
		frmMain.setLocationRelativeTo(null);
		frmMain.setShape(new RoundRectangle2D.Double(0, 0, frmMain.getWidth(), frmMain.getHeight(), 20, 20));
		frmMain.addMouseListener(drag);
		frmMain.addMouseMotionListener(drag);
		
		lblMe = new JLabel("di zAlweNy26");
		lblMe.setForeground(new Color(15, 45, 85));
		lblMe.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		lblMe.setBounds(115, 57, 95, 20);
		frmMain.getContentPane().add(lblMe);
		
		lblProvincia = new JLabel();
		lblProvincia.setForeground(Color.BLACK);
		lblProvincia.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		lblProvincia.setBounds(80, 210, 40, 20);
		frmMain.getContentPane().add(lblProvincia);
		
		lblDDS = new JLabel("##/##/####");
		lblDDS.setForeground(Color.BLACK);
		lblDDS.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
		lblDDS.setBounds(340, 95, 95, 20);
		frmMain.getContentPane().add(lblDDS);
		
		lblSesso = new JLabel("M");
		lblSesso.setForeground(Color.BLACK);
		lblSesso.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		lblSesso.setBounds(415, 150, 15, 20);
		frmMain.getContentPane().add(lblSesso);
		
		fieldCodFis = new JTextField();
		fieldCodFis.setForeground(Color.BLACK);
		fieldCodFis.setFont(new Font("Comic Sans MS", Font.ITALIC, 14));
		fieldCodFis.setBounds(80, 100, 160, 20);
		fieldCodFis.setOpaque(false);
		fieldCodFis.setEditable(false);
		fieldCodFis.setHorizontalAlignment(JTextField.CENTER);
		frmMain.getContentPane().add(fieldCodFis);
		
		btnSave = new JButton();
		btnSave.setBounds(250, 100, 20, 20);
		btnSave.setBackground(SystemColor.activeCaption);
		btnSave.setIcon(new ImageIcon(this.getClass().getResource("save.png")));
		frmMain.getContentPane().add(btnSave);
		btnSave.setUI(new ButtonStyles());
		btnSave.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
			if (!fieldCodFis.getText().isEmpty()) {
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				JsonObject jsonObject = new JsonObject();
				JsonObject dati = new JsonObject();
				dati.addProperty("Cognome", StringUtils.capitalize((fields[0].getText())));
				dati.addProperty("Nome", StringUtils.capitalize((fields[1].getText())));
				String sesso = null;
				if (lblSesso.getText() == "M") sesso = "Maschio";
				if (lblSesso.getText() == "F") sesso = "Femmina";
				dati.addProperty("Sesso", sesso);
				dati.addProperty("Luogo di nascita", WordUtils.capitalize(fields[2].getText()));
				dati.addProperty("Provincia", lblProvincia.getText());
				dati.addProperty("Data di nascita", fieldDDN.getText());
				jsonObject.add(fieldCodFis.getText(), dati);
				String json = gson.toJson(jsonObject);
		        if (!new File("CodFisCalcolati.json").exists()) {
		        	try (BufferedWriter bw = new BufferedWriter(new FileWriter("CodFisCalcolati.json", false))) {
						bw.write(json);
						bw.close();
					} catch (IOException e1) { e1.printStackTrace(); }
				} else {
					try {
						BufferedReader br = new BufferedReader(new FileReader("CodFisCalcolati.json"));
						String jsonString = gson.fromJson(br, JsonElement.class).toString();
						br.close();
						File file = new File("CodFisCalcolati.json");
						file.delete();
						JsonElement jelement = new JsonParser().parse(jsonString);
						JsonObject jobject = jelement.getAsJsonObject();
						jobject.add(fieldCodFis.getText(), dati);
						String resultingJson = gson.toJson(jelement);
						BufferedWriter bw = new BufferedWriter(new FileWriter("CodFisCalcolati.json", false));
						bw.write(resultingJson);
						bw.close();
					} catch (IOException e1) { e1.printStackTrace(); }
				} 
			} else 
				JOptionPane.showMessageDialog(null, "Per favore, calcolare il codice fiscale\nprima di premere questo bottone.", 
					"Codice fiscale mancante !", JOptionPane.ERROR_MESSAGE);
        }});
		
		Integer[] y = {130, 155, 180};
		for(int i = 0; i < 3; i++) {
			fields[i] = new JTextField();
			fields[i].setFont(new Font("Comic Sans MS", Font.ITALIC, 14));
			fields[i].setBounds(80, y[i], 140, 20);
			fields[i].addFocusListener(this);
			((AbstractDocument) fields[i].getDocument()).setDocumentFilter(new FiltriDoc());
			frmMain.getContentPane().add(fields[i]);
		}
		fields[2].setName("cdn");
		
		MaskFormatter mask = null;
        try { mask = new MaskFormatter("##/##/####"); }
        catch (ParseException e3) { e3.printStackTrace(); }
        mask.setPlaceholderCharacter('_');
		fieldDDN = new JFormattedTextField(mask);
		fieldDDN.setFont(new Font("Comic Sans MS", Font.ITALIC, 16));
		fieldDDN.setBounds(80, 238, 102, 23);
		fieldDDN.addFocusListener(this);
		fieldDDN.setHorizontalAlignment(JTextField.CENTER);
		frmMain.getContentPane().add(fieldDDN);
		
		String[] mf = {"Maschio", "Femmina"};
		for(int i = 0, j = 140; i < 2; i++, j = j + 20) {
			rbtns[i] = new JRadioButton(mf[i]);
			rbtns[i].setForeground(Color.BLACK);
			rbtns[i].setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 14));
			rbtns[i].setBounds(270, j, 85, 20);
			rbtns[i].setOpaque(false);
			frmMain.getContentPane().add(rbtns[i]);
		}
		rbtns[0].setSelected(true);
		rbtns[0].addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { lblSesso.setText("M"); } });
		rbtns[1].addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { lblSesso.setText("F"); } });
		ButtonGroup group = new ButtonGroup();
		group.add(rbtns[0]);
		group.add(rbtns[1]);
		
		btnClose = new JButton();
		btnClose.setName("close");
		btnClose.setBounds(408, 53, 28, 28);
		btnClose.setBackground(SystemColor.activeCaption);
		btnClose.setIcon(new ImageIcon(this.getClass().getResource("imgX.png")));
		frmMain.getContentPane().add(btnClose);
		btnClose.setUI(new ButtonStyles());
		btnClose.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { frmMain.dispose(); } });
		
		btnCalcolo = new JButton("Calcola !");
		btnCalcolo.setForeground(Color.BLACK);
		btnCalcolo.setBackground(SystemColor.activeCaption);
		btnCalcolo.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		btnCalcolo.setBounds(310, 225, 90, 35);
		frmMain.getContentPane().add(btnCalcolo);
		btnCalcolo.setUI(new ButtonStyles());
		btnCalcolo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fields[0].getText().length() < 3 || fields[1].getText().length() < 3 ||
						validDate(fieldDDN.getText()) == false || fields[2].getText().isEmpty() || okPR < 1)
					JOptionPane.showMessageDialog(null, "Per favore, riempire tutti i campi correttamente !", "Dati mancanti !", JOptionPane.ERROR_MESSAGE);
				else {
					int i, j = 0, cons = 0, csnm = 0, qcs = 1, ris = 0;
					char[] codfis = new char[16];
					String cgm = fields[0].getText().toLowerCase(), nm = fields[1].getText().toLowerCase(), ddn = fieldDDN.getText();
					nm = StringUtils.remove(nm, '\'');
					nm = StringUtils.remove(nm, ' ');
					cgm = StringUtils.remove(cgm, '\'');
					cgm = StringUtils.remove(cgm, ' ');
			        int lcgm = cgm.length(), ldn = ddn.length(), lnm = nm.length();
			        for (List<Character> key : accwords.keySet()) {
			        	if (key.contains(cgm.charAt(0)) == true) {
			        		System.out.println(key.get(key.indexOf(cgm.charAt(0))));
			        		//System.out.println(accwords.get(key.indexOf(cgm.charAt(0))));
			        		return;
			        	}
			        	//if (key.contains(cgm.charAt(0)) == true) cgm.replace(cgm.charAt(0), accwords.get(key.get(key.indexOf(cgm.charAt(0)))).charAt(0));
			        }
			        //for(i = 0; i < lcgm; i++) if (accwords.get(cgm.charAt(i)) != null) cgm.replace(cgm.charAt(i), accwords.get(cgm.charAt(i)).charAt(0));
			        //for(i = 0; i < lnm; i++) if (accwords.get(nm.charAt(i)) != null) nm.replace(nm.charAt(i), accwords.get(nm.charAt(i)).charAt(0));
			        for (i = 0; i < lcgm; i++) { // INIZIO CALCOLO COGNOME COD. FIS. CARATTERI 0 A 2
			            if (cgm.charAt(i) != 'a' && cgm.charAt(i) != 'e' && cgm.charAt(i) != 'i' && cgm.charAt(i) != 'o' && cgm.charAt(i) != 'u' ) {
			            	codfis[j] = Character.toUpperCase(cgm.charAt(i));
			            	j++;
			            	cons++;
			            }
			            if (cons == 3) break;
			        }
			        if (cons == 2) {
			        	for (i = 0; i < lcgm; i++) {
			        		if (cgm.charAt(i) != 'a' && cgm.charAt(i) != 'e' && cgm.charAt(i) != 'i' && cgm.charAt(i) != 'o' && cgm.charAt(i) != 'u') {
			        			codfis[2] = Character.toUpperCase(cgm.charAt(i));
			        			break;
			        		}
			        	}
			        }
			        if (cons == 1) {
			        	j = 1;
			        	for (i = 0; i < lcgm; i++) {
			        		 if (j == 3) break;
			        		 if (cgm.charAt(i) != 'a' && cgm.charAt(i) != 'e' && cgm.charAt(i) != 'i' && cgm.charAt(i) != 'o' && cgm.charAt(i) != 'u') {
			        			 codfis[j] = Character.toUpperCase(cgm.charAt(i));
			        			 j++;
			        		 }
			        	}
			        	if (j == 2) codfis[2] = 'X';
			        }
			        if (cons == 0) {
			        	j = 0;
			        	for (i = 0; i < lcgm; i++) {
			        		 if (j == 2) break;
			        		 if (cgm.charAt(i) != 'a' && cgm.charAt(i) != 'e' && cgm.charAt(i) != 'i' && cgm.charAt(i) != 'o' && cgm.charAt(i) != 'u') {
			        			 codfis[j] = Character.toUpperCase(cgm.charAt(i));
			        			 j++;
			        		 }
			        	}
			        	codfis[2] = 'X';
			        } // FINE CALCOLO COGNOME COD. FIS. CARATTERI 0 A 2
			        cons = 0; // INIZIO CALCOLO NOME COD. FIS. CARATTERI 3 A 5
				    for(i = 0; i < lnm; i++)
				    	if (nm.charAt(i) != 'a' && nm.charAt(i) != 'e' && nm.charAt(i) != 'i' && nm.charAt(i) != 'o' && nm.charAt(i) != 'u') csnm++;
				    j = 3;
				    for(i = 0; i < lnm; i++) {
				    	if (nm.charAt(i) != 'a' && nm.charAt(i) != 'e' && nm.charAt(i) != 'i' && nm.charAt(i) != 'o' && nm.charAt(i) != 'u') {
			                if (csnm >= 4) {
			                    if (qcs == 2) qcs++;
			                    else {
			                        codfis[j] = Character.toUpperCase(nm.charAt(i));
			                        qcs++;
			                        j++;
			                        cons++;
			                    }
			                } else {
			                    codfis[j] = Character.toUpperCase(nm.charAt(i));
			                    j++;
			                    cons++;
			                }
				        }
				        if (cons == 3) break;
				    }
				    if (cons == 2) {
				        for(i = 0; i < lnm; i++){
				        	if (nm.charAt(i) != 'a' && nm.charAt(i) != 'e' && nm.charAt(i) != 'i' && nm.charAt(i) != 'o' && nm.charAt(i) != 'u') {
			                    codfis[5] = Character.toUpperCase(nm.charAt(i));
			                    break; 
				            }
				        }
				    }
				    if (cons == 1) {
				        j = 4;
				        for(i = 0; i < lnm; i++){
				            if (j == 6) break;
				            if (nm.charAt(i) != 'a' && nm.charAt(i) != 'e' && nm.charAt(i) != 'i' && nm.charAt(i) != 'o' && nm.charAt(i) != 'u') {
				                codfis[j] = Character.toUpperCase(nm.charAt(i));
				                j++;    
				            }
				        }
				        if (j == 5) codfis[5] = 'X';
				    }
				    if (cons == 0) {
				        j = 3;
				        for(i = 0; i < lnm; i++){
				            if (j == 5) break;
				            if (nm.charAt(i) != 'a' && nm.charAt(i) != 'e' && nm.charAt(i) != 'i' && nm.charAt(i) != 'o' && nm.charAt(i) != 'u') {
				                codfis[j] = Character.toUpperCase(nm.charAt(i));
				                j++;    
				            }
				        }
				        codfis[5] = 'X';
				    } // FINE CALCOLO NOME COD. FIS. CARATTERI 3 A 5
			        codfis[6] = ddn.charAt(ldn-2); // INIZIO CALCOLO ANNO COD. FIS. CARATTERI 6 A 7
			        codfis[7] = ddn.charAt(ldn-1); // FINE CALCOLO ANNO COD. FIS. CARATTERI 6 A 7
			        char[] arrms = {'A','B','C','D','E','H','L','M','P','R','S','T'}; // INIZIO CALCOLO MESE COD. FIS. CARATTERE 8
			        if (ddn.charAt(3) == '0') codfis[8] = arrms[(ddn.charAt(4) - '0') - 1];
			        else if (ddn.charAt(3) == '1') codfis[8] = arrms[(ddn.charAt(4) - '0') + 9]; // FINE CALCOLO MESE COD. FIS. CARATTERE 8
			        if (rbtns[0].isSelected()) codfis[9] = ddn.charAt(0); // INIZIO CALCOLO SESSO COD. FIS. CARATTERI 9 A 10
			        else if (rbtns[1].isSelected()) codfis[9] = (char)(((ddn.charAt(0) - '0') + 4) + '0');
			        codfis[10] = ddn.charAt(1); // FINE CALCOLO SESSO COD. FIS. CARATTERI 9 A 10
			        try { // INIZIO RICERCA CODICE CATASTALE COMUNE CARATTERI 11 A 14
			        	JSONArray json = null;
		        		InputStreamReader reader = new InputStreamReader(this.getClass().getResourceAsStream("comuni.json"));
		        		JSONParser jsonParser = new JSONParser();
		        		json = (JSONArray) jsonParser.parse(reader);
		        		reader.close();
			            JSONObject person = null;
			            for (Object o : json) {
			            	person = (JSONObject) o;
			            	String nome = (String) person.get("nome");
			            	if (nome.toLowerCase().equals(fields[2].getText().toLowerCase())) {
			            		String cc = (String) person.get("codiceCatastale");
			            		codfis[11] = cc.charAt(0);
			                	codfis[12] = cc.charAt(1);
			                	codfis[13] = cc.charAt(2);
			                	codfis[14] = cc.charAt(3);
			            		break;
			            	}
			            }
			        }
			        catch (MalformedURLException e2) { System.out.println("Errore nella lettura dell'URL !"); } 
			        catch (org.json.simple.parser.ParseException e1) { System.out.println("Errore nella lettura json del file !"); } 
			        catch(IOException ex) { System.out.println("Errore di lettura del file !"); } // FINE RICERCA CODICE CATASTALE COMUNE CARATTERI 11 A 14
			        if (codfis[11] == 0 && codfis[12] == 0 && codfis[13] == 0 && codfis[14] == 0) {
			        	lblProvincia.setText("N.D.");
			        	JOptionPane.showMessageDialog(null, "Il comune inserito non è stato trovato !", "Comune non trovato !", JOptionPane.ERROR_MESSAGE);
			        	return;
			        }
			        Map<Character,Integer> valncp = new HashMap<Character,Integer>() { // INIZIO CALCOLO CODICE DI CONTROLLO COD. FIS. CARATTERE 15
			        	private static final long serialVersionUID = 1L; {
			            put('A', 0); put('B', 1); put('C', 2); put('D', 3); put('E', 4); put('F', 5);
			            put('G', 6); put('H', 7); put('I', 8); put('J', 9); put('K', 10); put('L', 11);
			            put('M', 12); put('N', 13); put('O', 14); put('P', 15); put('Q', 16); 
			            put('R', 17); put('S', 18); put('T', 19); put('U', 20); put('V', 21);
			            put('W', 22); put('X', 23); put('Y', 24); put('Z', 25);
			        }};
			        Map<Character,Integer> valncd = new HashMap<Character,Integer>() {
			        	private static final long serialVersionUID = 1L; {
			            put('A', 1); put('B', 0); put('C', 5); put('D', 7); put('E', 9); put('F', 13);
			            put('G', 15); put('H', 17); put('I', 19); put('J', 21); put('K', 2); put('L', 4);
			            put('M', 18); put('N', 20); put('O', 11); put('P', 3); put('Q', 6); 
			            put('R', 8); put('S', 12); put('T', 14); put('U', 16); put('V', 10);
			            put('W', 22); put('X', 25); put('Y', 24); put('Z', 23);
			        }};
			        Map<Character,Integer> valnnd = new HashMap<Character,Integer>() {
			        	private static final long serialVersionUID = 1L; {
			            put('0', 1); put('1', 0); put('2', 5); put('3', 7); put('4', 9); put('5', 13);
			            put('6', 15); put('7', 17); put('8', 19); put('9', 21);
			        }};
			        ris = valncd.get(codfis[0]) + valncp.get(codfis[1]) + valncd.get(codfis[2]) + valncp.get(codfis[3]) + 
			        	valncd.get(codfis[4]) + valncp.get(codfis[5]) + valnnd.get(codfis[6]) + ((int)codfis[7] - (int)'0') + 
			        	valncd.get(codfis[8]) + ((int)codfis[9] - (int)'0') + valnnd.get(codfis[10]) + valncp.get(codfis[11]) + 
			        	valnnd.get(codfis[12]) + ((int)codfis[13] - (int)'0') + valnnd.get(codfis[14]);
			    	char[] car = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
			        codfis[15] = car[ris % 26]; // FINE CALCOLO CODICE DI CONTROLLO COD. FIS. CARATTERE 15
			        fieldCodFis.setText(new String(codfis));
			        fieldCodFis.requestFocusInWindow();
			        fieldCodFis.selectAll();
			        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(fieldCodFis.getText()), null);
				}
			}
		});
	
		lblImage = new JLabel();
		lblImage.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("imgcodfis.png")).getImage()));
		lblImage.setBounds(0, 0, 440, 277);
		lblImage.setOpaque(false);
		frmMain.getContentPane().add(lblImage);
	}
	
	@Override
	public void focusGained(FocusEvent fe) { fieldCodFis.setText(null); }
	
	@Override
	public void focusLost(FocusEvent fe) {
		if (fe.getComponent().getName() == "cdn" && fields[2].getText().length() >= 2) {
			String txtProvincia = null;
			try {
				JSONArray json = null;
	    		InputStreamReader reader = new InputStreamReader(this.getClass().getResourceAsStream("comuni.json"));
	    		JSONParser jsonParser = new JSONParser();
	    		json = (JSONArray) jsonParser.parse(reader);
	    		reader.close();
	            JSONObject person = null;
	            for (Object o : json) {
	            	person = (JSONObject) o;
	            	String nome = (String) person.get("nome");
	            	if (nome.toLowerCase().equals(fields[2].getText().toLowerCase())) {
	            		txtProvincia = (String) person.get("sigla");
	            		okPR++;
	            		break;
	            	}
	            }
	        }
	        catch(FileNotFoundException ex) { System.out.println("Impossibile aprire il file !"); }
	        catch(IOException ex) { System.out.println("Errore di lettura del file !"); }
	        catch (org.json.simple.parser.ParseException e1) {  System.out.println("Errore nella lettura json del file !"); } 
			if (okPR >= 1) lblProvincia.setText(txtProvincia);
			else lblProvincia.setText("N.D.");
		}
	}
	
	public boolean validDate(String date) {
		String[] parts = date.split("/");
		Integer[] monthLength = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		if (date.length() < 10) return false;
		int day = Integer.parseInt(parts[0]), month = Integer.parseInt(parts[1]), year = Integer.parseInt(parts[2]);
		int curr = Calendar.getInstance().get(Calendar.YEAR);
		if (year % 400 == 0 || (year % 100 != 0 && year % 4 == 0)) monthLength[1] = 29;
		if (year < 1900 || year > curr || month <= 0 || month > 12) return false;
		if (day < 1 || day > monthLength[month - 1]) return false;
		return true;
	}
}