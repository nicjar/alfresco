/*
* $Revision: 1.1 $
* $Id: RepeatMasker.java,v 1.1 2003/04/04 10:14:55 niclas Exp $
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
 * For more information contact Niclas at niclas.jareborg@cgr.ki.se
*/
package alfresco;
import java.io.*;
import java.util.*;

/**
 * RepeatMasker uses and parses results from RepeatMasker
 * @see alfresco.UsefulConstants
 * @version 1
 * @author Niclas Jareborg, Center for Genomics Research
 */

public class RepeatMasker extends Thread{
	
/**
 * Entry 	
 */
	Entry entry;
/**
 * Sequence fasta file	
 */  
  File seqFile;
  
  String option;
/**
 * Output result from blastwise
 */
	Vector result;
/**
 * Result in gff format
 */
  String gff;
	
  Properties prop;
  static String[] executables = { "RepeatMasker" };
  

/**
 * Creates a RepeatMasker object
 * @param ent Entry to add exons to
 */
 	public RepeatMasker (Entry ent, String option, Properties prop) {
		entry = ent;
		result = new Vector();
    seqFile = ent.getEntryFile();
    this.option = option;
    this.prop = prop;
//     run();
	}

/**
 * Creates a RepeatMasker object
 * @param file Sequence fasta file
 */
 	public RepeatMasker(File file, String option, Properties prop) {
		result = new Vector();
		seqFile =  file;
    this.option = option;
    this.prop = prop;
//     run();
	}
  /**
   * Returns the defaults for the Method in a Hashtable
   * @return Hashtable of defaults
   */
  public static Hashtable getDefaults() {
    Hashtable hash = new Hashtable(1);
    Vector exv = new Vector(executables.length);
    for (int i =0; i<executables.length; i++) {
      exv.addElement(executables[i]);
    }
    hash.put("executables",exv);
    return hash;
  }
  
	/**
   * Runs RepeatMasker
   */
  public void run() {
		Runtime rt = Runtime.getRuntime();
		Process rmp = null;
// 		String cmnd = SystemConstants.REPEATMASKERPATH + "RepeatMasker " + option + " " + seqFile.getPath();
		String cmnd = prop.getProperty("RepeatMasker") + " " + option + " " + seqFile.getPath();
// 		System.out.println("Calling " + cmnd);
		try {
			rmp = rt.exec(cmnd);
			rmp. waitFor();
// 		String rmfn = entryFile.getPath() + ".RepMask";
      String rmfn = seqFile.getPath() + ".out";
//		System.out.println(rmfn);
// 		String path = System.getProperty("user.dir");
// 		RepeatMaskerFile rmFile = new RepeatMaskerFile(USRDIR, rmfn, this);
		  RepeatMaskerFile rmFile = new RepeatMaskerFile(rmfn, seqFile.getName());
		  rmFile.open();
// 		rmFile.parse();
      gff = rmFile.parseToGff();
		}catch (InterruptedException ie) { System.out.println("RepeatMasker interupted:\n" + ie); }
			catch (IOException ioe) { System.out.println(ioe); }
      System.out.println(gff);
  }
  
  /**
   * Returns result gff String
   * @return gff String
   */
  public String getGff() {
    return gff;
  }

	
}
