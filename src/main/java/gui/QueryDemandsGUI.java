package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import businessLogic.BLFacade;
import domain.Demand;
import domain.Seller;
import java.awt.Toolkit;

public class QueryDemandsGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tableDemands;
    private DefaultTableModel modelDemands;
    private Seller currentUser;

    /**
     * Eraikitzailea: Pantaila sortu eta datuak kargatzen ditu.
     * @param user Saioa hasia duen erabiltzailea (Seller).
     */
    public QueryDemandsGUI(Seller user) {
    	ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
        // Segurtasun egiaztapena: Erabiltzailea identifikatu gabe badago, itxi.
        if (user == null) {
            JOptionPane.showMessageDialog(null, "Error: Erabiltzailea ez da identifikatu.");
            this.dispose();
            return;
        }
        
        this.currentUser = user;
        BLFacade facade = MainGUI.getBusinessLogic();

        setTitle(ResourceBundle.getBundle("Etiquetas").getString("QueryDemandsGUI.MainTitle"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 750, 450);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(0, 10));
        setContentPane(contentPane);

        // Taularen zutabeen izenak
        String[] columnNames = {
        		ResourceBundle.getBundle("Etiquetas").getString("QueryDemandsGUI.Name"), 
        		ResourceBundle.getBundle("Etiquetas").getString("QueryDemandsGUI.Description"), 
        		ResourceBundle.getBundle("Etiquetas").getString("QueryDemandsGUI.MaxPrice"), 
        		ResourceBundle.getBundle("Etiquetas").getString("QueryDemandsGUI.Demander"), 
        		ResourceBundle.getBundle("Etiquetas").getString("QueryDemandsGUI.Object") // Ezkutuko zutabea objektu osoa gordetzeko
        };

        // Modeloa definitu (ezin dira gelaxkak zuzenean editatu)
        modelDemands = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableDemands = new JTable(modelDemands);
        // 'Objektua' zutabea ezkutatu bistatik, baina modeloan utzi datuak berreskuratzeko
        tableDemands.getColumnModel().removeColumn(tableDemands.getColumnModel().getColumn(4));

        // Datuak kargatu eta iragazi
        List<Demand> allDemands = facade.getAllDemands(); 
        if (allDemands != null) {
            for (Demand d : allDemands) {
                // ZUZENDUTA: Egiaztatu claimant ez dela nulua (kontua itxi dutenentzat)[cite: 2]
                if (d.getClaimant() != null && d.getClaimant().getEmail() != null) {
                    
                    // Iragazi: Bakarrik beste erabiltzaileen eskaerak erakutsi (norberarenak ez)
                    if (!d.getClaimant().getEmail().equals(currentUser.getEmail())) {
                        modelDemands.addRow(new Object[]{
                            d.getTitle(), 
                            d.getDescription(), 
                            d.getMaxPrice() + "€", 
                            d.getClaimant().getName(), 
                            d // Hemen objektu osoa gordetzen dugu 4. zutabean
                        });
                    }
                }
            }
        }

        // Klik bikoitzaren bidez eskaintza egin
        tableDemands.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    eginEskaintza();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableDemands);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Beheko botoia
        JButton btnEskaintza = new JButton(ResourceBundle.getBundle("Etiquetas").getString("QueryDemandsGUI.MakeOffer")); //$NON-NLS-1$ //$NON-NLS-2$
        btnEskaintza.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEskaintza.addActionListener(e -> eginEskaintza());
        
        JPanel panelBotoia = new JPanel();
        panelBotoia.add(btnEskaintza);
        contentPane.add(panelBotoia, BorderLayout.SOUTH);

        // Goiko informazio testua
        JLabel lblInfo = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("QueryDemandsGUI.Title")); //$NON-NLS-1$ //$NON-NLS-2$
        contentPane.add(lblInfo, BorderLayout.NORTH);

        this.setLocationRelativeTo(null);
    }

    /**
     * Hautatutako eskaerari eskaintza egiteko logika.
     */
    private void eginEskaintza() {
        int row = tableDemands.getSelectedRow();
        if (row != -1) {
            // Berreskuratu ezkutuko Demand objektua (modelotik kargatutako 4. zutabea)
            Demand selectedDemand = (Demand) modelDemands.getValueAt(row, 4);
            
            // CreateSaleGUI-ra joan eraikitzaile berezia erabiliz (emaila eta eskaera)
            if (currentUser != null && currentUser.getEmail() != null) {
                CreateSaleGUI csGUI = new CreateSaleGUI(currentUser.getEmail(), selectedDemand);
                csGUI.setVisible(true);
                this.dispose(); // Pantaila hau itxi
            }
        } else {
            JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("QueryDemandsGUI.SelectErrorAlert"));
        }
    }
}