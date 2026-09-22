package gui;

import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.toedter.calendar.JCalendar;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import businessLogic.BLFacade;
import configuration.UtilDate;
import domain.Demand;
import javax.swing.border.LineBorder;

public class CreateSaleGUI extends JFrame {
	
    File targetFile;
    BufferedImage targetImg;
    String encodedfile = null;

    public JPanel panel_1;
    private static final int baseSize = 128;
	private static final String basePath="src/main/resources/images/";

	private static final long serialVersionUID = 1L;

	private String sellerMail;
	private JTextField fieldTitle=new JTextField();
	private JTextField fieldDescription=new JTextField();
	
	private JLabel jLabelTitle = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.Title"));
	private JLabel jLabelDescription = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.Description")); 
	private JLabel jLabelProductStatus = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.Status"));
	private JLabel jLabelPrice = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.Price"));
	private JTextField jTextFieldPrice = new JTextField();

	private JCalendar jCalendar = new JCalendar();
	private Calendar calendarAct = null;
	private Calendar calendarAnt = null;

	private JScrollPane scrollPaneEvents = new JScrollPane();
	
	JComboBox<String> jComboBoxStatus = new JComboBox<String>();
	DefaultComboBoxModel<String> statusOptions = new DefaultComboBoxModel<String>();
	List<String> status;

