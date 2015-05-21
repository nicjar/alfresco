/*
* $Revision: 1.1 $
* $Id: BlastAlign.java,v 1.1 2003/04/04 10:13:48 niclas Exp $
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
// import org.omg.CORBA.*;
// import alfresco.*;
import alfresco.corba_wrappers.*;



/**
 * BlastAlign uses blastn and MSPcrunch to find conserved blocks in
 * two sequences
 * @version 1
 * @author Niclas Jareborg, Center for Genomics Research
 */

public class BlastAlign extends Thread{
// 	String seq1;
// 	String seq2;
	String name1;
	String name2;
// 	File dir;
	FastaFile f1;
	FastaFile f2;
// 	String program;
	GffDataStruct result;
// 	String pseudorand; 
  EntryPair entries;
  String gff;
  Properties properties;
  static String[] executables = { "blastall", "formatdb", "MSPcrunch"};
	
	/**
	 * Creates new BlastAlign
	 */
	public BlastAlign(EntryPair entries, Properties props) {
    this.entries = entries;
    Entry ent1 =entries.getEntry1();
    Entry ent2 =entries.getEntry2();
		Vector mask1V = ent1.getRepeats();
		Vector mask2V = ent2.getRepeats();
    f1 = new FastaFile(SystemConstants.TMPDIR, ent1.getFilename(), ent1); 
    f2 = new FastaFile(SystemConstants.TMPDIR, ent2.getFilename(), ent2); 
		f1.maskRanges(mask1V);
		f2.maskRanges(mask2V);
		f1.write();
		f2.write();
    properties = props;
	}
  
	/**
	 * Creates new BlastAlign
	 */
	public BlastAlign(FastaFile seq1file, FastaFile seq2file, Properties props) {
    f1 = seq1file;
    f2 = seq2file;
    properties = props;
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
    return hash;
  }
// /**
//  * Sets temporary directory for writing files
//  * @param td temporary directory file object
//  */
//   public void setTmpDir(File td) {
//     dir = td;
//   }
// /**
//  * Sets property object for method
//  * @param prop Properties object
//  */
//   public void setProperties(Properties prop) {
//     
//   }
//   
// /**
//  * Takes the input necessary for running the method
//  * @param input Input struct
//  */
//   public void input(InputStruct input) {
//     this.seq1 = input.seqs[0];
//     this.seq2 = input.seqs[1];
//     this.name1 = input.names[0];
//     this.name2 = input.names[1];
//     this.program = input.parameters[0];
//     if (!program.equals("blastn") || !program.equals("tblastx")) {
//       program = "blastn";
//     }
//   }

/**
 * Runs the analysis. 
 */
  public void run() {
    // Do the dirty stuff !!
//     if (dir == null) {
//       System.out.println("alfresco.corba_wrappers.blastalign.BlastAlign_Impl: tmpDir not set!");
//     }
//     save seqs to file
// 		pseudorand = new Long(System.currentTimeMillis()%1000000).toString();
// 		String tmpfn1 = "tmp1_" + pseudorand;
// 		String tmpfn2 = "tmp2_" + pseudorand;
//     f1 = new FastaFile(dir.getPath(), tmpfn1, ">tmp_file_1", seq1);
// 		f2 = new FastaFile(dir.getPath(), tmpfn2, ">tmp_file_2", seq2);
// 		f1.write();
// 		f2.write();
// 		System.out.println("BlastAlignProducer: files saved");
//     formatdb on seq1 
    name1 = f1.getName();
    name2 = f2.getName();
		Runtime rt = Runtime.getRuntime();
		try {
			String formatdb = properties.getProperty("formatdb")
          + " -p F -o T -t tmp -i " + f1.getPath();
			System.out.println("BlastAlign: running formatdb");
			Process p = rt.exec(formatdb);
			p.waitFor();
		}catch (IOException ioe) { System.out.println(ioe); }
			catch (InterruptedException ie) { System.out.println(ie); }

//     blastall -p blastn -i seq2 -d seq1 > resultfile
        
    File blastoutput = blastall(f1.getPath(), f2.getPath());
//     if (!blastoutput.exists()) {
//       throw new NoOutputException ("No blast output");
//     }
        
        
//     MSPcrunch -w -d resultfile
//     parse result to gff and cgff
//     set values and return a BlastAlignStruct
    	
    try {
    	result = runMSPcrunch(blastoutput);
		} catch (NoOutputException e) { 
//       throw e; 
      System.out.println(e.reason);
    }
		
// 		String [] files = dir.list(new FilenameFilter() {
// 			public boolean accept(File dir, String fn) {
// 				if (fn.indexOf(pseudorand) != -1) {
// 					return true;
// 				}
// 				return false;
// 			}
// 		});
// 	// 		System.out.println(files.length);
// 		if (files != null) {
// 			System.out.println("Files deleted:");
// 			for(int i = 0; i < files.length; i++) {
// 				File tmp = new File(dir, files[i]);
// 				tmp.delete();
// 				System.out.println(files[i]);	
// 			}
// 		}
    
  }
  
/**
 * Gets the resultant data
 * @return gff data structure
 */
  public GffDataStruct getGffData() throws alfresco.corba_wrappers.NoOutputException{
    if (result == null) { 
      throw new alfresco.corba_wrappers.NoOutputException("CpG produced no output"); 
    }
    return result;
  }

// /**
//  * Gets the gff data
//  * @return gff data
//  */
//   public String getGff(){
// //     if (result == null) { 
// //       throw new alfresco.corba_wrappers.NoOutputException("CpG produced no output"); 
// //     }
//     return result.gff;
//   }
// /**
//  * Gets the cgff data
//  * @return cgff data
//  */
//   public String getCgff(){
// //     if (result == null) { 
// //       throw new alfresco.corba_wrappers.NoOutputException("CpG produced no output"); 
// //     }
//     return result.cgff;
//   }

