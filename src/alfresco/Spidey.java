/*
* $Revision: 1.3 $
* $Id: Spidey.java,v 1.3 2003/04/07 12:27:48 niclas Exp $
 *
 * This file is part of Alfresco
 * Copyright (C) 2003  Niclas Jareborg
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
 * For more information contact Niclas at niclas.jareborg@astrazeneca.com
*/
package alfresco;
import java.io.*;
import java.util.*;

/**
 * Spidey uses and parses results from spidey 
 * @see alfresco.UsefulConstants
 * @version 1
 * @author Niclas Jareborg, AstraZeneca
 */

public class Spidey extends Thread{
	
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

  
  String option;
/**
 * Output result from spidey
 */
	Vector result;
  
  String gff;
  
  Properties properties;
  static String[] executables = { "spidey" };
  static String s_CB = "true";
	
	
/**
 * Creates a Spidey object, calls spidey, and stores the result.
 * @param ent Entry to add features to
 * @param path directory path to sequence file 
 * @param filename sequence file name
 */
 	Spidey(Entry ent, FastaFile eFile, String option, Properties props) {
		entry = ent;
		result = new Vector();
// 		gSeqFile = new FastaFile(ent.getEntryFile(), ent);
		Vector maskV = ent.getRepeats();
		gSeqFile = new FastaFile(SystemConstants.TMPDIR, ent.getFilename(), ent);
		gSeqFile.maskRanges(maskV);
		gSeqFile.write();
    eSeqFile = eFile;
    this.option = option;
    properties = props;
//     run();
	}

/**
 * Creates a Spidey object, calls est_genome, and stores the result.
 * @param file Sequence fasta file
 */
 	public Spidey(FastaFile gFile, FastaFile eFile, String option, Properties props) {
		result = new Vector();
    gSeqFile = gFile;
    eSeqFile = eFile;
    this.option = option;
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
    hash.put("s_CB", s_CB);
    return hash;
  }
  
	/**
   * Runs spidey
   */
  public void run() {
// 		String eg = SystemConstants.EST_GENOMEPATH + "est_genome -est " + eSeqFile.getPath() + " -genome " + gSeqFile.getPath();
// 		String eg = properties.getProperty("est_genome") + " -reverse -est " + eSeqFile.getPath() + " -genome " + gSeqFile.getPath();
		
    String sp = properties.getProperty("spidey") + " -m " + eSeqFile.getPath() + " -i " + gSeqFile.getPath() + " -p1";
    // consider using -s option for interspecies alignment...
    if (option == null) {
      if (properties.getProperty("s_CB").equals("true")) sp = sp + " -s";
    } else {
      sp = sp + " " + option;
    }
		Runtime rt = Runtime.getRuntime();
		try {
			System.out.println("calling " + sp);
			Process p = rt.exec(sp);
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
    String mRNAid;
    String size;
    while (lines.hasMoreElements()) {
    	String line = (String) lines.nextElement();
//       System.err.println(line);
      if (line.startsWith("mRNA:")) { // mRNA: lcl|BC000180 No definition line found, 1411 bp
//         StringTokenizer st = new StringTokenizer(line," No definition line found, ");
//         String rstr = st.nextToken(); // mRNA: lcl|BC000180
//         
//         System.err.println("String length: " + rstr.length() + " line: " + rstr);
//         mRNAid = rstr.substring(10); // BC000180
//         String sstr = st.nextToken(); // 1411 bp
//         int i = sstr.indexOf(" bp");
//         size = sstr.substring(0, i);

        
        
        StringTokenizer st = new StringTokenizer(line);
        st.nextToken(); // mRNA:
        String rstr = st.nextToken(); // lcl|BC000180
        mRNAid = rstr.substring(4); // BC000180
        st.nextToken(); // No
        st.nextToken(); // definition
        st.nextToken(); // line
        st.nextToken(); // found,
        size = st.nextToken(); // 1411
        
        continue;
      }
      if (line.startsWith("Strand:")) {
        if(line.indexOf("plus") == -1) {
          rev = true;
        }
//         int ind = line.indexOf("imply");
//         String sub = line.substring(ind);
//         if (sub.indexOf("forward") == -1) 
//           rev = true;
        continue;
      }
      if (!line.startsWith("Exon")) {
        continue;
      }
    	StringTokenizer st = new StringTokenizer(line); // Exon 1: 2334-2742 (gen)  29-458 (mRNA)  id 78.4%  gaps 33  splice site (d  a): 1  0
    	st.nextToken(); // Exon
    	st.nextToken(); // 1:
      String startstop1 = st.nextToken(); // 2334-2742
      st.nextToken(); // (gen)
      st.nextToken(); // 29-458
      st.nextToken(); // (mRNA)
      st.nextToken(); // id
    	String id = st.nextToken();// 78.4%
      if (line.indexOf("mismatches") != -1) { st.nextToken(); }
      st.nextToken(); // gaps
      st.nextToken(); // 33
      st.nextToken(); // splice
      st.nextToken(); // site
      st.nextToken(); // (d
      st.nextToken(); // a):
      st.nextToken(); // 1
      String phase = st.nextToken(); // 0
      
//     	String score = st.nextToken();
//     	System.err.println("To be parsed: " + startstop1);
      StringTokenizer startst = new StringTokenizer(startstop1, "-");
      String start1 = startst.nextToken();
//     	System.err.println("First token: " + start1);
    	String stop1 = startst.nextToken();
      
      id = id.substring(0, id.indexOf('%'));
//     	String n1 = st.nextToken();
//     	String start2 = st.nextToken();
//     	String stop2 = st.nextToken();
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
     	gffb.append(gSeqFile.getName() + "\tspidey\tExon\t" + start1 + "\t" + stop1 + "\t" + id);
    	gffb.append("\t" + (rev?"-":"+") + "\t" + phase + "\tTranscript structure predicted from " + eSeqFile.getName() + "\n");
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
