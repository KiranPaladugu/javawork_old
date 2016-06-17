import java.awt.Button;
import java.awt.Color;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class SplitterPanel extends Panel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Label file;
	private Label outFolder,out;
	private Button browse,change,split,cancel,exit;
	private Label toSize;
	private TextField size,in_file;
	private File selectedFile;

	public SplitterPanel() {
		super();
		init();
	}

	private void init() {
		file= new Label("File :");
		outFolder = new Label("Out Folder");
		out = new Label();
		toSize = new Label("Size of file:");
		size = new TextField("3");
		browse = new Button("Browse");
		change = new Button("Change");
		in_file = new TextField();
		change = new Button("Change");
		split=new Button("Split");
		cancel = new Button("Cancel");
		exit = new Button("Exit");
		this.setBackground(Color.LIGHT_GRAY);
		initPosition();
		initActions();
	}

	private void initActions() {
		in_file.setEditable(false);
		in_file.setFocusable(false);
		browse.requestFocusInWindow();	
		change.setEnabled(false);
		browse.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChoosed = new JFileChooser();				
				int x = fileChoosed.showDialog(null, "Open");
				if (x == JFileChooser.APPROVE_OPTION) {
					selectedFile = fileChoosed.getSelectedFile();
					updateFileText(selectedFile);
				}
			}
		});
		change.addActionListener(new ActionListener() {
			
			@SuppressWarnings("unused")
			private File selectedFolder;

			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChoosed = new JFileChooser();				
				int x = fileChoosed.showDialog(null, "Select");
				fileChoosed.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (x == JFileChooser.APPROVE_OPTION) {
					selectedFolder = fileChoosed.getSelectedFile();
					//updateFileText(selectedFolder);
				}
			}
		});
		exit.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		split.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int fSize = getFileSize();
				if ((fSize != 0) && (fSize < 0)) {
					JOptionPane.showMessageDialog(null, " Size Should be greater than '0'", "ERROR !! ", JOptionPane.ERROR_MESSAGE);
				} else {
					if (fSize > 25) {
						JOptionPane.showMessageDialog(null, "size cannot be handled.. ");
					} else {
						Splitter splitter =new Splitter(selectedFile, fSize);
						splitter.startSlit();
						String msg = "DONE with Success ..\n\n";
						msg += "Total Files :"+splitter.getPages()+"\n";
						msg += "Total bytes Written : "+splitter.getBytesWriten();
						msg += "\nOut path : "+splitter.getPath() ;
						JOptionPane.showMessageDialog(null, msg);
					}				
				}
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				selectedFile = null;
				in_file.setText("");
			}
		});
	}
	
	

	protected int getFileSize() {
		int x = -1;
		try {
			x = Integer.parseInt(size.getText());
		} catch (NumberFormatException ne) {
			JOptionPane.showMessageDialog(null, "Size should be in Numbers only !!");
			x = -1;
		}
		return x;
	}

	protected void updateFileText(File selectedFile) {
		in_file.setText(selectedFile.getAbsolutePath());
		out.setText(new Splitter(selectedFile).getPath());
		
	}

	private void initPosition() {
		this.setLayout(null);
		int x_start =70;
		int x_inc = 5;
		int x_label_width = 70;
		int y_start=50;
		int y_height = 25;
		int y_inc = 35;
		int x_text_width = 150 ;
		int x_button_width=100 ;
		int x_mm=35;
		
		file.setBounds(x_start, y_start, x_label_width, y_height);
		in_file.setBounds(x_start+x_label_width+x_inc, y_start, x_text_width, y_height);		
		browse.setBounds(x_start+x_label_width+x_inc+x_text_width+x_inc, y_start, x_button_width, y_height);	
		toSize.setBounds(x_start, y_start+y_inc, x_label_width, y_height);
		size.setBounds(x_start+x_label_width+x_inc, y_start+y_inc, 75, y_height);
		outFolder.setBounds(x_start, y_start+y_inc+y_inc, x_label_width, y_height);
		out.setBounds(x_start, y_start+y_inc+y_inc+y_inc, 300, y_height);
		change.setBounds(x_start+x_label_width+x_text_width+x_inc+x_inc, y_start+y_inc+y_inc, x_button_width, y_height);
		
		split.setBounds(x_mm+x_start, 200, 75, y_height);
		cancel.setBounds(x_mm+x_start+x_inc+75, 200, 75, y_height);
		exit.setBounds(x_mm+x_start+x_inc+75+75+x_inc, 200, 75, y_height);
		
		this.add(file);
		this.add(in_file);
		this.add(browse);
		this.add(toSize);
		this.add(size);
		this.add(outFolder);
		this.add(out);
		this.add(change);
		this.add(split);
		this.add(cancel);
		this.add(exit);
	}	
}
