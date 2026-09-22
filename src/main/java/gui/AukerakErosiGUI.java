package gui;

import javax.swing.*;
import businessLogic.BLFacade;
import domain.Seller;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;
import java.awt.Cursor;
import java.awt.Toolkit;

public class AukerakErosiGUI extends JFrame {
	
	// Saioa gordetzeko aldagai estatikoa
	public static Seller unekoErabiltzailea = null; 

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	
	private JButton btnErregistratu = null;
	private JButton btnSaioaHasi = null;
	private JButton btnBisitari = null;

	private static BLFacade appFacadeInterface;
	
	public static BLFacade getBusinessLogic(){
		return appFacadeInterface;
	}
	 
	public static void setBussinessLogic (BLFacade facade){
		appFacadeInterface=facade;
	}

	protected JLabel jLabelSelectOption;
	private JPanel panelHizkuntzak;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	public AukerakErosiGUI() {
		super();
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
		this.setSize(495, 420); // Tamaina pixka bat handitu dugu erosotasunagatik
		this.getContentPane().setLayout(null);
		this.getContentPane().setBackground(Color.WHITE);

		jLabelSelectOption = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
		jLabelSelectOption.setFont(new Font("Tahoma", Font.BOLD, 20));
		jLabelSelectOption.setForeground(Color.DARK_GRAY);
		jLabelSelectOption.setHorizontalAlignment(SwingConstants.CENTER);
		jLabelSelectOption.setBounds(0, 20, 481, 40);
		this.getContentPane().add(jLabelSelectOption);

		// 1. ERREGISTRATU BOTOIA
		btnErregistratu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("AukerakIkusiGUI.RegisterButton")); //$NON-NLS-1$ //$NON-NLS-2$
		btnErregistratu.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnErregistratu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnErregistratu.setForeground(Color.WHITE);
		btnErregistratu.setBackground(new Color(64, 224, 208));
		btnErregistratu.setBounds(50, 80, 380, 50);
		btnErregistratu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Leiho berria ireki eta hau ezkutatu (aukerakoa)
				JFrame a = new ErregistratuGUI(); 
				a.setVisible(true);
			}
		});
		this.getContentPane().add(btnErregistratu);

		// 2. SAIOA HASI BOTOIA
		btnSaioaHasi = new JButton(ResourceBundle.getBundle("Etiquetas").getString("AukerakIkusiGUI.LoginButton")); //$NON-NLS-1$ //$NON-NLS-2$
		btnSaioaHasi.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnSaioaHasi.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSaioaHasi.setForeground(new Color(64, 224, 208));
		btnSaioaHasi.setBackground(Color.WHITE);
		btnSaioaHasi.setBorder(new LineBorder(new Color(64, 224, 208)));
		btnSaioaHasi.setBounds(50, 145, 380, 50);
		btnSaioaHasi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Leiho berria ireki
				JFrame a = new SaioaHasiGUI(); 
				a.setVisible(true);
			}
		});
		this.getContentPane().add(btnSaioaHasi);

		// 3. BISITARI MODUAN KONTSULTATU (Saioa hasi gabe)
		btnBisitari = new JButton(ResourceBundle.getBundle("Etiquetas").getString("AukerakIkusiGUI.ContinueButton")); //$NON-NLS-1$ //$NON-NLS-2$
		btnBisitari.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnBisitari.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnBisitari.setForeground(Color.GRAY);
		btnBisitari.setBackground(new Color(245, 245, 245));
		btnBisitari.setBounds(50, 210, 380, 40);
		btnBisitari.addActionListener(e -> {
			unekoErabiltzailea = null; 
			JFrame a = new QuerySalesGUI(null); 
			a.setVisible(true);
		});
		this.getContentPane().add(btnBisitari);

		// 4. HIZKUNTZAK
		panelHizkuntzak = new JPanel();
		panelHizkuntzak.setBackground(Color.WHITE);
		panelHizkuntzak.setBounds(0, 280, 481, 50);
		this.getContentPane().add(panelHizkuntzak);
		
		JRadioButton rdbtnEus = new JRadioButton("Euskara");
		rdbtnEus.setSelected(true);
		rdbtnEus.addActionListener(e -> {
			Locale.setDefault(new Locale("eu"));
			paintAgain();
		});
		buttonGroup.add(rdbtnEus);
		panelHizkuntzak.add(rdbtnEus);

		JRadioButton rdbtnEs = new JRadioButton("Castellano");
		rdbtnEs.addActionListener(e -> {
			Locale.setDefault(new Locale("es"));
			paintAgain();
		});
		buttonGroup.add(rdbtnEs);
		panelHizkuntzak.add(rdbtnEs);

		JRadioButton rdbtnEn = new JRadioButton("English");
		rdbtnEn.addActionListener(e -> {
			Locale.setDefault(new Locale("en"));
			paintAgain();
		});
		buttonGroup.add(rdbtnEn);
		panelHizkuntzak.add(rdbtnEn);

		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle") + ": "+ResourceBundle.getBundle("Etiquetas").getString("Welcome"));
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});
		
		this.setLocationRelativeTo(null);
	}
	
	private void paintAgain() {
		jLabelSelectOption.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle") + ": "+ResourceBundle.getBundle("Etiquetas").getString("Welcome"));
	}

	public static void saioaIndarrezBerritu() {
		if (unekoErabiltzailea != null) {
			unekoErabiltzailea = getBusinessLogic().getUser(unekoErabiltzailea.getEmail()); 
		}
	}
}