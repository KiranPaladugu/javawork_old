package myUtils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.framework.gui.view.ConfigValue;
import com.framework.gui.view.LayoutMan;
import com.framework.utils.FootPrint;
import com.framework.utils.License;
import com.framework.utils.LicenseImpl;
import com.framework.utils.LicenseReader;
import com.tcs.berReader.gui.ApplicationContext;

public class LicGui extends LicenseReader implements ActionListener {

	private JLabel lbl_name,lbl_fp,lbl_licType;
	private JTextField txt_name,txt_fp;
	private JComboBox combo;
	private String path="C:\\Users\\ekirpal\\Documents";
	
	
	private File generate(String password,String name,String fp, int type) {
		File rF = null;
		String footPrint = fp;//FootPrint.getFootPrint();
		if(footPrint== null || footPrint.trim().length()==0){
			footPrint =FootPrint.getFootPrint();
		}
		int version = 10;
		LicenseImpl license = new LicenseImpl(footPrint, type, version);
		license.setName(name);
		byte[] key = PASSWORD.getBytes();
		String serial = footPrint + license.getLicenseType() + version;
		license.setSerial(serial);
		try {
			DESKeySpec desKeySpec = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(SECRET_KEY);
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

			// Create Cipher
			Cipher desCipher = Cipher.getInstance(CIPHER_NAME);
			desCipher.init(Cipher.ENCRYPT_MODE, secretKey);
			File file = new File(path + "\\"+"License_NEW_" + footPrint + ".license");
			FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			CipherOutputStream cos = new CipherOutputStream(bos, desCipher);
			ObjectOutputStream oos = new ObjectOutputStream(cos);
			oos.writeObject(license);
			oos.flush();
			oos.close();
			rF = new File(path + "\\"+"License_NEW_" + footPrint + ".license");
			System.out.println("License Generated Successfully....  for footprint: " + footPrint);
			System.out.println("\n@\n"+rF.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rF;
	}

	public void showGUI(){
		
		final String windowsTheme = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		try {
			UIManager.setLookAndFeel(windowsTheme);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		
		JDialog dialog = new JDialog();
		dialog.setTitle("BERViewer License Generator!");
		LayoutMan layMan = new LayoutMan();
		dialog.setLayout(null);		
		lbl_name = new JLabel("Name :");
		lbl_fp = new JLabel("FingerPrint :");
		lbl_licType = new JLabel("License Type :");
		JButton generate,cancel,close;
		
		txt_name = new JTextField();
		txt_fp = new JTextField();
		combo = new JComboBox();
		combo.addItem("EVOLUTION");
		combo.addItem("FULL");
		combo.addItem("UNLIMITED");
		combo.addItem("LIMITED");
		generate = new JButton("Generate");
		generate.addActionListener(this);
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		close = new JButton("Close");
		close.addActionListener(this);
		JPanel view = new JPanel();
		LayoutMan viewMan = new LayoutMan();
		view.setLayout(null);
		viewMan.setY(10);
		viewMan.setX_gap(5);
		
		viewMan.setX(5);
		lbl_name.setBounds(viewMan.getBounds(125, 25,LayoutMan.LAYOUTMAN_VERTICAL_ALIGN));
		txt_name.setBounds(viewMan.getBounds(250, 25,LayoutMan.LAYOUTMAN_HORIZANTAL_ALIGN));
		viewMan.setX(5);
		lbl_fp.setBounds(viewMan.getBounds(125, 25,LayoutMan.LAYOUTMAN_VERTICAL_ALIGN));
		txt_fp.setBounds(viewMan.getBounds(250, 25,LayoutMan.LAYOUTMAN_HORIZANTAL_ALIGN));
		viewMan.setX(5);
		lbl_licType.setBounds(viewMan.getBounds(125, 25,LayoutMan.LAYOUTMAN_VERTICAL_ALIGN));		
		combo.setBounds(viewMan.getBounds(200, 25,LayoutMan.LAYOUTMAN_HORIZANTAL_ALIGN));
		
		view.add(lbl_licType);
		view.add(txt_fp);
		view.add(lbl_fp);
		view.add(txt_name);
		view.add(lbl_name);
		view.add(combo);
		view.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		JPanel panel = new JPanel();
		LayoutMan panMan = new LayoutMan();
		panel.setLayout(null);
		panMan.setX(20);
		generate.setBounds(panMan.getBounds(120, 30,LayoutMan.LAYOUTMAN_HORIZANTAL_ALIGN));
		panel.add(generate);
		cancel.setBounds(panMan.getBounds(120, 30,LayoutMan.LAYOUTMAN_HORIZANTAL_ALIGN));
		panel.add(cancel);
		close.setBounds(panMan.getBounds(120, 30,LayoutMan.LAYOUTMAN_HORIZANTAL_ALIGN));
		panel.add(close);
		
		layMan.setY(50);
		layMan.setX(50);
		view.setBounds(layMan.getBounds(400, 100,LayoutMan.LAYOUTMAN_VERTICAL_ALIGN));
		layMan.setX(50);
		layMan.setY(layMan.getY()+35);
		panel.setBounds(layMan.getBounds(400, 50,LayoutMan.LAYOUTMAN_VERTICAL_ALIGN));
		
		dialog.add(view);
		dialog.add(panel);
		
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 500;
		int height = 300;
		int x = (dim.width - width) / 2;		
		int y = (dim.height - height) / 2;
		dialog.setBounds(x, y, width, height);
		dialog.setSize(width, height);
		
		dialog.setModal(true);
		dialog.setResizable(false);
		
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);		
	}

	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() instanceof JButton){
			JButton btn = (JButton) ae.getSource();
			if(btn.getText().equals("Generate")){
				System.out.println("Generate Called");
				String name = txt_name.getText().trim();
				String fp = txt_fp.getText().trim().toUpperCase();
				if(name.length()<8){
					JOptionPane.showMessageDialog(null,"Name should be 8 charecters" , "ERROR!", JOptionPane.ERROR_MESSAGE);
					return;
				}else if(fp.length()<4){
					JOptionPane.showMessageDialog(null,"Invalid FingerPrint", "ERROR!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				File file = generate("None", name,fp, combo.getSelectedIndex());
				if(file != null ){
				JOptionPane.showMessageDialog(null, "Generated SuccessFully\n@\n"+file.getAbsolutePath(), "SUCESS", JOptionPane.INFORMATION_MESSAGE);
				}else{
					JOptionPane.showMessageDialog(null,"Generation Failed \nTry again...", "ERROR!", JOptionPane.ERROR_MESSAGE);
				}
			}else if(btn.getText().equals("Cancel")){
				System.out.println("Cancel Called");
				reset();
			}else if(btn.getText().equals("Close")){
				System.out.println("Close Called");
				System.exit(0);
			}
		}		
	}
	
	public void reset(){
		txt_fp.setText("");
		txt_name.setText("");
	}
	
	
	public License readLicense(String path) {
		File file = null;
		if(path == null || path.trim().equals("")){
			path = this.path;
			
		}
		License license = null;
		String footPrint = FootPrint.getFootPrint();		
		byte[] key = PASSWORD.getBytes();

		try {
			DESKeySpec desKeySpec = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(SECRET_KEY);
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

			// Create Cipher
			Cipher desCipher = Cipher.getInstance(CIPHER_NAME);
			desCipher.init(Cipher.DECRYPT_MODE, secretKey);
			Object object =  ApplicationContext.getApplicationConfiguration().getObject("License.location");
			if(object == null){
				return null;				
			}else{
				ConfigValue value = (ConfigValue) object;
				file = (File)value.getObject();
			}
			if(file== null){
				new File(path + "License_NEW_" + footPrint + ".license");
			}
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			CipherInputStream cis = new CipherInputStream(bis, desCipher);
			ObjectInputStream ois = new ObjectInputStream(cis);

			Object obj = ois.readObject();
			ois.close();

			license = (License) obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return license;
	}
	
	public static void main(String args[]){
		new LicGui().showGUI();
	}
}
