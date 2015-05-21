/*
* $Revision: 1.1 $
* $Id: BlGenewise.java,v 1.1 2003/04/04 10:13:47 niclas Exp $
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
 * BlGenewise uses and parses results from genewise 
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class BlGenewise extends Thread{
	
/**
 * Entry to add predicted features to	
 */
	Entry entry;
/**
 * Genomic Sequence fasta file	
 */  
  FastaFile gSeqFile;

// /**
//  * EST Sequence fasta file	
//  */  
//   FastaFile eSeqFile;

// /**
//  * Output result from est_genome
//  */
// 	Vector result;
  
  String gff;
  Properties properties;
  static String[] executables = { "blGenewise" };
  static String d = "sptr";
  static String h = "2";
  static String e = "1e-5";
	
/**
 * Creates a BlGenewise object, calls blGenewise, and stores the result.
 * @param ent Entry to add features to
 * @param path directory path to sequence file 
 * @param filename sequence file name
 */
 	public BlGenewise(Entry ent, Properties props) {
		entry = ent;
// 		result = new Vector();
		Vector maskV = ent.getRepeats();
		gSeqFile = new FastaFile(SystemConstants.TMPDIR, ent.getFilename(), ent);
		gSeqFile.maskRanges(maskV);
		gSeqFile.write();
// 		gSeqFile.write();
    properties = props;
//     eSeqFile = eFile;
//     run();
	}

/**
 * Creates a BlGenewise object, calls blGenewise, and stores the result.
 * @param file Sequence fasta file
 */
 	public BlGenewise(FastaFile gFile, Properties props) {
// 		result = new Vector();
    gSeqFile = gFile;
//     eSeqFile = eFile;
    properties = props;
//     run();
	}

  /**
   * Returns the defaults for the Method in a Hashtable
   * @return Hashtable of defaults
   */
  public static Hashtable getDefaults() {
    Hashtable hash = new Hashtable(4);
    Vector exv = new Vector(executables.length);
    for (int i =0; i<executables.length; i++) {
      exv.addElement(executables[i]);
    }
    hash.put("executables",exv);
    hash.put("d",d);
    hash.put("h",h);
    hash.put("e",e);
    return hash;
  }
  
	/**
   * Runs blGenewise
   */
  public void run() {
// 		String eg = SystemConstants.EST_GENOMEPATH + "est_genome -reverse -est " + eSeqFile.getPath() + " -genome " + gSeqFile.getPath();
		String blgw = properties.getProperty("blGenewise") 
        + " -d " + properties.getProperty("d") 
        + " -e " + properties.getProperty("e") 
        + " -h " + properties.getProperty("h") + " " + gSeqFile.getPath();
		Runtime rt = Runtime.getRuntime();
    StringBuffer sb = new StringBuffer();
		try {
			System.out.println("calling " + blgw);
			Process p = rt.exec(blgw);
			InputStream stdout = p.getInputStream();
			InputStreamReader stdoutr = new InputStreamReader(stdout);
			BufferedReader stdoutbr = new BufferedReader(stdoutr);
// 			InputStream stderr = p.getInputStream();
// 			InputStreamReader serrr = new InputStreamReader(stderr);
// 			BufferedReader errbr = new BufferedReader(serrr);
			p.waitFor();
// 			String errline = errbr.readLine();
// 			System.out.println(errline);
			String outline = stdoutbr.readLine();
			while (outline != null) {
// 				result.addElement(outline);
        sb.append(outline + "\n");
// 				System.out.println("cpg: " + outline);
				outline = stdoutbr.readLine();
			}
		}catch (IOException ioe) { System.out.println(ioe); }
			catch (InterruptedException ie) { System.out.println(ie); }
		
//     gff = parseToGff();
    gff = new String(sb);
//     System.out.println("blEst_genome gff:\n"+gff);
  }
  /**
   * Returns result gff String
   * @return gff String
   */
  public String getGff() {
    return gff;
  }
	
// 	public static void main(String[] arg) {
// 		String fname = "MMU58105";
// 		File f = new File(fname);
// 		Entry ent = null;
// 		try {
// 			ent = new Entry(f);
// 		} catch (FileNotFoundException fnf) { System.out.println(fnf); }
// 		CpG c = new CpG(ent);
// 		System.out.println(c.result);
// 	}
	
}
