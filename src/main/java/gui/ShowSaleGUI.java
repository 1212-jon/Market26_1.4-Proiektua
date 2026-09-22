package gui;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.awt.image.BufferedImage;
import businessLogic.BLFacade;
import domain.Sale;
import javax.swing.border.LineBorder;

public class ShowSaleGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private BufferedImage targetImg;
	public JPanel panel_1;
	private static final int baseSize = 160;

	private JTextField fieldTitle = new JTextField();
	private JTextField fieldDescription = new JTextField();
	private JTextField fieldPrice = new JTextField();
	private JLabel labelStatus = new JLabel();
	private JLabel statusField = new JLabel();
	
	private JLabel jLabelTitle = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.Title"));
	private JLabel jLabelDescription = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.Description"));
	private JLabel jLabelProductStatus = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.Status"));
	private JLabel jLabelPrice = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.Price"));
	
	private JFrame thisFrame;
	private JFrame previousFrame;

	public ShowSaleGUI(Sale sale, JFrame previousFrame) {
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
		this.previousFrame = previousFrame;
		this.thisFrame = this;
		
		getContentPane().setBackground(Color.WHITE);
		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(604, 450)); 
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.MainTitle"));
		this.setLocationRelativeTo(null);

		// --- DATUAK KARGATU ---
		fieldTitle.setText(sale.getTitle());
		fieldTitle.setEditable(false);
		fieldTitle.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
		fieldTitle.setBounds(128, 23, 439, 26);
		getContentPane().add(fieldTitle);

		fieldDescription.setText(sale.getDescription());
		fieldDescription.setEditable(false);
		fieldDescription.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
		fieldDescription.setBounds(128, 63, 440, 73);
		getContentPane().add(fieldDescription);

		fieldPrice.setText(Float.toString(sale.getPrice()) + " €");
		fieldPrice.setEditable(false);
		fieldPrice.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
		fieldPrice.setBounds(128, 157, 100, 20);
		getContentPane().add(fieldPrice);

		labelStatus.setText(new SimpleDateFormat("dd-MM-yyyy").format(sale.getPublicationDate()));
		labelStatus.setHorizontalAlignment(SwingConstants.RIGHT);
		labelStatus.setBounds(301, 146, 266, 16);
		getContentPane().add(labelStatus);

		// --- LABEL-AK ---
		jLabelTitle.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		jLabelTitle.setBounds(16, 25, 92, 20);
		getContentPane().add(jLabelTitle);

		jLabelDescription.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		jLabelDescription.setBounds(16, 63, 109, 16);
		getContentPane().add(jLabelDescription);

		jLabelPrice.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		jLabelPrice.setBounds(16, 156, 101, 20);
		getContentPane().add(jLabelPrice);

		jLabelProductStatus.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		jLabelProductStatus.setBounds(16, 186, 140, 25);
		getContentPane().add(jLabelProductStatus);

		// --- IRUDIAREN PANELA ---
		panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(325, 170, 242, 170);
		getContentPane().add(panel_1);

		// --- BOTOIAK ---
		JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
		jButtonClose.setBounds(192, 350, 123, 30);
		jButtonClose.addActionListener(e -> thisFrame.dispose());
		this.getContentPane().add(jButtonClose);

		JButton btnErosi = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.BuyButton"));
		btnErosi.setBackground(new Color(46, 139, 87));
		btnErosi.setForeground(Color.WHITE);
		btnErosi.setBounds(42, 350, 114, 30);
		btnErosi.setOpaque(true);
		btnErosi.setBorderPainted(false);
		this.getContentPane().add(btnErosi);

		JButton btnSaskira = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.AddToCart"));
		btnSaskira.setBackground(new Color(255, 215, 0)); 
		btnSaskira.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnSaskira.setBounds(42, 257, 130, 40);
		btnSaskira.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.getContentPane().add(btnSaskira);

		JButton btnKendu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.DeleteButton"));
		btnKendu.setBackground(new Color(255, 127, 80));
		btnKendu.setForeground(Color.WHITE);
		btnKendu.setBounds(180, 257, 114, 40);
		btnKendu.setOpaque(true);
		btnKendu.setBorderPainted(false);
		btnKendu.setVisible(false); 
		this.getContentPane().add(btnKendu);

		// --- LOGIKA: BOTOIAK ETA EGOERA ---
		if (sale.isSold()) {
			btnErosi.setVisible(false);
			btnKendu.setVisible(false);
			btnSaskira.setVisible(false);
			jLabelProductStatus.setVisible(false);
			statusField.setText(ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.Selled"));
			statusField.setForeground(new Color(46, 139, 87));
			statusField.setFont(new Font("Segoe UI", Font.BOLD, 14));
			statusField.setBounds(16, 192, 150, 20); 
		} else {
			statusField.setText(Utils.getStatus(sale.getStatus()));
			statusField.setBounds(128, 192, 150, 16);
			
			if (MainGUI.unekoErabiltzailea != null) {
				if (sale.getSeller().getEmail().equals(MainGUI.unekoErabiltzailea.getEmail())) {
					btnKendu.setVisible(true);
					btnErosi.setEnabled(false);
					btnSaskira.setEnabled(false);
					btnSaskira.setToolTipText(ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.YourProduct"));
				}
			}
		}
		getContentPane().add(statusField);

		// --- SASKIRA GEHITU LOGIKA ---
		btnSaskira.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (MainGUI.unekoErabiltzailea == null) {
					JOptionPane.showMessageDialog(thisFrame, ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.LoginToAddToCartAlert"));
					return;
				}
				try {
					BLFacade facade = MainGUI.getBusinessLogic();
					boolean ondo = facade.addToBasket(MainGUI.unekoErabiltzailea.getEmail(), sale.getSaleNumber());
					if (ondo) {
						JOptionPane.showMessageDialog(thisFrame, ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.AddToCartCorrectAlert"));
						thisFrame.dispose();
					} else {
						JOptionPane.showMessageDialog(thisFrame, ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.CanNotAddToCartErrorAlert"), "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception ex) { ex.printStackTrace(); }
			}
		});

		// --- EROSI LOGIKA (ZUZENDUTA ETA AKTUALIZAZIOAREKIN) ---
		// --- EROSI LOGIKA (ZUZENDUTA ETA AKTUALIZAZIOAREKIN) ---
		btnErosi.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        if (MainGUI.unekoErabiltzailea == null) {
		            JOptionPane.showMessageDialog(thisFrame, ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.HaveToLogin"));
		            return;
		        }

		        int confirm = JOptionPane.showConfirmDialog(thisFrame, ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.SureBuy"), ResourceBundle.getBundle("Etiquetas").getString("Accept"), JOptionPane.YES_NO_OPTION);
		        if (confirm == JOptionPane.YES_OPTION) {
		            try {
		                BLFacade facade = MainGUI.getBusinessLogic();
		                
		                // 1. Erosketa gauzatu datu-basean
		                boolean ondo = facade.buySale(sale.getSaleNumber(), MainGUI.unekoErabiltzailea.getEmail());

		                if (ondo) {
		                    // --- GAKOA: ERABILTZAILEA EGUNERATU MEMORIAN ---
		                    // Datu-basetik erabiltzailearen bertsio berria ekarri (saldo berria eta erosketa zerrenda)
		                    MainGUI.unekoErabiltzailea = facade.getUser(MainGUI.unekoErabiltzailea.getEmail());
		                    
		                    JOptionPane.showMessageDialog(thisFrame, ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.BuyCorrectAlert"));

		                    // 2. ProfilaGUI freskatu (MainGUI.unekoErabiltzailea berria denez, datu berriak erakutsiko ditu)
		                    // Begiratu leiho guztien artean ProfilaGUI aurkitzeko eta freskatzeko
		                    Window[] windows = Window.getWindows();
		                    for (Window w : windows) {
		                        if (w instanceof ProfilaGUI) {
		                            ((ProfilaGUI) w).freskatuDatuak();
		                        }
		                        
		                        // Leiho laguntzaileak itxi (bilaketak, saskiak...) Profilera bueltatzeko
		                        if (w instanceof ErosketaAukeraketaGUI || w instanceof QuerySellersGUI || 
		                            w instanceof SellerSalesGUI || w instanceof QuerySalesGUI || 
		                            w instanceof SestoaGUI || w instanceof SestoaSaltzaileanGUI) { 
		                            w.dispose();
		                        }
		                    }
		                    
		                    thisFrame.dispose(); 
		                } else {
		                    JOptionPane.showMessageDialog(thisFrame, ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.IncorrectCurrencyErrorAlert"), "Error", JOptionPane.ERROR_MESSAGE);
		                }
		            } catch (Exception ex) { 
		                ex.printStackTrace(); 
		            }
		        }
		    }
		});
		// --- KENDU LOGIKA ---
		btnKendu.addActionListener(e -> {
			int confirm = JOptionPane.showConfirmDialog(thisFrame, ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.ShureText")+this.getTitle()+ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.DeleteQuestion"), ResourceBundle.getBundle("Etiquetas").getString("Accept"), JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				if (MainGUI.getBusinessLogic().removeSale(MainGUI.unekoErabiltzailea.getEmail(), sale)) {
					JOptionPane.showMessageDialog(thisFrame, ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.ComfirmDeleted"));
					if (previousFrame instanceof ProfilaGUI) {
						((ProfilaGUI) previousFrame).freskatuDatuak();
					}
					thisFrame.dispose();
				}
			}
		});

		// --- IRUDIA KARGATU ---
		BLFacade facade = MainGUI.getBusinessLogic();
		if (sale.getFile() != null) {
			try {
				Image img = facade.downloadImage(sale.getFile());
				if (img != null) {
					targetImg = rescale((BufferedImage) img);
					panel_1.setLayout(new BorderLayout());
					panel_1.add(new JLabel(new ImageIcon(targetImg)));
				}
			} catch (Exception ex) { ex.printStackTrace(); }
		}

		this.setVisible(true);
	}

	public BufferedImage rescale(BufferedImage originalImage) {
		BufferedImage resizedImage = new BufferedImage(baseSize, baseSize, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, baseSize, baseSize, null);
		g.dispose();
		return resizedImage;
	}
}