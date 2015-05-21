/*
* $Revision: 1.1 $
* $Id: DotterPoint.java,v 1.1 2003/04/04 10:14:09 niclas Exp $
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

/**
 * Represents a point in a dotter output file score matrix
 * @see alfresco.DotterFile
 * @see java.io.Serializable
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class DotterPoint implements Serializable{
/**
 * Horizontal range
 */
	SeqRange horizontal;
/**
 * Vertical range
 */
	SeqRange vertical;
/**
 * The dotter score
 */
	int score;
	
/**
 * Creates new DotterPoint
 * @param h horizontal range
 * @param v vertical range
 * @param s score
 */
	public DotterPoint(SeqRange h, SeqRange v, int s) {
		horizontal = h;
		vertical = v;
		score = s;
	}
	
/**
 * Gets dotter score
 * @return score
 */
	public int getScore() {
		return score;
	}
	
/**
 * Gets horizontal range
 * @return range
 */
	public SeqRange getHorizontal() {
		return horizontal;
	}
	
/**
 * Gets vertical range
 * @return range
 */
	public SeqRange getVertical() {
		return vertical;
	}
	
/**
 * Returns a string representation of a DotterPoint
 * @return string representation of a DotterPoint
 */
	public String toString() {
		return "{ h:" + horizontal + ", v:" + vertical + ", score: " + score + " }";
	}
}
