/*
 * $Revision: 1.1 $
 * $Id: AlignmentBlock.java,v 1.1 2003/04/04 10:13:44 niclas Exp $
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
import java.util.*;
import java.io.*;
/**
 * Represents a block of two aligned sequences
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class AlignmentBlock implements java.io.Serializable {

  static final long serialVersionUID = -527012126639000430L;
/**
 * sequence block 1
 */ 
	String seq1;
/**
 * sequence block 2
 */ 
	String seq2;
/**
 * match string for the block
 */ 
	String match;
/**
 * range for seqence block 1
 */ 	
	SeqRange range1;
/**
 * range for seqence block 2
 */
	SeqRange range2;
/**
 * Entry thet sequence block1 belongs to
 */
	Entry entry1;
/**
 * Entry thet sequence block2 belongs to
 */
	Entry entry2;
/**
 * percent idenity	
 */ 
	int id;
/**
 * number of gaps
 */ 	
	int gaps;
/**
 * score	
 */ 
	double score;
/**
 * length of alignment w/o gaps
 */
	int length;
/**
 * score/base
 */
	double basescore;
/**
 * Level for dbb alignments (defaults: 65, 75, 85, 95)
 */
	int level = 0;
	
	/**
	 * Creates new AlignmentBlock
	 * @param seq1 sequence block 1
	 * @param seq2 sequence block 2
	 * @param match match string for the block
	 * @param r1 range for seqence block 1
	 * @param r2 range for seqence block 2
	 * @param ent1 entry sequence block 1 belongs to
	 * @param ent2 entry sequence block 2 belongs to
	 */
	public AlignmentBlock(String seq1, 
	                      String seq2, 
	                      String match, 
	                      SeqRange r1, 
	                      SeqRange r2,
	                      Entry ent1,
	                      Entry ent2) {
		this.seq1 = seq1;
		this.seq2 = seq2;
		this.match = match;
		range1 = r1;
		range2 = r2;
		entry1 = ent1;
		entry2 = ent2;
		length = Math.min(range1.length(), range2.length());
	}
	
	/**
	 * Creates new AlignmentBlock
	 * @param seq1 sequence block 1
	 * @param seq2 sequence block 2
	 * @param match match string for the block
	 * @param r1 range for seqence block 1
	 * @param r2 range for seqence block 2
	 * @param ent1 entry sequence block 1 belongs to
	 * @param ent2 entry sequence block 2 belongs to
	 * @param level dbb level
	 */
	public AlignmentBlock(String seq1, 
	                      String seq2, 
	                      String match, 
	                      SeqRange r1, 
	                      SeqRange r2,
	                      Entry ent1,
	                      Entry ent2,
												int level) {
		this.seq1 = seq1;
		this.seq2 = seq2;
		this.match = match;
		range1 = r1;
		range2 = r2;
		entry1 = ent1;
		entry2 = ent2;
		length = Math.min(range1.length(), range2.length());
		this.level = level;
	}
	
	
  /**
   * To make a AlignmentBlock from gffStruct input
   * IN NON-FUNCTIONAL STATE!!!!
   * @return descr
   * @param param descr
   */
//   public static Vector makeBlockVector(EntryPair entp, String gff, String cgff, String [] matchstringss) {
//     Entry ent1 = entp.getEntry1();
//     Entry ent2 = entp.getEntry2();
//     Vector blocks = new Vector();
//     
//     Vector gfflines = new Vector();
//     Vector cgfflines = new Vector();
//     if (gff == null) return null;
//     StringReader sr = new StringReader(gff);
//     BufferedReader br = new BufferedReader(sr);
//     String inline = null;
//     try {
//       while ((inline = br.readLine()) != null) {
// 				gfflines.addElement(inline);
// 		  }
//     } catch (IOException ioe) { ioe.printStackTrace(); }
//     String name1 = entp.getEntry1().getFilename();
//     String name2 = entp.getEntry2().getFilename();
//     Enumeration gffen = gfflines.elements();
//     while (gffen.hasMoreElements()) {
//       
//     }
//     
//   }

