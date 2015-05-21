/*
 * $Revision: 1.1 $
 * $Id: ColleagueScrollbar.java,v 1.1 2003/04/04 10:13:55 niclas Exp $
 *
 * This file is part of Alfresco
 * Copyright (C) 1998  Niclas Jareborg
 * 
 * The Alfresco source code is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA  02111-1307, USA. 
 *
 * For more information contact Niclas at nic@sanger.ac.uk
 */
package alfresco;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A Scrollbar class to interact with a ScrollZoomDirector object.<p>
 * Part of implementation of the Mediator design pattern
 * @see java.awt.Scrollbar
 * @see alfresco.ScrollZoomDirector
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class ColleagueScrollbar extends Scrollbar{
/**
 * Director that manages interactions between scroll and zoom bars
 */
	ScrollZoomDirector director;

/**
 * Holds the previous value of the scrollbar
 */
	int prevVal;

/**
 * Creates a new vertical ColleagueScrollbar
 * @param dir The director that mediates scroll and zoombar interactions
 */	
	ColleagueScrollbar(ScrollZoomDirector dir) { 
		super();
		director = dir;
	}

/**
 * Creates a new ColleagueScrollbar with the specified orientation.
 * @param dir The director that mediates scroll and zoombar interactions
 * @param or orientation of the scrollbar, Scrollbar.HORIZONTAL or Scrollbar.VERTICAL
 */	
	ColleagueScrollbar(ScrollZoomDirector dir, int or) { 
		super(or);
		director = dir;
	}
	
/**
 * Creates a new ColleagueScrollbar with the specified orientation, initial
 * value, visible portion, and min and max values.
 * @param dir The director that mediates scroll and zoombar interactions
 * @param or orientation of the scrollbar, Scrollbar.HORIZONTAL or Scrollbar.VERTICAL
 * @param val inital value
 * @param vis visible portion
 * @param min min value
 * @param max max value
 */	
	ColleagueScrollbar(ScrollZoomDirector dir, int or, int val, int vis, int min, int max) { 
		super(or, val, vis, min, max);
		director = dir;
		prevVal = val;
	}

/**
 * Gets the previous value of the scrollbar
 * @return the previous value of the scrollbar
 */	
	public int getPrevVal(){
		return prevVal;
	}

/**
 * Changes the value of the scrollbar with the difference parameter
 * @param diff difference to adjust scrollbar value
 */	
	public void changeValue(int diff){
		setValue(getValue() + diff);
	}
	
/**
 * Sets the previous value
 * @param val the value to be set
 */
 	public void setPrevVal(int val) {
		prevVal = val;
	}
	
/**
 * Sets the ScrollZoomDirector for the scrollbar
 * @param dir the ScrollZoomDirector
 */
 	public void setDirector(ScrollZoomDirector dir) {
		director = dir;
	}

/**
 * Sets min and max for the scrollbar
 * @param min min value
 * @param max max value
 */
 	public void setMinMax(int min, int max) {
		setMinimum(min);
		setMaximum(max);
	}
		
// /**
//  * Notifies the ScrollZoomDirector that this scrollbar has been changed
//  * @param e the event that changed the scrollbar
//  */
//  	public void changed(AdjustmentEvent e) {
// // 		System.out.println(this + " changed");
// // 		System.out.println("director: " +  director);
// 		director.scrollBarChanged(this, e);
// 	}
}
