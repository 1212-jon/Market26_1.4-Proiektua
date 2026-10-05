package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import domain.Seller;
import java.util.ResourceBundle;

public class ErosketaGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private float kopurua;
    private Seller user;
    private JFrame previousFrame;
    private boolean isSarrera;

    // GAKOA: Eraikitzaile honek 4 parametro jaso behar ditu derrigorrez!
    public ErosketaGUI(float kopurua, Seller user, JFrame previousFrame, boolean isSarrera) {
    	ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
        this.kopurua = kopurua;
        this.user = user;
        this.previousFrame = previousFrame;
        this.isSarrera = isSarrera;
        
        setTitle(ResourceBundle.getBundle("Etiquetas").getString("ErosketaGUI.MainTitle")); //$NON-NLS-1$ //$NON-NLS-2$
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 350);
        
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Testua: Sartu ala Atera
        String tit = isSarrera ? ResourceBundle.getBundle("Etiquetas").getString("ErosketaGUI.PutMoney") : ResourceBundle.getBundle("Etiquetas").getString("ErosketaGUI.WithdrawMoney");
        JLabel lblInfo = new JLabel(tit + ": "+kopurua + "€", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblInfo.setBounds(10, 40, 416, 40);
        contentPane.add(lblInfo);

        // TXARTELA BOTOIA
        JButton btnCard = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ErosketaGUI.CardButton")); //$NON-NLS-1$ //$NON-NLS-2$
        btnCard.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        btnCard.setForeground(Color.WHITE);
        btnCard.setBackground(new Color(64, 224, 208));
        btnCard.setBounds(32, 120, 375, 50);
        btnCard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TxartelaGUI ireki parametro berdinekin
                new TxartelaGUI(kopurua, user, previousFrame, isSarrera).setVisible(true);
                
            }
        });
        contentPane.add(btnCard);

        // BIZUM BOTOIA
        JButton btnBizum = new JButton("Bizum");
        btnBizum.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        btnBizum.setForeground(new Color(64, 224, 208));
        btnBizum.setBackground(Color.WHITE);
        btnBizum.setBorder(new LineBorder(new Color(64, 224, 208), 2));
        btnBizum.setBounds(32, 190, 375, 50);
        btnBizum.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // BizumGUI ireki parametro berdinekin
                new BizumGUI(kopurua, user, previousFrame, isSarrera).setVisible(true);
                
            }
        });
        contentPane.add(btnBizum);
        
        this.setLocationRelativeTo(null);
    }
}