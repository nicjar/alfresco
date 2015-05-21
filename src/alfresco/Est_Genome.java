/*
* $Revision: 1.1 $
* $Id: Est_Genome.java,v 1.1 2003/04/04 10:14:18 niclas Exp $
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
 * Est_Genome uses and parses results from est_genome 
 * @see alfresco.UsefulConstants
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class Est_Genome extends Thread{
	
/**
 * Entry to add predicted features to	
 */
	Entry entry;
/**
 * Genomic Sequence fasta file	
 */  
  FastaFile gSeqFile;

/**
 * EST Sequence fasta file	
 */  
  FastaFile eSeqFile;

/**
 * Output result from est_genome
 */
	Vector result;
  
  String gff;
  
  Properties properties;
  static String[] executables = { "est_genome" };
	
	
/**
 * Creates a Est_Genome object, calls est_genome, and stores the result.
 * @param ent Entry to add features to
 * @param path directory path to sequence file 
 * @param filename sequence file name
 */
 	Est_Genome(Entry ent, FastaFile eFile, Properties props) {
		entry = ent;
		result = new Vector();
// 		gSeqFile = new FastaFile(ent.getEntryFile(), ent);
		Vector maskV = ent.getRepeats();
		gSeqFile = new FastaFile(SystemConstants.TMPDIR, ent.getFilename(), ent);
		gSeqFile.maskRanges(maskV);
		gSeqFile.write();
    eSeqFile = eFile;
    properties = props;
//     run();
	}

/**
 * Creates a Est_Genome object, calls est_genome, and stores the result.
 * @param file Sequence fasta file
 */
 	public Est_Genome(FastaFile gFile, FastaFile eFile, Properties props) {
		result = new Vector();
    gSeqFile = gFile;
    eSeqFile = eFile;
    properties = props;
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
   * Runs est_genome
   */
  public void run() {
// 		String eg = SystemConstants.EST_GENOMEPATH + "est_genome -est " + eSeqFile.getPath() + " -genome " + gSeqFile.getPath();
		String eg = properties.getProperty("est_genome") + " -reverse -est " + eSeqFile.getPath() + " -genome " + gSeqFile.getPath();
		Runtime rt = Runtime.getRuntime();
		try {
			System.out.println("calling " + eg);
			Process p = rt.exec(eg);
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
				result.addElement(outline);
// 				System.out.println("cpg: " + outline);
				outline = stdoutbr.readLine();
			}
		}catch (IOException ioe) { System.out.println(ioe); }
			catch (InterruptedException ie) { System.out.println(ie); }
		
    gff = parseToGff();
  }
	
/**
 * Parses the result and returns the result as a Gff string
 */
 	public String parseToGff() {
	
    StringBuffer gffb = new StringBuffer(new GffHeader().getHeader());
    
		Enumeration lines = result.elements();
    boolean rev = false;
    while (lines.hasMoreElements()) {
    	String line = (String) lines.nextElement();
      if (line.startsWith("Note")) {
        int ind = line.indexOf("imply");
        String sub = line.substring(ind);
        if (sub.indexOf("forward") == -1) 
          rev = true;
        continue;
      }
      if (!line.startsWith("Exon")) {
        continue;
      }
    	StringTokenizer st = new StringTokenizer(line);
    	String type = st.nextToken();
    	String score = st.nextToken();
    	String id = st.nextToken();
    	String start1 = st.nextToken();
    	String stop1 = st.nextToken();
    	String n1 = st.nextToken();
    	String start2 = st.nextToken();
    	String stop2 = st.nextToken();
//     	boolean rev1 = false;
//     	boolean rev2 = false;
//     	if (Integer.parseInt(start1) > Integer.parseInt(stop1)) {
//     		rev1 = true;
//     		String tmpval = start1;
//     		start1 = stop1;
//     		stop1 = tmpval;
//     	}
//     	if (Integer.parseInt(start2) > Integer.parseInt(stop2)) {
//     		rev2 = true;
//     		String tmpval = start2;
//     		start2 = stop2;
//     		stop2 = tmpval;
//     	}
     	gffb.append(gSeqFile.getName() + "\test_genome\tExon\t" + start1 + "\t" + stop1 + "\t" + score);
//     	gffb.append(name1 + "\t" + program + "\tSimilarity\t" + start1 + "\t" + stop1 + "\t" + score);
    	gffb.append("\t" + (rev?"-":"+") + "\t.\tGene structure predicted from " + eSeqFile.getName() + "\n");
//    	gffb.append(f2.getName() + "\t" + program + "\tSimilarity\t" + start2 + "\t" + stop2 + "\t" + score);
//     	gffb.append(name2 + "\t" + program + "\tSimilarity\t" + start2 + "\t" + stop2 + "\t" + score);
//     	gffb.append("\t" + (rev2?"-":"+") + "\t.\t" + "id = " + id + "\n");
//     	cgffb.append("Similarity\t" + start1 + "\t" + stop1 + "\t" + "Similarity\t" + start2 + "\t" + stop2 + "\n"); 
    }
    // end from BlastAlign_Impl.parseMSPcrunch()
    return gffb.toString();
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
