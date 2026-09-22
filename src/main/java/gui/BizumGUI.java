package gui;

import java.awt.*;
import javax.swing.*;
import domain.Seller;
import businessLogic.BLFacade;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.awt.event.ActionEvent;

public class BizumGUI extends JFrame {
    private float kopurua;
    private Seller user;
    private JFrame previousFrame;
    private boolean isSarrera;

    public BizumGUI(float kopurua, Seller user, JFrame previousFrame, boolean isSarrera) {
    	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
        this.kopurua = kopurua;
        this.user = user;
        this.previousFrame = previousFrame;
        this.isSarrera = isSarrera;

        setTitle(ResourceBundle.getBundle("Etiquetas").getString("BizumGUI.Title"));
        setSize(400, 300); // Pixka bat handitu dugu botoi berria sartzeko
        getContentPane().setLayout(null);
        this.getContentPane().setBackground(Color.WHITE);

        JLabel lblInfo = new JLabel((isSarrera ? ResourceBundle.getBundle("Etiquetas").getString("BizumGUI.Add")+": " : ResourceBundle.getBundle("Etiquetas").getString("BizumGUI.Withdraw")+": ") + kopurua + "€");
        lblInfo.setBounds(50, 20, 300, 30);
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        getContentPane().add(lblInfo);

        JLabel lblTlf = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("BizumGUI.AskTlfLabel"));
        lblTlf.setBounds(50, 70, 131, 30);
        getContentPane().add(lblTlf);

        JTextField txtTlf = new JTextField();
        txtTlf.setBounds(180, 70, 150, 30);
        getContentPane().add(txtTlf);

        // --- ONARTU BOTOIA ---
        JButton btnOnartu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Accept"));
        btnOnartu.setFocusable(false);
        btnOnartu.setBounds(50, 120, 280, 40);
        btnOnartu.setBackground(new Color(64, 224, 208));
        btnOnartu.setForeground(Color.WHITE);
        
        btnOnartu.addActionListener(e -> {
            BLFacade facade = MainGUI.getBusinessLogic();
            float saldoBerria = isSarrera ? user.getDirua() + kopurua : user.getDirua() - kopurua;
            
            // 1. Datu basean saldoa aldatu
            facade.updateSaldo(user.getEmail(), saldoBerria);
            
            // 2. GAKOA: MainGUI-ko estatikoa berritu (Saioa hasi bezala egiteko)
            MainGUI.saioaIndarrezBerritu();
            
            JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("BizumGUI.Done"));
            
            // 3. Aurreko leihora joan eta freskatu
            this.dispose();
            if (previousFrame != null) {
                previousFrame.setVisible(true);
                if (previousFrame instanceof TransakzioakGUI) {
                    ((TransakzioakGUI) previousFrame).freskatuTaula();
                }
            }
        });
        getContentPane().add(btnOnartu);

        // --- ITXI BOTOIA (Zuk eskatutakoa) ---
        JButton btnItxi = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
        btnItxi.setFocusable(false);
        btnItxi.setBounds(50, 170, 280, 40);
        btnItxi.setBackground(Color.LIGHT_GRAY);
        
        btnItxi.addActionListener(e -> {
            this.dispose(); // Leiho hau itxi
            if (previousFrame != null) {
                previousFrame.setVisible(true); // TrantsakzioakGUI erakutsi
            }
        });
        getContentPane().add(btnItxi);

        this.setLocationRelativeTo(null);
    }
}