	private JButton jButtonCreate = new JButton(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.CreateProduct"));
	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
	private JLabel jLabelMsg = new JLabel();
	private JLabel jLabelError = new JLabel();
	private JFrame thisFrame;
	
	
	private Demand parentDemand = null;

	/**
	 * Eraikitzaile nagusia
	 * @wbp.parser.constructor
	 */
	public CreateSaleGUI(String mail) {
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
		getContentPane().setBackground(Color.WHITE);
		getContentPane().setFocusable(false);
		setFocusable(false);

		thisFrame=this;
		this.sellerMail=mail;
		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(604, 420)); // Tamaina pixka bat handitua botoiak sartzeko
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.CreateProduct"));
		
		jLabelTitle.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		jLabelTitle.setForeground(Color.DARK_GRAY);
		jLabelTitle.setBounds(new Rectangle(16, 57, 92, 20));
		
		jLabelPrice.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		jLabelPrice.setForeground(Color.DARK_GRAY);
		jLabelPrice.setBounds(new Rectangle(14, 174, 101, 20));
		
		jTextFieldPrice.setBorder(new LineBorder(Color.LIGHT_GRAY));
		jTextFieldPrice.setBounds(new Rectangle(96, 176, 60, 20));

		jButtonCreate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jButtonCreate.setBorder(new LineBorder(new Color(64, 224, 208), 1, true));
		jButtonCreate.setForeground(Color.WHITE);
		jButtonCreate.setBackground(new Color(64, 224, 208));
		jButtonCreate.setFont(new Font("Segoe UI", Font.BOLD, 15));
		jButtonCreate.setBounds(new Rectangle(63, 270, 216, 41));

		jButtonCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jLabelMsg.setText("");
				String error=check_fields_Errors();
				if (error!=null) 
					jLabelMsg.setText(error);
				else
					try {
						BLFacade facade = MainGUI.getBusinessLogic();
						float price = Float.parseFloat(jTextFieldPrice.getText());
						String s=(String)jComboBoxStatus.getSelectedItem();
						int numStatus=status.indexOf(s);
						
						// ALDAKETA: parentDemand pasatzen da parametro gisa
						facade.createSale(fieldTitle.getText(), fieldDescription.getText(), numStatus, price, UtilDate.trim(jCalendar.getDate()), sellerMail, targetFile, parentDemand);
						
						jLabelMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.ProductCreated"));
					
					} catch (Exception e1) {
						jLabelMsg.setText(e1.getMessage());
					}
			}
		});

		jButtonClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jButtonClose.setForeground(Color.GRAY);
		jButtonClose.setBackground(Color.WHITE);
		jButtonClose.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
		jButtonClose.setBounds(new Rectangle(302, 278, 101, 30));
		jButtonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				thisFrame.setVisible(false);
			}
		});

		jLabelMsg.setBounds(new Rectangle(26, 248, 377, 20));
		jLabelMsg.setForeground(Color.red);
		
		status=Utils.getStatus();
		for(String s:status) statusOptions.addElement(s);

		this.getContentPane().add(jLabelMsg, null);
		this.getContentPane().add(jButtonClose, null);
		this.getContentPane().add(jButtonCreate, null);
		this.getContentPane().add(jLabelTitle, null);
		this.getContentPane().add(jLabelPrice, null);
		this.getContentPane().add(jTextFieldPrice, null);
		
		jLabelProductStatus.setBounds(16, 216, 140, 25);
		getContentPane().add(jLabelProductStatus);
		
		jLabelDescription.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		jLabelDescription.setForeground(Color.DARK_GRAY);
		jLabelDescription.setBounds(16, 87, 109, 16);
		getContentPane().add(jLabelDescription);
		
		fieldTitle.setBorder(new LineBorder(Color.LIGHT_GRAY));
		fieldTitle.setBounds(98, 51, 250, 26);
		getContentPane().add(fieldTitle);
		
		fieldDescription.setBorder(new LineBorder(Color.LIGHT_GRAY));
		fieldDescription.setBounds(98, 88, 250, 73);
		getContentPane().add(fieldDescription);
		
		jComboBoxStatus.setModel(statusOptions);
		jComboBoxStatus.setBounds(90, 215, 114, 27);
		getContentPane().add(jComboBoxStatus);
		
		JButton btnLoadPic = new JButton(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.LoadPicture"));
		btnLoadPic.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLoadPic.setBorder(new LineBorder(new Color(64, 224, 208), 1, true));
		btnLoadPic.setForeground(new Color(64, 224, 208));
		btnLoadPic.setBackground(Color.WHITE);
		btnLoadPic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    // 1. Obtener la ruta a la carpeta de descargas de forma dinámica
			    String downloadsPath = System.getProperty("user.home") + File.separator + "Downloads";
			    
			    // 2. Pasar esa ruta al constructor
			    JFileChooser fileChooser = new JFileChooser(downloadsPath);
			    
			    FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF", "jpg", "gif");
			    fileChooser.setFileFilter(filter);
			    
			    int result = fileChooser.showOpenDialog(null);  
			    if (result == JFileChooser.APPROVE_OPTION) {
			        targetFile = fileChooser.getSelectedFile();
			        panel_1.removeAll();
			        try {
			            targetImg = rescale(ImageIO.read(targetFile));
			            encodeFileToBase64Binary(targetFile);
			        } catch (IOException ex) { 
			            ex.printStackTrace(); 
			        }
			        panel_1.setLayout(new BorderLayout(0, 0));
			        panel_1.add(new JLabel(new ImageIcon(targetImg))); 
			        panel_1.revalidate();
			        panel_1.repaint();
			    }
			}
		});
		btnLoadPic.setBounds(186, 171, 162, 29);
		getContentPane().add(btnLoadPic);
		
		panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_1.setBackground(Color.LIGHT_GRAY);
		panel_1.setBounds(432, 214, 148, 94);
		getContentPane().add(panel_1);

		
		
		jCalendar.setBounds(new Rectangle(360, 50, 225, 150));
		this.getContentPane().add(jCalendar, null);
		
		JLabel jLabelPublicationDate = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.PublicationDate"));
		jLabelPublicationDate.setBounds(360, 26, 197, 20);
		getContentPane().add(jLabelPublicationDate);

		this.jCalendar.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent propertychangeevent) {
				if (propertychangeevent.getPropertyName().equals("locale")) {
					jCalendar.setLocale((Locale) propertychangeevent.getNewValue());
				} else if (propertychangeevent.getPropertyName().equals("calendar")) {
					calendarAnt = (Calendar) propertychangeevent.getOldValue();
					calendarAct = (Calendar) propertychangeevent.getNewValue();
					if (calendarAnt != null && calendarAct != null) {
						int monthAnt = calendarAnt.get(Calendar.MONTH);
						int monthAct = calendarAct.get(Calendar.MONTH);
						if (monthAct != monthAnt) {
							if (monthAct == monthAnt + 2) { 
								calendarAct.set(Calendar.MONTH, monthAnt + 1);
								calendarAct.set(Calendar.DAY_OF_MONTH, 1);
							}
							jCalendar.setCalendar(calendarAct);						
						}
					}
				}
			}
		});
	}

	/**
	 * Eraikitzaile berria QueryDemandsGUI-tik deitzeko
	 */
	public CreateSaleGUI(String email, Demand parent) {
		this(email); 
		this.parentDemand = parent;
		if (parent != null) {
			this.fieldTitle.setText("RE: " + parent.getTitle());
		}
	}

	public BufferedImage rescale(BufferedImage originalImage) {
        BufferedImage resizedImage = new BufferedImage(baseSize, baseSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, baseSize, baseSize, null);
        g.dispose();
        return resizedImage;
    }

	private String check_fields_Errors() {
		try {
			if ((fieldTitle.getText().length()==0) || (fieldDescription.getText().length()==0) || (jTextFieldPrice.getText().length()==0))
				return ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.ErrorQuery");
			
			float price = Float.parseFloat(jTextFieldPrice.getText());
			if (price <= 0) 
				return ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.PriceMustBeGreaterThan0");
			
			return null;
		} catch (java.lang.NumberFormatException e1) {
			return ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.ErrorNumber");		
		} catch (Exception e1) {
			return e1.getMessage();
		}
	}
	
	public String encodeFileToBase64Binary(File file){
        try {
			FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile=new String(Base64.getEncoder().encode(bytes));
            fileInputStreamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodedfile;
    }
}