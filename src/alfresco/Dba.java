/*
 * $Revision: 1.1 $
 * $Id: Dba.java,v 1.1 2003/04/04 10:14:03 niclas Exp $
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
import alfresco.corba_wrappers.*;
/**
 * Class representing a dba process and results.
 * Dba Dna Block Alignment - Ewan Birney and Richard Durbin
 * @see Serializable
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class Dba extends Thread implements Serializable {

  static final long serialVersionUID = -2150862602037552788L;	
/**
 * Range 1
 */
	SeqRange range1;	
/**
 * Range2
 */
	SeqRange range2;
  
  int offset1;	// dba pos values run from 0 - N, so no need to subtract 1 to get offset
	
  int offset2;	// dba pos values run from 0 - N, so no need to subtract 1 to get offset

/**
 * Source Entry for range 1
 */
	Entry entry1;
/**
 * Source Entry for range 2
 */
	Entry entry2;
/**
 * Fasta file with sequence 1
 */
  FastaFile ff1;
/**
 * Fasta file with sequence 2
 */
  FastaFile ff2;
/**
 * Vector that stores the dba output
 */
	Vector result = new Vector();
/**
 * dba command string
 */
	String dbac = "";
/**
 * dba command prefix
 */
	String dbapc; // = "dba -match 2 -mismatch -3 -gap -6 -blockopen -24 -align -params ";
/**
 * Vector of AlignmentBlocks
 */
	Vector blocks;
/**
 * Over all score of dba alignment	
 */
	double overallScore;
/**
 * Match prob	for level A
 */
	double matchA;
/**
 * Match prob	for level B
 */
	double matchB;
/**
 * Match prob	for level C
 */
	double matchC;
/**
 * Match prob	for level D
 */
	double matchD;
/**
 * umatch prob	
 */
	double umatch;
/**
 * Gap prob	
 */	
	double gap;
