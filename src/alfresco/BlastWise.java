/*
* $Revision: 1.1 $
* $Id: BlastWise.java,v 1.1 2003/04/04 10:13:49 niclas Exp $
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
 * BlastWise uses and parses results from blastwise  
 * @see alfresco.UsefulConstants
 * @version 1
 * @author Niclas Jareborg, Center for Genomics Research
 */

public class BlastWise extends Thread{
	
/**
 * Entry to add found Exons islands to	
 */
	Entry entry;
/**
 * Sequence fasta file	
 */  
  File seqFile;

/**
 * Output result from blastwise
 */
	Vector result;
/**
 * Names of prot entry names taken from genewise stderr
 */
  Vector genenames;
/**
 * Result in gff format
 */
  String gff;
  
  Properties properties;
  
  static String[] executables = { "blastwise.pl" };
  static String db = "swtr";
	
/**
 * Creates a BlastWise object, calls blastwise, and stores the result.
 * @param ent Entry to add exons to
 */
 	public BlastWise (Entry ent, Properties props) {
		entry = ent;
		result = new Vector();
		genenames = new Vector();
// 		FastaFile tmpFile = new FastaFile(ent.getEntryFile(), ent);
// 		tmpFile.write();
//     seqFile = tmpFile;
		Vector maskV = entry.getRepeats();
		FastaFile maskedf = new FastaFile(SystemConstants.TMPDIR, entry.getFilename(), entry);
		maskedf.maskRanges(maskV);
		maskedf.write();
    seqFile = maskedf;
    properties = props;
//     run();
	}

/**
 * Creates a BlastWise object, calls blastwise, and stores the result.
 * @param file Sequence fasta file
 */
 	public BlastWise(File file, Properties props) {
		result = new Vector();
		genenames = new Vector();
		seqFile =  file;
    properties = props;
//     run();
	}
  
  /**
   * Returns the defaults for the Method in a Hashtable
   * @return Hashtable of defaults
   */
  public static Hashtable getDefaults() {
    Hashtable hash = new Hashtable(2);
    Vector exv = new Vector(executables.length);
    for (int i =0; i<executables.length; i++) {
      exv.addElement(executables[i]);
    }
    hash.put("executables",exv);
    hash.put("database", db);
    return hash;
  }
  
