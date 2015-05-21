/*
* $Revision: 1.1 $
* $Id: GffHeader.java,v 1.1 2003/04/04 10:14:29 niclas Exp $
*
* This file is part of Alfresco
* Copyright (C) 1999  Niclas Jareborg
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
* For more information contact Niclas at niclas.jareborg@cgr.ki.se
*
*/
package alfresco;
import java.util.*;
/**
 * Class for creating gff header strings
 * @version 1
 * @author Niclas Jareborg, Center for Genomics Research
 */

public class GffHeader {
  
  private String gff;
  
  /**
   * Creates new gff header string
   */
    public GffHeader() {
//       System.out.println("Constructing GffHeader");
	    Calendar cal = Calendar.getInstance();
	    StringBuffer csb = new StringBuffer();
	    csb.append(String.valueOf(cal.get(Calendar.YEAR)) + "-");
	    int month = cal.get(Calendar.MONTH) + 1;	// MONTH = 0-11
	    if (month < 10 ) csb.append("0");
	    csb.append(String.valueOf(month) + "-");
	    if (cal.get(Calendar.DAY_OF_MONTH) < 10 ) csb.append("0");
	    csb.append(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
// 	    String datestring = new String(csb);
      gff = "##gff-version\t1\n##date\t" + new String(csb) + "\n";
//       System.out.println("Inside GffHeader: \n" + gff);
    }
    
    /**
     * Gets the gff header string
     * @return header string
     */
    public String getHeader() {
      return gff;
    }
}
