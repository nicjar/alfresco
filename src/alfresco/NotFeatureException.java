/*
* $Revision: 1.1 $
* $Id: NotFeatureException.java,v 1.1 2003/04/04 10:14:43 niclas Exp $
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
/**
 * An exception that indicates that a Glyph is not a SeqFeature or 
 * a CompositeSeqFeature
 * @see Exception
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class NotFeatureException extends Exception {
	/**
	 * Creates a NotFeatureException
	 * @param message error message
	 */
	public NotFeatureException(String message) {
		super(message);
	}
}
