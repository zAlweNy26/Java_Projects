package it.alwe.apps;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import org.apache.commons.io.FileUtils;
import com.sun.xml.internal.ws.util.StringUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.Frame;
import java.awt.HeadlessException;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.JEditorPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class UnturnedTranslator {
	private JFrame frame;
	private JButton btnStart, btnSave, btnOpenDir;
	private JButton[] btnsImgs = new JButton[3];
	private RoundJTextField txtDir, txtFile;
	private JLabel lblChoose, lblFile, lblSave;
	private JTree Dirs;
	private JScrollPane sp;
	private JEditorPane editPane;
	public DragListener drag = new DragListener();
	public ImageIcon FrameIcon = new ImageIcon(this.getClass().getResource("icon.png"));
	public Font base = new Font("Comic Sans MS", Font.BOLD, 14);
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try { 
					new UnturnedTranslator(); 
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}
				catch (Exception e) { e.printStackTrace(); }
			}
		});
	}

	public UnturnedTranslator() { initialize(); }

	public void initialize() {
		frame = new JFrame();
		baseFrame(frame, 750, 625);
	    
		txtDir = new RoundJTextField(0); txtFile = new RoundJTextField(0);
		txtDir.setFont(base); txtFile.setFont(base);
		txtDir.setBounds(330, 13, 186, 24); txtFile.setBounds(235, 53, 145, 24);
		txtDir.setEditable(false);
		
		lblFile = new JLabel("Nome file tradotto (con .dat) :");
		lblChoose = new JLabel("Cartella contenente i file da tradurre :");
		lblChoose.setForeground(new Color(0x2dce98)); lblFile.setForeground(new Color(0x2dce98));
		lblChoose.setFont(base); lblFile.setFont(base);
		lblChoose.setBounds(10, 13, 275, 24); lblFile.setBounds(10, 53, 215, 24);
		
		lblSave = new JLabel("", SwingConstants.CENTER);
		lblSave.setFont(base);
		lblSave.setBounds(525, 53, 110, 24);
		
 		sp = new JScrollPane(); editPane = new JEditorPane();
  	    sp.setBounds(15, 90, 250, 520); editPane.setBounds(275, 90, 460, 520);
  	    
  	    Integer[] xImgs = {295, 710, 675}, yImgs = {15, 13, 13}, hImgs = {20, 24, 24};
		String[] imgs = {"open_dir.png", "imgX.png", "imgMin.png"};
		for (int i = 0; i < 3 ; i++) {
			btnsImgs[i] = new JButton(new ImageIcon(this.getClass().getResource(imgs[i])));
			btnsImgs[i].setBounds(xImgs[i], yImgs[i], 24, hImgs[i]);
			btnsImgs[i].setUI(new ButtonStyles());
			btnsImgs[i].setBackground(new Color(41, 41, 41));
			btnsImgs[i].setName("img");
			frame.getContentPane().add(btnsImgs[i]);
		}
		btnsImgs[0].addActionListener(new ActionListener() { 
	      	public void actionPerformed(ActionEvent e) {
	      		JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()) {
						private static final long serialVersionUID = 1L;
						@Override
	      			protected JDialog createDialog(Component parent) throws HeadlessException {
		        			JDialog dialog = super.createDialog( parent );
					        BufferedImage image = null;
							try { image = ImageIO.read(UnturnedTranslator.class.getResourceAsStream("icon.png")); }
							catch (IOException e) { e.printStackTrace(); }
					        dialog.setIconImage(image);
					        return dialog;
	      			}
	      		};
	      	    chooser.setDialogTitle("Scegli la cartella contenente i file da tradurre :");
	      	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	      	    chooser.setAcceptAllFileFilterUsed(false);
	      	    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
	      	    	txtDir.setText(chooser.getSelectedFile().toString());
	      	}
		});
		btnsImgs[1].addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { frame.dispose(); } });
		btnsImgs[2].addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { frame.setState(Frame.ICONIFIED); } });
		
		btnOpenDir = new JButton("Apri cartella"); btnStart = new JButton("Inizia");
		btnOpenDir.setFont(base); btnStart.setFont(base);
		btnOpenDir.setForeground(Color.BLACK); btnStart.setForeground(Color.BLACK);
		btnOpenDir.setBackground(new Color(0x2dce98)); btnStart.setBackground(new Color(0x2dce98));
		btnOpenDir.setUI(new ButtonStyles()); btnStart.setUI(new ButtonStyles());
		btnOpenDir.setBounds(390, 50, 125, 30); btnStart.setBounds(525, 10, 90, 30);
		
	    btnSave = new JButton("Salva");
	    btnSave.setBackground(new Color(0x2dce98));
	    btnSave.setUI(new ButtonStyles());
	    btnSave.setBounds(645, 50, 90, 30);
	    btnSave.setFont(base);
	    btnSave.setForeground(Color.BLACK);
	    btnSave.setVisible(false);
        
        btnOpenDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (txtDir.getText() != null && new File(txtDir.getText()).exists()) {
					try { Desktop.getDesktop().open(new File(txtDir.getText())); }
					catch (IOException e) { e.printStackTrace(); }
				}
			}
        });
        
        btnStart.addActionListener(new ActionListener() { 
        	public void actionPerformed(ActionEvent e) { 
        		if (txtDir.getText().length() > 0 && Files.exists(Paths.get(txtDir.getText()))) {
        			File root = new File(txtDir.getText());
	    	        String[] extensions = {"dat"};
	    	        boolean recursive = true;
	    	        Collection<?> files = FileUtils.listFiles(root, extensions, recursive);
	    	        for (Iterator<?> iterator = files.iterator(); iterator.hasNext();) {
	    	        	File file = (File) iterator.next();
	    	        	if (file.getName().equals("English.dat") && txtFile.getText() != null) {
	    	        		Path sourceDir = Paths.get(file.getAbsolutePath());
	    	        		Path targetDir = Paths.get(file.getAbsolutePath()
	    	        				.replaceAll("\\bEnglish.dat\\b", StringUtils.capitalize(txtFile.getText())));
	    	        		if (!new File(targetDir.toString()).exists()) {
	    	        			try { Files.copy(sourceDir, targetDir); } 
		    	        		catch (IOException e1) { e1.printStackTrace(); }
	    	        		}
	    	        	}
	    	        }
	    	        Dirs = new JTree(new FileSystemModel(root));
	    	      	Dirs.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    	      	Dirs.addTreeSelectionListener(new TreeSelectionListener() {
		    	  	      public void valueChanged(TreeSelectionEvent e) {
		    	  	    	  File file = (File) Dirs.getLastSelectedPathComponent();
		    	  	    	  if (file.isFile()) {
		    	  	    		  lblSave.setForeground(Color.RED);
			    	  	    	  lblSave.setText("Non salvato !");
		    	  	    		  editPane.setEditable(true);
		    	  	    		  try { editPane.setPage(file.toURI().toURL()); }
			    	  	    	  catch (IOException e1) { e1.printStackTrace(); }
		    	  	    		  btnSave.setVisible(true);
		    	  	    	  } else {
		    	  	    		  lblSave.setText("");
		    	  	    		  editPane.setText(null);
		    	  	    		  editPane.setEditable(false);
		    	  	    	  }
		    	  	      }
		    	  	});
	    	      	sp.setViewportView(Dirs);
        		}
        	}
        });
        
        btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Dirs.getLastSelectedPathComponent() != null && editPane.getText() != null) {
					lblSave.setForeground(Color.GREEN);
					lblSave.setText("Salvato !");
					try {
						FileWriter writer = new FileWriter(Dirs.getSelectionPath().toString()
  	  	    				  .replace("[", "").replace("]", "").replace(", ", "\\"));
						writer.write(editPane.getText());
					    writer.close();
					} catch (IOException e1) { e1.printStackTrace(); } 
				}
			}
        });
        
        frame.getContentPane().add(sp); frame.getContentPane().add(editPane);
		frame.getContentPane().add(txtDir); frame.getContentPane().add(txtFile);
		frame.getContentPane().add(lblChoose); frame.getContentPane().add(lblFile);
		frame.getContentPane().add(btnSave); frame.getContentPane().add(lblSave);
		frame.getContentPane().add(btnOpenDir); frame.getContentPane().add(btnStart);
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
}
