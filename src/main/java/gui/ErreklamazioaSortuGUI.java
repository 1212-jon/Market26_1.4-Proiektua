				package gui;
				
				import javax.swing.*;
				import java.awt.*;
import java.util.ResourceBundle;

import domain.Sale;
				import businessLogic.BLFacade;
				
				public class ErreklamazioaSortuGUI extends JFrame {
				    private static final long serialVersionUID = 1L;
				    private JTextArea txtKexa;
				    private JButton btnBidali;
				
				    public ErreklamazioaSortuGUI(Sale sale, ProfilaGUI parent) {
				    	ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
				    	setIconImage(icon.getImage());
				        setTitle(ResourceBundle.getBundle("Etiquetas").getString("ErreklamazioaSortuGUI.MainTitle")+": " + sale.getTitle());
				        setSize(450, 300);
				        getContentPane().setLayout(new BorderLayout(10, 10));
				        setLocationRelativeTo(parent);
				        getContentPane().setBackground(Color.WHITE);
				
				        // Azalpen labela
				        JLabel lblInfo = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ErreklamazioaSortuGUI.ExplainProblem"));
				        lblInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
				        getContentPane().add(lblInfo, BorderLayout.NORTH);
				
				        // Testu eremua kexa idazteko
				        txtKexa = new JTextArea();
				        txtKexa.setLineWrap(true);
				        txtKexa.setWrapStyleWord(true);
				        txtKexa.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
				        
				        JScrollPane scrollPane = new JScrollPane(txtKexa);
				        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
				        getContentPane().add(scrollPane, BorderLayout.CENTER);
				
				        // Bidali botoia
				        btnBidali = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ErreklamazioaSortuGUI.SendReclamation"));
				        btnBidali.setBackground(new Color(46, 139, 87));
				        btnBidali.setForeground(Color.WHITE);
				        btnBidali.setFocusPainted(false);
				        
				        btnBidali.addActionListener(e -> {
				            String kexaTestua = txtKexa.getText().trim();
				            
				            if (kexaTestua.isEmpty()) {
				                JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("ErreklamazioaSortuGUI.WriteExplication"));
				                return;
				            }
				
				            BLFacade facade = MainGUI.getBusinessLogic();
				            // Deitu DataAccess-era (BL bidez)
				            boolean ondo = facade.createReclamation(
				                sale.getSaleNumber(), 
				                kexaTestua, 
				                MainGUI.unekoErabiltzailea.getEmail()
				            );
				
				            if (ondo) {
				                JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("ErreklamazioaSortuGUI.ReclamationGood"));
				                // Datuak freskatu ProfilaGUI-n
				                parent.freskatuDatuak(); 
				                dispose();
				            } else {
				                JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("ErreklamazioaSortuGUI.ErrorSending"), "Error", JOptionPane.ERROR_MESSAGE);
				            }
				        });
				
				        JPanel panelBotoia = new JPanel();
				        panelBotoia.add(btnBidali);
				        panelBotoia.setBackground(Color.WHITE);
				        getContentPane().add(panelBotoia, BorderLayout.SOUTH);
				    }
				}