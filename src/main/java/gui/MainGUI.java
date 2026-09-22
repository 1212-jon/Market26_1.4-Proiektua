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

public class MainGUI extends JFrame {
	
	public static Seller unekoErabiltzailea = null; 
	private String sellerMail;
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
	private JRadioButton rdbtnEnglish, rdbtnEuskara, rdbtnCastellano;
	private JPanel panelHizkuntzak;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	/**
	 * Konstruktorea. 
	 * @param mail Hasieran null izan daiteke saioa hasi gabe badator.
	 */
	public MainGUI(String mail) {
		
		super();
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.sellerMail = mail;
		setFocusable(false);
		setBackground(Color.WHITE);
		this.setSize(495, 420); // Tamaina pixka bat egokituta botoiak kabitzeko
		
		jContentPane = new JPanel();
		jContentPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		jContentPane.setBackground(Color.WHITE);
		jContentPane.setLayout(null);
		setContentPane(jContentPane);

		jLabelSelectOption = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
		jLabelSelectOption.setFont(new Font("Tahoma", Font.BOLD, 20));
		jLabelSelectOption.setForeground(Color.DARK_GRAY);
		jLabelSelectOption.setHorizontalAlignment(SwingConstants.CENTER);
		jLabelSelectOption.setBounds(0, 10, 481, 50);
		jContentPane.add(jLabelSelectOption);

		// 1. ERREGISTRATU
		btnErregistratu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("AukerakIkusiGUI.RegisterButton")); //$NON-NLS-1$ //$NON-NLS-2$
		btnErregistratu.setFocusable(false);
		btnErregistratu.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnErregistratu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnErregistratu.setForeground(Color.WHITE);
		btnErregistratu.setBackground(new Color(64, 224, 208));
		btnErregistratu.setBounds(50, 70, 380, 50);
		btnErregistratu.addActionListener(e -> {
			// Botoia aktibatuta
			JFrame a = new ErregistratuGUI(); 
			a.setVisible(true);
			JFrame b = this; 
			b.setVisible(false);
		});
		jContentPane.add(btnErregistratu);

		// 2. SAIOA HASI
		btnSaioaHasi = new JButton(ResourceBundle.getBundle("Etiquetas").getString("AukerakIkusiGUI.LoginButton")); //$NON-NLS-1$ //$NON-NLS-2$
		btnSaioaHasi.setFocusable(false);
		btnSaioaHasi.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnSaioaHasi.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSaioaHasi.setForeground(new Color(64, 224, 208));
		btnSaioaHasi.setBackground(Color.WHITE);
		btnSaioaHasi.setBorder(new LineBorder(new Color(64, 224, 208)));
		btnSaioaHasi.setBounds(50, 130, 380, 50);
		btnSaioaHasi.addActionListener(e -> {
			// Botoia aktibatuta
			JFrame a = new SaioaHasiGUI(); 
			a.setVisible(true);
			JFrame b = this; 
			b.setVisible(false);
		});
		jContentPane.add(btnSaioaHasi);

		// 3. BISITARI MODUAN KONTSULTATU
		btnBisitari = new JButton(ResourceBundle.getBundle("Etiquetas").getString("AukerakIkusiGUI.ContinueButton")); //$NON-NLS-1$ //$NON-NLS-2$
		btnBisitari.setFocusable(false);
		btnBisitari.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnBisitari.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnBisitari.setForeground(Color.GRAY);
		btnBisitari.setBackground(new Color(245, 245, 245));
		btnBisitari.setBounds(50, 190, 380, 40);
		btnBisitari.addActionListener(e -> {
			unekoErabiltzailea = null; 
			JFrame a = new QuerySalesGUI(null); 
			a.setVisible(true);
		});
		jContentPane.add(btnBisitari);

		// --- HIZKUNTZEN PANELA ---
		panelHizkuntzak = new JPanel();
		panelHizkuntzak.setBackground(Color.WHITE);
		panelHizkuntzak.setBounds(0, 260, 481, 63);
		panelHizkuntzak.setLayout(null);
		jContentPane.add(panelHizkuntzak);

		rdbtnEuskara = new JRadioButton("Euskara");
		rdbtnEuskara.setFocusable(false);
		rdbtnEuskara.setSelected(true);
		rdbtnEuskara.setBounds(75, 22, 94, 21);
		rdbtnEuskara.setBackground(Color.WHITE);
		rdbtnEuskara.addActionListener(e -> { Locale.setDefault(new Locale("eus")); paintAgain(); });
		buttonGroup.add(rdbtnEuskara);
		panelHizkuntzak.add(rdbtnEuskara);

		rdbtnCastellano = new JRadioButton("Castellano");
		rdbtnCastellano.setFocusable(false);
		rdbtnCastellano.setBounds(198, 22, 107, 21);
		rdbtnCastellano.setBackground(Color.WHITE);
		rdbtnCastellano.addActionListener(e -> { Locale.setDefault(new Locale("es")); paintAgain(); });
		buttonGroup.add(rdbtnCastellano);
		panelHizkuntzak.add(rdbtnCastellano);

		rdbtnEnglish = new JRadioButton("English");
		rdbtnEnglish.setFocusable(false);
		rdbtnEnglish.setBounds(369, 22, 94, 21);
		rdbtnEnglish.setBackground(Color.WHITE);
		rdbtnEnglish.addActionListener(e -> { Locale.setDefault(new Locale("en")); paintAgain(); });
		buttonGroup.add(rdbtnEnglish);
		panelHizkuntzak.add(rdbtnEnglish);

		String titulua = ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle");
		setTitle(titulua + (sellerMail != null ? ": " + sellerMail : ": Bisitari"));
		
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
		String titulua = ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle");
		this.setTitle(titulua + (sellerMail != null ? ": " + sellerMail : ": Bisitari"));
		btnErregistratu.setText(ResourceBundle.getBundle("Etiquetas").getString("AukerakIkusiGUI.RegisterButton"));
	    btnSaioaHasi.setText(ResourceBundle.getBundle("Etiquetas").getString("AukerakIkusiGUI.LoginButton"));
	    btnBisitari.setText(ResourceBundle.getBundle("Etiquetas").getString("AukerakIkusiGUI.ContinueButton"));
	}

	public static void saioaIndarrezBerritu() {
		if (unekoErabiltzailea != null) {
			unekoErabiltzailea = getBusinessLogic().getUser(unekoErabiltzailea.getEmail()); 
		}
	}
}