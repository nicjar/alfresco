/*
* $Revision: 1.1 $
* $Id: SystemConstants.java,v 1.1 2003/04/04 10:15:14 niclas Exp $
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

/**
 * Interface defining some system constants
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public interface SystemConstants {
/**
 * Path to dotter executable.
 */
	public static final String DOTTERPATH = (System.getProperty("alfresco.dotterpath") != null)? System.getProperty("alfresco.dotterpath"): "";

/**
 * Path to cpg executable.
 */
	public static final String CPGPATH = (System.getProperty("alfresco.cpgpath") != null)? System.getProperty("alfresco.cpgpath"): "";

/**
 * Path to dba executable.
 */
	public static final String DBAPATH = (System.getProperty("alfresco.dbapath") != null)? System.getProperty("alfresco.dbapath"): "";
// /**
//  * Path to dbb executable.
//  */
// 	public static final String DBBPATH = (System.getProperty("alfresco.dbbpath") != null)? System.getProperty("alfresco.dbbpath"): "";
/**
 * Path to RepeatMasker executable.
 */
	public static final String REPEATMASKERPATH = (System.getProperty("alfresco.repeatmaskerpath") != null)? System.getProperty("alfresco.repeatmaskerpath"): "";

/**
 * Path to genscan directory
 */
	public static final String GENSCANPATH = (System.getProperty("alfresco.genscanpath") != null)? System.getProperty("alfresco.genscanpath"): "/usr/local/bin/";
/**
 * Path to est_genome directory
 */
	public static final String EST_GENOMEPATH = (System.getProperty("alfresco.est_genomepath") != null)? System.getProperty("alfresco.est_genomepath"): "/usr/local/bin/";
/**
 * Path to tssw executable.
 */
	public static final String TSSWPATH = (System.getProperty("alfresco.tsswpath") != null)? System.getProperty("alfresco.tsswpath"): "";
// /**
//  * url to blastalign.ior.
//  */
// 	public static final String BLASTALIGN_IOR_URL = "http://kisac.cgr.ki.se/nic/blastalign.ior";
/**
 * url to corbaserver.ior.
 */
	public static final String METHODS_SERVER_IOR_URL = "http://kisac.cgr.ki.se/nic/methods_server.ior";
/**
 * Directory path to users pwd
 */
	public static final String USRDIR = System.getProperty("user.dir");
/**
 * Directory path to temporary directory used by Alfresco
 */
	public static final String TMPDIR = USRDIR + File.separator + ".alfrescotmp" + (System.currentTimeMillis()%1000000);
	
}