/**
 * Blockopen prob	
 */	
	double blockopen;
  String gff;
  String cgff;
  String [] pffBlocks;
  Properties properties;
  
  static String[] executables = { "dba" }; 
  static String match_A = "0.65";
  static String match_B = "0.75";
  static String match_C = "0.85";
  static String match_D = "0.95";
  static String Umatch = "0.99";
  static String Gap = "0.05";
  static String Blockopen = "0.01";
  
	
	/**
	 * Creates a Dba object with the specified subsequences
	 * @param ent1 first Entry
	 * @param ent2 second Entry
	 * @param r1 first range
	 * @param r2 second range
	 */
	public Dba(Entry ent1, Entry ent2, SeqRange r1, SeqRange r2, Properties props) {
		entry1 = ent1;
		entry2 = ent2;
		range1 = r1;
		range2 = r2;
		ff1 = new FastaFile(SystemConstants.TMPDIR, ent1.getSubSeqFileName(r1), ent1, r1);
		ff2 = new FastaFile(SystemConstants.TMPDIR, ent2.getSubSeqFileName(r2), ent2, r2);
		ff1.write();
		ff2.write();
    offset1 = range1.getStart() - 1;	// dba pos values run from 0 - N, so no need to subtract 1 to get offset
		offset2 = range2.getStart() - 1;	// dba pos values run from 0 - N, so no need to subtract 1 to get offset

    properties = props;
		setParameters();
// 		dbac = dbapc + "-nomatchn "  + ff1.getPath() + " " + ff2.getPath();
// 		callDba();
	}
	
	/**
	 * Creates a Dba object with the specified subsequences with the specified ranges masked
	 * @param ent1 first Entry
	 * @param ent2 second Entry
	 * @param r1 first range
	 * @param r2 second range
	 * @param mask1 vector of ranges to mask in sequence 1
	 * @param mask2 vector of ranges to mask in sequence 2
	 */
	public Dba(Entry ent1, Entry ent2, SeqRange r1, SeqRange r2, Vector mask1, Vector mask2, Properties props) {
		entry1 = ent1;
		entry2 = ent2;
		range1 = r1;
		range2 = r2;
		ff1 = new FastaFile(SystemConstants.TMPDIR, ent1.getSubSeqFileName(r1), ent1, r1);
		ff2 = new FastaFile(SystemConstants.TMPDIR, ent2.getSubSeqFileName(r2), ent2, r2);
		ff1.maskRanges(mask1);
		ff2.maskRanges(mask2);
		ff1.write();
		ff2.write();
    offset1 = range1.getStart() - 1;	// dba pos values run from 0 - N, so no need to subtract 1 to get offset
		offset2 = range2.getStart() - 1;	// dba pos values run from 0 - N, so no need to subtract 1 to get offset
    properties = props;
		setParameters();
// 		dbac = dbapc + "-nomatchn " + ff1.getPath() + " " + ff2.getPath();
// 		callDba();
	}
	
	/**
	 * Creates a Dba object with the subsequences specified by features
	 * @param feature1 first feature
	 * @param feature2 second feature
	 */
	public Dba(SeqFeature feature1, SeqFeature feature2, Properties props) {
		entry1 = feature1.getEntry();
		entry2 = feature2.getEntry();
		range1 = feature1.getSeqRange();
		range2 = feature2.getSeqRange();		
		ff1 = new FastaFile(feature1);
		ff2 = new FastaFile(feature2);
		ff1.write();
		ff2.write();		
    offset1 = range1.getStart() - 1;	// dba pos values run from 0 - N, so no need to subtract 1 to get offset
		offset2 = range2.getStart() - 1;	// dba pos values run from 0 - N, so no need to subtract 1 to get offset
    properties = props;
		setParameters();
// 		dbac = dbapc + "-nomatchn "  + ff1.getPath() + " " + ff2.getPath();
// 		callDba();
	}
	
	/**
	 * Creates a Dba object with the subsequences specified by features
	 * @param feature1 first feature
	 * @param feature2 second feature
	 */
	public Dba(SeqFeature feature1, SeqFeature feature2, Vector mask1, Vector mask2, Properties props) {
		entry1 = feature1.getEntry();
		entry2 = feature2.getEntry();
		range1 = feature1.getSeqRange();
		range2 = feature2.getSeqRange();		
		ff1 = new FastaFile(feature1);
		ff2 = new FastaFile(feature2);
		ff1.maskRanges(mask1);
		ff2.maskRanges(mask2);
		ff1.write();
		ff2.write();		
    offset1 = range1.getStart() - 1;	// dba pos values run from 0 - N, so no need to subtract 1 to get offset
		offset2 = range2.getStart() - 1;	// dba pos values run from 0 - N, so no need to subtract 1 to get offset
    properties = props;
		setParameters();
// 		dbac = dbapc + "-nomatchn "  + ff1.getPath() + " " + ff2.getPath();
// 		callDba();
	}
  
	/**
	 * Creates a Dba object with the specified FastaFiles
	 * @param file1 first file
	 * @param file2 second file
	 */
	public Dba(FastaFile file1, FastaFile file2, String offset1, String offset2, Properties props) {
// 		entry1 = feature1.getEntry();
// 		entry2 = feature2.getEntry();
// 		range1 = feature1.getSeqRange();
// 		range2 = feature2.getSeqRange();		
// 		ff1 = new FastaFile(feature1);
// 		ff2 = new FastaFile(feature2);
// 		ff1.write();
// 		ff2.write();		
		ff1 = file1;
		ff2 = file2;
    this.offset1 = Integer.parseInt(offset1) - 1;
    this.offset2 = Integer.parseInt(offset2) - 1;
    properties = props;
		setParameters();
// 		dbac = dbapc + "-nomatchn "  + ff1.getPath() + " " + ff2.getPath();
// 		callDba();
	}
	
  /**
   * Returns the defaults for the Method in a Hashtable
   * @return Hashtable of defaults
   */
  public static Hashtable getDefaults() {
    Hashtable hash = new Hashtable(8);
    Vector exv = new Vector(executables.length);
    for (int i =0; i<executables.length; i++) {
      exv.addElement(executables[i]);
    }
    hash.put("executables",exv);
    hash.put("matchA",match_A);
    hash.put("matchB",match_B);
    hash.put("matchC",match_C);
    hash.put("matchD",match_D);
    hash.put("umatch",Umatch);
    hash.put("gap",Gap);
    hash.put("blockopen",Blockopen);
    return hash;
  }
