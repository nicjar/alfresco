/*
* $Revision: 1.1 $
* $Id: Glyph.java,v 1.1 2003/04/04 10:14:31 niclas Exp $
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
import java.awt.*;
import java.util.*;
import java.io.*;

/**
 * A superclass of all drawable objects.<p>
 * A Glyph can either be a node, that has children, or a leaf, that doesn't
 * have children. 
 * @see Serializable
 * @see UsefulConstants
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
public abstract class Glyph implements Serializable, Cloneable, UsefulConstants{
/**
 * The containing Glyph
 */
	Glyph parent;

/**
 * Vector of child Glyphs
 */
	Vector children;

/**
 * Zoomlevel
 */
	double zoom = -1;

/**
 * Height of Glyph in pixels
 */
	int canvHeight;

/**
 * Width of Glyph in pixels on drawing area
 */
	int canvWidth;

/**
 * Real width of Glyph in pixels
 */
	int realWidth;

/**
 * X position of Glyph in pixels on drawing area. -ABSXMAX < canvX < ABSXMAX to 
 * fix problem with XWindows 32k limit
 */
	int canvX;

/**
 * Real X positon of Glyph in pixels.realX can be smaller than -ABSXMAX and larger 
 * than ABSXMAX 
 */
	int realX;

/**
 * Real end position of Glyph in pixels
 */
	int realXend;

/**
 * Y position of Glyph in pixels on drawing area
 */
	int canvY;

/**
 * Tier of Glyph. -CoordVal.MAXTIERS < tier < CoordVal.MAXTIERS
 */
	int tier = 0;

/**
 * Coordinate value object that holds information about the draw canvas and offset
 * position for Glyphs
 */
	transient CoordVal coord;

/**
 * Bounding Rectangle of the Glyph
 */
	Rectangle boundingRect; 

/**
 * If the Glyph represents the reverse-complement of a sequence 
 */
	boolean complement;

/**
 * If the Glyph is visible
 */
	boolean visible = true;

/**
 * If the Glyph is selected
 */
	boolean selected;
	
/**
 * Drawing color of the Glyph
 */
	Color fillColor;

/**
 * Default color of the Glyph
 */
	Color origColor;

/**
 * Highlight color of the Glyph
 */
	Color highlightColor = Color.red;
	
	
/**
 * An empty constructor. Only subclasses can be instanciated.
 */
 	public Glyph() { }
	
/**
 * Abstract draw method that all Glyphs should implement
 * @param g Graphics object to draw to
 */
 	public abstract void draw(Graphics g);
	
/**
 * Default cliced method. Determines if the Glyph was clicked. Returns itself 
 * if x,y is within boundingRect, otherwise returns null.
 * @return the clicked Glyph
 * @param x mouse x position
 * @param y mouse y position
 */
 	public Glyph clicked(int x, int y) {
		// System.out.println("checking Glyph: " + this);
		if (visible) {
			if (boundingRect.contains(x,y)) {return this;}
		}
		return null;
	}
	
/**
 * Sets fillColor to highlightColor if true, otherwise sets fillColor to 
 * original Color
 * @param select boolean to highlight or not
 */
 	public void select(boolean select) {
		if (select) {
			selected = true;
			origColor = fillColor;
			fillColor = highlightColor;
		} else {
			selected = false;
			fillColor = origColor;
		}
	}
	
	/**
	 * Determines if the Glyph overlaps another Glyph. Same tier, complement, and 
	 * overlap x wise.
	 * @return true if glyphs overlap, otherwise false
	 * @param gl glyph to compare
	 */
	public boolean overlap(Glyph gl) {
// 		if( ((this.canvX >= gl.canvX) && (this.canvX <= gl.canvX + gl.canvWidth)) ||
// 				((this.canvX + this.canvWidth >= gl.canvX) && (this.canvX + this.canvWidth <= gl.canvX + gl.canvWidth)) ||
// 				((this.canvX < gl.canvX) && (this.canvX + this.canvWidth > gl.canvX + gl.canvWidth)) )
// 			if ( this.tier == gl.tier ) 
// 				if ( (this.complement && gl.complement) || (!this.complement && !gl.complement) )
// 					return true;
		if(this.getBounds().intersects(gl.getBounds()))
			return true;
		return false;
	}
	
