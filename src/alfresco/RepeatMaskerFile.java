/*
* $Revision: 1.1 $
* $Id: RepeatMaskerFile.java,v 1.1 2003/04/04 10:14:56 niclas Exp $
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
 * Holds the content of a RepeatMasker .RepMask file
 * @see java.io.File
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class RepeatMaskerFile extends File {
/**
 * The content of the file as Vector of lines
 */
	Vector lines;
/**
 * 
 */
	String seqname;
	
/**
 * Creates new RepeatMaskerFile object with the specified path, name and Entry
 * @param path full path to file
 * @param ent the Entry to add Repeats to
 */
	RepeatMaskerFile(String path, String seqname) {
		super(path);
		this.seqname = seqname;
    
	}
	
/**
 * Reads the contents of a .RepMask file and stores it in the lines Vector
 */
	public void open() throws FileNotFoundException, IOException {
		if (this.exists()) {
			lines = new Vector();
			FileReader fr=null;
			BufferedReader br = null;
			try {
		  	fr = new FileReader(this);
		  	br = new BufferedReader(fr);
			} catch(FileNotFoundException fnf) { throw fnf;}
			String line = null;
			try {
		  	while ((line = br.readLine()) != null) {
					lines.addElement(line);
		  	}
			} catch(IOException ioe) { throw ioe;}
			return;
		}
		throw new FileNotFoundException();
	}

// /**
//  * Parses the result and generates the appropriate Repeat objects which are
//  * added to the Entry
//  */
// 	public void parse() {
// 		Enumeration lineEnum = lines.elements();
// 		while (lineEnum.hasMoreElements()) {
// 			Repeat rep = null;
// 			boolean complement = false;
// 			String line = (String) lineEnum.nextElement();
// 			if (line.indexOf("SW") > -1 || line.indexOf("score") > -1 || line.equals("")) {
// 				continue;
// 			}
// 			StringTokenizer st = new StringTokenizer(line); // tokenize on white space
// 			if (st.hasMoreElements()) {
// 				try {
// 					int score = Integer.parseInt((String) st.nextElement());
// 				} catch (NumberFormatException nfe) { return; }
// 				Float subst = new Float((String) st.nextElement());
// 				Float del = new Float((String) st.nextElement());
// 				Float insert = new Float((String) st.nextElement());
// 				String seqName = (String) st.nextElement();
// 				int start = Integer.parseInt((String) st.nextElement());
// 				int stop = Integer.parseInt((String) st.nextElement());
// 				String rest = (String) st.nextElement();
// 				String complString = (String) st.nextElement();
// 				if (complString.equals("C")) { complement = true; }
// 				String repName = (String) st.nextElement();
// 				String type = (String) st.nextElement();
// 				if (type.startsWith("LINE")) {
// 					rep = new LINERepeat(entry, start, stop, complement);
// 				} else if (type.startsWith("SINE")) {
// 					rep = new SINERepeat(entry, start, stop, complement);
// 				} else if (type.startsWith("Simple")) {
// 					rep = new SimpleRepeat(entry, start, stop, complement);
// 				} else if (type.startsWith("Low")) {
// 					rep = new LowComplexityRepeat(entry, start, stop, complement);
// 				} else if (type.startsWith("LTR")) {
// 					rep = new LTRRepeat(entry, start, stop, complement);
// 				} else if (type.startsWith("DNA")) {
// 					rep = new DNAtranspRepeat(entry, start, stop, complement);
// 				} else {
// // 					System.out.println("Neither bird nor fish");
// 					rep = new Repeat(entry, start, stop, complement);
// 				}
// 				rep.setAttributes(repName, type);
// 				rep.predicted(true);
// 				rep.setPredMethod("RepeatMasker");
// 				entry.addChild(rep);
// 			}
// 		}
// 	}
/**
 * Parses the result and generates Gff string
 */
	public String parseToGff() {
	  Calendar cal = Calendar.getInstance();
	  StringBuffer csb = new StringBuffer();
	  csb.append(String.valueOf(cal.get(Calendar.YEAR)) + "-");
	  int month = cal.get(Calendar.MONTH) + 1;	// MONTH = 0-11
	  if (month < 10 ) csb.append("0");
	  csb.append(String.valueOf(month) + "-");
	  if (cal.get(Calendar.DAY_OF_MONTH) < 10 ) csb.append("0");
	  csb.append(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
	  String datestring = new String(csb);
    StringBuffer gffb = new StringBuffer();
    gffb.append("##gff-version\t1\n##date\t" + datestring + "\n");
		Enumeration lineEnum = lines.elements();
		while (lineEnum.hasMoreElements()) {
			Repeat rep = null;
			boolean complement = false;
			String line = (String) lineEnum.nextElement();
      if (line.indexOf("There were no repetitive") > -1 ) {
        break;
      }
			if (line.indexOf("SW") > -1 || line.indexOf("score") > -1 || line.equals("")) {
				continue;
			}
			StringTokenizer st = new StringTokenizer(line); // tokenize on white space
			if (st.hasMoreElements()) {
				try {
					int score = Integer.parseInt((String) st.nextElement());
				} catch (NumberFormatException nfe) { nfe.printStackTrace(); return null; }
				Float subst = new Float((String) st.nextElement());
				Float del = new Float((String) st.nextElement());
				Float insert = new Float((String) st.nextElement());
				String seqName = (String) st.nextElement();
				int start = Integer.parseInt((String) st.nextElement());
				int stop = Integer.parseInt((String) st.nextElement());
				String rest = (String) st.nextElement();
				String complString = (String) st.nextElement();
				if (complString.equals("C")) { complement = true; }
				String repName = (String) st.nextElement();
				String type = (String) st.nextElement();
        gffb.append(seqname + "\tRepeatMasker\trepeat\t" + Integer.toString(start) + "\t" + Integer.toString(stop) + "\t");
				gffb.append("0" + "\t"+ (complement?"-":"+") +"\t.\t" + type + ", " + repName + "\n");
			}
		}
    return new String(gffb);
	}
  
}
