package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;
import java.util.List;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import businessLogic.BLFacade;
import domain.Sale;
import domain.Seller;
import domain.Demand;

public class ProfilaGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Seller user;
    private String hasierakoRola;
    private JLabel lblSaldo;
    private JTabbedPane tabbedPane;

    public ProfilaGUI(Seller user, String rola) {
    	ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
        BLFacade facade = MainGUI.getBusinessLogic();
        Seller eguneratua = facade.getUser(user.getEmail()); 
        this.user = (eguneratua != null) ? eguneratua : user;
        this.hasierakoRola = rola;

        setTitle(ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Profile") + " - " + this.user.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 850, 600); 
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(0, 10));
        setContentPane(contentPane);

        // --- GOIKO PARTEA: Erabiltzaile informazioa eta Saldoa ---
        JPanel panelInfo = new JPanel(new GridLayout(1, 3, 10, 0));
        JLabel lblUser = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Hello") + this.user.getName());
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        lblSaldo = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Money") + this.user.getDirua() + "€", SwingConstants.CENTER);
        lblSaldo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSaldo.setForeground(new Color(0, 128, 0));
        
        JButton btnTrantsakzioak = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Wallet"));
        btnTrantsakzioak.addActionListener(e -> {
            TransakzioakGUI tGUI = new TransakzioakGUI(this.user, this);
            tGUI.setVisible(true);
            this.setVisible(false);
        });
        
        panelInfo.add(lblUser); 
        panelInfo.add(lblSaldo); 
        panelInfo.add(btnTrantsakzioak);
        contentPane.add(panelInfo, BorderLayout.NORTH);

        // --- ERDIKO PARTEA: Fitxak ---
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        freskatuTabbedPane(); 

        if (hasierakoRola.equals("Saltzailea")) tabbedPane.setSelectedIndex(1);
        else if (hasierakoRola.equals("Eskatzailea")) tabbedPane.setSelectedIndex(2);
        else tabbedPane.setSelectedIndex(0);

        contentPane.add(tabbedPane, BorderLayout.CENTER);

        // --- BEHEKO PARTEA: Ekintza Botoiak ---
        JPanel panelBotoiak = new JPanel();
        contentPane.add(panelBotoiak, BorderLayout.SOUTH);

        JButton btnSaskia = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Basket")); //$NON-NLS-1$ //$NON-NLS-2$
        btnSaskia.setBackground(new Color(255, 215, 0)); 
        btnSaskia.addActionListener(e -> {
            SestoaGUI sGUI = new SestoaGUI(this.user, this);
            sGUI.setVisible(true);
            sGUI.addWindowListener(new WindowAdapter() {
                @Override public void windowClosed(WindowEvent e) { freskatuDatuak(); }
            });
        });
        panelBotoiak.add(btnSaskia);

        JButton btnErosi = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.FindProducts"));
        btnErosi.addActionListener(e -> {
            ErosketaAukeraketaGUI aukeraketaGUI = new ErosketaAukeraketaGUI(this.user); 
            aukeraketaGUI.setVisible(true);
        });
        panelBotoiak.add(btnErosi);
        
        JButton btnEskaria = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.AskForButton"));
        btnEskaria.addActionListener(e -> {
        	EskariaGUI eGUI = new EskariaGUI(this.user.getEmail());
            eGUI.setVisible(true);
            eGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    freskatuDatuak();
                }
            });
        });
        panelBotoiak.add(btnEskaria);

        JButton btnSaldu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.SellProduct"));
        btnSaldu.addActionListener(e -> {
            new SalduAukeratuGUI(this.user).setVisible(true);
        });
        panelBotoiak.add(btnSaldu);

        JButton btnItxi = new JButton("Logout");
        btnItxi.setBackground(new Color(255, 102, 102));
        btnItxi.addActionListener(e -> {
            MainGUI.unekoErabiltzailea = null; 
            dispose();
            new SaioaHasiGUI().setVisible(true); 
        });
        panelBotoiak.add(btnItxi);
        
        this.setLocationRelativeTo(null);
    }

    public void freskatuDatuak() {
        BLFacade facade = MainGUI.getBusinessLogic();
        this.user = facade.getUser(this.user.getEmail()); 
        if (this.user != null) {
            MainGUI.unekoErabiltzailea = this.user; 
            lblSaldo.setText(ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Money") + this.user.getDirua() + "€");
            int selected = tabbedPane.getSelectedIndex();
            freskatuTabbedPane(); 
            if(selected != -1) tabbedPane.setSelectedIndex(selected);
        }
        this.revalidate();
        this.repaint();
    }

    private void freskatuTabbedPane() {
        tabbedPane.removeAll();
        tabbedPane.addTab(ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.BuyerProfile"), sortuEroslePanela()); 
        tabbedPane.addTab(ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.SellerProfile"), sortuSaltzailePanela());
        tabbedPane.addTab(ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.DemanderProfile"), sortuEskatzailePanela());
    }

    private JPanel sortuEroslePanela() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] kolumnak = {
            ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Product"), 
            ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Price"), 
            ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Date"), 
            ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Reclamation"), 
            "Obj"
        };
        
        DefaultTableModel model = new DefaultTableModel(kolumnak, 0);
        
        if (user.getErosketak() != null) {
            for (Sale s : user.getErosketak()) {
                if (s != null) {
                    // Datuak lortu, null balioak kudeatuz
                    String izena = (s.getTitle() != null) ? s.getTitle() : ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.NoName");
                    float prezioa = s.getPrice();
                    Date data = s.getPublicationDate();
                    
                    // Erreklamazio mezua
                    String kexaInfo = (s.getReclamation() == null) ? 
                        ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.PutReclamation") : 
                        ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.SeeReclamationState");

                    // Datuak gehitu (ziurtatu prezioa ez dela 0.0 hutsik badago)
                    model.addRow(new Object[] { 
                        izena, 
                        prezioa + "€", 
                        (data != null ? data : ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.NoDate")), 
                        kexaInfo, 
                        s 
                    });
                }
            }
        }
        
        JTable tableErosketak = new JTable(model);
        tableErosketak.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                int row = tableErosketak.rowAtPoint(mouseEvent.getPoint());
                int col = tableErosketak.columnAtPoint(mouseEvent.getPoint());
                if (row != -1 && col == 3) { 
                    Sale s = (Sale) model.getValueAt(row, 4);
                    if (s.getReclamation() == null) {
                        new ErreklamazioaSortuGUI(s, ProfilaGUI.this).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.State")+": " + s.getReclamation().getEgoera());
                    }
                }
            }
        });

        tableErosketak.getColumnModel().removeColumn(tableErosketak.getColumnModel().getColumn(4));
        panel.add(new JScrollPane(tableErosketak), BorderLayout.CENTER);
        return panel;
    }

    private JPanel sortuSaltzailePanela() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 10));
        JPanel panelSaldu = new JPanel(new BorderLayout());
        String[] kol = {
            ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Product"), 
            ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Price"),
            ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Buyer")
        };
        DefaultTableModel modSaldu = new DefaultTableModel(kol, 0);
        
        if (user.getSaldutakoak() != null) {
            for (Sale s : user.getSaldutakoak()) {
                if (s != null) {
                    String izena = s.getTitle();
                    float prezioa = s.getPrice();
                    String erosleaInfo = (s.getBuyer() != null) ? s.getBuyer().getEmail() : ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.DeletedProdut");
                    
                    modSaldu.addRow(new Object[] { izena, prezioa + "€", erosleaInfo });
                }
            }
        }
        panelSaldu.add(new JScrollPane(new JTable(modSaldu)), BorderLayout.CENTER);
        panelSaldu.add(new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.SellerProfile")), BorderLayout.NORTH);
        panel.add(panelSaldu);
        return panel;
    }

    
    private JPanel sortuEskatzailePanela() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] kolumnak = {ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.DemandName"), 
        		ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.MaxPrice"), 
        		ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Action"), 
        		ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Action")};
        
        final DefaultTableModel model = new DefaultTableModel(kolumnak, 0) {
            @Override public boolean isCellEditable(int row, int column) { return column == 2; }
        };
        
        List<Demand> demands = user.getDemands();
        if (demands != null) {
            for (Demand d : demands) {
                model.addRow(new Object[] { d.getTitle(), d.getMaxPrice() + "€", ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Delete"), d });
            }
        }
        
        JTable tableDemands = new JTable(model);
        tableDemands.getColumnModel().removeColumn(tableDemands.getColumnModel().getColumn(3));
        tableDemands.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
        tableDemands.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox(), tableDemands));

        tableDemands.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tableDemands.getSelectedColumn() != 2) { 
                    int viewRow = tableDemands.getSelectedRow();
                    if (viewRow != -1) {
                        int modelRow = tableDemands.convertRowIndexToModel(viewRow);
                        Demand d = (Demand) model.getValueAt(modelRow, 3);
                        new OfertakGUI(d).setVisible(true);
                    }
                }
            }
        });

        panel.add(new JScrollPane(tableDemands), BorderLayout.CENTER);
        return panel;
    }

    // --- BOTOIA RENDERIZATZEKO KLASEAK ---
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() { setOpaque(true); }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            setBackground(new Color(255, 102, 102));
            setForeground(Color.WHITE);
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private JTable table;

        public ButtonEditor(JCheckBox checkBox, JTable t) {
            super(checkBox);
            this.table = t;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            return button;
        }

        public Object getCellEditorValue() {
            int row = table.getSelectedRow();
            if (row != -1) {
                int modelRow = table.convertRowIndexToModel(row);
                Demand d = (Demand) table.getModel().getValueAt(modelRow, 3);
                int response = JOptionPane.showConfirmDialog(button, ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Sure") + d.getTitle() + ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.SureDelete"));
                if (response == JOptionPane.YES_OPTION) {
                    if (MainGUI.getBusinessLogic().deleteDemand(d)) {
                        freskatuDatuak();
                    }
                }
            }
            return label;
        }
    }
}