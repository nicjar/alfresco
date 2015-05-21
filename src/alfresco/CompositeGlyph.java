/*
 * $Revision: 1.1 $
 * $Id: CompositeGlyph.java,v 1.1 2003/04/04 10:13:56 niclas Exp $
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
 * Abstract Glyph with children.<p>
 * @see alfresco.Glyph
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
public abstract class CompositeGlyph extends Glyph {

/**
 * Creates a CompositeGlyph(). <p>
 * CompositeGlyphs should not be instanciated.
 */
 	CompositeGlyph() {
		children = new Vector();
	}
	
/**
 * Determines the bounds of the Glyph, children included
 * @return the bounding rectangle of the Glyph
 */
 	public Rectangle getBounds() {
		if (children.isEmpty()) {
			if (boundingRect == null ) {
				boundingRect = new Rectangle(canvX, canvY, canvWidth, canvHeight);
			}
			return boundingRect;
		}
		Rectangle rect = null;	// 
 		boolean first = true;
		Enumeration ce = children.elements();
		while (ce.hasMoreElements()) {
			Glyph gl = (Glyph) ce.nextElement();
			if (gl instanceof Scale) { continue; }
// 			if (this instanceof Entry && Math.abs(gl.tier) > 1) { continue; }
			Rectangle tmprect = gl.getBounds();
			if (tmprect != null) {
				if (first) {
					rect = tmprect;
					first = false;
				} else {
					rect = tmprect.union(rect);
				}
			}
		}
// 		if (boundingRect == null) {
// 			boundingRect = rect;
// 		} else {
// 			boundingRect = boundingRect.union(rect);
// 		}
		boundingRect = rect;
		//System.out.println(this + ": " + boundingRect);
		return boundingRect;
	}
	
/**
 * If x,y is within boundingRect returns the child that was clicked or the 
 * Glyph itself, otherwise null.
 * @return the clicked Glyph
 * @param x mouse x position
 * @param y mouse y position
 */
 	public Glyph clicked(int x, int y) {
		// System.out.println("checking compositeGlyph: " + this);
		if (boundingRect.contains(x,y)) {
			CompositeGlyph compgl = null;
			Enumeration ce = children.elements();
			while (ce.hasMoreElements()) {
				Glyph gl = (Glyph) ce.nextElement();
				Glyph cgl = gl.clicked(x, y);
				if (cgl instanceof CompositeGlyph) {
					if (compgl == null) 
						compgl = (CompositeGlyph) cgl;
					continue;
				}
				if (cgl != null) { return cgl; }
			}
			if (compgl != null)
				return compgl;
			return this;
		}
		return null;
	}
		
/**
 * Adds a Glyph to the Vector of children
 * @param gl the Glyph to be added
 */
 	public void addChild(Glyph gl) {	// Note! children get stored backwards!!
    if ((gl instanceof Similarity) && ( featureExists((SeqFeature)gl) ) ) {
      return;
    }
		if (!(gl instanceof Gene)) {
			children.insertElementAt(gl, 0);	//add to beginning of Vector
		} else {
			children.addElement(gl);	// add Gene to end of Vector
		}
 		gl.parent = this;
// 		gl.setTier(this.tier);
		//System.out.println("Adding child " + gl + " to parent " + this);
		//System.out.println("this tier: " + this.tier + " childs tier: " + gl.tier);
	}
	
/**
 * Adds a contents of Vector to the Vector of children
 * @param gl the Vector of Glyphs to be added
 */
	public void addChildren(Vector children) {
		Enumeration kids = children.elements();
		while(kids.hasMoreElements()){
			Glyph gl = (Glyph) kids.nextElement();
			addChild(gl);
		}
	}
	
	/**
	 * Removes all children
	 */
	public void removeChildren() {
		for(int i = children.size() - 1; i >= 0; i--) {
			Glyph gl = (Glyph) children.elementAt(i);
			if (gl instanceof CompositeGlyph) {
				((CompositeGlyph)gl).removeChildren();
				removeChild(gl);
			} else {
				removeChild(gl);
			}
		}
	}
	/**
	 * Removes children in specified Vector<br>
   * HAS TO BE FIXED TO DO A PROPER JOB !
   * @param chlidren vector of children to be removed
	 */
	public void removeChildren(Vector glyphs) {
    Enumeration ren = glyphs.elements();
    while (ren.hasMoreElements() ) {
      Glyph gl = (Glyph) ren.nextElement();
      removeChild(gl);
    }
    
	}
	
	/**
	 * Updates the CoordVal object for the children. Useful after loading a saved
	 * object
	 */
	public void updateCoord() {
		Enumeration kids = children.elements();
		while(kids.hasMoreElements()){
			Glyph gl = (Glyph) kids.nextElement();
			gl.coord = coord;
			if (gl instanceof CompositeGlyph) {
				((CompositeGlyph)gl).updateCoord();
			}
		}
		
	}
	
	
/**
 * Gets an enumeration for the children
 * @return Enumeration for children
 */
 	Enumeration getChildEnumeration () {
		return children.elements();
	}

/**
 * Gets a vector of children of the type specified by the Class object
 * @return Vector of Glyphs
 * @param cl Class object of the type you want to get children of
 */
	public Vector getChildrenByClass(Class cl) {
		Vector result = new Vector();
		Enumeration kids = children.elements();
		while(kids.hasMoreElements()){
			Object child = kids.nextElement();
			if (cl.isInstance(child)) {
				result.addElement(child);
			} else if (child instanceof CompositeSeqFeature) {
				CompositeSeqFeature csf = (CompositeSeqFeature) child;
				Vector childresult = csf.getChildrenByClass(cl);
				if (!childresult.isEmpty()) {
					Enumeration grandkids = childresult.elements();
					while (grandkids.hasMoreElements()) {
						result.addElement(grandkids.nextElement());
					}
				}
			}
		}
		return result;
	}
/**
 * Gets a vector of children of the type specified by the Class object
 * @param cl Class object of the type you want to remove
 */
	public void removeChildrenByClass(Class cl) {
		Vector clChildren = getChildrenByClass(cl);
    removeChildren(clChildren);
	}
  /**
   * Checks if an SeqFeature alreaddy exists
   * @return true if feature exists, otherwise false
   * @param f sequence feature object
   */
  public boolean featureExists(SeqFeature f) {
    Class cl = f.getClass();
    Enumeration fen = getChildrenByClass(cl).elements();
    while (fen.hasMoreElements()) {
      SeqFeature tf = (SeqFeature)fen.nextElement();
      if (f.equals(tf)) return true;
    }
    return false;
  }
/**
 * Sets the tier of the Glyph (and it's children)
 * @param tier the tier of the Glyph
 */
 	public void setTier(int tier) {
		this.tier = tier;
		Enumeration kids = getChildEnumeration();
		while(kids.hasMoreElements()) {
			Glyph gl = (Glyph) kids.nextElement();
			gl.setTier(tier);
		}
	}

/**
 * Draws the Glyph to the Graphics object specified
 * @param g Graphics obj to draw to
 */
 	public void draw(Graphics g) {
		//System.out.println("Drawing " + this);
// 		if (parent != null) {
// 			zoom = parent.zoom;
// 		}
		Enumeration e = getChildEnumeration();
 		while(e.hasMoreElements()) {
 			Glyph glyph = (Glyph) e.nextElement();
 			// System.out.println("Drawing " + glyph);
 			glyph.draw(g);
 		}
 		boundingRect = getBounds();
 		// System.out.println(this + " " + boundingRect);
 	}
	
}

