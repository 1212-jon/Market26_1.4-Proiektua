package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import businessLogic.BLFacade;
import domain.Seller;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter; // Importado
import java.awt.event.WindowEvent;   // Importado
import java.awt.Color;

import java.util.ResourceBundle;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.Toolkit;

public class ErregistratuGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldEmail; 
    private JTextField textFieldIzena; 
    private JPasswordField passwordField;
    private JPasswordField passwordField_1;
    private JLabel lblError; 

    public ErregistratuGUI() {
    	ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
        setFocusable(false);
        setTitle(ResourceBundle.getBundle("Etiquetas").getString("ErregistratuGUI.MainTitle"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setBounds(100, 100, 480, 360);
        
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MainGUI main = new MainGUI("seller3@gmail.com");
                main.setVisible(true);
            }
        });

        contentPane = new JPanel();
        contentPane.setForeground(Color.DARK_GRAY);
        contentPane.setBackground(Color.WHITE);
        contentPane.setFocusable(false);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        // --- ETIKETAK (Labels) ---
        JLabel lblEmail = new JLabel("E-MAIL");
        lblEmail.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        lblEmail.setForeground(Color.DARK_GRAY);
        lblEmail.setFocusable(false);
        lblEmail.setBounds(49, 47, 91, 14);
        contentPane.add(lblEmail);
        
        JLabel lblIzena = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ErregistratuGUI.NameLabel"));
        lblIzena.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        lblIzena.setForeground(Color.DARK_GRAY);
        lblIzena.setFocusable(false);
        lblIzena.setBounds(49, 96, 120, 18);
        contentPane.add(lblIzena);
        
        JLabel lblPass = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("PasswordLabel"));
        lblPass.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        lblPass.setForeground(Color.DARK_GRAY);
        lblPass.setFocusable(false);
        lblPass.setBounds(46, 142, 91, 18);
        contentPane.add(lblPass);
        
        JLabel lblPassRep = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ErregistratuGUI.RepeatPasswordLabel"));
        lblPassRep.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        lblPassRep.setForeground(Color.DARK_GRAY);
        lblPassRep.setFocusable(false);
        lblPassRep.setBounds(46, 195, 150, 18);
        contentPane.add(lblPassRep);
        
        // --- TESTU EREMUAK (Fields) ---
        textFieldEmail = new JTextField();
        textFieldEmail.setBorder(new MatteBorder(0, 0, 2, 0, (Color) new Color(64, 224, 208)));
        textFieldEmail.setForeground(Color.DARK_GRAY);
        textFieldEmail.setBackground(Color.WHITE);
        textFieldEmail.setBounds(208, 45, 180, 20);
        contentPane.add(textFieldEmail);
        
        textFieldIzena = new JTextField();
        textFieldIzena.setBorder(new MatteBorder(0, 0, 2, 0, (Color) new Color(64, 224, 208)));
        textFieldIzena.setForeground(Color.DARK_GRAY);
        textFieldIzena.setBackground(Color.WHITE);
        textFieldIzena.setBounds(208, 94, 180, 20);
        contentPane.add(textFieldIzena);
        
        passwordField = new JPasswordField();
        passwordField.setBorder(new MatteBorder(0, 0, 2, 0, (Color) new Color(64, 224, 208)));
        passwordField.setForeground(Color.DARK_GRAY);
        passwordField.setBackground(Color.WHITE);
        passwordField.setBounds(208, 140, 180, 20);
        contentPane.add(passwordField);
        
        passwordField_1 = new JPasswordField();
        passwordField_1.setBorder(new MatteBorder(0, 0, 2, 0, (Color) new Color(64, 224, 208)));
        passwordField_1.setForeground(Color.DARK_GRAY);
        passwordField_1.setBackground(Color.WHITE);
        passwordField_1.setBounds(208, 193, 180, 20);
        contentPane.add(passwordField_1);

        lblError = new JLabel("");
        lblError.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblError.setFocusable(false);
        lblError.setHorizontalAlignment(SwingConstants.CENTER);
        lblError.setForeground(Color.RED);
        lblError.setBounds(49, 267, 369, 56);
        contentPane.add(lblError);

        // --- BOTOIA ETA LOGIKA ---
        JButton btnErregistratu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("AukerakIkusiGUI.RegisterButton"));
        btnErregistratu.setBorder(new LineBorder(new Color(64, 224, 208)));
        btnErregistratu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnErregistratu.setForeground(new Color(255, 255, 255));
        btnErregistratu.setBackground(new Color(64, 224, 208));
        btnErregistratu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnErregistratu.setFocusable(false);
        btnErregistratu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lblError.setText(""); 
                
                String email = textFieldEmail.getText();
                String izena = textFieldIzena.getText();
                String pass = new String(passwordField.getPassword());
                String passRep = new String(passwordField_1.getPassword());

                if (email.isEmpty() || izena.isEmpty() || pass.isEmpty() || passRep.isEmpty()) {
                    lblError.setText(ResourceBundle.getBundle("Etiquetas").getString("ErregistratuGUI.CompleteFieldAlert"));
                    return;
                }

                if (!pasahitzaSeguruaDa(pass)) {
                    lblError.setText("<html><center>"+ResourceBundle.getBundle("Etiquetas").getString("ErregistratuGUI.PasswordError1")+"<br>"
                                   + ResourceBundle.getBundle("Etiquetas").getString("ErregistratuGUI.PasswordError2")+"</center></html>");
                    return;
                }

                if (!pass.equals(passRep)) {
                    lblError.setText(ResourceBundle.getBundle("Etiquetas").getString("ErregistratuGUI.DifferentPasswordAlert"));
                    return;
                }
                
                try {
                    BLFacade facade = MainGUI.getBusinessLogic();
                    
                    if (facade.isBlacklisted(email)) {
                        lblError.setText(ResourceBundle.getBundle("Etiquetas").getString("ErregistratuGUI.BlacklistedErrorAlert"));
                        return;
                    }
                    
                    Seller s = facade.register(email, izena, pass);

                    if (s != null) {
                        JOptionPane.showMessageDialog(null, ResourceBundle.getBundle("Etiquetas").getString("ErregistratuGUI.CorrectAlert"));
                        SaioaHasiGUI saioa = new SaioaHasiGUI(); 
                        saioa.setVisible(true);
                        dispose(); 
                    } else {
                        lblError.setText(ResourceBundle.getBundle("Etiquetas").getString("ErregistratuGUI.ExistingMailError"));
                    }
                } catch (Exception ex) {
                    lblError.setText("Error: " + ex.getMessage());
                }
            }
        });
        btnErregistratu.setBounds(123, 235, 205, 30);
        contentPane.add(btnErregistratu);
    }

    private boolean pasahitzaSeguruaDa(String password) {
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^*&+=!?.])(?=\\S+$).{8,12}$";
        return password.matches(pattern);
    }
}