/*
* $Revision: 1.1 $
* $Id: GffFile.java,v 1.1 2003/04/04 10:14:28 niclas Exp $
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
 * GffFile represents a .gff file
 * @see java.io.File
 * @see alfresco.UsefulConstants
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */ 
 
class GffFile extends File implements UsefulConstants{
/**
 * Holds Entries that are the source of gff lines
 */
	EntryPair entries;
/**
 * Holds Entry that is the source of gff lines
 */
	Entry entry;
  SeqRange range;

/**
 * Creates GffFile with the specified name and EntryPair
 * @param path directory path
 * @param filename name of file
 * @param entp the EntryPair
 */
	GffFile(String path, String filename, EntryPair entp){
		super(path, filename);
		entries = entp;
	}
/**
 * Creates GffFile with the specified name, Entry and range
 * @param path directory path
 * @param filename name of file
 * @param entp the EntryPair
 */
	GffFile(String path, String filename, Entry ent, SeqRange r){
		super(path, filename);
		entry = ent;
    range = r;
	}
	
/**
 * Writes the feature information from entryPair to a file in gff format
 */
	public void write() {
		FileWriter fw = null;
    Enumeration lines = null;
    if (entries != null) {
		  lines = entries.gffLines();
    } else if (entry != null) {
      lines = getRelativeGff();
    }
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
// 			fw.write("##gff-version\t1\n##date\t" + datestring + "\n");
			fw.write("##gff-version\t1" + linesep + "##date\t" + datestring + linesep);
			while (lines.hasMoreElements()) {
				String line = (String) lines.nextElement();
// 				fw.write(line+"\n");
				fw.write(line + linesep);
			}
			fw.flush();
			fw.close();
		} catch (IOException ioe) { System.out.println(ioe); }		
	}
  
  /**
   * Recalculates and alters gff lines covering range
   * @return enumeration of gff lines
   */
  private Enumeration getRelativeGff(){
    Enumeration gffLines = entry.getGffLines().elements();
    Vector changedLines = new Vector();
    while (gffLines.hasMoreElements()) {
      String line = (String) gffLines.nextElement();
      StringTokenizer st = new StringTokenizer(line);
      String seq = st.nextToken();
      String method = st.nextToken();
      String feature = st.nextToken();
      int start = Integer.parseInt(st.nextToken());
      int stop = Integer.parseInt(st.nextToken());
      if(!range.overlap(new SeqRange(start, stop))) { continue; }
      String score = st.nextToken();
      String strand = st.nextToken();
      String frame = st.nextToken();
      StringBuffer rest = new StringBuffer(st.nextToken());
      while(st.hasMoreElements()) {
        rest.append(" " + st.nextToken());
      }
      String group = new String(rest);
//       System.out.println("Range: "+ range + ", Start: " + start + ", Stop: " + stop);
      int newStart = start - range.getStart() + 1;
//       System.out.println("newStart: " + newStart);
      if (newStart <= 0) { newStart = 1; }
      int newStop = stop - range.getStart() + 1;
//       System.out.println("newStop: " + newStop);
      if (newStop > range.length()) { newStop = range.length(); }
      seq = entry.getFilename() + "_" + range.getStart() + "-" + range.getStop();
      String chLine = seq + "\t" + method + "\t" + feature + "\t" + newStart + "\t" + newStop + "\t" + score + "\t" + strand + "\t" + frame + "\t" + group;
      changedLines.addElement(chLine);
    }
    return changedLines.elements();
  }
}
