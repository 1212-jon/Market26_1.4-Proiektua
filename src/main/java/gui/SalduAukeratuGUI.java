package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import domain.Seller;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

public class SalduAukeratuGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Seller user;

    public SalduAukeratuGUI(Seller user) {
    	ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
        // SEGURTASUNA: Erabiltzailea null bada, errorea ekiditeko
        if (user == null) {
            JOptionPane.showMessageDialog(null, "Saioa iraungi da edo erabiltzailea ez da aurkitu.");
            this.dispose();
            return;
        }
        this.user = user;
        
        setTitle(ResourceBundle.getBundle("Etiquetas").getString("SalduAukeratuGUI.Title")); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(2, 1, 10, 10));

        JButton btnSaldu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("SalduAukeratuGUI.Product")); //$NON-NLS-1$ //$NON-NLS-2$
        btnSaldu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSaldu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Erabili gordetako 'user' objektua
                JFrame a = new CreateSaleGUI(user.getEmail());
                a.setVisible(true);
                dispose();
            }
        });
        contentPane.add(btnSaldu);

        JButton btnEskaintza = new JButton(ResourceBundle.getBundle("Etiquetas").getString("SalduAukeratuGUI.OfferToDemand")); //$NON-NLS-1$ //$NON-NLS-2$
        btnEskaintza.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEskaintza.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Pasatu 'user' objektu osoa
                JFrame a = new QueryDemandsGUI(user);
                a.setVisible(true);
                dispose(); 
            }
        });
        contentPane.add(btnEskaintza);
        
        this.setLocationRelativeTo(null);
    }
}