/*
* $Revision: 1.1 $
* $Id: SelectorPair.java,v 1.1 2003/04/04 10:15:04 niclas Exp $
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

/**
 * A Glyph class to hold a pair of Selector objects.
 * @see alfresco.CompositeGlyph
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class SelectorPair extends CompositeGlyph {
/**
 * First selector
 */
	Selector sel1;
/**
 * Second selector
 */
	Selector sel2;
	
/**
 * Creates Selector
 */
	SelectorPair() { }
	
/**
 * Adds a selector. To add a second selector single must be false. 
 * Adding a second Selector that overlaps the first will remove the first
 * selector.
 * @param sel the Selector to add
 * @param single true if there should be only one Selector, false otherwise
 */
	public void addSelector(Selector sel, boolean single) {
		EntryPair par = (EntryPair) parent;
		if(single) {
			reset();
			sel1 = sel;
			par.selectGlyph(sel1);
		} else {
			if (sel1 != null) {
				if (sel.overlap(sel1)) {
					par.removeSelectedGlyph(sel1);
					sel1 = sel;
					par.selectGlyph(sel1);
				} else {
					if(sel2 != null) {
						par.removeSelectedGlyph(sel2);
					}
					sel2 = sel;
					par.selectGlyph(sel2);
				}
			} else {
				sel1 = sel;
				par.selectGlyph(sel1);
			}
		}
	}
	
/**
 * Removes all selectors
 */
	public void reset() {
		EntryPair par = (EntryPair) parent;
		if (sel1 != null) {
			par.removeSelectedGlyph(sel1);
			sel1 = null;
		}
		if (sel2 != null) {
			par.removeSelectedGlyph(sel2);
			sel2 = null;
		}
	}
	
/**
 * Draws the child Selectors to Graphics specified
 * @param g Graphics to draw to
 */
	public void draw(Graphics g) {
 		if(sel1 != null) {
 			sel1.draw(g);
 		}
 		if (sel2 != null) {
 			sel2.draw(g);
 		}
 	}

/**
 * String representation of SelectorPair
 * @return string representation
 */
	public String toString() {
		if (sel1 == null && sel2 == null) return "Empty SelectorPair";
		StringBuffer sb = new StringBuffer();
		if (sel1 != null) {
			sb.append(sel1.toString());
		}
		if (sel2 != null) {
			sb.append(" and " + sel2.toString());
		}
		return new String(sb);
	}
}
