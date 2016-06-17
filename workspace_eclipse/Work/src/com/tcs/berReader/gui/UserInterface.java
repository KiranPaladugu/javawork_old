package com.tcs.berReader.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.UIManager;

import com.framework.gui.view.AppConfig;
import com.framework.gui.view.ConfigValue;
import com.framework.reg.Register;
import com.framework.reg.RegisterException;
import com.framework.utils.FootPrint;
import com.framework.utils.License;
import com.framework.utils.LicenseReader;
import com.logService.Logger;
import com.tcs.ber.resource.PropertyFinder;
import com.tcs.ber.resource.ResourceFinder;

public class UserInterface {

	private JFrame frame;
	private UserOperations operations;
	private UserMenuBar menu;
	private UserToolbar toolbar;
	private Contoller control;
	private ResultPane result;
	private StatusPane status;
	private GridBagConstraints gc;

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public UserInterface() {
		initTheme();
		loadConfiguraion();
		Register.register(this);

		gc = new GridBagConstraints();
		gc.insets = new Insets(1, 1, 1, 1);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.fill = GridBagConstraints.BOTH;
		gc.gridwidth = GridBagConstraints.REMAINDER;

		operations = (UserOperations) Register.getObject(UserOperations.class);
		if (operations == null) {
			operations = new UserOperations();
			Register.register(operations);
		}		
		checkLicense();
		init();
		startAnimation();
	}

	private void startAnimation() {
		new Animation(getFrame());		
	}

	private void loadConfiguraion() {
		try {
			AppConfig config = ApplicationContext.getApplicationConfiguration();
			if (config == null) {
				config = loadNewConfig();
			}
			ApplicationContext.getApplicationProfile();
			Register.oneTimeRegister(config);
		} catch (Exception e) {
			Logger.log("Unable to Load Cconfiguration!");
		}

	}

	private AppConfig loadNewConfig() {
		AppConfig config = new AppConfig();
		return config;

	}

	private void checkLicense() {
		if (!isLicenseRequired()) {
			return;
		}
		boolean isFpRequired = true;
		LicenseReader reader = new LicenseReader();
		String fingerPrint = FootPrint.getFootPrint();
		License lic = reader.readLicense(null);
		if (lic != null) {
			if (!isFpRequired()) {
				isFpRequired = true;
			} else {
				isFpRequired = FootPrint.isValidFootPrint(lic.getFootPrint());
			}
			if (!lic.isExpired() && isFpRequired) {

				if (lic.isEvalutionLicense()) {
					MessageHandler.displayInfoMessage("You are using Evalution License\nLicense will be exipred in "
							+ lic.getRemainingDays() + " days!\n your FingerPrint : " + FootPrint.getFootPrint());
					Register.register(License.class.getClass(), lic);
				} else {
					if (lic.getLicenseType() == License.FULL_TYPE || lic.getLicenseType() == License.LIMITED_TYPE
							|| lic.getLicenseType() == License.UNLIMITED_TYPE) {
						Register.register(License.class.getClass(), lic);
						Logger.log("Using License Type :" + lic.getLicenseType());
					} else {
						MessageHandler
								.displayErrorMessage("License Type is Not valid for your FingerPrint!!\nYour FingerPrint : "
										+ fingerPrint);
						System.out.println("your Finger Print is :" + fingerPrint);
						Logger.log("{3$}Need License for Finger Print:" + fingerPrint);
						operations.loadLicenseOperation();
						operations.applicationExit(false);
					}
				}
			} else {
				MessageHandler.displayErrorMessage("License is Not valid for your FingerPrint!!\nYour FingerPrint : "
						+ fingerPrint);
				System.out.println("your Finger Print is :" + fingerPrint);
				Logger.log("{2$}Need License for Finger Print:" + fingerPrint);
				operations.loadLicenseOperation();
				operations.applicationExit(false);
			}
		} else {
			MessageHandler.displayErrorMessage("License Not Found!\nApplication will be exited..\nYour FingerPrint : "
					+ fingerPrint);
			System.out.println("your Finger Print is :" + fingerPrint);
			Logger.log("{1$}Need License for Finger Print:" + fingerPrint);
			operations.loadLicenseOperation();
			operations.applicationExit(false);
		}
	}

	private boolean isFpRequired() {
		boolean flag = true;
		try {
			AppConfig config = ApplicationContext.getApplicationConfiguration();
			Object obj = config.getObject("Application.License.check.fingerPrintVerificationRequired");
			if (obj != null) {
				ConfigValue value = (ConfigValue) obj;
				Boolean bool = (Boolean) value.getObject();
				flag = bool.booleanValue();
			}
		} catch (Exception e) {
		}
		return flag;
	}

