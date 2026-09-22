package gui;

import javax.swing.*;
import businessLogic.BLFacade;
import domain.Seller;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

import java.awt.Cursor;
import java.awt.Toolkit;

public class SaioaHasiGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldEmail;
	private JPasswordField passwordField;
	private JLabel lblError; 

	public SaioaHasiGUI() {
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
		
		this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MainGUI main = new MainGUI("seller3@gmail.com");
                main.setVisible(true);
            }
        });
		
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("SaioaHasiGUI.MainTitle")); //$NON-NLS-1$ //$NON-NLS-2$
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(450, 300); 
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JLabel lblIzenburua = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("SaioaHasiGUI.MainTitle")); //$NON-NLS-1$ //$NON-NLS-2$
		lblIzenburua.setHorizontalAlignment(SwingConstants.CENTER);
		lblIzenburua.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblIzenburua.setBounds(10, 20, 414, 30);
		contentPane.add(lblIzenburua);

		// Email eta Pasahitza lehengo toki berean
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setBounds(50, 80, 80, 25);
		contentPane.add(lblEmail);

		textFieldEmail = new JTextField();
		textFieldEmail.setBounds(130, 80, 230, 25);
		contentPane.add(textFieldEmail);

		JLabel lblPassword = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("PasswordLabel")); //$NON-NLS-1$ //$NON-NLS-2$
		lblPassword.setBounds(50, 120, 80, 25);
		contentPane.add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setBounds(130, 120, 230, 25);
		contentPane.add(passwordField);
		
		lblError = new JLabel("");
        lblError.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblError.setFocusable(false);
        lblError.setHorizontalAlignment(SwingConstants.CENTER);
        lblError.setForeground(Color.RED);
        lblError.setBounds(40, 215, 370, 30);
        contentPane.add(lblError);


		// --- ITXI BOTOIA (Ezkerrean) ---
		JButton btnItxi = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close")); //$NON-NLS-1$ //$NON-NLS-2$
		btnItxi.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnItxi.setBackground(Color.WHITE);
		btnItxi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnItxi.setBorder(new LineBorder(Color.LIGHT_GRAY));
		btnItxi.setBounds(130, 170, 110, 40); 
		btnItxi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose(); // SaioaHasiGUI itxi
				MainGUI m = new MainGUI(null); // Menu nagusira bueltatu
				m.setVisible(true);
			}
		});
		contentPane.add(btnItxi);

		// --- SARTU BOTOIA (Eskuinean) ---
		JButton btnSartu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Accept")); //$NON-NLS-1$ //$NON-NLS-2$
		btnSartu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSartu.setBackground(new Color(64, 224, 208));
		btnSartu.setForeground(Color.WHITE);
		btnSartu.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnSartu.setBounds(250, 170, 110, 40); 
		// --- SARTU BOTOIA (Eskuinean) ---
		btnSartu.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	lblError.setText(""); 
		        String email = textFieldEmail.getText();
		        String pass = new String(passwordField.getPassword());
		        BLFacade facade = MainGUI.getBusinessLogic();
		        
		     // 1. BEGIRATU ZERRENDA BELTZEAN DAGOEN (Saioa hasi aurretik)
		        if (facade.isBlacklisted(email)) {
		            lblError.setText(ResourceBundle.getBundle("Etiquetas").getString("ErregistratuGUI.BlacklistedErrorAlert"));
		            return; // Hemendik ez pasatu
		        }
		        
		        Seller s = facade.isLogin(email, pass);
		        
		        if (s != null) {
		        	// Kontua aktibo dagoen begiratu
		        	if (!s.isAktibo()) {
		                lblError.setText(ResourceBundle.getBundle("Etiquetas").getString("SaioaHasiGUI.AccountDisabled")); // Gehitu gako hau Etiquetas-en
		                return;
		            }
		        	
		            MainGUI.unekoErabiltzailea = s;
		            JOptionPane.showMessageDialog(null, ResourceBundle.getBundle("Etiquetas").getString("Welcome")+", " + s.getName());
		            
		            dispose(); // Saioa hasi leihoa itxi
		            
		            if(s.getEmail().equals("admin") && s.getPassword().equals("admin")) {
		            	JFrame a = new AdminAukGUI(); 
			            a.setVisible(true);
		            }else {
		            		// KONPONBIDEA: ProfilaGUI-ri behar dituen bi parametroak pasatzen dizkiogu:
				            // 1. parametroa: Seller objektua (s)
				            // 2. parametroa: Rol bat (adibidez "Eroslea" lehenetsi gisa)
				            JFrame a = new ProfilaGUI(s, ResourceBundle.getBundle("Etiquetas").getString("SaioaHasiGUI.SelectBuyerOption")); 
				            a.setVisible(true);
		            	
		            }
		            
		        } else {
		            JOptionPane.showMessageDialog(null, ResourceBundle.getBundle("Etiquetas").getString("SaioaHasiGUI.IncorrectErrorAlert"), "Error", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});
		contentPane.add(btnSartu);
	}
}