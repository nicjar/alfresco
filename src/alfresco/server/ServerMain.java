/*
* $Revision: 1.1 $
* $Id: ServerMain.java,v 1.1 2003/04/04 10:36:02 niclas Exp $
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
package alfresco.server;
import org.omg.CORBA.*;
import java.io.*;
/**
 * The main function provider for the AlfrescoServer
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class ServerMain {
	/**
	 * Main function
	 * @param args command line arguments
	 */
		public static void main(String [] args) {
			if (args.length != 2) usage();
			File dir = new File(args[0]);
      File iordir = new File(args[1]);
			if (!dir.isDirectory()) usage();
			if (!iordir.isDirectory()) usage();
			ORB orb = ORB.init(args, new java.util.Properties());
			BOA boa = ((com.ooc.CORBA.ORB)orb).BOA_init(args, new java.util.Properties());
			AlfrescoServer_Impl as = new AlfrescoServer_Impl(dir);
			System.out.println("Starting Alfresco server with repository dir " + dir.getPath());
			try {
				String ref = orb.object_to_string(as);
// 				String refFile = "/dna/nic/www/PairServer.ior";
				String refFile = iordir.getPath() + "/PairServer.ior";
        System.out.println("Writing ior ref file to " + refFile);
				FileWriter fw = new FileWriter(refFile);
				fw.write(ref);
				fw.flush();
				fw.close();
			} catch (IOException ioe) { ioe.printStackTrace(); }
			
			boa.impl_is_ready(null);
		}
		
		/**
		 * Prints a usage statement and exits
		 */
		private static void usage() {
			System.out.println("usage: java alfresco.server.ServerMain <repository dir> <ior file dir>");
			System.exit(1);
		}
}
