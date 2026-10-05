package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import businessLogic.BLFacade;
import domain.Demand;
import domain.Sale;

public class OfertakGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tableOfertak;
    private DefaultTableModel modelOfertak;
    private Demand demand;
    
    private String[] kolumnak = {
        ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Product"),
        ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Price"),
        ResourceBundle.getBundle("Etiquetas").getString("SestoaGUI.Seller"),
        ResourceBundle.getBundle("Etiquetas").getString("SestoaSaltzaileanGUI.Object") // Ezkutuko zutabea objektua gordetzeko
    };

    public OfertakGUI(Demand d) {
    	ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
        this.demand = d;
        
        setTitle(ResourceBundle.getBundle("Etiquetas").getString("OfertakGUI.MainTitle")+": " + demand.getTitle());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(0, 10));
        setContentPane(contentPane);

        // Titulua eta informazioa
        JPanel panelInfo = new JPanel(new GridLayout(2, 1));
        JLabel lblTitulua = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("OfertakGUI.Title"));
        lblTitulua.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel lblDeskribapena = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("OfertakGUI.Subtitle")+": " + demand.getTitle() + " (Max: " + demand.getMaxPrice() + "€)");
        panelInfo.add(lblTitulua);
        panelInfo.add(lblDeskribapena);
        contentPane.add(panelInfo, BorderLayout.NORTH);

        // Taula konfiguratu
        modelOfertak = new DefaultTableModel(kolumnak, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tableOfertak = new JTable(modelOfertak);
        // Ezkutatu 'Object' zutabea
        tableOfertak.getColumnModel().removeColumn(tableOfertak.getColumnModel().getColumn(3));

        // Eskaintzak kargatu
        kargatuEskaintzak();

        // Klik bikoitzaren logika
        tableOfertak.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tableOfertak.getSelectedRow();
                    if (row != -1) {
                        Sale s = (Sale) modelOfertak.getValueAt(row, 3);
                        ShowSaleGUI sGUI = new ShowSaleGUI(s, OfertakGUI.this);
                        sGUI.setVisible(true);
                    }
                }
            }
        });

        contentPane.add(new JScrollPane(tableOfertak), BorderLayout.CENTER);

        // Itxi botoia
        JButton btnItxi = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
        btnItxi.addActionListener(e -> dispose());
        contentPane.add(btnItxi, BorderLayout.SOUTH);
        
        this.setLocationRelativeTo(null);
    }

    private void kargatuEskaintzak() {
        try {
            BLFacade facade = MainGUI.getBusinessLogic();
            List<Sale> eskaintzak = facade.getOffersForDemand(demand);
            
            modelOfertak.setRowCount(0); // Taula garbitu
            
            if (eskaintzak != null && !eskaintzak.isEmpty()) {
                for (Sale s : eskaintzak) {
                    // ZUZENDUTA: Baldintza hirukoitza
                    // 1. Produktua saldu gabe egon behar da.
                    // 2. Saltzaileak existitu behar du (null check).
                    // 3. Saltzaileak AKTIBO egon behar du (kontua itxi ez izana).
                    if (!s.isSold() && s.getSeller() != null && s.getSeller().isAktibo()) {
                        Object[] row = new Object[4];
                        row[0] = s.getTitle();
                        row[1] = s.getPrice() + "€";
                        row[2] = s.getSeller().getName();
                        row[3] = s;
                        modelOfertak.addRow(row);
                    }
                }
            }
            
            if (modelOfertak.getRowCount() == 0) {
                System.out.println(ResourceBundle.getBundle("Etiquetas").getString("OfertakGUI.NoDemand"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}