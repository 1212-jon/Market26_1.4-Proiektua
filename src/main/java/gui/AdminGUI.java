package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ResourceBundle;

import businessLogic.BLFacade;
import domain.Reclamation;

public class AdminGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableReclamations;
	private DefaultTableModel model;

	/**
	 * Aplikazioa abiarazteko main metodoa.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminGUI frame = new AdminGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Leihoaren eraikitzailea.
	 */
	public AdminGUI() {
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
		setTitle("Admin - " + ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Reclamation"));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 450);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(0, 10));
		setContentPane(contentPane);

		// 1. TAULAREN KONFIGURAZIOA
		String[] columnNames = {"ID", ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Buyer"),
        		ResourceBundle.getBundle("Etiquetas").getString("AdminGUI.Seller"), 
        		ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Product"), 
        		ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.Description"), 
        		ResourceBundle.getBundle("Etiquetas").getString("ProfilaGUI.State")};
		model = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Taula ez editagarria izateko
			}
		};
		tableReclamations = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(tableReclamations);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		// 2. BOTOIEN PANELA (BEHEAN)
		JPanel panelBotoiak = new JPanel();
		panelBotoiak.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
		contentPane.add(panelBotoiak, BorderLayout.SOUTH);

		// FRESKATU BOTOIA
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(e -> freskatuErreklamazioak());
		panelBotoiak.add(btnRefresh);

		// EGOERA ALDATU BOTOIA
		JButton btnAldatuEgoera = new JButton(ResourceBundle.getBundle("Etiquetas").getString("AdminGUI.ChangeState"));
		btnAldatuEgoera.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        int row = tableReclamations.getSelectedRow();
		        if (row != -1) {
		            // 1. Lortu IDa lehen zutabetik
		            int id = (int) model.getValueAt(row, 0); 
		            
		            // 2. Egoera hautatu
		            String[] aukerak = { ResourceBundle.getBundle("Etiquetas").getString("AdminGUI.Opened"), ResourceBundle.getBundle("Etiquetas").getString("AdminGUI.Admmited"), ResourceBundle.getBundle("Etiquetas").getString("AdminGUI.Refused"), ResourceBundle.getBundle("Etiquetas").getString("AdminGUI.Solved") };
		            String egoeraBerria = (String) JOptionPane.showInputDialog(
		                AdminGUI.this, 
		                ResourceBundle.getBundle("Etiquetas").getString("AdminGUI.SelectNewState"), 
		                ResourceBundle.getBundle("Etiquetas").getString("AdminGUI.ChangeState")+" (ID: " + id + ")",
		                JOptionPane.QUESTION_MESSAGE, 
		                null, 
		                aukerak, 
		                aukerak[0]);

		            if (egoeraBerria != null) {
		                String soluzioBerria = ""; // Hasieratu hutsik

		                // 3. Soluzioa idatzi (SOILIK "Ebatzita" bada)
		                if (egoeraBerria.equals("Ebatzita") || egoeraBerria.equals("Solved")||egoeraBerria.equals("Solucionado")) {
		                    soluzioBerria = JOptionPane.showInputDialog(
		                        AdminGUI.this, 
		                        ResourceBundle.getBundle("Etiquetas").getString("AdminGUI.WriteSolution"),
		                        ResourceBundle.getBundle("Etiquetas").getString("AdminGUI.AddSolution"),
		                        JOptionPane.PLAIN_MESSAGE);
		                    
		                    // Erabiltzaileak "Cancel" sakatzen badu soluzioan, prozesua eten
		                    if (soluzioBerria == null) return; 
		                }

		                // 4. Eguneraketa exekutatu
		                BLFacade facade = MainGUI.getBusinessLogic();
		                facade.updateReclamationStatus(id, egoeraBerria, soluzioBerria);
		                
		                freskatuErreklamazioak(); // Taula berritu aldaketa ikusteko
		                JOptionPane.showMessageDialog(AdminGUI.this, ResourceBundle.getBundle("Etiquetas").getString("AdminGUI.StateGood"));
		            }
		        } else {
		            JOptionPane.showMessageDialog(AdminGUI.this, ResourceBundle.getBundle("Etiquetas").getString("AdminGUI.SelectReclamation"));
		        }
		    }
		});
		panelBotoiak.add(btnAldatuEgoera);

		// 3. HASIERAKO DATUAK KARGATU
		freskatuErreklamazioak();
		
		setLocationRelativeTo(null); // Pantaila erdian zentratu
	}

	/**
	 * Datu-basetik erreklamazio guztiak ekarri eta taula berritzen du.
	 */
	private void freskatuErreklamazioak() {
		try {
			model.setRowCount(0); // Taula hustu
			BLFacade facade = MainGUI.getBusinessLogic();
			List<Reclamation> reclamations = facade.getAllReclamations();

			if (reclamations != null) {
				for (Reclamation r : reclamations) {
					model.addRow(new Object[]{
						r.getReclamationNumber(),
						(r.getEroslea() != null ? r.getEroslea().getEmail() : "---"),
						(r.getSaltzailea() != null ? r.getSaltzailea().getEmail() : "---"),
						(r.getProduktua() != null ? r.getProduktua().getTitle() : "---"),
						r.getDeskribapena(),
						r.getEgoera()
					});
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}