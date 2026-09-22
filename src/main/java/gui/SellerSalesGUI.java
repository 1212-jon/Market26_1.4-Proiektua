package gui;

import businessLogic.BLFacade;
import domain.Sale;
import domain.Seller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class SellerSalesGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private final JLabel jLabelSellerName = new JLabel();
	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));

	private JScrollPane scrollPanelProducts = new JScrollPane();
	private JTable tableProducts = new JTable();
	private DefaultTableModel tableModelProducts;

	private JFrame thisFrame;
	private Seller unekoErabiltzailea;
	private Seller saltzaileAukeratua;

	private String[] columnNamesProducts = new String[] {
			ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.Title"), 
			ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.Price"),
			ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.PublicationDate"),
			"Objektua" // Ezkutuko zutabea
	};

	public SellerSalesGUI(Seller user, Seller targetSeller) {
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
		this.unekoErabiltzailea = user;
		this.saltzaileAukeratua = targetSeller;
		this.thisFrame = this;

		getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(null);
		this.setSize(new Dimension(650, 450));
		this.setTitle(targetSeller.getName());

		// Saltzailearen izena goian
		jLabelSellerName.setText(targetSeller.getName());
		jLabelSellerName.setFont(new Font("Segoe UI", Font.BOLD, 16));
		jLabelSellerName.setBounds(50, 20, 500, 30);
		getContentPane().add(jLabelSellerName);

		// Taula konfiguratu
		scrollPanelProducts.setBounds(50, 70, 530, 250);
		getContentPane().add(scrollPanelProducts);

		tableModelProducts = new DefaultTableModel(null, columnNamesProducts) {
			@Override
			public boolean isCellEditable(int row, int column) { return false; }
		};
		tableProducts.setModel(tableModelProducts);
		tableProducts.setRowHeight(25);
		scrollPanelProducts.setViewportView(tableProducts);

		// Ezkutatu 'Objektua' zutabea
		tableProducts.getColumnModel().getColumn(3).setMinWidth(0);
		tableProducts.getColumnModel().getColumn(3).setMaxWidth(0);
		tableProducts.getColumnModel().getColumn(3).setPreferredWidth(0);

		// Itxi botoia
		jButtonClose.setBounds(250, 350, 150, 40);
		jButtonClose.addActionListener(e -> thisFrame.dispose());
		getContentPane().add(jButtonClose);

		// KLIK BIKOITZA LOGIKA (ShowSaleGUI irekitzeko)
		tableProducts.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mouseEvent) {
				if(mouseEvent.getClickCount() == 2) {
					int row = tableProducts.getSelectedRow();
					if (row != -1) {
						Sale s = (Sale) tableModelProducts.getValueAt(row, 3);
						// ShowSaleGUI ireki, leiho hau (thisFrame) pasatuz freskatu ahal izateko
						new ShowSaleGUI(s, thisFrame).setVisible(true);
					}
				}
			}
		});

		this.setLocationRelativeTo(null);
		eguneratuTaula();
	}

	/**
	 * Taula eguneratzen du saltzailearen produktu erabilgarriekin.
	 * Publikoa da ShowSaleGUI-k erosketa ostean dei ahal izateko.
	 */
	public void freskatuTaula() {
		eguneratuTaula();
	}

	private void eguneratuTaula() {
	    try {
	        tableModelProducts.setDataVector(null, columnNamesProducts);
	        // Zutabeak berriz ezkutatu
	        tableProducts.getColumnModel().getColumn(3).setMinWidth(0);
	        tableProducts.getColumnModel().getColumn(3).setMaxWidth(0);

	        BLFacade facade = MainGUI.getBusinessLogic();
	        // Bakarrik saltzaile honen produktuak lortu
	        List<Sale> sales = facade.getSalesBySeller(saltzaileAukeratua.getEmail());

	        if (sales != null) {
	            for (Sale sale : sales) {
	                // GEHITUTAKO BALDINTZA: Bakarrik demandGurasoa null dutenak
	                if (sale.getParentDemand() == null) { 
	                    Vector<Object> row = new Vector<Object>();
	                    row.add(sale.getTitle());
	                    row.add(sale.getPrice() + " €");
	                    row.add(new SimpleDateFormat("dd-MM-yyyy").format(sale.getPublicationDate()));
	                    row.add(sale);
	                    tableModelProducts.addRow(row);
	                }
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
