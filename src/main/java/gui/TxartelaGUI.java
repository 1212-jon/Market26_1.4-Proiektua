package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import domain.Seller;
import businessLogic.BLFacade;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.awt.event.ActionEvent;

public class TxartelaGUI extends JFrame {
    private float kopurua;
    private Seller user;
    private JFrame previousFrame;
    private boolean isSarrera;
    private JTextField txtIzena, txtZenbakia, txtCVV, txtData;

    public TxartelaGUI(float kopurua, Seller user, JFrame previousFrame, boolean isSarrera) {
    	ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
        this.kopurua = kopurua;
        this.user = user;
        this.previousFrame = previousFrame;
        this.isSarrera = isSarrera;

        setTitle(ResourceBundle.getBundle("Etiquetas").getString("TxartelaGUI.MainTitle"));
        setSize(450, 480); // Tamaina apur bat handitu dugu botoi berria sartzeko
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel contentPane = new JPanel(null);
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);

        JLabel lblInfo = new JLabel((isSarrera ? ResourceBundle.getBundle("Etiquetas").getString("BizumGUI.Add")+": " : ResourceBundle.getBundle("Etiquetas").getString("BizumGUI.Withdraw")+": ") + kopurua + "€", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblInfo.setBounds(20, 20, 396, 30);
        contentPane.add(lblInfo);

        // Inprimakiaren eremuak
        txtIzena = new JTextField(); txtIzena.setBounds(180, 70, 200, 30);
        txtZenbakia = new JTextField(); txtZenbakia.setBounds(180, 120, 200, 30);
        txtCVV = new JTextField(); txtCVV.setBounds(180, 170, 200, 30);
        txtData = new JTextField(); txtData.setBounds(180, 220, 200, 30);
        
        contentPane.add(new JLabel(ResourceBundle.getBundle("Etiquetas").getString("TxartelaGUI.NameLabel")+":")).setBounds(40, 70, 120, 30);
        contentPane.add(new JLabel(ResourceBundle.getBundle("Etiquetas").getString("TxartelaGUI.CardNumberLabel")+":")).setBounds(40, 120, 120, 30);
        contentPane.add(new JLabel("CVV:")).setBounds(40, 170, 120, 30);
        contentPane.add(new JLabel(ResourceBundle.getBundle("Etiquetas").getString("TxartelaGUI.DateLabel")+":")).setBounds(40, 220, 140, 30);
        contentPane.add(txtIzena); contentPane.add(txtZenbakia); contentPane.add(txtCVV); contentPane.add(txtData);

        // --- PROZESATU BOTOIA ---
        JButton btnProzesatu = new JButton(isSarrera ? ResourceBundle.getBundle("Etiquetas").getString("TxartelaGUI.MoneyIn") : ResourceBundle.getBundle("Etiquetas").getString("TxartelaGUI.MoneyOut"));
        btnProzesatu.setFocusable(false);
        btnProzesatu.setBounds(40, 280, 340, 40);
        btnProzesatu.setBackground(isSarrera ? new Color(46, 139, 87) : new Color(178, 34, 34));
        btnProzesatu.setForeground(Color.WHITE);
        btnProzesatu.addActionListener(e -> balidatuEtaGorde());
        contentPane.add(btnProzesatu);

        // --- ITXI BOTOIA (BERRIA) ---
        JButton btnItxi = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
        btnItxi.setFocusable(false);
        btnItxi.setBounds(40, 330, 340, 40);
        btnItxi.setBackground(Color.LIGHT_GRAY);
        btnItxi.addActionListener(e -> {
            this.dispose(); // TxartelaGUI itxi
            if (previousFrame != null) {
                previousFrame.setVisible(true); // TrantsakzioakGUI erakutsi
            }
        });
        contentPane.add(btnItxi);

        this.setLocationRelativeTo(null);
    }

    private void balidatuEtaGorde() {
        if (txtIzena.getText().isEmpty() || txtZenbakia.getText().length() != 16) {
            JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("TxartelaGUI.ErrorData"));
            return;
        }

        try {
            BLFacade facade = MainGUI.getBusinessLogic();
            float saldoBerria = isSarrera ? user.getDirua() + kopurua : user.getDirua() - kopurua;
            
            // 1. Datu-basean saldoa eguneratu
            facade.updateSaldo(user.getEmail(), saldoBerria);
            
            // 2. GAKOA: MainGUI-ko saioa berritu datu berriekin
            MainGUI.saioaIndarrezBerritu();
            
            JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("TxartelaGUI.ActionGood"));
            
            // 3. Leihoen arteko nabigazioa
            this.dispose();
            if (previousFrame != null) {
                previousFrame.setVisible(true);
                if (previousFrame instanceof TransakzioakGUI) {
                    ((TransakzioakGUI) previousFrame).freskatuTaula();
                }
            }
        } catch (Exception ex) { 
            ex.printStackTrace(); 
            JOptionPane.showMessageDialog(this, "Error");
        }
    }
}