	/**
   * Runs blastwise
   */
  public void run() {
// 		String bw = "blastwise.pl swtr " + seqFile.getPath();
		String bw = properties.getProperty("blastwise.pl") + " " +  properties.getProperty("database") + " " + seqFile.getPath();
		Runtime rt = Runtime.getRuntime();
		try {
			System.out.println("calling " + bw);
			Process p = rt.exec(bw);
			InputStream stdout = p.getInputStream();
			InputStreamReader stdoutr = new InputStreamReader(stdout);
			BufferedReader stdoutbr = new BufferedReader(stdoutr);
      
			InputStream stderr = p.getErrorStream();
			InputStreamReader serrr = new InputStreamReader(stderr);
			BufferedReader errbr = new BufferedReader(serrr);
			p.waitFor();

			String outline = stdoutbr.readLine();
// 			while (outline != null) {
//         if(!outline.startsWith("subsequence")) {
//           outline = stdoutbr.readLine();
//           continue;
//         }
//         while (outline != null) {
// 				  result.addElement(outline);
//   // 				System.out.println("blastwise: " + outline);
// 				  outline = stdoutbr.readLine();
//         }
// 			}
			while (outline != null) {
        result.addElement(outline);
  // 				System.out.println("blastwise: " + outline);
			  outline = stdoutbr.readLine();
      }
			
      
// use STDERR to get info on which proteins used by genewise
// FIX this! Example:
//       Information
//               Using protein [TNFA_MOUSE] [1/1] in region 1735 6801
//       Information
//               Using DPEnvelope over matrix
//       GeneWise6 Matrix calculation: [1190000] Cells 99%
//       Information
//               Using protein [TNFB_MOUSE] [1/1] in region 927 6684
//       Information
//               Using DPEnvelope over matrix
//       GeneWise6 Matrix calculation: [1162000] Cells 99%
      
      String errline = errbr.readLine();
      while (errline != null) {
//        System.out.println("BlastWise STDERR: " + errline);
        if (errline.indexOf("Using protein") != -1) {
          int start = errline.indexOf("[") + 1;
          int stop = errline.indexOf("]");
          String protname = errline.substring(start, stop);
          genenames.addElement(protname);
        }
        errline = errbr.readLine();
      }
		}catch (IOException ioe) { System.out.println(ioe); }
			catch (InterruptedException ie) { System.out.println(ie); }
		
    if (result.size() == 0) {
      System.out.println("No result from blastwise");
      return;
//       System.exit (1);
    }
    // Should add a gff header before this, really
    StringBuffer sb = new StringBuffer();
		Enumeration lines = result.elements();
		while (lines.hasMoreElements()) {
      String line = (String) lines.nextElement();
      sb.append(line + "\n");
    }
    gff = new String(sb);
//     parse();
    
  }
/**
 * Parses the result and adds CpGIsland objects to Entry
 */
 	public void parse() {
    Vector genelines = new Vector();
		Enumeration lines = result.elements();
    String line = (String) lines.nextElement();
		while (lines.hasMoreElements()) {
			Vector tmp = new Vector();
      tmp.addElement(line);
      line = (String) lines.nextElement();
      while (lines.hasMoreElements() && !line.startsWith("subsequence")){
        tmp.addElement(line);
        line = (String) lines.nextElement();
      }
      genelines.addElement(tmp);
    }
	  Calendar cal = Calendar.getInstance();
	  StringBuffer csb = new StringBuffer();
	  csb.append(String.valueOf(cal.get(Calendar.YEAR)) + "-");
	  int month = cal.get(Calendar.MONTH) + 1;	// MONTH = 0-11
	  if (month < 10 ) csb.append("0");
	  csb.append(String.valueOf(month) + "-");
	  if (cal.get(Calendar.DAY_OF_MONTH) < 10 ) csb.append("0");
	  csb.append(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
	  String datestring = new String(csb);
    gff = "##gff-version\t1\n##date\t" + datestring + "\n";
    
    Enumeration gen = genelines.elements();
    while (gen.hasMoreElements()) {
      Vector glines = (Vector) gen.nextElement();
      toGff(glines);
    } 
      
// 			StringTokenizer st = new StringTokenizer(line);      
// 			st.nextElement();	// subsequence
// 			String qsubseq = st.nextElement();	// seqname.#
// 			int genestart = Integer.parseInt((String) st.nextElement());
// 			int genestop = Integer.parseInt((String) st.nextElement());
//       
//       
// // 			String info = (String) st.nextElement();
// 			StringBuffer sb = new StringBuffer();
// 			while(st.hasMoreElements()) {	// take the rest of the line
// 				String s = (String) st.nextElement();
// 				sb.append(s + " ");
// 			}
// 			String info = new String(sb);
// 			CpGIsland island = new CpGIsland(entry, start, stop);
// 			island.setInfo(info);
// 			island.setPredMethod("cpg");
// 			entry.addChild(island);
		
	}
	/**
   * parses the lines for each protein hit and adds them to the objects gff string
   * @param genelines vector of string for each protein hit
   */
  private void toGff(Vector genelines) {
	
    StringBuffer gffb = new StringBuffer();
    Enumeration lines = genelines.elements();
    while(lines.hasMoreElements()) {
      String line = (String) lines.nextElement();
      StringTokenizer st = new StringTokenizer(line);
      // e.g. subsequence MMTNFAB.1 4527 6214
      st.nextToken(); 
      String gene = st.nextToken();
      int dotind = gene.indexOf(".");
      int genenum = Integer.parseInt(gene.substring(dotind + 1));
      gene = (String) genenames.elementAt(genenum - 1);
      int offset = Integer.parseInt(st.nextToken()) - 1;
      while(lines.hasMoreElements() && !line.startsWith("CDS_predicted")) {
        line = (String) lines.nextElement();
      }
      st = new StringTokenizer(line);
      st.nextToken(); //CDS_predicted_by
      st.nextToken(); //genewise
      String genescore = st.nextToken();
      while(lines.hasMoreElements() ) {
        line = (String) lines.nextElement();
        if (line.startsWith("source_Exons")) { 
          st = new StringTokenizer(line);
          st.nextToken(); //source_Exons
          int start = Integer.parseInt(st.nextToken()) + offset;
          int stop = Integer.parseInt(st.nextToken()) + offset;
          gffb.append(seqFile.getName() + "\tgenewise\texon\t" + Integer.toString(start) + "\t" + Integer.toString(stop) + "\t");
          gffb.append(genescore + "\t+\t.\t" + gene + "\n");
        }
      }
      gff = gff + new String(gffb);
    }
  }
  
  /**
   * Returns result gff String
   * @return gff String
   */
  public String getGff() {
    return gff;
  }

  /**
   * Description
   * @return descr
   * @param param descr
   */
  public void sidestepRun(File bwout) {
    FileReader fr = null;
    try {
      fr = new FileReader(bwout);
    } catch (Exception e) { e.printStackTrace(); }
		BufferedReader br = new BufferedReader(fr);
		String inline = null;
		try {
		  while ((inline = br.readLine()) != null) {
				result.addElement(inline);
		  }
		} catch(IOException ioe) { ioe.printStackTrace();}
    
  }	
// 	public static void main(String[] arg) {
// 		String fname = "A31613";
// 		File f = new File(fname);
// // 		Entry ent = null;
// // 		try {
// // 			ent = new Entry(f);
// // 		} catch (FileNotFoundException fnf) { System.out.println(fnf); }
// 		BlastWise bw = new BlastWise(f);
//     bw.start();
//     try {
//       bw.join();
//     } catch (Exception e) { e.printStackTrace(); }
// 		System.out.println(bw.getGff());
// 	}
	
}
