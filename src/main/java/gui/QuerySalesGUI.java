package gui;

import businessLogic.BLFacade;
import configuration.UtilDate;
import domain.Sale;
import domain.Seller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class QuerySalesGUI extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private final JLabel jLabelProducts = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("QuerySalesGUI.Products")); 

    private JButton jButtonSearch = new JButton(ResourceBundle.getBundle("Etiquetas").getString("QuerySalesGUI.Search")); 
    private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));

    private JScrollPane scrollPanelProducts = new JScrollPane();
    private JTable tableProducts = new JTable();
    private DefaultTableModel tableModelProducts;

    private JFrame thisFrame; 
    private Seller unekoErabiltzailea; 

    private String[] columnNamesProducts = new String[] {
            ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.Title"), 
            ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.Price"),
            ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.PublicationDate"),
    };
    private JTextField jTextFieldSearch;

    public QuerySalesGUI(Seller user) {
    	ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
        this.unekoErabiltzailea = user;
        this.thisFrame = this;
        
        setBackground(Color.WHITE);
        getContentPane().setLayout(null);
        this.setSize(new Dimension(700, 520));
        this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("QuerySalesGUI.FindProducts"));

        tableProducts.setRowHeight(25);
        tableProducts.setEnabled(true); 
        tableProducts.setRowSelectionAllowed(true);
        tableProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableProducts.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        jLabelProducts.setFont(new Font("Segoe UI", Font.BOLD, 15));
        jLabelProducts.setBounds(52, 108, 427, 16);
        getContentPane().add(jLabelProducts);

        jButtonClose.setBounds(270, 410, 150, 40);
        jButtonClose.addActionListener(e -> thisFrame.dispose());
        getContentPane().add(jButtonClose);

        scrollPanelProducts.setBounds(52, 137, 575, 240);
        getContentPane().add(scrollPanelProducts);

        tableModelProducts = new DefaultTableModel(null, columnNamesProducts) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableProducts.setModel(tableModelProducts);
        scrollPanelProducts.setViewportView(tableProducts);

        jTextFieldSearch = new JTextField();
        jTextFieldSearch.setBorder(new MatteBorder(0, 0, 2, 0, new Color(64, 224, 208)));
        jTextFieldSearch.setBounds(52, 56, 379, 26);
        getContentPane().add(jTextFieldSearch);

        jButtonSearch.setBounds(463, 47, 164, 42);
        jButtonSearch.addActionListener(e -> eguneratuTaula());
        getContentPane().add(jButtonSearch);

        tableProducts.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() == 2) {
                    int row = tableProducts.getSelectedRow();
                    if (row != -1) {
                        Sale s = (Sale) tableModelProducts.getValueAt(row, 3);
                        // Hemen ShowSaleGUI deitzen dugu, uneko leihoa (thisFrame) pasatuz
                        new ShowSaleGUI(s, thisFrame).setVisible(true);
                    }
                }
            }
        });
        this.setLocationRelativeTo(null);
    }

    // Metodo hau publikoa izan behar da ShowSaleGUI-tik deitu ahal izateko
    public void freskatuTaula() {
        eguneratuTaula();
    }

    private void eguneratuTaula() {
        try {
            tableModelProducts.setDataVector(null, columnNamesProducts);
            tableModelProducts.setColumnCount(4); 
            BLFacade facade = MainGUI.getBusinessLogic();
            Date today = UtilDate.trim(new Date());
            
            // Datu-basetik salmenta guztiak lortu
            List<domain.Sale> sales = facade.getPublishedSales(jTextFieldSearch.getText(), today);

            if (sales != null) {
                for (domain.Sale sale : sales) {
                    // BALDINTZA: Bakarrik guraso den demand-ik ez dutenak (null direnak)
                    // Eta bide batez, erosita ez daudenak (isSold == false)
                    if (sale.getParentDemand() == null && !sale.isSold()) {
                        Vector<Object> row = new Vector<Object>();
                        row.add(sale.getTitle());
                        row.add(sale.getPrice() + " €");
                        row.add(new SimpleDateFormat("dd-MM-yyyy").format(sale.getPublicationDate()));
                        row.add(sale); 
                        tableModelProducts.addRow(row);     
                    }
                }
            }
            // Ezkutatu objektuaren zutabea
            tableProducts.getColumnModel().getColumn(3).setMinWidth(0);
            tableProducts.getColumnModel().getColumn(3).setMaxWidth(0);
            tableProducts.getColumnModel().getColumn(3).setPreferredWidth(0);
            
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}