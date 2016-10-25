package com.movie.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/*Cette classe génère un objet JPanel intégrant un slider JSlider et un
titre JLabel afin d'être intégré directement dans la fenêtre créée*/

public class Slider extends JPanel implements ChangeListener {


static final int KM_MIN = 0;
static final int KM_MAX = 50;
static final int KM_INIT = 10;
	
	//Constructeur
	public Slider(){
        //On crée le titre du JPanel
        JLabel sliderLabel = new JLabel("Je veux aller dans un cinéma situé à maximum : (km) ", JLabel.CENTER);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        //On crée le slider.
        JSlider distanceMax = new JSlider(JSlider.HORIZONTAL, KM_MIN, KM_MAX, KM_INIT);
         
 
        distanceMax.addChangeListener(this);
 
        //On met en forme le slider et l'axe du slider
 
        distanceMax.setMajorTickSpacing(10);
        distanceMax.setMinorTickSpacing(1);
        distanceMax.setPaintTicks(true);
        distanceMax.setPaintLabels(true);
        distanceMax.setBorder(
                BorderFactory.createEmptyBorder(0,0,10,0));
        Font font = new Font("Serif", Font.ITALIC, 15);
        distanceMax.setFont(font);
        
        //On charge le tout dans le JPanel qui sera utilisé dans la classe Fenetre
        add(sliderLabel);
        add(distanceMax);
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}
	
	//Méthode de récupération de la valeur courante sur le slider JSlider
	public JSlider getDistanceMax() {
		for (Component dm : this.getComponents()) {
			if (dm instanceof JSlider) {
				JSlider distanceMax = (JSlider) dm;
				return distanceMax;
			}
		}
		return null;
	}
	
	//ChangeListener permettant de détecter les changements sur le slider
	//Actuellement non-utilisé afin d'éviter l'envoi continu de données
	//(qui peut potentiellement épuiser les limites d'appel API...)
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider slider = (JSlider) e.getSource();
        if (!slider.getValueIsAdjusting())
            System.out.println(slider.getValue());        
	}

	
	
}
