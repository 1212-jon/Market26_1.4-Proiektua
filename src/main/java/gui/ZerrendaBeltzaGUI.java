package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;
import domain.BlacklistedUser;
import java.awt.Toolkit;

public class ZerrendaBeltzaGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JList<String> list;
    private DefaultListModel<String> listModel;

    public ZerrendaBeltzaGUI() {
    	ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
        setTitle(ResourceBundle.getBundle("Etiquetas").getString("ZerrendaBeltzaGUI.MainTitle"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 350);
        
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 15));

        // Izenburua estiloarekin
        JLabel lblTitle = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ZerrendaBeltzaGUI.Title"));
        lblTitle.setForeground(new Color(0, 128, 128)); // Turkesa iluna
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblTitle, BorderLayout.NORTH);

        // Zerrenda kudeatzeko modeloa
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(list);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Datuak kargatu
        refreshBlacklist();
    }

    /**
     * Zerrenda beltzeko emailak kargatzen ditu datu-basetik
     */
    private void refreshBlacklist() {
        listModel.removeAllElements();
        try {
            BLFacade facade = MainGUI.getBusinessLogic();
            List<BlacklistedUser> blockedUsers = facade.getBlacklistedUsers();
            
            if (blockedUsers != null) {
                for (BlacklistedUser bu : blockedUsers) {
                    listModel.addElement(bu.getEmail());
                }
            }
        } catch (Exception e) {
            System.err.println(ResourceBundle.getBundle("Etiquetas").getString("ZerrendaBeltzaGUI.ErrorAlert")+": " + e.getMessage());
        }
    }
}