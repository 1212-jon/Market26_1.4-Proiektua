package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import domain.Seller;
import java.awt.Toolkit;

public class ErosketaAukeraketaGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Seller unekoErabiltzailea;

    public ErosketaAukeraketaGUI(Seller user) {
    	ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
        this.unekoErabiltzailea = user;
        
        // ZUZENKETA: Hemen testu finko bat jarri dugu erroreik ez emateko
        setTitle(ResourceBundle.getBundle("Etiquetas").getString("ErosketaAukeraketaGUI.MainTitle"));  //$NON-NLS-1$ //$NON-NLS-2$
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblIzenburua = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ErosketaAukeraketaGUI.Title")); //$NON-NLS-1$ //$NON-NLS-2$
        lblIzenburua.setHorizontalAlignment(SwingConstants.CENTER);
        lblIzenburua.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblIzenburua.setBounds(10, 30, 414, 30);
        contentPane.add(lblIzenburua);

        // PRODUKTUAK IKUSI botoia
        JButton btnProduktuak = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ErosketaAukeraketaGUI.Sale")); //$NON-NLS-1$ //$NON-NLS-2$
        btnProduktuak.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnProduktuak.setBounds(118, 90, 200, 50);
        btnProduktuak.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // QuerySalesGUI-ra doa
                new QuerySalesGUI(unekoErabiltzailea).setVisible(true);
                dispose();
            }
        });
        contentPane.add(btnProduktuak);

        // SALTZAILEAK IKUSI botoia
        JButton btnSaltzaileak = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ErosketaAukeraketaGUI.Seller")); //$NON-NLS-1$ //$NON-NLS-2$
        btnSaltzaileak.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnSaltzaileak.setBounds(118, 160, 200, 50);
        btnSaltzaileak.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // QuerySellersGUI-ra doa
                new QuerySellersGUI(unekoErabiltzailea).setVisible(true);
                dispose();
            }
        });
        contentPane.add(btnSaltzaileak);

        this.setLocationRelativeTo(null); 
    }
}