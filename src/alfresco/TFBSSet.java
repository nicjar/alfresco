/*
* $Revision: 1.1 $
* $Id: TFBSSet.java,v 1.1 2003/04/04 10:15:19 niclas Exp $
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
/**
 * A set of visible TFBS ids
 * @see java.util.Observable
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
public class TFBSSet extends Observable {
/**
 * Vector of TFBS IDs
 */ 
	Vector tfbsV;
	
	/**
	 * Creates new TFBSSet
	 */
	public TFBSSet() {
		tfbsV = new Vector();
	}
	
	/**
	 * Description
	 * @return descr
	 * @param param descr
	 */
	public void setIDs(Vector ids) {
		tfbsV = ids;
		setChanged();
		notifyObservers(tfbsV);
	}
	
// /**
//  * Description
//  * @return descr
//  * @param param descr
//  */
// 	public void notifyObservers(Object arg) {
// 		super.notifyObservers(arg);
// 		System.out.println("TFBSSet: Notifying observers");
// 		System.out.println("TFBSSet: " + countObservers() + " observers");
// 	}
	
// 	/**
// 	 * Adds a TFBS id to list
// 	 * @param id TFBS id
// 	 */
// 	public void addId(String id) {
// 		
// 	}
// 	
// 	/**
// 	 * Removes a TFBS id from list
// 	 * @param id TFBS id
// 	 */
// 	public void removeId(String id) {
// 		
// 	}
	
	
	/**
	 * Determines if TFBSSet is empty or not
	 * @return true if set is empty, otherwise false
	 */
	public boolean isEmpty() {
		return tfbsV.isEmpty();
	}
}
