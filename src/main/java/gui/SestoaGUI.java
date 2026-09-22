package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import businessLogic.BLFacade;
import domain.Sale;
import domain.Seller;

public class SestoaGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel model;
    private Seller user;
    private ProfilaGUI gurasoa; 

    public SestoaGUI(Seller user, ProfilaGUI gurasoa) {
    	ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
        this.user = user;
        this.gurasoa = gurasoa;

        setTitle(ResourceBundle.getBundle("Etiquetas").getString("SestoaGUI.MainTitle")+" - " + user.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 500, 400);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(0, 10));
        setContentPane(contentPane);

        JLabel lblIzenburua = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("SestoaGUI.Title")); //$NON-NLS-1$ //$NON-NLS-2$
        lblIzenburua.setFont(new Font("Segoe UI", Font.BOLD, 14));
        contentPane.add(lblIzenburua, BorderLayout.NORTH);

        // Taula: Saltzaileak eta produktu kopurua
        String[] kolumnak = {ResourceBundle.getBundle("Etiquetas").getString("SestoaGUI.Seller"), 
        		ResourceBundle.getBundle("Etiquetas").getString("SestoaGUI.ProductAmount"), 
        		ResourceBundle.getBundle("Etiquetas").getString("SestoaGUI.SellerObject")};
        model = new DefaultTableModel(kolumnak, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(model);
        // Saltzaile objektuaren zutabea ezkutatu
        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(2));
        
        // Klik bikoitza egitean saltzaile horren xehetasunak ireki
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        Seller s = (Seller) model.getValueAt(row, 2);
                        irekiSaltzaileSestoa(s);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setFocusable(false);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JButton btnItxi = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
        btnItxi.setFocusable(false);
        btnItxi.addActionListener(e -> {
            if (gurasoa != null) gurasoa.freskatuDatuak();
            dispose();
        });
        contentPane.add(btnItxi, BorderLayout.SOUTH);

        freskatuTaula();
        this.setLocationRelativeTo(null);
    }

    public void freskatuTaula() {
        model.setRowCount(0);
        BLFacade facade = MainGUI.getBusinessLogic();
        this.user = facade.getUser(user.getEmail());
        
        List<Sale> saskia = user.getBasket();
        if (saskia != null && !saskia.isEmpty()) {
            // Saltzaileka taldekatu (Emaila -> Saltzaile objektua)
            Map<String, Seller> saltzaileakMap = new HashMap<>();
            Map<String, Integer> kopuruaMap = new HashMap<>();

            for (Sale s : saskia) {
                String email = s.getSeller().getEmail();
                saltzaileakMap.put(email, s.getSeller());
                kopuruaMap.put(email, kopuruaMap.getOrDefault(email, 0) + 1);
            }

            for (String email : saltzaileakMap.keySet()) {
                model.addRow(new Object[]{saltzaileakMap.get(email).getName(), kopuruaMap.get(email), saltzaileakMap.get(email)});
            }
        }
    }

    private void irekiSaltzaileSestoa(Seller s) {
        // Leiho berria irekiko dugu saltzaile horren produktuak bakarrik ikusteko
        SestoaSaltzaileanGUI sGUI = new SestoaSaltzaileanGUI(this.user, s, this);
        sGUI.setVisible(true);
    }
}