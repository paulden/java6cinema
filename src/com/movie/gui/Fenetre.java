package com.movie.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.movie.locations.ClosestCinemas;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.JSONException;
 
//Les imports habituels

public class Fenetre extends JFrame {
private JPanel container = new JPanel();
private JFormattedTextField jtf = new JFormattedTextField(NumberFormat.getIntegerInstance());
private JLabel label = new JLabel("Saisissez un rayon de recherche (en km)");
private JLabel labelResults = new JLabel();
private JButton b = new JButton ("OK");

public Fenetre(){
  this.setTitle("Recherche de séances de cinéma");
  this.setSize(800, 800);
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  this.setLocationRelativeTo(null);
  
  container.setBackground(Color.white);
  container.setLayout(new BorderLayout());
  
  JPanel top = new JPanel();        
  Font police = new Font("Arial", Font.BOLD, 14);
  jtf.setFont(police);
  jtf.setPreferredSize(new Dimension(150, 30));
  jtf.setForeground(Color.BLUE);
  
  b.addActionListener(new BoutonListener());
  
  top.add(label);
  top.add(jtf);
  top.add(b);
  top.add(labelResults, BorderLayout.SOUTH);
  
  this.setContentPane(top); 
}       

class BoutonListener implements ActionListener{
  public void actionPerformed(ActionEvent e) {
    System.out.println("Distance saisie : " + jtf.getText() + " km");
    int radius = Integer.parseInt(jtf.getText())*1000;
    ClosestCinemas closestCinemas = new ClosestCinemas();
		try {
			closestCinemas.setClosestCinemas(radius);
			
			int n = closestCinemas.getClosestCinemas().size();
			
			String results = "<html>";
			
			for(int i = 0; i<n;i++){
				results = results + closestCinemas.getClosestCinemas().get(i).getNom() + "<br>";
				results = results + closestCinemas.getClosestCinemas().get(i).getAdresse() + "<br>";
				results = results + String.valueOf((closestCinemas.getClosestCinemas().get(i).getLat())) + "<br>";
				results = results + String.valueOf((closestCinemas.getClosestCinemas().get(i).getLng())) + "<br>";
			}
			
			results = results + "</html>";
			
			System.out.println(results);
			labelResults.setText(results);
			
		} catch (JSONException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
  }
  

}
}