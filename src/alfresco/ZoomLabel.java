/*
* $Revision: 1.1 $
* $Id: ZoomLabel.java,v 1.1 2003/04/04 10:15:27 niclas Exp $
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
import java.math.*;
/**
 * Label for displaying zoomlevel of an entry
 * @see java.awt.Label
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class ZoomLabel extends Label {
/**
 * The zoomlevel value
 */
	String value;
/**
 * Description text prefix of the label
 */
	String prefix;
/**
 * Suffix of the label: "pixels/bp"
 */
	String suffix = " pixels/bp";
	
/**
 * Creates ZoomLabel with the specified prefix
 * @param prefix description text prefix
 */
	ZoomLabel(String prefix) {
		super();
		this.prefix = prefix + " ";
		setText(prefix + " - " + suffix);
	}
	
/**
 * Sets the zoomlevel value of the label
 * @param val zoomlevel value
 */
	public void setValue(double val) {
		BigDecimal bd = new BigDecimal(val);
		bd = bd.setScale(3, BigDecimal.ROUND_HALF_EVEN);
		value = bd.toString();
// 		value = String.valueOf((float) val);
		setText(prefix + value + suffix);
	}
	
/**
 * Removes the zoomlevel value of the label
 */
	public void reset() {
		setText(prefix + " - " + suffix);
	}
}
