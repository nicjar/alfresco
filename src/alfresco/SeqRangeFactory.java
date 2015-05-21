/*
* $Revision: 1.1 $
* $Id: SeqRangeFactory.java,v 1.1 2003/04/04 10:15:07 niclas Exp $
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
import java.io.*;
import java.util.*;
/**
 * Class for generating SeqRange objects, and keeping track of which 
 * SeqRanges have already been created. Generated ranges are stored in a 
 * Hashtable of Hashtables. {start}->{stop}->range
 * @see java.util.Hashtable
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class SeqRangeFactory extends Hashtable {
	
/**
 * Creates new SeqRangeFactory
 */
	public SeqRangeFactory() {
		super();
	}
	
/**
 * Returns a SeqRange with the specified begin and end. If an equal SeqRange
 * has already been created it will be returned, otherwise a new SeqRange will
 * be created.
 * @return the requested SeqRange
 * @param begin begin of SeqRange
 * @param end end of SeqRange
 */
	public SeqRange getSeqRange(int begin, int end) {
		Integer startInt = new Integer(begin);
		Integer stopInt = new Integer(end);
		Hashtable stops = (Hashtable) this.get(startInt);
		if (stops != null ) {
			SeqRange r = (SeqRange) stops.get(stopInt);
			if (r != null) {
				return r;
			}
			r = new SeqRange(begin, end);
			stops.put(stopInt, r);
			return r;
		}
		SeqRange r = new SeqRange(begin, end);
		stops = new Hashtable();
		stops.put(stopInt, r);
		this.put(startInt, stops);
		return r;
	}
}