  /**
   * Runs blastall 
   * @return name of the result file
   * @param database name of seq1 database
   * @param query name of seq2 file
   */
  private File blastall(String database, String query) {
    File resFile = new File(query + ".blastn");
    Runtime rt = Runtime.getRuntime();
    try {
      String blast = properties.getProperty("blastall")
          + " -p blastn -i " 
          + query + " -d " 
          + database + " -o " 
          + resFile.getPath();
      System.out.println("blastall: calling " + blast);
      Process p = rt.exec(blast);
//		InputStream err = p.getInputStream();
//		InputStreamReader errR = new InputStreamReader(err);
//		BufferedReader errbr = new BufferedReader(errR);
//	  String errline = errbr.readLine();
//     while (errline != null) {
//      	System.out.println(errline);
//      	errline = errbr.readLine();
//      }
     p.waitFor();  
//      System.out.println("blast exited with a value of " + p.exitValue());
    }catch (IOException ioe) { System.out.println(ioe); }
			catch (InterruptedException ie) { System.out.println(ie); }
    return resFile;
  }
  
  /**
   * Runs 'MSPcrunch -w -d' on a Blast output file
   * @return a result Reader
   * @param blastresult blast result file
   */
  public GffDataStruct runMSPcrunch(File blastresult) throws NoOutputException{
  	if (!blastresult.exists()) {
  		System.out.println("runMSPcrunch: "+blastresult.getPath() + " doesn't exist!");
  	}
	  Runtime rt = Runtime.getRuntime();
	  GffDataStruct resStruct = null;
	  try {
//		String mspc = "MSPcrunch -w -d " + blastresult.getPath();
		  String mspc = properties.getProperty("MSPcrunch") + " -d " + blastresult.getPath();
		  System.out.println("Calling " + mspc);
		  Process p = rt.exec(mspc);
		  InputStream stdout = p.getInputStream();
		  InputStreamReader stdoutr = new InputStreamReader(stdout);
		  BufferedReader stdoutbr = new BufferedReader(stdoutr);
		  p.waitFor();
		  resStruct = parseMSPcrunch(stdoutbr);
	  }catch (IOException ioe) { System.out.println(ioe); }
		catch (InterruptedException ie) { System.out.println(ie); }
		catch (NoOutputException noe) {throw noe;}
    return resStruct;
  }
  
