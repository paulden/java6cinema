package com.movie.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

/** Class generating a JPanel object with a slider Jslider and a title
 * JLabel so that it can be easily integrated in the window we want to build
 * @author paul
 *
 */

public class Slider extends JPanel implements ChangeListener {


static final int KM_MIN = 0;
static final int KM_MAX = 50;
static final int KM_INIT = 10;
	
	//Constructor
	public Slider(){
        //JPanel title
        JLabel sliderLabel = new JLabel("Je veux aller dans un cinéma situé à maximum : (km) ", JLabel.CENTER);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        //JSlider slider
        JSlider distanceMax = new JSlider(JSlider.HORIZONTAL, KM_MIN, KM_MAX, KM_INIT);
         
 
        distanceMax.addChangeListener(this);
 
        //Formatting the slider
 
        distanceMax.setMajorTickSpacing(10);
        distanceMax.setMinorTickSpacing(1);
        distanceMax.setPaintTicks(true);
        distanceMax.setPaintLabels(true);
        distanceMax.setBorder(
                BorderFactory.createEmptyBorder(0,0,10,0));
        Font font = new Font("Serif", Font.ITALIC, 15);
        distanceMax.setFont(font);
        distanceMax.setBackground(Color.WHITE);
        
        //Integrating everything in the JPanel object to use it directly in our window
        add(sliderLabel);
        add(distanceMax);
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}
	
	//Method to get current value on our slider
	public JSlider getDistanceMax() {
		for (Component dm : this.getComponents()) {
			if (dm instanceof JSlider) {
				JSlider distanceMax = (JSlider) dm;
				return distanceMax;
			}
		}
		return null;
	}
	
	/**ChangeListener may detects every change on the slider
	 * It is currently not used to prevent sending a constant data flux
	 * which may overuse our API key and generate high loading time
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider slider = (JSlider) e.getSource();
        if (!slider.getValueIsAdjusting())
            System.out.println(slider.getValue());        
	}

	
	
}
