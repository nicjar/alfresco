/*
 * $Revision: 1.1 $
 * $Id: CompositeSeqFeature.java,v 1.1 2003/04/04 10:13:57 niclas Exp $
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
 * Represents a sequence feature that can have children.<p>
 * @see alfresco.CompositeGlyph
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public abstract class CompositeSeqFeature extends CompositeGlyph {

/**
 * Entry that the feature belongs to
 */
	Entry entry;

	
/**
 * Creates a CompositeSeqFeature without an Entry
 */
 	CompositeSeqFeature() {
		super();
	}
	
/**
 * Creates a CompositeSeqFeature with an Entry
 * @param ent Entry the feature belongs to
 */
 	CompositeSeqFeature(Entry ent) {
		super();
		entry = ent;
		coord = entry.coord;
		tier = coord.entryTier;
	}	
	
/**
 * Creates a CompositeSeqFeature with an Entry
 * @param ent Entry the feature belongs to
 * @param children Vector of child Glyphs
 */
 	CompositeSeqFeature(Entry ent, Vector children) {
		super();
		entry = ent;
		coord = entry.coord;
		tier = coord.entryTier;
		addChildren(children);
// 		Enumeration kids = children.getElements();
// 		while (kids.hasMoreElements()) {
// 			Glyph gl = (Glyph) kids.nextElement();
// 			addChild(gl);
// 		}
	}	
/**
 * Adds a Glyph to the Vector of children
 * @param gl the Glyph to be added
 */
	public void addChild(Glyph gl) {
		super.addChild(gl);
		complement = gl.complement;
		if (parent != null) { 			// Que'?
			parent.complement = complement;
		}
	}
	
// /**
//  * Adds a contents of Vector to the Vector of children
//  * @param gl the Vector of Glyphs to be added
//  */
// 	public void addChildren(Vector children) {
// 		Enumeration kids = children.elements();
// 		while(kids.hasMoreElements()){
// 			Glyph gl = (Glyph) kids.nextElement();
// 			addChild(gl);
// 		}
// 	}
// 	

// 	/**  Moved to CompositeGlyph
// 	 * Gets a vector of children of the type specified by the Class object
// 	 * @return Vector of Glyphs
// 	 * @param cl Class object of the type you want to get children of
// 	 */
// 		public Vector getChildrenByClass(Class cl) {
// 			Vector result = new Vector();
// 			Enumeration kids = children.elements();
// 			while(kids.hasMoreElements()){
// 				Object child = kids.nextElement();
// 				if (cl.isInstance(child)) {
// 					result.addElement(child);
// 				} else if (child instanceof CompositeSeqFeature) {
// 					CompositeSeqFeature csf = (CompositeSeqFeature) child;
// 					Vector childresult = csf.getChildrenByClass(cl);
// 					if (!childresult.isEmpty()) {
// 						Enumeration grandkids = childresult.elements();
// 						while (grandkids.hasMoreElements()) {
// 							result.addElement(grandkids.nextElement());
// 						}
// 					}
// 				}
// 			}
// 			return result;
// 		}

	/**
	 * Evaluates if the CompositeSeqFeature overlaps another object
	 * @return true if CompositeSeqFeature overlaps object, otherwise false
	 * @param param descr
	 */	
	public boolean overlap(SeqFeature sf) {
		boolean overlap = false;
		if (children == null) return false;
		Enumeration kids = getChildEnumeration();
		while (kids.hasMoreElements()) {
			Object o = kids.nextElement();
			if (o instanceof CompositeSeqFeature) {
				CompositeSeqFeature csf = (CompositeSeqFeature) o;
				overlap = csf.overlap(sf);
			} else if (o instanceof SeqFeature) {
				SeqFeature tsf = (SeqFeature) o;
				overlap = tsf.overlap(sf);
			}
			if (overlap) return true;
		}
		return false;
	}
	
	public SeqFeature getFeature(SeqRange range, String classname) {
		Enumeration kids = getChildEnumeration();
		while(kids.hasMoreElements()) {
			Glyph gl = (Glyph) kids.nextElement();
			if (gl instanceof SeqFeature) {
				if (gl.getClass().getName().equals("alfresco." + classname))
					if ( range.isEqual( ((SeqFeature) gl).getSeqRange() ) )
						return (SeqFeature) gl;
			} else if (gl instanceof CompositeSeqFeature){
				SeqFeature sf = ((CompositeSeqFeature) gl).getFeature(range, classname);
				if (sf != null) return sf;
			}
		}
		return null;
	}
	
	/**
	 * Gets the SeqRange of the CompositeSeqFeature
	 * @return range
	 */
	public SeqRange getSeqRange() {
		if(children.size() == 0) return null;
		Enumeration kids = children.elements();
		int minstart = 0;
		int maxstop = 0;
		while (kids.hasMoreElements()){
			SeqRange r = null;
			Object child = kids.nextElement();
			if (child instanceof CompositeSeqFeature) {
				CompositeSeqFeature csf = (CompositeSeqFeature) child;
				r = csf.getSeqRange();
			} else if (child instanceof SeqFeature) {
				SeqFeature sf = (SeqFeature) child;
				r = sf.getSeqRange();
			} else continue;
			if (minstart == 0) {
				minstart = r.getStart();
			} else {
				minstart = (r.getStart() < minstart)?r.getStart():minstart;
			}
			maxstop = (r.getStop() > maxstop)?r.getStop():maxstop;
		}
		return new SeqRange(minstart, maxstop);
	}
	
/**
 * Draws the Glyph to the Graphics object specified
 * @param g Graphics obj to draw to
 */
	public void draw(Graphics g) {
// 		canvY = entry.canvY;
		canvY = coord.originY;
		super.draw(g);
		boundingRect = getBounds();
 	}

}

