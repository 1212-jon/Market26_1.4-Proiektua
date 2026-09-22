package gui;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import domain.Seller;
import domain.Transaction;
import businessLogic.BLFacade;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TransakzioakGUI extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private Seller user;
    private JLabel lblSaldo;
    private DefaultTableModel tableModel;
    private JTable table;
    private JFrame parentFrame; // Gurasoa gordetzeko (ProfilaGUI)

    // Konstruktorea aldatu dugu parentFrame jasotzeko
    public TransakzioakGUI(Seller user, JFrame parent) {
    	ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
        this.user = user;
        this.parentFrame = parent;
        
        // Leihoaren konfigurazioa
        setTitle(ResourceBundle.getBundle("Etiquetas").getString("TransakzioakGUI.MainTitle")+" - " + user.getName());
        setSize(600, 500); // Altuera apur bat handitu dugu botoi gehiagorentzako
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(10, 10));

        // --- GOIKO PANELA: Saldoa erakutsi ---
        JPanel panelNorth = new JPanel();
        panelNorth.setBackground(new Color(245, 245, 245));
        lblSaldo = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Money")+": " + user.getDirua() + "€");
        lblSaldo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panelNorth.add(lblSaldo);
        getContentPane().add(panelNorth, BorderLayout.NORTH);

        // --- ERDIKO PANELA: Trantsakzioen Taula ---
        String[] columnNames = {ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Date"), 
        		ResourceBundle.getBundle("Etiquetas").getString("TransakzioakGUI.Type"), 
        		ResourceBundle.getBundle("Etiquetas").getString("TransakzioakGUI.Concept"), 
        		ResourceBundle.getBundle("Etiquetas").getString("TransakzioakGUI.Cuantity")};
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setFocusable(false);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // --- BEHEKO PANELA: Botoiak ---
        JPanel panelSouth = new JPanel();
        panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        JButton btnKargatu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("TransakzioakGUI.AddMoney"));
        btnKargatu.setFocusable(false);
        btnKargatu.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        btnKargatu.setBackground(new Color(64, 224, 208));
        btnKargatu.setForeground(Color.WHITE);
        
        JButton btnAtera = new JButton(ResourceBundle.getBundle("Etiquetas").getString("TransakzioakGUI.WithdrawMoney"));
        btnAtera.setFocusable(false);
        btnAtera.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        btnAtera.setBackground(new Color(255, 99, 71));
        btnAtera.setForeground(Color.WHITE);

        // ITXI BOTOIA (Zuk eskatutakoa: ProfilaGUI-ra itzultzeko)
        JButton btnItxi = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
        btnItxi.setFocusable(false);
        btnItxi.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        btnItxi.setBackground(Color.LIGHT_GRAY);
        btnItxi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Leiho hau itxi
                if (parentFrame != null) {
                    parentFrame.setVisible(true); // ProfilaGUI erakutsi
                    // Profila freskatu saioa hasi berri balitz bezala
                    if (parentFrame instanceof ProfilaGUI) {
                        ((ProfilaGUI) parentFrame).freskatuDatuak();
                    }
                }
            }
        });
        
        btnKargatu.addActionListener(e -> diruaAldatuTresnaBidez(true));
        btnAtera.addActionListener(e -> diruaAldatuTresnaBidez(false));
        
        panelSouth.add(btnKargatu);
        panelSouth.add(btnAtera);
        panelSouth.add(btnItxi); // Botoia gehitu
        getContentPane().add(panelSouth, BorderLayout.SOUTH);

        freskatuTaula();
        this.setLocationRelativeTo(null);
    }

    public void freskatuTaula() {
        tableModel.setRowCount(0);
        BLFacade facade = MainGUI.getBusinessLogic();
        this.user = facade.getUser(user.getEmail());
        
        lblSaldo.setText(ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Money")+": " + String.format("%.2f", user.getDirua()) + "€");
        
        List<Transaction> trans = user.getTransactions();
        if (trans != null && !trans.isEmpty()) {
            List<Transaction> kopia = new ArrayList<>(trans);
            Collections.reverse(kopia); 
            
            for (Transaction t : kopia) {
                tableModel.addRow(new Object[]{
                    t.getData(), 
                    t.getMota(), 
                    t.getKontzeptua(), 
                    t.getKantitatea() + "€"
                });
            }
        }
    }

    private void diruaAldatuTresnaBidez(boolean kargatu) {
        String msg = kargatu ? ResourceBundle.getBundle("Etiquetas").getString("TransakzioakGUI.HowManyAdd") : ResourceBundle.getBundle("Etiquetas").getString("TransakzioakGUI.HowManyWithdraw");
        String input = JOptionPane.showInputDialog(this, msg, ResourceBundle.getBundle("Etiquetas").getString("TransakzioakGUI.Wallet"), JOptionPane.QUESTION_MESSAGE);
        
        if (input != null && !input.isEmpty()) {
            try {
                float kopurua = Float.parseFloat(input);
                if (kopurua <= 0) {
                    JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("TransakzioakGUI.MoreThanZero"), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!kargatu && kopurua > user.getDirua()) {
                    JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("TransakzioakGUI.NoMoneyMessage"), ResourceBundle.getBundle("Etiquetas").getString("TransakzioakGUI.NoMOneyTitle"), JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // ErosketaGUI ireki eta 'this' pasatu (TransakzioakGUI)
                ErosketaGUI aukeraLeihoa = new ErosketaGUI(kopurua, user, this, kargatu);
                this.setVisible(false); // Hau ezkutatu aukera leihoa irekitzean
                aukeraLeihoa.setVisible(true);
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("TransakzioakGUI.InvalidNumberMessage"), ResourceBundle.getBundle("Etiquetas").getString("TransakzioakGUI.InvalidNumberTitle"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}