/*
* $Revision: 1.1 $
* $Id: AlfrescoApplet.java,v 1.1 2003/04/04 11:25:41 niclas Exp $
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
package alfresco.applet;
import java.applet.*;
import java.net.*;
import java.io.*;
import alfresco.*;
import alfresco.server.*;

/**
 * Class description
 * @see 
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class AlfrescoApplet extends Applet {
/**
 * CORBA server
 */
	AlfrescoServer server;
	/**
	 * inits the applet
	 */
		public void init() {
    	String iorurl = getParameter("iorurl");
			org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(this,null);
			
			org.omg.CORBA.Object obj = null;

      try {
        URL u = new URL(iorurl);
        BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));
        obj = orb.string_to_object(in.readLine());
      } catch (Exception ex) { ex.printStackTrace(); }	
			
			server = AlfrescoServerHelper.narrow(obj);
			Wcomp wc = new Wcomp(false, false);
			wc.setServer(server);
		}
}
