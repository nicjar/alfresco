/*
* $Revision: 1.1 $
* $Id: CpG.java,v 1.1 2003/04/04 10:14:00 niclas Exp $
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
 * CpG uses and parses results from cpg and cpg2ace 
 * @see alfresco.UsefulConstants
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class CpG extends Thread{
	
/**
 * Entry to add found CpG islands to	
 */
	Entry entry;
/**
 * Sequence fasta file	
 */  
  FastaFile seqFile;

  Properties properties;
/**
 * Output result from cpg | cpg2ace
 */
	Vector result;
  
  String gff;
	
  static String[] executables = { "cpg", "cpg2ace" };
//   static String minlength = "200";
//   static String gcContent = "50";
//   static String obs_to_exp = "0.6";
  static String l = "200";
  static String g = "50";
  static String o = "0.6";
  
/**
 * Creates a CpG object, calls cpg and cpg2ace, and stores the result.
 * @param ent Entry to add CpG islands to
 * @param path directory path to sequence file 
 * @param filename sequence file name
 */
 	CpG(Entry ent, Properties props) {
		entry = ent;
		result = new Vector();
		seqFile = new FastaFile(ent.getEntryFile(), ent);
    properties = props;
		seqFile.write();
//     run();
	}

/**
 * Creates a CpG object, calls cpg and cpg2ace, and stores the result.
 * @param file Sequence fasta file
 */
 	public CpG(FastaFile file, Properties props) {
		result = new Vector();
    properties = props;
		seqFile = file;
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
    hash.put("l",l);
    hash.put("g",g);
    hash.put("o",o);
    return hash;
  }
	/**
   * Runs cpg and cpg2ace
   */
  public void run() {
		Vector tmp = new Vector();
// 		String cpg = "cpg " + path + File.separator + filename;
// 		String cpg = SystemConstants.CPGPATH + "cpg " + entry.getEntryFile().getPath();
// 		String cpg = SystemConstants.CPGPATH + "cpg " + seqFile.getPath();
		String cpg = properties.getProperty("cpg") + " " + seqFile.getPath();
// 		String cpg2ace = SystemConstants.CPGPATH + "cpg2ace -l 200 -g 50 -o 0.6";
		String cpg2ace = properties.get("cpg2ace") + " -l " + properties.get("l") + " -g " + properties.get("g") + " -o " + properties.get("o") ;
		Runtime rt = Runtime.getRuntime();
		try {
			System.out.println("calling " + cpg);
			Process p = rt.exec(cpg);
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
				tmp.addElement(outline);
// 				System.out.println("cpg: " + outline);
				outline = stdoutbr.readLine();
			}
		}catch (IOException ioe) { System.out.println(ioe); }
			catch (InterruptedException ie) { System.out.println(ie); }
		
		Enumeration tmpe = tmp.elements();
		
		try {
			System.out.println("calling " + cpg2ace);
			Process p = rt.exec(cpg2ace);
			OutputStream stdin = p.getOutputStream();
			OutputStreamWriter stdinw = new OutputStreamWriter(stdin);
			BufferedWriter stdinbw = new BufferedWriter(stdinw);
			while(tmpe.hasMoreElements()){
				String line = (String) tmpe.nextElement();
				stdinbw.write(line + "\n");
			}
			stdinbw.flush();
			stdinbw.close();
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
				outline = stdoutbr.readLine();
			}
		}catch (IOException ioe) { System.out.println(ioe); }
			catch (InterruptedException ie) { System.out.println(ie); }
    
  }
/**
 * Parses the result and adds CpGIsland objects to Entry
 */
 	public void parse() {
		Enumeration lines = result.elements();
		while (lines.hasMoreElements()) {
			String line = (String) lines.nextElement();
			StringTokenizer st = new StringTokenizer(line);
			st.nextElement();	// Feature
			st.nextElement();	// Predicted_CpG_island
			int start = Integer.parseInt((String) st.nextElement());
			int stop = Integer.parseInt((String) st.nextElement());
			st.nextElement();	// score
// 			String info = (String) st.nextElement();
			StringBuffer sb = new StringBuffer();
			while(st.hasMoreElements()) {	// take the rest of the line
				String s = (String) st.nextElement();
				sb.append(s + " ");
			}
			String info = new String(sb);
			CpGIsland island = new CpGIsland(entry, start, stop);
			island.setInfo(info);
			island.setPredMethod("cpg");
			entry.addChild(island);
		}
	}
	
/**
 * Parses the result and returns the result as a Gff string
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
	
    StringBuffer gffb = new StringBuffer("##gff-version\t1\n##date\t" + datestring + "\n");
    
		Enumeration lines = result.elements();
		while (lines.hasMoreElements()) {
			String line = (String) lines.nextElement();
			StringTokenizer st = new StringTokenizer(line);
			st.nextElement();	// Feature
			st.nextElement();	// Predicted_CpG_island
// 			int start = Integer.parseInt((String) st.nextElement());
// 			int stop = Integer.parseInt((String) st.nextElement());
			String start = (String) st.nextElement();
			String stop = (String) st.nextElement();
			st.nextElement();	// score
// 			String info = (String) st.nextElement();
			StringBuffer sb = new StringBuffer();
			while(st.hasMoreElements()) {	// take the rest of the line
				String s = (String) st.nextElement();
				sb.append(s + " ");
			}
			String info = new String(sb);
      gffb.append(seqFile.getName() + "\tcpg\tCpGIsland\t" + start + "\t" + stop + "\t0\t+\t.\t" + info + "\n");
// 			CpGIsland island = new CpGIsland(entry, start, stop);
// 			island.setInfo(info);
// 			island.setPredMethod("cpg");
// 			entry.addChild(island);
      
		}
    return gffb.toString();
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
