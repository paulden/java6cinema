package com.movie.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.NumberFormat;

import com.movie.locations.ClosestCinemas;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

import org.json.JSONException;
 
//Cette classe correspond à l'objet fenêtre qui sera affichée au lancement

public class Fenetre extends JFrame {
	
//private JFormattedTextField jtf = new JFormattedTextField(NumberFormat.getIntegerInstance());
//private JLabel labelSearch = new JLabel("Je veux aller dans un ciné situé à maximum (km) :");
private JLabel labelResultsCinemas = new JLabel();
private JLabel labelResultsTime = new JLabel();
private JLabel labelResultsMovies = new JLabel();
private JLabel labelResultsAddress = new JLabel();
private JButton b = new JButton ("J'y vais !");
private JOptionPane jopWarning = new JOptionPane();
private Slider slider = new Slider();

//On récupère la résolution de l'utilisateur
Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
double userWidth = screenSize.getWidth();
double userHeight = screenSize.getHeight();

public Fenetre(){
	//Il s'agit de la fenêtre principale
  this.setTitle("Recherche de séances de cinéma");
  this.setSize((int)(userWidth*0.9), (int)(userHeight*0.9));
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  this.setLocationRelativeTo(null);
  
  //On construit les différents JPanels qui s'inséreront dans la fenêtre
  //afin d'avoir une structure organisée qui affichera les données
  //----------------------------------------------
  
  JPanel cell1 = new JPanel();
  cell1.setBackground(Color.WHITE);
  cell1.setPreferredSize(new Dimension((int)(userWidth*0.80), (int)(userHeight*0.20)));
  
  
  JPanel cell5 = new JPanel();
  cell5.setBackground(Color.WHITE);
  cell5.setPreferredSize(new Dimension((int)(userWidth*0.20), (int)(userHeight*0.60)));
  
  JPanel cell6 = new JPanel();
  cell6.setBackground(Color.WHITE);
  cell6.setPreferredSize(new Dimension((int)(userWidth*0.20), (int)(userHeight*0.60)));
  
  JPanel cell7 = new JPanel();
  cell7.setBackground(Color.WHITE);
  cell7.setPreferredSize(new Dimension((int)(userWidth*0.20), (int)(userHeight*0.60)));
  
  JPanel cell8 = new JPanel();
  cell8.setBackground(Color.WHITE);
  cell8.setPreferredSize(new Dimension((int)(userWidth*0.20), (int)(userHeight*0.60)));
  
  //----------------------------------------------
  
  //Il s'agit du conteneur principal accueillant les différents JPanels
  //instanciés précédemment
  JPanel content = new JPanel();
  content.setPreferredSize(new Dimension((int)(userWidth*0.80), (int)(userHeight*0.70)));
  content.setBackground(Color.WHITE);
  //On définit le layout manager
  content.setLayout(new GridBagLayout());
  
  //L'objet servant à positionner les composants
  GridBagConstraints gbc = new GridBagConstraints();
  
  //Composant de recherche (formulaire à remplir avec le rayon en input)
  gbc.gridx = 0;
  gbc.gridy = 0;
  gbc.gridheight = 1;
  gbc.gridwidth = GridBagConstraints.REMAINDER;
  slider.setBackground(Color.WHITE);
  cell1.add(slider);
  cell1.add(b);
  content.add(cell1, gbc);
  
  int value = slider.getDistanceMax().getValue();
  System.out.println("Valeur récupérée :" + value);
 
  //Bloc réservé à l'affichage des cinémas
  gbc.gridx = 0;
  gbc.gridy = 1;
  gbc.gridwidth = 1;
  gbc.gridheight = 1;
  cell5.add(labelResultsCinemas);
  content.add(cell5, gbc);
  
//Bloc réservé à l'affichage des adresses des cinémas
  gbc.gridx = 1;
  gbc.gridy = 1;
  gbc.gridwidth = 1;
  gbc.gridheight = 1;
  cell6.add(labelResultsAddress);
  content.add(cell6, gbc);
  
//Bloc réservé à l'affichage des films projetés
  gbc.gridx = 2;
  gbc.gridy = 1;
  gbc.gridwidth = 1;
  gbc.gridheight = 1;
  cell7.add(labelResultsMovies);
  content.add(cell7, gbc);
  
//Bloc réservé à l'affichage des horaires et/ou du temps de trajet
  gbc.gridx = 3;
  gbc.gridy = 1;
  gbc.gridwidth = GridBagConstraints.REMAINDER;
  gbc.gridheight = 1;
  cell8.add(labelResultsTime);
  content.add(cell8, gbc);

  //Ajout du bouton qui provient de la classe BoutonListener et qui dispose d'une
  //méthode permettant d'écouter les inputs et afficher les résultats
  b.addActionListener(new BoutonListener());
  
 
  this.setContentPane(content); 
}       

//Cette classe correspond à un event listener permettant d'écouter les inputs (le rayon
//de recherche) et d'afficher ensuite la liste des cinémas sous forme de String
class BoutonListener implements ActionListener{
  public void actionPerformed(ActionEvent e) {
    System.out.println("Distance saisie : " + slider.getDistanceMax().getValue() + " km");
    int radius = slider.getDistanceMax().getValue()*1000;
    ClosestCinemas closestCinemas = new ClosestCinemas();
		try {
			closestCinemas.setClosestCinemas(null, radius);
			
			int n = closestCinemas.getClosestCinemas().size();
			
			//Strings results configurés en html pour les besoins de l'affichage dans la fenêtre
			
			/* 24/10 : les résultats affichés ne correspondent pas aux noms des blocs et fait
			 * simplement figure de test d'affichage à l'heure actuelle
			 */
			
			String resultsCinemas = "<html> <h1 style ='color:blue; font-size:16;'> Cinémas correspondants : </h1><br> <br>";
			String resultsAddress = "<html> <h1 style ='color:blue; font-size:16;'> Adresses : </h1> <br> <br>";
			String resultsMovies = "<html> <h1 style ='color:blue; font-size:16;'> Films : </h1> <br> <br>";
			String resultsTime = "<html> <h1 style ='color:blue; font-size:16;'> Horaires des séances : </h1> <br> <br>";
			
			for(int i = 0; i<n;i++){
				resultsCinemas = resultsCinemas + closestCinemas.getClosestCinemas().get(i).getNom() + "<br>";
				resultsAddress = resultsAddress + closestCinemas.getClosestCinemas().get(i).getAdresse() + "<br>";
				resultsMovies = resultsMovies + String.valueOf((closestCinemas.getClosestCinemas().get(i).getLat())) + "<br>";
				resultsTime = resultsTime + String.valueOf((closestCinemas.getClosestCinemas().get(i).getLng())) + "<br>";
			}
			
			resultsCinemas = resultsCinemas + "</html>";
			resultsAddress = resultsAddress + "</html>";
			resultsMovies = resultsMovies + "</html>";
			resultsTime = resultsTime + "</html>";
			
			//Affichage des résultats
			labelResultsCinemas.setText(resultsCinemas);
			labelResultsAddress.setText(resultsAddress);
			labelResultsMovies.setText(resultsMovies);
			labelResultsTime.setText(resultsTime);
			
		} catch (JSONException | IOException e1) {
			System.err.println("Nous n'avons pas pu trouver de résutats correspondants à la recherche");
			JOptionPane.showMessageDialog(null, "La recherche n'a pas pu aboutir", "Erreur", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
  }
  

}
}