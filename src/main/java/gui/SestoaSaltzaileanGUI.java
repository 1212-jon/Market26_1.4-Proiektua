package gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import businessLogic.BLFacade;
import domain.Sale;
import domain.Seller;

public class SestoaSaltzaileanGUI extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private Seller eroslea;
    private Seller saltzailea;
    private SestoaGUI gurasoa;

    public SestoaSaltzaileanGUI(Seller eroslea, Seller saltzailea, SestoaGUI gurasoa) {
    	ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/Logo.png"));
    	setIconImage(icon.getImage());
        this.eroslea = eroslea;
        this.saltzailea = saltzailea;
        this.gurasoa = gurasoa;

        setTitle(ResourceBundle.getBundle("Etiquetas").getString("SestoaSaltzaileanGUI.MainTitle1")+saltzailea.getName() + ResourceBundle.getBundle("Etiquetas").getString("SestoaSaltzaileanGUI.MainTitle2"));
        setBounds(150, 150, 500, 350);
        JPanel cp = new JPanel(new BorderLayout(10, 10));
        cp.setBorder(new javax.swing.border.EmptyBorder(10,10,10,10));
        setContentPane(cp);

        model = new DefaultTableModel(new String[]{ResourceBundle.getBundle("Etiquetas").getString("SestoaSaltzaileanGUI.Product"), 
        		ResourceBundle.getBundle("Etiquetas").getString("SestoaSaltzaileanGUI.Price"), 
        		ResourceBundle.getBundle("Etiquetas").getString("SestoaSaltzaileanGUI.Object")}, 0);
        table = new JTable(model);
        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(2));
        cp.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnErosi = new JButton(ResourceBundle.getBundle("Etiquetas").getString("SestoaSaltzaileanGUI.BuyAll"));
        btnErosi.setFocusable(false);
        btnErosi.setBackground(Color.GREEN);
        JButton btnHustu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("SestoaSaltzaileanGUI.SetEmptyThisSeller"));
        btnHustu.setFocusable(false);
        
        btnPanel.add(btnHustu);
        btnPanel.add(btnErosi);
        cp.add(btnPanel, BorderLayout.SOUTH);

        // Logika: Filtrokatu saskia saltzaile honen produktuak lerekin
        freskatu();

        btnHustu.addActionListener(e -> {
            BLFacade facade = MainGUI.getBusinessLogic();
            // Saltzaile honen produktuak bakarrik ezabatu saskitik
            for (int i = 0; i < model.getRowCount(); i++) {
                Sale s = (Sale) model.getValueAt(i, 2);
                facade.removeFromBasket(eroslea.getEmail(), s.getSaleNumber());
            }
            gurasoa.freskatuTaula();
            dispose();
        });

        btnErosi.addActionListener(e -> {
            BLFacade facade = MainGUI.getBusinessLogic();
            boolean ok = true;
            // Banan-banan erosi saltzaile honen produktuak
            for (int i = 0; i < model.getRowCount(); i++) {
                Sale s = (Sale) model.getValueAt(i, 2);
                if (!facade.buySale(s.getSaleNumber(), eroslea.getEmail())) ok = false;
            }
            if(ok) JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Etiquetas").getString("SestoaSaltzaileanGUI.OkayAlert"));
            gurasoa.freskatuTaula();
            dispose();
        });
        
        this.setLocationRelativeTo(null);
    }

    private void freskatu() {
        model.setRowCount(0);
        for (Sale s : eroslea.getBasket()) {
            if (s.getSeller().getEmail().equals(saltzailea.getEmail())) {
                model.addRow(new Object[]{s.getTitle(), s.getPrice() + "€", s});
            }
        }
    }
}