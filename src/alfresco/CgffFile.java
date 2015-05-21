/*
 * $Revision: 1.1 $
 * $Id: CgffFile.java,v 1.1 2003/04/04 10:13:51 niclas Exp $
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
 * CgffFile represents a .cgff file which holds information about corresponding features 
 * specified in a .gff file.
 * @see java.io.File
 * @see alfresco.GffFile
 * @see alfresco.UsefulConstants
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */ 
 
public class CgffFile extends File implements UsefulConstants{
/**
 * The EntryPair holding the Entries which features are to be written to file
 */
	EntryPair entries;

/**
 * Name of file
 */
	String gffFilename;

/**
 * Creates CgffFile object.
 * @param path directory path
 * @param filename filename
 * @param gffname name of .gff file containing features
 * @param entp EntryPair that holds the sequences
 */
	CgffFile(String path, String filename, String gffname, EntryPair entp){
		super(path, filename);
		entries = entp;
		gffFilename = gffname;
	}
/**
 * Gets the information of corresponding regions and writes to file
 */	
	public void write() {
		FileWriter fw = null;
		Enumeration lines = entries.cgffLines();
		Calendar cal = Calendar.getInstance();
		StringBuffer sb = new StringBuffer();
		sb.append(String.valueOf(cal.get(Calendar.YEAR)) + "-");
		int month = cal.get(Calendar.MONTH) + 1;	// MONTH = 0-11
		if (month < 10 ) sb.append("0");
		sb.append(String.valueOf(month) + "-");
		if (cal.get(Calendar.DAY_OF_MONTH) < 10 ) sb.append("0");
		sb.append(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
		String datestring = new String(sb);
    String linesep = System.getProperty("line.separator");
		try {
			fw = new FileWriter(this);
			fw.write("##cgff-version\t1" + linesep + "##date\t" + datestring + linesep);
			fw.write("##gff\t" + gffFilename + linesep);
			fw.write("##seq\t" + entries.entry1.getFilename() + "\t" + entries.entry2.getFilename() + linesep);
			while (lines.hasMoreElements()) {
				String line = (String) lines.nextElement();
// 				fw.write(line+"\n");
				fw.write(line + linesep);
			}
			fw.flush();
			fw.close();
		} catch (IOException ioe) { System.out.println(ioe); }		
	}
}
