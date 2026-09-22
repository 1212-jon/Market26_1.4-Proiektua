package gui;

import businessLogic.BLFacade;
import domain.Seller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.MatteBorder;

public class QuerySellersGUI extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private final JLabel jLabelSellers = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("QuerySellers.ActiveSellers"));  //$NON-NLS-1$ //$NON-NLS-2$
    private JButton jButtonSearch = new JButton(ResourceBundle.getBundle("Etiquetas").getString("QuerySalesGUI.Search")); 
    private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
    private JTextField jTextFieldSearch = new JTextField();
    
    private JScrollPane scrollPanelSellers = new JScrollPane();
    private JTable tableSellers = new JTable();
    private DefaultTableModel tableModelSellers;

    private JFrame thisFrame; 
    private Seller unekoErabiltzailea; 

    private String[] columnNamesSellers = new String[] {
    		ResourceBundle.getBundle("Etiquetas").getString("QuerySellers.Name"), 
    		ResourceBundle.getBundle("Etiquetas").getString("QuerySellers.Mail"),
    		ResourceBundle.getBundle("Etiquetas").getString("QuerySellers.Object") // Ezkutuko zutabea objektua gordetzeko
    };

    public QuerySellersGUI(Seller user) {
    	ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
        this.unekoErabiltzailea = user;
        this.thisFrame = this;
        
        getContentPane().setBackground(Color.WHITE);
        getContentPane().setLayout(null);
        this.setSize(new Dimension(600, 500));
        this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("QuerySellers.MainTitle"));

        // Bilaketa koadroa
        jTextFieldSearch.setBorder(new MatteBorder(0, 0, 2, 0, new Color(64, 224, 208)));
        jTextFieldSearch.setBounds(50, 40, 320, 30);
        getContentPane().add(jTextFieldSearch);

        // Bilatu botoia
        jButtonSearch.setBounds(400, 35, 130, 40);
        jButtonSearch.addActionListener(e -> eguneratuTaula());
        getContentPane().add(jButtonSearch);

        jLabelSellers.setFont(new Font("Segoe UI", Font.BOLD, 15));
        jLabelSellers.setBounds(50, 90, 400, 20);
        getContentPane().add(jLabelSellers);

        // Taula konfiguratu
        scrollPanelSellers.setBounds(50, 120, 480, 250);
        getContentPane().add(scrollPanelSellers);

        tableModelSellers = new DefaultTableModel(null, columnNamesSellers) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableSellers.setModel(tableModelSellers);
        tableSellers.setRowHeight(25);
        scrollPanelSellers.setViewportView(tableSellers);
        
        // Ezkutatu 'Objektua' zutabea
        tableSellers.getColumnModel().getColumn(2).setMinWidth(0);
        tableSellers.getColumnModel().getColumn(2).setMaxWidth(0);
        tableSellers.getColumnModel().getColumn(2).setPreferredWidth(0);

        // Itxi botoia
        jButtonClose.setBounds(225, 400, 150, 40);
        jButtonClose.addActionListener(e -> thisFrame.dispose());
        getContentPane().add(jButtonClose);

        // KLIK BIKOITZA LOGIKA
        tableSellers.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() == 2) {
                    int row = tableSellers.getSelectedRow();
                    if (row != -1) {
                        Seller aukeratutakoSaltzailea = (Seller) tableModelSellers.getValueAt(row, 2);
                        // Ireki saltzaile horren produktuak erakutsiko dituen leihoa
                        new SellerSalesGUI(unekoErabiltzailea, aukeratutakoSaltzailea).setVisible(true);
                    }
                }
            }
        });

        this.setLocationRelativeTo(null);
        eguneratuTaula(); // Hasieran denak kargatu
    }

    private void eguneratuTaula() {
        try {
            tableModelSellers.setDataVector(null, columnNamesSellers);
            // Ezkutuko zutabea berriz definitu behar da setDataVector egin ondoren
            tableSellers.getColumnModel().getColumn(2).setMinWidth(0);
            tableSellers.getColumnModel().getColumn(2).setMaxWidth(0);

            BLFacade facade = MainGUI.getBusinessLogic();
            String testua = jTextFieldSearch.getText();
            List<Seller> sellers = facade.getActiveSellers(testua);

            if (sellers != null) {
                for (Seller s : sellers) {
                    Vector<Object> row = new Vector<Object>();
                    row.add(s.getName());
                    row.add(s.getEmail());
                    row.add(s); // Objektu osoa gorde klik bikoitzerako
                    tableModelSellers.addRow(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}