/**
 * Overrides Thread run
 */
  public void run() {
    callDba();
    // should also parse AlignmentBlocks to gff and cgff
  }  
	/**
	 * Calls dba and stores the result in the result Vector
	 */
	public void callDba() {
		System.out.println("Calling " + dbac);
		Runtime rt = Runtime.getRuntime();
		try {
			Process p = rt.exec(dbac);
			InputStream stdout = p.getInputStream();
			InputStreamReader stdoutr = new InputStreamReader(stdout);
			BufferedReader stdoutbr = new BufferedReader(stdoutr);
			p.waitFor();
			String outline = stdoutbr.readLine();
			while (outline != null) {
				result.addElement(outline);
				outline = stdoutbr.readLine();
			}
		} catch (IOException ioe) { System.out.println(ioe); }
		catch (InterruptedException ie) { System.out.println(ie); }	
// 		System.out.println(result);		
		parseResult();
	}
	
// 	/**
// 	 * Parses the result from dba
// 	 */
// 	public void parseResult() {
// 		blocks = new Vector();
// 		StringBuffer blockSb1 = new StringBuffer();
// 		StringBuffer blockSb2 = new StringBuffer();
// 		StringBuffer matchSb = new StringBuffer();
// 		boolean inblock = false;
// 		int nMatch = 0,
// 			nMismatch = 0,
// 			nGap = 0;
// 		String code1 = "",
// 			code2 = "",
// 			base1 = "",
// 			base2 = "";
// 		int pos1 = 0,
// 			pos2 = 0;
// 		int start1 = 0,
// 			start2 = 0,
// 			stop1 = 0,
// 			stop2 = 0;
// // 		int offset1 = range1.getStart();	// dba pos values run from 0 - N, so no need to subtract 1 to get offset
// // 		int offset2 = range2.getStart();	// dba pos values run from 0 - N, so no need to subtract 1 to get offset
// 		Enumeration lines = result.elements();
// 		if (lines.hasMoreElements()) {
// 			String paramline = (String) lines.nextElement();
// 			// handle parameters
// 			StringTokenizer pst = new StringTokenizer(paramline, ",");
// 			matchA = findVal((String) pst.nextElement());
// 			matchB = findVal((String) pst.nextElement());
// 			matchC = findVal((String) pst.nextElement());
// 			matchD = findVal((String) pst.nextElement());
// 			umatch = findVal((String) pst.nextElement());
// 			gap = findVal((String) pst.nextElement());
// 			blockopen = findVal((String) pst.nextElement());
// 			double currMatch = 0;
// 			double s = 1-2*gap-blockopen;
// 			double score = 0;
// 			int level = 0;
// // 			System.out.println(match + "\t" + mismatch + "\t" + gap + "\t" + blockopen);
// 			String scoreline = (String) lines.nextElement();
// 			// handle over all score
// // 			int ind = scoreline.indexOf('=');
// // 			String overallS = scoreline.substring(ind +2);
// // 			overallScore = Integer.parseInt(overallS);
// 			overallScore = findVal(scoreline);
// 			while (lines.hasMoreElements()) {
// 				String line = (String) lines.nextElement();
// 				if (line.startsWith("END")) break;
// 				if (line.startsWith("M")) {
// 					// parse line into code1, pos1, base1, base2, pos2, code2. toUpperCase() on base1 and base2
// 
// 					StringTokenizer st = new StringTokenizer(line);
// 					if (line.startsWith("MI")) {
// 						code1 = (String) st.nextElement();
// 						String sPos1 = (String) st.nextElement();
// 						pos1 = Integer.parseInt(sPos1);
// 						base1 = (String) st.nextElement();
// 						base1 = base1.toUpperCase();
// 						code2 = (String) st.nextElement();
// // 					} else if (line.endsWith("MI")){
// 					} else if (line.indexOf("MI") != -1){
// 						code1 = (String) st.nextElement();
// 						base2 = (String) st.nextElement();
// 						base2 = base2.toUpperCase();
// 						String sPos2 = (String) st.nextElement();
// 						pos2 = Integer.parseInt(sPos2);
// 						code2 = (String) st.nextElement();
// 					} else {
// 						code1 = (String) st.nextElement();
// 						String sPos1 = (String) st.nextElement();
// 						pos1 = Integer.parseInt(sPos1);
// 						base1 = (String) st.nextElement();
// 						base1 = base1.toUpperCase();
// 						base2 = (String) st.nextElement();
// 						base2 = base2.toUpperCase();
// 						String sPos2 = (String) st.nextElement();
// 						pos2 = Integer.parseInt(sPos2);
// 						code2 = (String) st.nextElement();
// 					}
// 					if (!inblock) {
// 						inblock = true;
// 						score = (((1-umatch)/(umatch*umatch)) * (blockopen/umatch)) / 4;
// 						nMatch = nMismatch = nGap = 0;
// 						//get start1 and start2
// 						start1 = pos1;
// 						stop1 = start1 - 1;
// 						start2 = pos2;
// 						stop2 = start2 - 1;
// 						// Got to set a currMatch for score calc
// 						switch (Integer.parseInt(code1.substring(2))) {
// 							case 65: level = (int) (matchA * 100);
// 							         currMatch = matchA;
// 							         break;
// 							case 75: level = (int) (matchB * 100);
// 							         currMatch = matchB;
// 							         break;
// 							case 85: level = (int) (matchC * 100);
// 							         currMatch = matchC;
// 							         break;
// 							case 95: level = (int) (matchD * 100);
// 							         currMatch = matchD;
// 							         break;
// 						}
// 					}
// 					if (code1.startsWith("MI")) {
// 						nGap++;
// 						blockSb1.append(base1);
// 						blockSb2.append("-");
// 						matchSb.append(" ");
// 						stop1++;
// 						score *= gap/umatch;
// 					} else if (code2.startsWith("MI")) {
// 						nGap++;
// 						blockSb1.append("-");
// 						blockSb2.append(base2);
// 						matchSb.append(" ");
// 						stop2++;					
// 						score *= gap/umatch;
// 					} else if (base1.equals(base2)){
// 						nMatch++;
// 						blockSb1.append(base1);
// 						blockSb2.append(base2);
// 						matchSb.append("|");
// 						stop1++;
// 						stop2++;
// 						score *= (s/(umatch*umatch)) * (currMatch/0.25);					
// 					} else {
// 						nMismatch++;
// 						blockSb1.append(base1);
// 						blockSb2.append(base2);
// 						matchSb.append(" ");
// 						stop1++;
// 						stop2++;						
// 						score *= (s/(umatch*umatch)) * ( ((1-currMatch)/3)/0.25 );					
// 					}
// 				} else if (inblock) {
// 					//sumarize start1, stop1, start2, stop2, %ident, gaps, score
// 					SeqRange r1 = new SeqRange(offset1 + start1, offset1 + stop1);
// 					SeqRange r2 = new SeqRange(offset2 + start2, offset2 + stop2);
// 					r2.setComplement(range2.isComplement());
// 					AlignmentBlock bl = new AlignmentBlock(new String(blockSb1),
// 					                                        new String(blockSb2),
// 					                                        new String(matchSb),
// 					                                        r1,
// 					                                        r2,
// 					                                        entry1,
// 					                                        entry2,
// 					                                        level);
// // 					System.out.println("AlignmentBlock created, level: " + level);
// 					int ident = (int) Math.round( (100.0 * nMatch) / (nMatch + nMismatch) );
// 					//This is not correct must be fixed
// // 					double score = nMatch*match + nMismatch*(1-match) + nGap*gap + blockopen;
// 					double bitscore = Math.log(score)/Math.log(2);
// 					bl.setValues(ident, nGap, bitscore);
// // 					System.out.println(ident + "%, " + nGap + " gaps, score: " + score);
// // 					System.out.println((offset1 + start1) + blockSb1.toString() + (offset1 + stop1));
// // 					System.out.println("    " + matchSb);
// // 					System.out.println((offset2 + start2) + blockSb2.toString() + (offset2 + stop2));
// // 					System.out.println("found block ");
// 					blocks.addElement(bl);
// 					
// 					blockSb1 = new StringBuffer();
// 					blockSb2 = new StringBuffer();
// 					matchSb = new StringBuffer();
// 					inblock = false;
// 				}
// 			}
// 		}
// 	}
	/**
	 * Parses the result from dba
	 */
	public void parseResult() {
    int blocknum = 0;
    StringBuffer pffsb = new StringBuffer();
    Vector pffBlocksV = new Vector();
    StringBuffer gffsb = new StringBuffer();
    StringBuffer cgffsb = new StringBuffer();
//     String cgff1 = null;
//     String cgff2 = null;
		Enumeration lines = result.elements();
		while (lines.hasMoreElements()) {
      String line = (String) lines.nextElement();
//       System.out.println("DBA: " + line);
      if (line.startsWith(">")) {
        StringTokenizer st = new StringTokenizer(line, "\t");
        String seq = st.nextToken();
				String source = st.nextToken();
				source = source.toLowerCase();
				String feature = st.nextToken();
				feature = feature.toLowerCase();
				String start = st.nextToken();
				String end = st.nextToken();
				String score = st.nextToken();
				String strand = st.nextToken();
				String frameStr = st.nextToken();
				String group = st.nextToken();
        StringTokenizer gst = new StringTokenizer(group, ";");
        Hashtable groupHash = new Hashtable(4);
        while (gst.hasMoreElements()) {
          StringTokenizer sgst = new StringTokenizer(gst.nextToken());
          groupHash.put(sgst.nextToken(), sgst.nextToken());
        }
        // add line to gff string buffer (redo to change positions using offset)
        gffsb.append(line.substring(1) + "\n");
        int currblock = Integer.parseInt((String)groupHash.get("block"));
        if (currblock > blocknum) { // new block, i.e. first sequence of cgff
          cgffsb.append(seq + "\t" + (Integer.parseInt(start) + offset1) + "\t" + (Integer.parseInt(end) + offset1) + "\t");
          blocknum = currblock;
          if (blocknum > 1) { // got to have the first block before we can add it to the vector
            pffBlocksV.addElement(new String(pffsb));
            pffsb = new StringBuffer();
          }
          // build new pff comment line addign the offset to the position coordinates
//           pffsb.append(line + "\n");
          pffsb.append(seq + "\tDBA\tHCR\t" + (Integer.parseInt(start) + offset1) + "\t");
          pffsb.append((Integer.parseInt(end) + offset1) + "\t" + score + "\t" + strand + "\t" + frameStr + "\t" +  group + "\n");
         
        } else if (currblock == blocknum) { // second seq of cgff
          cgffsb.append(seq + "\t" + (Integer.parseInt(start) + offset2) + "\t" + (Integer.parseInt(end) + offset2) + "\n");
          // build new pff comment line addign the offset to the position coordinates
//           pffsb.append(line + "\n"); // so we don't miss the second part of the pffblock
          pffsb.append(seq + "\tDBA\tHCR\t" + (Integer.parseInt(start) + offset2) + "\t");
          pffsb.append((Integer.parseInt(end) + offset2) + "\t" + score + "\t" + strand + "\t" + frameStr + "\t" +  group + "\n");
        } else {
          System.out.println("Previous block number is larger than current!!!");
        }
      } else { // if it's a sequence line
        pffsb.append(line + "\n");
      }
    }
    gff = new String(gffsb);
    cgff = new String(cgffsb);
    pffBlocksV.addElement(new String(pffsb)); // add last pff block
    pffBlocks = new String [pffBlocksV.size()];
    pffBlocksV.copyInto(pffBlocks);
//     System.out.println(pffBlocksV);
//     int numblocks = pffBlocks.length;
//     System.out.println("Dba, number of blocks: " + numblocks);
//     pff = new String(pffsb);
    
    
    // old stuff below
// 		blocks = new Vector();
// 		StringBuffer blockSb1 = new StringBuffer();
// 		StringBuffer blockSb2 = new StringBuffer();
// 		StringBuffer matchSb = new StringBuffer();
// 		boolean inblock = false;
// 		int nMatch = 0,
// 			nMismatch = 0,
// 			nGap = 0;
// 		String code1 = "",
// 			code2 = "",
// 			base1 = "",
// 			base2 = "";
// 		int pos1 = 0,
// 			pos2 = 0;
// 		int start1 = 0,
// 			start2 = 0,
// 			stop1 = 0,
// 			stop2 = 0;
// // 		int offset1 = range1.getStart();	// dba pos values run from 0 - N, so no need to subtract 1 to get offset
// // 		int offset2 = range2.getStart();	// dba pos values run from 0 - N, so no need to subtract 1 to get offset
// 		Enumeration lines = result.elements();
// 		if (lines.hasMoreElements()) {
// 			String paramline = (String) lines.nextElement();
// 			// handle parameters
// 			StringTokenizer pst = new StringTokenizer(paramline, ",");
// 			matchA = findVal((String) pst.nextElement());
// 			matchB = findVal((String) pst.nextElement());
// 			matchC = findVal((String) pst.nextElement());
// 			matchD = findVal((String) pst.nextElement());
// 			umatch = findVal((String) pst.nextElement());
// 			gap = findVal((String) pst.nextElement());
// 			blockopen = findVal((String) pst.nextElement());
// 			double currMatch = 0;
// 			double s = 1-2*gap-blockopen;
// 			double score = 0;
// 			int level = 0;
// // 			System.out.println(match + "\t" + mismatch + "\t" + gap + "\t" + blockopen);
// 			String scoreline = (String) lines.nextElement();
// 			// handle over all score
// // 			int ind = scoreline.indexOf('=');
// // 			String overallS = scoreline.substring(ind +2);
// // 			overallScore = Integer.parseInt(overallS);
// 			overallScore = findVal(scoreline);
// 			while (lines.hasMoreElements()) {
// 				String line = (String) lines.nextElement();
// 				if (line.startsWith("END")) break;
// 				if (line.startsWith("M")) {
// 					// parse line into code1, pos1, base1, base2, pos2, code2. toUpperCase() on base1 and base2
// 
// 					StringTokenizer st = new StringTokenizer(line);
// 					if (line.startsWith("MI")) {
// 						code1 = (String) st.nextElement();
// 						String sPos1 = (String) st.nextElement();
// 						pos1 = Integer.parseInt(sPos1);
// 						base1 = (String) st.nextElement();
// 						base1 = base1.toUpperCase();
// 						code2 = (String) st.nextElement();
// // 					} else if (line.endsWith("MI")){
// 					} else if (line.indexOf("MI") != -1){
// 						code1 = (String) st.nextElement();
// 						base2 = (String) st.nextElement();
// 						base2 = base2.toUpperCase();
// 						String sPos2 = (String) st.nextElement();
// 						pos2 = Integer.parseInt(sPos2);
// 						code2 = (String) st.nextElement();
// 					} else {
// 						code1 = (String) st.nextElement();
// 						String sPos1 = (String) st.nextElement();
// 						pos1 = Integer.parseInt(sPos1);
// 						base1 = (String) st.nextElement();
// 						base1 = base1.toUpperCase();
// 						base2 = (String) st.nextElement();
// 						base2 = base2.toUpperCase();
// 						String sPos2 = (String) st.nextElement();
// 						pos2 = Integer.parseInt(sPos2);
// 						code2 = (String) st.nextElement();
// 					}
// 					if (!inblock) {
// 						inblock = true;
// 						score = (((1-umatch)/(umatch*umatch)) * (blockopen/umatch)) / 4;
// 						nMatch = nMismatch = nGap = 0;
// 						//get start1 and start2
// 						start1 = pos1;
// 						stop1 = start1 - 1;
// 						start2 = pos2;
// 						stop2 = start2 - 1;
// 						// Got to set a currMatch for score calc
// 						switch (Integer.parseInt(code1.substring(2))) {
// 							case 65: level = (int) (matchA * 100);
// 							         currMatch = matchA;
// 							         break;
// 							case 75: level = (int) (matchB * 100);
// 							         currMatch = matchB;
// 							         break;
// 							case 85: level = (int) (matchC * 100);
// 							         currMatch = matchC;
// 							         break;
// 							case 95: level = (int) (matchD * 100);
// 							         currMatch = matchD;
// 							         break;
// 						}
// 					}
// 					if (code1.startsWith("MI")) {
// 						nGap++;
// 						blockSb1.append(base1);
// 						blockSb2.append("-");
// 						matchSb.append(" ");
// 						stop1++;
// 						score *= gap/umatch;
// 					} else if (code2.startsWith("MI")) {
// 						nGap++;
// 						blockSb1.append("-");
// 						blockSb2.append(base2);
// 						matchSb.append(" ");
// 						stop2++;					
// 						score *= gap/umatch;
// 					} else if (base1.equals(base2)){
// 						nMatch++;
// 						blockSb1.append(base1);
// 						blockSb2.append(base2);
// 						matchSb.append("|");
// 						stop1++;
// 						stop2++;
// 						score *= (s/(umatch*umatch)) * (currMatch/0.25);					
// 					} else {
// 						nMismatch++;
// 						blockSb1.append(base1);
// 						blockSb2.append(base2);
// 						matchSb.append(" ");
// 						stop1++;
// 						stop2++;						
// 						score *= (s/(umatch*umatch)) * ( ((1-currMatch)/3)/0.25 );					
// 					}
// 				} else if (inblock) {
// 					//sumarize start1, stop1, start2, stop2, %ident, gaps, score
// 					SeqRange r1 = new SeqRange(offset1 + start1, offset1 + stop1);
// 					SeqRange r2 = new SeqRange(offset2 + start2, offset2 + stop2);
// 					r2.setComplement(range2.isComplement());
// 					AlignmentBlock bl = new AlignmentBlock(new String(blockSb1),
// 					                                        new String(blockSb2),
// 					                                        new String(matchSb),
// 					                                        r1,
// 					                                        r2,
// 					                                        entry1,
// 					                                        entry2,
// 					                                        level);
// // 					System.out.println("AlignmentBlock created, level: " + level);
// 					int ident = (int) Math.round( (100.0 * nMatch) / (nMatch + nMismatch) );
// 					//This is not correct must be fixed
// // 					double score = nMatch*match + nMismatch*(1-match) + nGap*gap + blockopen;
// 					double bitscore = Math.log(score)/Math.log(2);
// 					bl.setValues(ident, nGap, bitscore);
// // 					System.out.println(ident + "%, " + nGap + " gaps, score: " + score);
// // 					System.out.println((offset1 + start1) + blockSb1.toString() + (offset1 + stop1));
// // 					System.out.println("    " + matchSb);
// // 					System.out.println((offset2 + start2) + blockSb2.toString() + (offset2 + stop2));
// // 					System.out.println("found block ");
// 					blocks.addElement(bl);
// 					
// 					blockSb1 = new StringBuffer();
// 					blockSb2 = new StringBuffer();
// 					matchSb = new StringBuffer();
// 					inblock = false;
// 				}
// 			}
// 		}
	}
	
  
  /**
   * Gets the gff result string
   * @return gff string
   */
  public String getGff() {
    return gff;
  }
  /**
   * Gets the cgff result string
   * @return cgff string
   */
  public String getCgff() {
    return cgff;
  }
  /**
   * Gets the alignment blocks produced by dba as a vector of blocks.
   * Each block is a pair of pff sequences
   * @return pff string array
   */
  public String[] getPffBlocks() {
    return pffBlocks;
  }
  /**
   * Gets the gff data structure
   * @return gff data struct
   */
  public GffDataStruct getGffData() {
    return new GffDataStruct(gff, cgff, pffBlocks);
  }
	/**
	 * Gets the alignment blocks produced by dba
	 * @return vector of 
	 * @param param descr
	 */
	public Vector getAlignments() {
		return blocks;
	}
	
	/**
	 * Gets the overall score
	 * @return overall score
	 */
	public double getOverallScore() {
		return overallScore;
	}
	
	/**
	 * Gets the range1
	 * @return range
	 */
	public SeqRange getRange1() {
		return range1;
	}
	
	/**
	 * Gets the range2
	 * @return range
	 */
	public SeqRange getRange2() {
		return range2;
	}
	
	
	/**
	 * determines if Dba has any AlignmentBlocks
	 * @return true if Dba has AlignmentBlocks, otherwise false
	 */
	public boolean hasBlocks() {
		return !blocks.isEmpty();
	}
	
	/**
	 * finds the value in a String of the type "IDENTIFIER = val"
	 * @return int value, Note! 0 if no '=' is found
	 * @param s String to be parsed
	 */
	private double findVal(String s) {
		int ind = s.indexOf('=');
		if (ind >= 0) {
			String valS = s.substring(ind + 2);
// 			return Integer.parseInt(valS);
			return Double.valueOf(valS).doubleValue();
		}
		return 0;
	}
	
	/**
	 * Removes those alignment blocks that don't overlap the specified ranges
	 * from the blocks Vector
	 * @param r1 range that blocks from seq 1 should overlap
	 * @param r2 range that blocks from seq 1 should overlap
	 */
	public void onlyOverlapping(SeqRange r1, SeqRange r2) {
		Enumeration blen = blocks.elements();
		while(blen.hasMoreElements()) {
			AlignmentBlock bl = (AlignmentBlock) blen.nextElement();
			if (!(r1.overlap(bl.getRange1())) || !(r2.overlap(bl.getRange2()))) {
				blocks.removeElement(bl);
				System.out.println("Removed " + bl);
			}
		}
	}
	
	/**
	 * Returns an array of [minstart1, maxend1, minstart2, maxend2] for all alignment blocks,
	 * ie the alignment boundaries of both sequences.
	 * @return an array [minstart1, maxend1, minstart2, maxend2]. Note! if there are no alignment blocks
	 * minstart1 == maxend1 == minstart2 == maxend2 == 0
	 */
	public int[] getMinMax() {
		int min1 = 0;
		int max1 = 0;
		int min2 = 0;
		int max2 = 0;
		int len = blocks.size();
		if (len > 0) {
			AlignmentBlock bl1 = (AlignmentBlock) blocks.elementAt(0);
			AlignmentBlock blLast = (AlignmentBlock) blocks.elementAt(len-1);
			if (bl1 != null) {
				min1 = bl1.getRange1().getStart();
				min2 = bl1.getRange2().getStart();
				max1 = blLast.getRange1().getStop();
				max2 = blLast.getRange2().getStop();
			}
		}
		return new int[] {min1, max1, min2, max2};
	}
	
