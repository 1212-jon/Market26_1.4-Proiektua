package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;
import domain.Seller;
import java.awt.Toolkit;

public class KontuaItxiGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JList<Seller> userList;
	private DefaultListModel<Seller> listModel;

	public KontuaItxiGUI() {
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("KontuaItxi.Tittle")); 
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 400);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 10));

		// Izenburua (image_9836f7.png-ko estiloaren antzekoa)
		JLabel lblTitle = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("KontuaItxi.Tittle")); //$NON-NLS-1$ //$NON-NLS-2$
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblTitle, BorderLayout.NORTH);

		// Erabiltzaile zerrenda kudeatzeko modeloa
		listModel = new DefaultListModel<>();
		userList = new JList<>(listModel);
		userList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		
		// Datuak kargatu DataAccess-etik (BLFacade bidez)
		refreshList();

		JScrollPane scrollPane = new JScrollPane(userList);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		// Kontua itxi botoia
		JButton btnItxi = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Accept")); //$NON-NLS-1$ //$NON-NLS-2$
		btnItxi.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnItxi.setForeground(Color.WHITE);
		btnItxi.setBackground(new Color(64, 224, 208)); // Turkesa kolorea
		
		btnItxi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Seller hautatua = userList.getSelectedValue();
				
				if (hautatua != null) {
					int aukera = JOptionPane.showConfirmDialog(null, 
							ResourceBundle.getBundle("Etiquetas").getString("KontuaItxiGUI.Sure") + hautatua.getEmail() + ResourceBundle.getBundle("Etiquetas").getString("KontuaItxiGUI.Ban"), 
							ResourceBundle.getBundle("Etiquetas").getString("Accept"), JOptionPane.YES_NO_OPTION);
					
					if (aukera == JOptionPane.YES_OPTION) {
						BLFacade facade = MainGUI.getBusinessLogic();
						// Kontua itxi (ezabatu eta zerrenda beltzera gehitu)
						boolean ondo = facade.kontuaItxi(hautatua.getEmail());
						
						if (ondo) {
							JOptionPane.showMessageDialog(null, ResourceBundle.getBundle("Etiquetas").getString("KontuaItxiGUI.CorrectBanAlert"));
							refreshList(); // Zerrenda eguneratu
						} else {
							JOptionPane.showMessageDialog(null, ResourceBundle.getBundle("Etiquetas").getString("KontuaItxiGUI.IncorrectBanErrorAlert"), "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, ResourceBundle.getBundle("Etiquetas").getString("KontuaItxiGUI.Select"));
				}
			}
		});
		
		contentPane.add(btnItxi, BorderLayout.SOUTH);
	}
	
	

	/**
	 * Zerrenda eguneratzen du datu-basean dauden Seller guztiak erakutsiz
	 */
	private void refreshList() {
		listModel.removeAllElements();
		BLFacade facade = MainGUI.getBusinessLogic();
		List<Seller> erabiltzaileak = facade.getAllSellers();
		
		for (Seller s : erabiltzaileak) {
			listModel.addElement(s);
		}
	}
}