	private boolean isLicenseRequired() {
		boolean flag = true;
		try {
			AppConfig config = ApplicationContext.getApplicationConfiguration();
			Object obj = config.getObject("Application.License.check");
			if (obj != null) {
				ConfigValue value = (ConfigValue) obj;
				Boolean bool = (Boolean) value.getObject();
				flag = bool.booleanValue();
			}
		} catch (Exception e) {
		}
		return flag;
	}

	private void init() {
		initFrame();
		initMenuBar();
		initToolbar();
		initController();
		initMiddle();
		initStatus();
		setFrameLocation();
		setVisibility();
	}

	private void initToolbar() {
		toolbar = (UserToolbar) Register.getObject(UserToolbar.class);
		if (toolbar == null) {
			toolbar = new UserToolbar();
			Register.register(toolbar);
		}

		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 1;
		gc.weighty = 0;
		gc.gridheight = 1;

		frame.add(toolbar, gc);
		toolbar.setVisible(ApplicationContext.getApplicationProperties().getBoolProperty("Application.ToolBar", false));
	}

	private void initController() {
		try {
			control = (Contoller) Register.getCheckedObject(Contoller.class);

			gc.gridx = 0;
			gc.gridy = 1;
			gc.weightx = 1;
			gc.weighty = 0;
			gc.gridheight = 1;

			frame.add(control, gc);
			control.setVisible(ApplicationContext.getApplicationProperties().getBoolProperty("Application.Controller", true));
		} catch (RegisterException e) {
			e.printStackTrace();
		}
	}

	private void initMiddle() {
		try {
			result = (ResultPane) Register.getCheckedObject(ResultPane.class);

			gc.gridx = 0;
			gc.gridy = 2;
			gc.weightx = 1;
			gc.weighty = 1;
			gc.gridheight = 15;

			frame.add(result, gc);
			result.setVisible(ApplicationContext.getApplicationProperties().getBoolProperty("Application.BERViewer", true));
		} catch (RegisterException e) {
			e.printStackTrace();
		}
	}

	private void initStatus() {
		try {
			status = (StatusPane) Register.getCheckedObject(StatusPane.class);
			status.setBorder(BorderFactory.createLineBorder(Color.GRAY));

			gc.anchor = GridBagConstraints.LAST_LINE_START;
			gc.gridx = 0;
			gc.gridy = 17;
			gc.weightx = 1;
			gc.weighty = 0;
			gc.gridheight = GridBagConstraints.REMAINDER;

			frame.add(status);
			status.setVisible(ApplicationContext.getApplicationProperties().getBoolProperty("Application.StatusBar", true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setFrameLocation() {
		int widht = ApplicationContext.getApplicationProperties().getIntProperty("Application.Window.PreferedWidth", 800);
		int height = ApplicationContext.getApplicationProperties().getIntProperty("Application.Window.PreferedHeight", 600);
		
		frame.setResizable(ApplicationContext.getApplicationProperties().getBoolProperty("Application.Window.ResizableSupport",
				true));
		if (ApplicationContext.getApplicationProperties().getBoolProperty("Application.Window.Maximized", false)) {
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			widht = dim.width;
			height = dim.height;
		}

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width - widht) / 2;
		int y = (dim.height - height) / 2;
		frame.setBounds(x, y, widht, height);
		frame.setSize(widht, height);
	}

	private void setVisibility() {
		frame.addWindowListener(new WindowListenerImpl());
		frame.setVisible(true);
	}

	private void initMenuBar() {
		menu = (UserMenuBar) Register.getObject(UserMenuBar.class);
		if (menu == null) {
			menu = new UserMenuBar();
			Register.register(menu);
		}
		frame.setJMenuBar(menu);
		menu.setVisible(ApplicationContext.getApplicationProperties().getBoolProperty("Application.MenuBar", true));
	}

	private void initTheme() {
		final String windowsTheme = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		//final String nimbus = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
		try {
			UIManager.setLookAndFeel(windowsTheme);			
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void initFrame() {

		try {
			frame = new JFrame(PropertyFinder.getPropertyWithName("BERViewer.application.frame.name"));
			frame.setIconImage(ResourceFinder.getImageWithName(PropertyFinder.getPropertyWithName("BERViewer.Icon.name.VIEWER"))
					.getImage());
		} catch (IOException e) {
		}
		if (frame != null){
			frame.setLayout(new GridBagLayout());
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		}
		
	}

	class WindowListenerImpl implements WindowListener {

		
		public void windowActivated(WindowEvent e) {
		}

		
		public void windowClosed(WindowEvent e) {
		}

		
		public void windowClosing(WindowEvent e) {
			operations.applicationExit(true);
		}

		
		public void windowDeactivated(WindowEvent e) {
		}

		
		public void windowDeiconified(WindowEvent e) {
		}

		
		public void windowIconified(WindowEvent e) {
		}

		
		public void windowOpened(WindowEvent e) {
		}

	}
}
