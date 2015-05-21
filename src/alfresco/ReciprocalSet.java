/*
 * $Revision: 1.1 $
 * $Id: ReciprocalSet.java,v 1.1 2003/04/04 10:14:51 niclas Exp $
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
 * A Glyph that represents a set of Reciprocal objects
 * @see alfresco.CompositeGlyph
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class ReciprocalSet extends CompositeGlyph {
// 	Entry entry1;
// 	Entry entry2;
	
/**
 * Creates ReciprocalSet
 */
	ReciprocalSet () { 
		children = new Vector();
	}
	
// 	ReciprocalSet (Entry ent1, Entry ent2) {
// 		entry1 = ent1;
// 		entry2 = ent2;
// 		children = new Vector();
// 	}
// 	
// 	ReciprocalSet (EntryPair entp) {
// 		parent = entp;
// 		entry1 = entp.entry1;
// 		entry2 = entp.entry2;
// 		children = new Vector();
// 	}
// 	
// 	ReciprocalSet (Entry ent1, Entry ent2, Vector reciprocals) {
// 		entry1 = ent1;
// 		entry2 = ent2;
// 		children = reciprocals;
// 	}
// 	
// 	public void addReciprocal(Reciprocal r) {
// 		children.addElement(r);
// 	}
	
/**
 * Adds a Glyph to the Vector of children
 * @param gl the Glyph to be added
 */
	public void addChild(Glyph gl) {
		children.addElement(gl);
		gl.parent = this;
	}
	
/**
 * Gets an Enumeration of the Reciprocals
 * @return reciprocal enumeration
 */
	public Enumeration getReciprocals() {
		return children.elements();
	}
	
/**
 * Whether Reciprocals of Introns should be shown or not
 * @param show true if Reciprocals of Introns should be shown, false if not
 */
	public void showIntronReciprocals(boolean show) {
		Enumeration kids = children.elements();
		while(kids.hasMoreElements()) {
			Reciprocal r = (Reciprocal) kids.nextElement();
			if (r.feature1 instanceof Intron) {
				r.setVisible(show);
			}
		}
	}
	
	/**
	 * Removes all Reciprocals that have features w/o parents
	 */
	public void purgeReciprocals() {
		for(int i = children.size() -1; i >= 0; i--) {
			Reciprocal r = (Reciprocal) children.elementAt(i);
			if (r.getFeature1().getParent() == null ||
					r.getFeature2().getParent() == null)
				removeChild(r);
		}
	}
}