/**
 * Should be overridden by subclasses with children.
 * @param child Glyph to be added
 */
 	public void addChild(Glyph child) {
		System.out.println("Can't add child to this type of Glyph");
	}
	
/**
 * Default removeChild method
 * @param gl Glyph to be removed
 */
 	public void removeChild(Glyph gl) {
		if (children != null) {
			if (gl instanceof CompositeGlyph) {
				((CompositeGlyph)gl).removeChildren();
				children.removeElement(gl);
				gl.setParent(null);
			} else {
				children.removeElement(gl);
				gl.setParent(null);
			}
// 			System.out.println("removing " + gl);
		}
	}
	
/**
 * Removes the Glyph as a child to the parent Glyph
 */
 	public void removeFromParent() {
		if (parent != null) {
			parent.removeChild(this);
		}
	}
/**
 * Gets the children of the Glyph
 * @return Vector of Glyphs
 */
 	public Vector getChildren() {
		return children;
	}
	/**
	 * Gets the parent glyph
	 * @return parent glyph
	 */
	public Glyph getParent() {
		return parent;
	}
	/**
	 * Sets the parent glyph
	 * @param parent glyph
	 */
	public void setParent(Glyph parent) {
		this.parent = parent;
	}
/**
 * Gets the boundingRect of the Glyph
 * @return Rectangle defining the bounds of the Glyph
 */
 	public Rectangle getBounds() {
		if (visible) {
			return boundingRect;
		}
		return null;
	}
	
/**
 * Gets the zoom level of the Glyph
 * @return zoom level
 */
 	public double getZoom() {
		return zoom;
	}
	
/**
 * Sets the tier of the Glyph
 * @param tier the tier of the Glyph
 */
 	public void setTier(int tier) {
		this.tier = tier;
	}
	
/**
 * Gets the tier of the Glyph
 * @return the tier of the Glyph
 */
 	public int getTier() {
		return tier;
	}
	
/**
 * Sets the fillColor of the Glyph
 * @param c the Color of the Glyph
 */
	public void setFillColor(Color c) {
		fillColor = c;
	}
	
/**
 * Sets whether the Glyph should be reverse-complement
 * @param vis boolean to make complement
 */
	public void complement(boolean compl) {
		complement = compl;
	}

/**
 * Sets whether the Glyph should be visible
 * @param vis boolean to make visible or not
 */
	public void setVisible(boolean vis) {
		visible = vis;
	}
	
	/**
	 * Determines if Glyph is visible or not
	 * @return true if visible, otherwise false
	 */
	public boolean isVisible() {
		return visible;
	}

/**
 * Converts an object to a String
 * @return the object description
 */
 	public String toString() {
		return this.getClassName();
	}

/**
 * Gets the name of the class without the package name
 * @return class name
 */
	public String getClassName() {
 		String classn = this.getClass().getName();
		int dotind = classn.lastIndexOf('.');
		if (dotind >= 0) {
			classn = classn.substring(dotind + 1);
		}
		return classn;		
	}

/**
 * Makes the fillColor brighter by the number of specified steps
 * @param steps number of steps to brighten
 */
 	protected void brighter(int steps) {
		for(int i = 0; i < steps; i++) {
			fillColor = fillColor.brighter();
		}
	}
	
/**
 * Makes the fillColor darker by the number of specified steps
 * @param steps number of steps to darken
 */
	protected void darker(int steps) {
		for(int i = 0; i < steps; i++) {
			fillColor = fillColor.darker();
		}
	}
	
	
}

