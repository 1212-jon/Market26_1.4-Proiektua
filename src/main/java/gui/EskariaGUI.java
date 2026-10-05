package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;
import configuration.UtilDate;

import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.awt.event.ActionEvent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import java.awt.Color;
import java.awt.Toolkit;

public class EskariaGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldIzena;
	private JLabel jLabelMsg;
	private JTextField textFieldDeskribapena;
	private JTextField textFieldPrezioa;
	private String sellerMail;

	/**
	 * Create the frame.
	 */
	public EskariaGUI(String mail) {
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
		this.sellerMail = mail;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 562, 385);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textFieldIzena = new JTextField();
		textFieldIzena.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldIzena.setBounds(134, 36, 303, 28);
		contentPane.add(textFieldIzena);
		textFieldIzena.setColumns(10);
		
		JLabel lblIzena = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("EskariaGUI.Name"));
		lblIzena.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblIzena.setBounds(32, 43, 92, 14);
		contentPane.add(lblIzena);
		
		JLabel lblAzalpena = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("EskariaGUI.Title"));
		lblAzalpena.setHorizontalAlignment(SwingConstants.CENTER);
		lblAzalpena.setBounds(32, 11, 484, 14);
		contentPane.add(lblAzalpena);
		
		JLabel lblDeskribapena = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("EskariaGUI.Description")); //$NON-NLS-1$ //$NON-NLS-2$
		lblDeskribapena.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDeskribapena.setBounds(32, 100, 96, 14);
		contentPane.add(lblDeskribapena);
		
		JLabel lblPrezioa = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("EskariaGUI.MaxPrice")); //$NON-NLS-1$ //$NON-NLS-2$
		lblPrezioa.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPrezioa.setBounds(32, 215, 128, 17);
		contentPane.add(lblPrezioa);
		
		JButton btnEskatu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Accept")); //$NON-NLS-1$ //$NON-NLS-2$
		btnEskatu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jLabelMsg.setText("");
                String error = check_fields_Errors();
                
                if (error != null) {
                    jLabelMsg.setText(error);
                } else {
                    try {
                        // Logika geruzara atzitu
                        BLFacade facade = MainGUI.getBusinessLogic();
                        
                        // Balioak testu-eremuetatik jaso
                        String titulua = textFieldIzena.getText();
                        String deskribapena = textFieldDeskribapena.getText();
                        float prezioa = Float.parseFloat(textFieldPrezioa.getText());
                        
                        // Eskaera sortu datu-basean
                        facade.createDemand(titulua, deskribapena, prezioa, sellerMail);
                        
                        // Mezua erakutsi hizkuntza fitxategiko etiketa erabiliz
                        jLabelMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.ProductCreated"));
                        
                        // Oharra: Nahi izanez gero, eremuak garbitu ditzakezu hemen
                        // textFieldIzena.setText("");
                        // textFieldDeskribapena.setText("");
                        // textFieldPrezioa.setText("");
                        
                    } catch (Exception e1) {
                        jLabelMsg.setText(e1.getMessage());
                    }
                }
            }
		});
		btnEskatu.setFocusable(false);
		btnEskatu.setBounds(229, 258, 89, 43);
		contentPane.add(btnEskatu);
		
		JLabel lblPrezioaEuro = new JLabel("€");
		lblPrezioaEuro.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPrezioaEuro.setBounds(328, 215, 128, 17);
		contentPane.add(lblPrezioaEuro);
		
		jLabelMsg = new JLabel();
		jLabelMsg.setForeground(Color.RED);
		jLabelMsg.setBounds(32, 312, 484, 14);
		contentPane.add(jLabelMsg);
		
		textFieldDeskribapena = new JTextField();
		textFieldDeskribapena.setBounds(134, 99, 303, 100);
		contentPane.add(textFieldDeskribapena);
		textFieldDeskribapena.setColumns(10);
		
		textFieldPrezioa = new JTextField();
		textFieldPrezioa.setBounds(229, 215, 89, 20);
		contentPane.add(textFieldPrezioa);
		textFieldPrezioa.setColumns(10);

	}
private String check_fields_Errors() {
		
		try {
			if ((textFieldIzena.getText().length()==0) || (textFieldDeskribapena.getText().length()==0)  || (textFieldPrezioa.getText().length()==0))
				return ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.ErrorQuery");
			else {

				// trigger an exception if the introduced string is not a number
					float price = Float.parseFloat(textFieldPrezioa.getText());
					if (price <= 0) 
						return ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.PriceMustBeGreaterThan0");
					
					else 
						return null;
			}
		} catch (java.lang.NumberFormatException e1) {

			return  ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.ErrorNumber");		
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;

		}
	}
}
