package gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Toolkit;

public class AdminAukGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	protected JLabel jLabelSelectOption;
	
	private JButton jButtonKontua = null;
	private JButton jButtonErrek = null;
	private JButton jButtonItxi = null;

	/**
	 * Create the frame.
	 */
	public AdminAukGUI() {
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
		
		this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MainGUI main = new MainGUI("seller3@gmail.com");
                main.setVisible(true);
            }
        });
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.setSize(495, 290); // Tamaina jatorrizkora bueltatu dugu (290)
		jLabelSelectOption = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("AdminAukGUI.Admin"));
		jLabelSelectOption.setFocusable(false);
		jLabelSelectOption.setBounds(0, 1, 481, 63);
		jLabelSelectOption.setBackground(Color.WHITE);
		jLabelSelectOption.setFont(new Font("Tahoma", Font.BOLD, 20));
		jLabelSelectOption.setForeground(Color.DARK_GRAY);
		jLabelSelectOption.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(jLabelSelectOption);
		
		jButtonKontua = new JButton();
		jButtonKontua.setFocusable(false);
		jButtonKontua.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jButtonKontua.setFocusPainted(false);
		jButtonKontua.setBorder(null);
		jButtonKontua.setBounds(25, 62, 428, 53);
		jButtonKontua.setForeground(new Color(255, 255, 255));
		jButtonKontua.setBackground(new Color(64, 224, 208));
		jButtonKontua.setText(ResourceBundle.getBundle("Etiquetas").getString("AdminAukGUI.LogOut"));
		jButtonKontua.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new KontuaItxiGUI();
				a.setVisible(true);
			}
		});
		contentPane.add(jButtonKontua);
		
		jButtonErrek = new JButton();
		jButtonErrek.setFocusable(false);
		jButtonErrek.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jButtonErrek.setBorder(new LineBorder(new Color(128, 128, 128)));
		jButtonErrek.setBounds(25, 125, 428, 53);
		jButtonErrek.setForeground(new Color(64, 224, 208));
		jButtonErrek.setBackground(new Color(255, 255, 255));
		jButtonErrek.setText(ResourceBundle.getBundle("Etiquetas").getString("AdminAukGUI.Reclamation"));
		jButtonErrek.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new AdminGUI();
				a.setVisible(true);
			}
		});
		contentPane.add(jButtonErrek);
		
		jButtonItxi = new JButton();
		jButtonItxi.setFocusable(false);
		jButtonItxi.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jButtonItxi.setBorder(new LineBorder(new Color(128, 128, 128)));
		jButtonItxi.setBounds(25, 188, 428, 53);
		jButtonItxi.setForeground(new Color(64, 224, 208));
		jButtonItxi.setBackground(new Color(255, 255, 255));
		jButtonItxi.setText(ResourceBundle.getBundle("Etiquetas").getString("AdminAukGUI.List"));
		jButtonItxi.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new ZerrendaBeltzaGUI();
				a.setVisible(true);
			}
		});
		contentPane.add(jButtonItxi);
		
	}
}