/**
 * Creates a vector of AlignmentBlocks from an array of pff block strings
 * @return vector of alignment blocks
 * @param pffBlocks array of pff block strings
 */
  public static Vector pffToAlignmentBlocks(String [] pffBlocks,
                                            Entry ent1, 
                                            Entry ent2) {
    Vector alBlocks = new Vector();
    for (int i = 0; i < pffBlocks.length; i++) {
      String block = pffBlocks[i];
//       System.out.println("Block " + i + "\n" + block + "\n");
      StringTokenizer st = new StringTokenizer(block,  "\n");
      String gff1 = st.nextToken().substring(1);
      String gff2 = null;
      StringBuffer sb1 = new StringBuffer();
      StringBuffer sb2 = new StringBuffer();
      StringBuffer sbM = new StringBuffer();
      boolean first = true;
      while (st.hasMoreTokens()) {
        String line = st.nextToken();
        if (line.startsWith(">")) {
          gff2 = line.substring(1);
          first = false;
        } else if (first) {
          sb1.append(line);
        } else {
          sb2.append(line);
        }
      }
      sbM = new StringBuffer();
      for (int j = 0; j < sb1.length(); j++) {
        char c1 = sb1.charAt(j);
        char c2 = sb2.charAt(j);
        if (c1 == c2) {
          sbM.append("|");
        } else {
          sbM.append(" ");
        }
        if (c1 == '.') {
          sb1.setCharAt(j, '-');
        } else if (c2 == '.') {
          sb2.setCharAt(j, '-');
        }
      }
      // parse the gff lines: ranges, id, gaps, score, level...
      
      /**
       * Inner class for pff comment line parsing
       */

      class PffCommentParser {
        int id;
        int gaps;
        double score;
        SeqRange range;
        int level;
        String type;
        
        PffCommentParser(String commline) {
          StringTokenizer ist = new StringTokenizer(commline, "\t");
          String seq = ist.nextToken();
				  String source = ist.nextToken();
				  source = source.toLowerCase();
				  String feature = ist.nextToken();
				  feature = feature.toLowerCase();
				  String start = ist.nextToken();
				  String end = ist.nextToken();
				  String scoreS = ist.nextToken();
				  String strand = ist.nextToken();
				  String frameStr = ist.nextToken();
				  String group = ist.nextToken();
          StringTokenizer gst = new StringTokenizer(group, ";");
          Hashtable groupHash = new Hashtable(4);
          while (gst.hasMoreElements()) {
            StringTokenizer sgst = new StringTokenizer(gst.nextToken());
            groupHash.put(sgst.nextToken(), sgst.nextToken());
          }
          id = Integer.parseInt((String)groupHash.get("id"));
          gaps = Integer.parseInt((String)groupHash.get("gaps"));
          type = (String)groupHash.get("type");
          score = Double.valueOf(scoreS).doubleValue();
          if (type.endsWith("A")) {
            level = 65;
          } else if (type.endsWith("B")) {
            level = 75;
          } else if (type.endsWith("C")) {
            level = 85;
          } else if (type.endsWith("D")) {
            level = 95;
          } else {
            level = 0;
          }
          boolean reverse = false;
          if (strand.equals("-")) { reverse = true; }
          range = new SeqRange(Integer.parseInt(start), Integer.parseInt(end), reverse);
        }
        
        int getId () {
          return id;
        }
        int getGaps () {
          return gaps;
        }
        double getScore () {
          return score;
        }
        SeqRange getRange () {
          return range;
        }
        int getLevel () {
          return level;
        }
        String getType () {
          return type;
        }
      }
      
      PffCommentParser pp1 = new PffCommentParser(gff1);
      PffCommentParser pp2 = new PffCommentParser(gff2);
      SeqRange r1 = pp1.getRange();
      SeqRange r2 = pp2.getRange();
      int id = pp1.getId();
      int gaps = pp1.getGaps();
      double score = pp1.getScore();
      int level = pp1.getLevel();
      
      AlignmentBlock alb = new AlignmentBlock(new String(sb1),
                                              new String(sb2),
                                              new String(sbM),
                                              r1, r2, ent1, ent2, level);
      alb.setValues(id, gaps, score);
      alBlocks.addElement(alb);
    }
    return alBlocks;
  }
	/**
	 * Sets values for identity, number of gaps and score
	 * @param i percent idenity
	 * @param g number of gaps
	 * @param s score
	 */
	public void setValues(int i, int g, double s) {
		id = i;
		gaps = g;
		score = s;
		basescore = score/length;
	}
	
	/**
	 * Gets sequence 1
	 * @return seq 1
	 */
	public String getSeq1() {
		return seq1;
	}

	/**
	 * Gets sequence 2
	 * @return seq 2
	 */
	public String getSeq2() {
		return seq2;
	}
	
	/**
	 * Gets match string
	 * @return match string
	 */
	public String getMatchString() {
		return match;
	}
	
	/**
	 * Gets range 1
	 * @return range 1
	 */
	public SeqRange getRange1() {
		return range1;
	}

	/**
	 * Gets range 2
	 * @return range 2
	 */
	public SeqRange getRange2() {
		return range2;
	}
	
	/**
	 * Gets Entry 1
	 * @return Entry 1
	 */
	public Entry getEntry1() {
		return entry1;
	}

	/**
	 * Gets Entry 2
	 * @return Entry 2
	 */
	public Entry getEntry2() {
		return entry2;
	}

	/**
	 * Gets the length w/o gaps of the alignment 
	 * @return length
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * Gets percent identity
	 * @return percent identity
	 */
	public int getIdentity() {
		return id;
	}
	
	/**
	 * Gets number of gaps
	 * @return nummber of gaps
	 */
	public int getGaps() {
		return gaps;
	}
	
	/**
	 * Gets alignment score
	 * @return score
	 */
	public double getScore() {
		return score;
	}
	
	/**
	 * Gets score/base for alignment
	 * @return score
	 */
	public double getBaseScore() {
		return basescore;
	}
	
	/**
	 * Gets level (dbb alignments)
	 * @return level
	 */
	public int getLevel() {
		return level;
	}
	/**
	 * Returns gff string representation of regions of AlignmentBlock
   * IN NON-FUNCTIONAL STATE
	 * @return string representation
	 */
// 	public String toGffString() {
// 	  StringBuffer sb = new StringBuffer();
//     String gff1 = seqName1 + "\tDBA\tHCR\t" + range1;
// 	}
	/**
	 * Returns string representation of AlignmentBlock
	 * @return string representation
	 */
	public String toString() {
		return entry1.getFilename() + range1 + "-" + entry2.getFilename() + range2;
	}
}
