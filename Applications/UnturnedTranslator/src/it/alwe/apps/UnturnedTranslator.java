package it.alwe.apps;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.JEditorPane;
import javax.swing.SwingConstants;

public class UnturnedTranslator {
	private JFrame frame;
	private JButton btnDirChoose, btnStart, btnSave, btnOpenDir;
	private JTextField txtDir, txtFile;
	private JLabel lblChoose, lblFile, lblSave;
	private JTree Dirs;
	private JScrollPane sp;
	private JEditorPane editPane;
	private Font base = new Font("Comic Sans MS", Font.PLAIN, 12);
	private Font bold = new Font("Comic Sans MS", Font.BOLD, 14);
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try { new UnturnedTranslator(); }
				catch (Exception e) { e.printStackTrace(); }
			}
		});
	}

	public UnturnedTranslator() { initialize(); }

	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 750, 650);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Unturned Translator");
		frame.setResizable(false);
		frame.setVisible(true);
		frame.getContentPane().setLayout(null);
	    
		txtDir = new JTextField(); txtFile = new JTextField();
		txtDir.setFont(base); txtFile.setFont(base);
		txtDir.setBounds(274, 10, 186, 24); txtFile.setBounds(205, 45, 90, 24);
		txtDir.setEditable(false);
		
		lblFile = new JLabel("Nome file tradotto (con .dat) :");
		lblChoose = new JLabel("Cartella contenente i file da tradurre :");
		lblChoose.setFont(base); lblFile.setFont(base);
		lblChoose.setBounds(10, 10, 220, 24); lblFile.setBounds(10, 45, 185, 24);
		
		lblSave = new JLabel();
		lblSave.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSave.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 12));
		lblSave.setBounds(514, 45, 120, 24);
		
 		sp = new JScrollPane();
  	    sp.setBounds(10, 80, 254, 530);
  	    
  	    editPane = new JEditorPane();
		editPane.setBounds(274, 80, 460, 530);
		
		btnOpenDir = new JButton("Apri cartella"); btnSave = new JButton("Salva");
		btnOpenDir.setFont(bold); btnSave.setFont(bold);
		btnOpenDir.setBounds(335, 45, 125, 24); btnSave.setBounds(644, 44, 90, 24);
		
	    btnDirChoose = new JButton(); btnStart = new JButton("Inizia");
	    btnDirChoose.setIcon(new ImageIcon(this.getClass().getResource("open_dir.png")));
	    btnDirChoose.setBounds(240, 10, 24, 24); btnStart.setBounds(470, 9, 90, 24);
	    btnStart.setFont(bold);
	    
        btnDirChoose.addActionListener(new ActionListener() { 
        	public void actionPerformed(ActionEvent e) {
        		JFileChooser chooser = new JFileChooser();
        	    chooser.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
        	    chooser.setDialogTitle("Scegli la cartella");
        	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        	    chooser.setAcceptAllFileFilterUsed(false);
        	    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
        	    	txtDir.setText(chooser.getSelectedFile().toString());
        	}
        });
        
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
        		if (txtDir.getText() != null && Files.exists(Paths.get(txtDir.getText()))) {
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
		frame.getContentPane().add(btnDirChoose); frame.getContentPane().add(btnStart);
		frame.getContentPane().add(lblChoose); frame.getContentPane().add(lblFile);
		frame.getContentPane().add(btnSave); frame.getContentPane().add(lblSave);
		frame.getContentPane().add(btnOpenDir);
	}
}
