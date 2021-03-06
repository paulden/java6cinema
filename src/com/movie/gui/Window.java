package com.movie.gui;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.movie.cinema.TheaterFinder;
import com.movie.cinema.Movie;
import com.movie.cinema.Screening;
import com.movie.locations.Path;
import com.movie.locations.Path.TransportationMode;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.json.JSONException;
 
/** This class builds the main window where the user may enter information
 * and get results following user's inputs rather than using the console.
 * This class uses Swing to build a graphical user interface.
 * @author paul
 *
 */

public class Window extends JFrame {
	
/**Definition of the main parameters
 * used in the window
 */
private JLabel labelResultsCinemas = new JLabel();
private JLabel labelResultsTime = new JLabel();
private JLabel labelResultsMovies = new JLabel();
private JLabel labelResultsAddress = new JLabel();
private JButton b = new JButton ("C'est parti !");
private JOptionPane jopWarning = new JOptionPane();
private Slider sliderDistance = new Slider();
private JFormattedTextField maxTime = new JFormattedTextField();
private JLabel maxTimeLabel = new JLabel("Temps maximum avant le début de la séance (en minutes)");

//Checkboxes to allow user select (a) travelling mode(s)-----------
JPanel travellingModes = new JPanel();;
Checkbox walking = new Checkbox("J'y vais à pied");
Checkbox driving = new Checkbox("J'y vais en voiture");
Checkbox transit = new Checkbox("J'y vais en transport en commun");
Checkbox bicycling = new Checkbox("J'y vais à bicylette");
//----------------------------------------------------------------

//Getting user's resolution so that the dimension will be adapted even if resolutions are different
Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
double userWidth = screenSize.getWidth();
double userHeight = screenSize.getHeight();

/**Main constructor of our window
 * which is to be used as a final representation of the program
 */
public Window(){
	//Main window
  this.setTitle("Recherche de séances de cinéma");
  this.setSize((int)(userWidth*0.9), (int)(userHeight*0.9));
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  this.setLocationRelativeTo(null);
  
  /**Building the different JPanels so that the display will be organized
   * by "blocks" which will receive data (or record inputs)
   */
  
  //----------------------------------------------
  
  JPanel cell1 = new JPanel();
  cell1.setBackground(Color.WHITE);
  cell1.setPreferredSize(new Dimension((int)(userWidth*0.80), (int)(userHeight*0.20)));
  
  JPanel cell2 = new JPanel();
  cell1.setBackground(Color.WHITE);
  cell1.setPreferredSize(new Dimension((int)(userWidth*0.80), (int)(userHeight*0.10)));
  
  JPanel cell5 = new JPanel();
  cell5.setBackground(Color.WHITE);
  cell5.setPreferredSize(new Dimension((int)(userWidth*0.20), (int)(userHeight*0.50)));
  
  JPanel cell6 = new JPanel();
  cell6.setBackground(Color.WHITE);
  cell6.setPreferredSize(new Dimension((int)(userWidth*0.20), (int)(userHeight*0.50)));
  
  JPanel cell7 = new JPanel();
  cell7.setBackground(Color.WHITE);
  cell7.setPreferredSize(new Dimension((int)(userWidth*0.20), (int)(userHeight*0.50)));
  
  JPanel cell8 = new JPanel();
  cell8.setBackground(Color.WHITE);
  cell8.setPreferredSize(new Dimension((int)(userWidth*0.20), (int)(userHeight*0.50)));
  
  //----------------------------------------------
  
  //This global JPanel will receive the previous JPanels previously built and organize them 
  //following a GridBagLayout structure
  JPanel content = new JPanel();
  content.setPreferredSize(new Dimension((int)(userWidth*0.80), (int)(userHeight*0.80)));
  content.setBackground(Color.WHITE);
  //On définit le layout manager
  content.setLayout(new GridBagLayout());
  
  //This objects is used to position the different components
  GridBagConstraints gbc = new GridBagConstraints();
  
  //Component used to record inputs (radius, travelling modes and maximum time before show starts)
  gbc.gridx = 0;
  gbc.gridy = 0;
  gbc.gridheight = 1;
  gbc.gridwidth = GridBagConstraints.REMAINDER;
  sliderDistance.setBackground(Color.WHITE);
  cell1.add(sliderDistance);
  travellingModes.setBackground(Color.WHITE);
  travellingModes.setLayout(new GridLayout(4, 1));
  travellingModes.add(walking);
  travellingModes.add(driving);
  travellingModes.add(transit);
  travellingModes.add(bicycling);
  cell1.add(travellingModes);
  maxTime.setPreferredSize(new Dimension(100, 30));
  cell1.add(maxTime);
  cell1.add(maxTimeLabel);
  content.add(cell1, gbc);
  
  //Block toggling button
  gbc.gridx = 0;
  gbc.gridy = 1;
  gbc.gridheight = 1;
  gbc.gridwidth = GridBagConstraints.REMAINDER;
  Font fontButton = new Font("Sans Serif", Font.BOLD, 20);
  b.setFont(fontButton);
  b.setPreferredSize(new Dimension(200, 50));
  cell2.add(b);
  content.add(cell2, gbc);
 
  //Block toggling cinemas
  gbc.gridx = 0;
  gbc.gridy = 2;
  gbc.gridwidth = 1;
  gbc.gridheight = 1;
  cell5.add(labelResultsMovies);
  content.add(cell5, gbc);
  
  //Block toggling cinemas addresses
  gbc.gridx = 1;
  gbc.gridy = 2;
  gbc.gridwidth = 1;
  gbc.gridheight = 1;
  cell6.add(labelResultsCinemas);
  content.add(cell6, gbc);
  
  //Block toggling movies
  gbc.gridx = 2;
  gbc.gridy = 2;
  gbc.gridwidth = 1;
  gbc.gridheight = 1;
  cell7.add(labelResultsTime);
  content.add(cell7, gbc);
  
  //Block toggling time of show
  gbc.gridx = 3;
  gbc.gridy = 2;
  gbc.gridwidth = GridBagConstraints.REMAINDER;
  gbc.gridheight = 1;
  cell8.add(labelResultsAddress);
  content.add(cell8, gbc);

  //This ButtonListener (defined below) implements a method to execute a method when the user
  //clicks on the button. It uses inputs entered by user to display proper results
  b.addActionListener(new ButtonListener());
  
 
  this.setContentPane(content); 
}       

/** This class is an event listener to record inputs entered in the first block
 * and then display results according to what the user is looking for
 * @author paul
 *
 */
class ButtonListener implements ActionListener{
  public void actionPerformed(ActionEvent e) {	
	  
	//Summary of user inputs in the console
    System.out.println("Distance saisie : " + sliderDistance.getDistanceMax().getValue() + " km");
    System.out.println("Modes de transports sélectionnés :");
    if (walking.getState()) {
    	System.out.println("Marche");
    	};
	if (driving.getState()) {
    	System.out.println("Voiture");
    	};
    if (transit.getState()) {
    	System.out.println("Transport en commun");
    	};
	if (bicycling.getState()) {
    	System.out.println("Vélo");
    	};
    System.out.println("Temps maximum avant la séance : " + maxTime.getText() + " minutes");
    	
    //Getting correct data using methods defined in other packages
    int radius = sliderDistance.getDistanceMax().getValue()*1000;
    
    int time;
    
    //Managing case where user enters invalid/void duration
    try {
    	time = Integer.parseInt(maxTime.getText());
    } catch(NumberFormatException ex) {
    	time = 60;
    	maxTime.setText("60");
    	System.out.println("Durée rentrée invalide, durée automatique de 60 minutes fixée par défaut");

    }

    Set<Path.TransportationMode> modeTrajetPossible = new HashSet<>();
    if (walking.getState()) {
    	modeTrajetPossible.add(TransportationMode.WALKING);
    	};
	if (driving.getState()) {
		modeTrajetPossible.add(TransportationMode.DRIVING);
    	};
    if (transit.getState()) {
    	modeTrajetPossible.add(TransportationMode.TRANSIT);
    	};
	if (bicycling.getState()) {
		modeTrajetPossible.add(TransportationMode.BICYCLING);
    	};
    	
    //Managing case where user has not selected any means of transportation
    if (!walking.getState() && !driving.getState() && !transit.getState() && !bicycling.getState()) {
    	System.out.println("Aucun mode de transport choisi, modes de transport tous sélectionnés par défaut");
    	modeTrajetPossible.add(TransportationMode.WALKING);
    	walking.setState(true);
    	modeTrajetPossible.add(TransportationMode.DRIVING);
    	driving.setState(true);
    	modeTrajetPossible.add(TransportationMode.TRANSIT);
    	transit.setState(true);
    	modeTrajetPossible.add(TransportationMode.BICYCLING);
    	bicycling.setState(true);
    }
    

    //Using CinemaFinder class to collect data and display them
	TheaterFinder cinemaFinder = new TheaterFinder();

	try {			
		cinemaFinder.updateTempsTrajet(null, modeTrajetPossible);
		
		//For convenience, HTML is used to format text results easily
		String resultsCinemas = "<html> <h1 style ='color:blue; font-size:16;'> Cinémas correspondants : </h1><br> <br>";
		String resultsAddress = "<html> <h1 style ='color:blue; font-size:16;'> Adresses : </h1> <br> <br>";
		String resultsMovies = "<html> <h1 style ='color:blue; font-size:16;'> Films : </h1> <br> <br>";
		String resultsTime = "<html> <h1 style ='color:blue; font-size:16;'> Horaires des séances : </h1> <br> <br>";

		
		List<Screening> bestSeanceList = cinemaFinder.findBestSeances(time, radius,null,null,modeTrajetPossible,true);
		for(Screening seance : bestSeanceList) {
			System.out.println(seance);
			resultsCinemas = resultsCinemas + seance.getTheater().getName() + "<br>";
			resultsAddress = resultsAddress + seance.getTheater().getAddress() + "<br>";
			resultsMovies = resultsMovies + seance.getMovie().getName() + " - " + seance.getLanguage() + "<br>";
			
			//Formatting hour and displaying means of transportation in Time column
			String hour = String.valueOf(seance.getDate().get(Calendar.HOUR_OF_DAY));
			String minute;
			if (seance.getDate().get(Calendar.MINUTE) < 10) {
				minute = "0" + String.valueOf(seance.getDate().get(Calendar.MINUTE));
			} else {
				minute = String.valueOf(seance.getDate().get(Calendar.MINUTE));
			}
			resultsTime = resultsTime + hour + "h" + minute + " (";
            boolean transportAdded = false;

			if (seance.getTransportationModeList().contains(TransportationMode.WALKING)){
				resultsTime = resultsTime + "à pied";
                transportAdded = true;
			}
			if (seance.getTransportationModeList().contains(TransportationMode.BICYCLING)){
                if (transportAdded) resultsTime += ", ou ";
				resultsTime = resultsTime + "à vélo";
                transportAdded = true;
			}
			if (seance.getTransportationModeList().contains(TransportationMode.DRIVING)){
                if (transportAdded) resultsTime += ", ou ";
				resultsTime = resultsTime + "en voiture";
                transportAdded = true;
			}
			if (seance.getTransportationModeList().contains(TransportationMode.TRANSIT)){
                if (transportAdded) resultsTime += ", ou ";
				resultsTime = resultsTime + "en transports";
			}
			resultsTime = resultsTime + ") <br>";
			

		}
		
		resultsCinemas = resultsCinemas + "</html>";
		resultsAddress = resultsAddress + "</html>";
		resultsMovies = resultsMovies + "</html>";
		resultsTime = resultsTime + "</html>";
		
		//Displaying final results
		//Managing case where no results are found		
		if (bestSeanceList.isEmpty()) {
			labelResultsCinemas.setText("<html> <h1 style ='color:red; font-size:24;'> Aucune séance n'a été trouvée :(</h1> <br> "
					+ "<h2 style ='color:red; font-size:18;'> Avez-vous essayé de chercher dans un peu plus longtemps ? </h2> "
					+ "<h2 style ='color:red; font-size:18;'> Ou peut-être utiliser d'autres modes de transport ? </h2> </html>");
			labelResultsMovies.setText("");
			labelResultsAddress.setText("");
			labelResultsTime.setText("");
		} else {
			labelResultsCinemas.setText(resultsCinemas);
			labelResultsAddress.setText(resultsAddress);
			labelResultsMovies.setText(resultsMovies);
			labelResultsTime.setText(resultsTime);
		}
		
	} catch (JSONException | IOException e1) { //Managing exceptions
		System.err.println("Erreur dans la recherche");
		JOptionPane.showMessageDialog(null, "La recherche n'a pas pu aboutir \nAvez-vous vérifié votre connexion Internet ?", "Oups, erreur...", JOptionPane.ERROR_MESSAGE);
		e1.printStackTrace();
	}
  }
}


}