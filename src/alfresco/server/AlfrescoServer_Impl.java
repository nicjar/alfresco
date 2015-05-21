/*
* $Revision: 1.1 $
* $Id: AlfrescoServer_Impl.java,v 1.1 2003/04/04 10:35:58 niclas Exp $
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
import java.io.*;
/**
 * Implements the AlfrescoServer interface
 * @see alfresco.server.AlfrescoServer
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class AlfrescoServer_Impl extends _AlfrescoServerImplBase {
/**
 * The directory of entry pairs
 */
	File pairDirectory;
	/**
	 * Creates new AlfrescoServer_Impl
	 * @param dir directory of entry pairs
	 */
	public AlfrescoServer_Impl(File dir) {
		super();
		pairDirectory = dir;
// 		PairStruct p = getPair("MMENDOBA-HSKER101");
// 		System.out.println(p.cgff);
	}

/**
 * Gets the name pairs of entry pairs
 * @return array of names
 */
	public String[] getNames () {
		String[] files = pairDirectory.list(new FilenameFilter(){		// filter *.cgff. 
			public boolean accept(File dir, String fn) {
// 					System.out.println("accept() called with: " + dir + ", " + fn);
				return fn.endsWith(".cgff");
			}
		});
		for (int i = 0; i < files.length; i++) {
			String tmp = files[i];
			int ind = files[i].lastIndexOf(".");
			files[i] = tmp.substring(0, ind);
// 				System.out.println(files[i]);
		}
		return files;
	}

	/**
	 * Gets a PairStruct of the specified name
	 * @return pair struct
	 * @param name name of entry pair
	 */
	public PairStruct getPair(String name) {
		int hind = name.indexOf("-");
		String first = name.substring(0, hind);
		String second = name.substring(hind + 1);
// 		System.out.println("First: " + first + ", second: " + second);
		PairStruct pair = new PairStruct(readFile(first),
		                                 readFile(second),
		                                 readFile(name + ".gff"),
		                                 readFile(name + ".cgff"));
		return pair;
	}

	/**
	 * Reads the specified file and returns the the contents like a String
	 * @return Contents of the file as a \n delimeted string
	 * @param fname filename
	 */
	private String readFile(String fname) {
		FileReader fr=null;
		try {
		  fr = new FileReader(new File(pairDirectory.getPath(), fname));
		} catch(FileNotFoundException fnf) { fnf.printStackTrace();}
		BufferedReader br = new BufferedReader(fr);
		StringBuffer sb = new StringBuffer();
		String inline = null;
		try {
		  while ((inline = br.readLine()) != null) {
				sb.append(inline + "\n");
		  }
		} catch(IOException ioe) { ioe.printStackTrace();}
		return new String(sb);

	}
}
