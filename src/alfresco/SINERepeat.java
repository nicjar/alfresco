/*
* $Revision: 1.1 $
* $Id: SINERepeat.java,v 1.1 2003/04/04 10:14:58 niclas Exp $
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
 * Glyph that represents a SINE repeat
 * @see alfresco.Repeat
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class SINERepeat extends Repeat {
	
/**
 * Creates a SINERepeat of the specified Entry, with the specified start and stop
 * on the upper strand
 * @param entry parent Entry
 * @param start repeat start
 * @param stop repeat stop
 */
	SINERepeat(Entry ent, int start, int stop) {
		super(ent, start, stop);
		fillColor = Color.blue;
		brighter(4);
	}
	
/**
 * Creates a SINERepeat of the specified Entry, with the specified start and stop
 * on the upper strand
 * @param entry parent Entry
 * @param start repeat start
 * @param stop repeat stop
 */
	SINERepeat(Entry ent, SeqRange r) {
		super(ent, r);
		fillColor = Color.blue;
		brighter(4);
	}

/**
 * Creates a SINERepeat of the specified Entry, with the specified star and stop,
 * on the strand specified by compl
 * @param entry parent Entry
 * @param start repeat start
 * @param stop repeat stop
 * @param compl boolean value wheter feature is reverse-complement or not
 */
	SINERepeat(Entry ent, int start, int stop, boolean compl) {
		super(ent, start, stop, compl);
		fillColor = Color.blue;
		brighter(4);
	}
	
/**
 * Creates a SINERepeat of the specified Entry, with the specified range
 * on the strand specified by compl
 * @param entry parent Entry
 * @param r range of repeat
 * @param compl boolean value wheter feature is reverse-complement or not
 */
	SINERepeat(Entry ent, SeqRange r, boolean compl) {
		super(ent, r, compl);
		fillColor = Color.blue;
		brighter(4);
	}
	
}