  /**
   * Parses MSPcrunch output to a BlastAlignStruct
   * @return blast align struct
   * @param param descr
   */
  private GffDataStruct parseMSPcrunch(BufferedReader mspr) throws NoOutputException{
    Vector tmp = new Vector();
    try {
		  String outline = mspr.readLine();
		  if (outline == null) {
			  System.out.println("MSPcrunch didn't find any matches");
			  throw new NoOutputException("No MSPcrunch output");
		  }
	    while (outline != null) {
// 	    	System.out.println("MSPcrunch output: " + outline);
			  tmp.addElement(outline);
			  outline = mspr.readLine();
		  }
	  } catch (IOException ioe) { System.out.println(ioe); }
    // do the parsing to a gff string and a cgff string
    
	  Calendar cal = Calendar.getInstance();
	  StringBuffer sb = new StringBuffer();
	  sb.append(String.valueOf(cal.get(Calendar.YEAR)) + "-");
	  int month = cal.get(Calendar.MONTH) + 1;	// MONTH = 0-11
	  if (month < 10 ) sb.append("0");
	  sb.append(String.valueOf(month) + "-");
	  if (cal.get(Calendar.DAY_OF_MONTH) < 10 ) sb.append("0");
	  sb.append(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
	  String datestring = new String(sb);
	
    StringBuffer gffb = new StringBuffer("##gff-version\t1\n##date\t" + datestring + "\n");
    StringBuffer cgffb = new StringBuffer("##cgff-version\t1\n##date\t" + datestring + "\n");
//	  cgffb.append("##gff\t" + f1.getName() + "-" + f2.getName() + ".gff" + "\n");
//	  cgffb.append("##seq\t" + f1.getName() + "\t" + f2.getName() + "\n");
	  cgffb.append("##gff\t" + name1 + "-" + name2 + ".gff" + "\n");
	  cgffb.append("##seq\t" + name1 + "\t" + name2 + "\n");

    Enumeration lines = tmp.elements();
    while (lines.hasMoreElements()) {
    	String line = (String) lines.nextElement();
    	StringTokenizer st = new StringTokenizer(line);
    	String score = st.nextToken();
    	String id = st.nextToken();
    	String start2 = st.nextToken();
    	String stop2 = st.nextToken();
    	String n2 = st.nextToken();
    	String start1 = st.nextToken();
    	String stop1 = st.nextToken();
    	boolean rev1 = false;
    	boolean rev2 = false;
    	if (Integer.parseInt(start1) > Integer.parseInt(stop1)) {
    		rev1 = true;
    		String tmpval = start1;
    		start1 = stop1;
    		stop1 = tmpval;
    	}
    	if (Integer.parseInt(start2) > Integer.parseInt(stop2)) {
    		rev2 = true;
    		String tmpval = start2;
    		start2 = stop2;
    		stop2 = tmpval;
    	}
//    	gffb.append(f1.getName() + "\t" + program + "\tSimilarity\t" + start1 + "\t" + stop1 + "\t" + score);
    	gffb.append(name1 + "\tblastn\tSimilarity\t" + start1 + "\t" + stop1 + "\t" + score);
    	gffb.append("\t" + (rev1?"-":"+") + "\t.\t" + "id = " + id + "\n");
//    	gffb.append(f2.getName() + "\t" + program + "\tSimilarity\t" + start2 + "\t" + stop2 + "\t" + score);
    	gffb.append(name2 + "\tblastn\tSimilarity\t" + start2 + "\t" + stop2 + "\t" + score);
    	gffb.append("\t" + (rev2?"-":"+") + "\t.\t" + "id = " + id + "\n");
    	cgffb.append("Similarity\t" + start1 + "\t" + stop1 + "\t" + "Similarity\t" + start2 + "\t" + stop2 + "\n"); 
    }
    String [] supl = { "" };
    return new GffDataStruct(gffb.toString(), cgffb.toString(), supl);
  }
  
// 	public static void main(String [] args) {
// 		if (args.length == 0) usage();
// 		File dir = new File(args[0]);
// 		if (!dir.isDirectory()) usage();
// 		ORB orb = ORB.init(args, new java.util.Properties());
// 		BOA boa = orb.BOA_init(args, new java.util.Properties());
// 		BlastAlign_Impl ba = new BlastAlign_Impl(dir);
// 		
// 		try {
// 			String ref = orb.object_to_string(ba);
// 			String refFile = "/dna2/nic/www/blastalign.ior";
// 			FileWriter fw = new FileWriter(refFile);
// 			fw.write(ref);
// 			fw.flush();
// 			fw.close();
// 		} catch (IOException ioe) { ioe.printStackTrace(); }
// 		
// 		boa.impl_is_ready(null);
// 	
// 	}
// 	
// 	/**
// 	 * Prints a usage statement and exits
// 	 */
// 	private static void usage() {
// 		System.out.println("usage: java alfresco.corba_wrappers.blastalign.BlastAlign_Impl <tmp dir>");
// 		System.exit(1);
// 	}
// 
// 	/**
// 	 * Runs blastn on the specified sequences
// 	 * @return blast align struct
// 	 * @param seq1 sequence 1 in fasta format
// 	 * @param seq2 sequence 2 in fasta format
// 	 */
// 	public BlastAlignStruct alignN(String name1, String name2, String seq1, String seq2) 
// 															throws NoOutputException{
// 		
// 		BlastAlignProducer bp = null;
// 		try {
// 			bp = new BlastAlignProducer(writedir, name1, name2, seq1, seq2, "blastn");
// 		} catch (NoOutputException e) { throw e; }
// 		//bp.start();
// 		return bp.getResult();
// 	}
// 
// 	/**
// 	 * Runs tblastx on the specified sequences
// 	 * @return blast align struct
// 	 * @param seq1 sequence 1 in fasta format
// 	 * @param seq2 sequence 2 in fasta format
// 	 */
// 	public BlastAlignStruct tAlignX(String name1, String name2, String seq1, String seq2) 
// 															throws NoOutputException{
//  		BlastAlignProducer bp = null;
//  		try {
//  			bp = new BlastAlignProducer(writedir, name1, name2, seq1, seq2, "tblastx");
//  		} catch (NoOutputException e) { throw e; }
// 		//bp.start();
// 		return bp.getResult();
//    
// 	}
  

}