// 	/**
// 	 * Sets the parameter values using the values of the application DbaParamDialog.
// 	 * Dreadfully implemented
// 	 */
// 	public void setParameters() {
// 		DbaParamDialog pd = entry1.parentEntryPair.wc.dbapw;
// 		matchA = pd.matchA;
// 		matchB = pd.matchB;
// 		matchC = pd.matchC;
// 		matchD = pd.matchD;
// 		umatch = pd.umatch;
// 		gap = pd.gap;
// 		blockopen = pd.blockopen;
// 		dbapc = SystemConstants.DBAPATH + "dba -matchA " + matchA + " -matchB " + matchB + " -matchC " 
// 				+ matchC + " -matchD " + matchD + " -umatch " + umatch + " -gap " + gap + " -blockopen " + blockopen + " -align -params ";
// 	}
	/**
	 * Sets the parameter values using the values of the application properties.
	 */
	public void setParameters() {
		matchA = Double.valueOf(properties.getProperty("matchA")).doubleValue();
		matchB = Double.valueOf(properties.getProperty("matchB")).doubleValue();
		matchC = Double.valueOf(properties.getProperty("matchC")).doubleValue();
		matchD = Double.valueOf(properties.getProperty("matchD")).doubleValue();
		umatch = Double.valueOf(properties.getProperty("umatch")).doubleValue();
		gap = Double.valueOf(properties.getProperty("gap")).doubleValue();
		blockopen = Double.valueOf(properties.getProperty("blockopen")).doubleValue();
    
// 		dbapc = properites.getProperty("dba") + 
		dbac = properties.getProperty("dba") + 
            " -pff" +
            " -quiet" +
            " -matchA " + matchA + 
            " -matchB " + matchB + 
            " -matchC " + matchC + 
            " -matchD " + matchD + 
            " -umatch " + umatch + 
            " -gap " + gap + 
            " -blockopen " + blockopen + 
//             " -align -params -nomatchn " + 
            " -nomatchn " + 
            ff1.getPath() + " " + ff2.getPath();
    
//     System.out.println("Dba call set to: " + dbac);
	}

}
