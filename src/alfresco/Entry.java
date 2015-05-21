/*
 * $Revision: 1.1 $
 * $Id: Entry.java,v 1.1 2003/04/04 10:14:12 niclas Exp $
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
import java.awt.*;
import java.awt.event.*;

/**
 * Glyph that represents a sequence.<p>
 * Children are SeqFeature or CompositeSeqFeature objects
 * @see alfresco.CompositeGlyph
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class Entry extends CompositeGlyph {
// /**
//  * Name of fasta sequence file
//  */
// 	String filename = "";
/**
 * Name of sequence
 */
	String name;
/**
 * Entry file in fasta format	
 */
	File entryFile;
/**
 * The actual DNA sequence
 */
	transient String seq = "";
/**
 *   Holds sequence in shorter pieces when saving
 */ 
  private String [] seqPieces;
/**
 * The first line in the fasta sequence file
 */
	String comment = "";
/**
 * sequence length
 */
	int length = 0;
/**
 * Parent EntryPair
 */
	EntryPair parentEntryPair;

/**
 * empty default constructor
 */
	Entry () { }
	
/**
 * Creates a new Entry, reading in the fasta file specified<br>If the file specified 
 * can't be found the constructor calls getz to create a new sequence file.
 * @param file File object for sequence file
 */
	public Entry (File file) throws FileNotFoundException {	// constructor
// 		String tmpfn = file.getName();
// 		this.filename = tmpfn.trim();
		entryFile = file;
		if (!entryFile.exists()) {
			Runtime rt = Runtime.getRuntime();
			try {
// 				Process p = rt.exec("efetch -f em:" + filename);
// 				FastaFile ff = new FastaFile(USRDIR, filename, p.getInputStream());
// 				Process p = rt.exec("efetch -f em:" + entryFile.getName());
        String getzcommand = "getz '[embl-id:" + entryFile.getName() + "]' -d -sf fasta";
        System.out.println("Calling " + getzcommand);
				Process p = rt.exec(getzcommand);
				FastaFile ff = new FastaFile(entryFile, p.getInputStream());
				p.waitFor();
				ff.write();
			} catch (IOException ioe) { System.out.println(ioe); }
			catch (InterruptedException ie) { System.out.println(ie); }
		}
// 		System.out.println("Entry filename: " + filename);
// 		int test = filename.indexOf('\t');
// 		if (test > 0) { System.out.println("Filename contains space at position " + test); }
// 		if (this.filename == null ) { throw new FileNotFoundException(); }
		if (!entryFile.exists()) { throw new FileNotFoundException(); }
		String tmp;
		FileReader fr = null;
 		try {
 		fr = new FileReader(entryFile.getPath());
		} catch (FileNotFoundException fnf) { System.out.println(fnf); }
		read(fr);
// 		BufferedReader br = new BufferedReader(fr);
// 
// 		
// 		System.out.println("Reading file " + entryFile.getPath()+ "...");
// // 		char buffer[] = new char[MAXSEQLEN];
// 		
// 		long start = System.currentTimeMillis();
// 		try {
// 			comment = br.readLine();
// 
// // 			int r;
// // 			int i=0;
// // 			while ((r=br.read()) != EOF) {
// // 				if ( r == '\n' ) r=br.read();
// // 				buffer[i++] = (char) r;
// // 			}
// // 			seq = new String(buffer,0,i).toUpperCase();
// // 			length = i-1;
// 			String inline = null;
// 			StringBuffer sb = new StringBuffer();
// 			while((inline = br.readLine()) != null) {
// 				if(inline.startsWith(">")) {
// 					StringBuffer nb = new StringBuffer();
// 					for(int i = 0; i < 4; i++) {
// 						nb.append("NNNNNNNNNNNNNNNNNNNNNNNNN");
// 					}
// 					inline = new String(nb);
// 				}
// 				sb.append(inline);
// 			}
// 			seq = new String(sb).toUpperCase();
// 			length = seq.length();
// 		} catch (IOException ioe) { System.out.println(ioe); }
// 		long stop = System.currentTimeMillis();
// 		System.out.println("Run time: "+(stop-start)+" millisecs");
// 		System.out.println(length + " nucleotides read.");
		fillColor = Color.black;
	}
	
/**
 * Creates a new Entry, reading in the fasta file string specified<br>
 * @param fastaString sequence string
 */
	public Entry (String fastaString) {	// constructor
		entryFile = null;
// 		System.out.println("Entry filename: " + filename);
// 		int test = filename.indexOf('\t');
// 		if (test > 0) { System.out.println("Filename contains space at position " + test); }
// 		if (this.filename == null ) { throw new FileNotFoundException(); }
		StringReader sr = new StringReader(fastaString);
		read(sr);
		fillColor = Color.black;
	}
	
	/**
	 * Does the reading of the sequence into the seq string
	 * @param r reader
	 */
	private void read(Reader r) {
		BufferedReader br = new BufferedReader(r);

		if(entryFile != null) {
			System.out.println("Reading file " + entryFile.getPath()+ "...");
		} else {
			System.out.println("Reading sequence...");
		}
// 		char buffer[] = new char[MAXSEQLEN];
		
		long start = System.currentTimeMillis();
		try {
			comment = br.readLine();
			if (!comment.startsWith(">"))
				System.out.println("Comment line doesn't start with >");
			StringTokenizer st = new StringTokenizer(comment.substring(1));
			String tname = st.nextToken();
			int cind = tname.indexOf(":");
			if (cind != -1) {
				name = tname.substring(cind + 1);
			} else {
				name = tname;
			}
			System.out.println("name set to: " + name);
// 			int r;
// 			int i=0;
// 			while ((r=br.read()) != EOF) {
// 				if ( r == '\n' ) r=br.read();
// 				buffer[i++] = (char) r;
// 			}
// 			seq = new String(buffer,0,i).toUpperCase();
// 			length = i-1;
			String inline = null;
			StringBuffer sb = new StringBuffer();
			while((inline = br.readLine()) != null) {
				if(inline.startsWith(">")) {
// 					System.out.println("Found another sequence");
					StringBuffer nb = new StringBuffer();
					for(int i = 0; i < 4; i++) {
						nb.append("NNNNNNNNNNNNNNNNNNNNNNNNN");
					}
					inline = new String(nb);
				}
				sb.append(inline);
			}
			seq = new String(sb).toUpperCase();
			length = seq.length();
			char[] seqarr = seq.toCharArray();
//       boolean inNrep = false;
//       int rstart = 0;
//       int rstop =0;
			for (int i = 0; i < seqarr.length; i++) {
				char b = seqarr[i];
// 				if (b == 'A' || b == 'G' || b == 'C' || b == 'T' || b == 'N') {
				if (b == 'A' || b == 'G' || b == 'C' || b == 'T') {
// 					if(inNrep) {
//             addChild(new Nrepeat(this, new SeqRange(rstart+1, rstop+1)));
//           }
          continue;
				}
				seqarr[i] = 'N';
//         if (inNrep) {
//           rstop++;
//         } else {
//           rstart = i;
//           rstop =i;
//           inNrep = true;
//         }
			}
//       if (inNrep) {
//         addChild(new Nrepeat(this, new SeqRange(rstart+1, seqarr.length)));
//       }
			seq = new String(seqarr);
			
		} catch (IOException ioe) { System.out.println(ioe); }
		long stop = System.currentTimeMillis();
		System.out.println("Run time: "+(stop-start)+" millisecs");
		System.out.println(length + " nucleotides read.");
// 		System.out.println("Identifying N's");
// 		maskN();
	}
	
/**
 * Gets the whole DNA sequence
 * @return DNA sequence
 */
	public String getSequence() {
		return getSequence(1, length);
	}
	
/**
 * Gets the DNA sequence between positions start and end
 * @return DNA sequence
 * @param start start position
 * @param end stop position
 */
	public String getSequence(int start, int end) {
		String subseq = "";
		end = getEnd(end);
		if (start > length) { return subseq; }
		try {
			subseq = seq.substring(start-1, end);
		} catch (StringIndexOutOfBoundsException e) {
			System.out.println("Index out of bounds! Start: " + start + ", end: " + end); 
		}
		return subseq;
	}
	
/**
 * Gets the DNA sequence between positions defined by range. 
 * Note! if range is complement the reverse complement sequence will be returned
 * @return DNA sequence
 * @param r range
 */
	public String getSequence(SeqRange r) {
		String s = getSequence(r.getStart(), r.getStop());
		if (!r.isComplement()) {
			return s;
		} else {
			char[] farr = s.toUpperCase().toCharArray();
			StringBuffer sb = new StringBuffer();
			for (int i = farr.length - 1 ; i > -1; i--) {
				if(farr[i] == 'G') {
					sb.append('C');
				} else if (farr[i] == 'C') {
					sb.append('G');
				} else if (farr[i] == 'A') {
					sb.append('T');
				} else if (farr[i] == 'T') {
					sb.append('A');
				} else if (farr[i] == 'N') {
					sb.append('N');
				}
			}
			return new String(sb);
		}
	}
/**
 * Gets the whole DNA sequence, repeats masked
 * @return DNA masked sequence string
 */
	public String getRepeatMaskedSequence() {
    return maskStringSeq(getSequence(), getRepeats());
	}
	
/**
 * Gets the whole DNA sequence, repeats masked
 * @return DNA masked sequence string
 */
	public String getRepeatMaskedSequence(SeqRange r) {
//     return maskStringSeq(this.getSequence(r), getRepeats());
    return maskStringSeq(r, getRepeats());
	}
	
/**
 * Gets the whole DNA sequence, repeats masked except LowComplexity repeats
 * @return DNA masked sequence string
 */
	public String getRepeatMaskedSequenceNoLC() {
    return maskStringSeq(getSequence(), getRepeatsNoLC());
	}
	
/**
 * Adds child Glyph
 * @param gl Glyph to be added
 */
	public void addChild(Glyph gl) {
		super.addChild(gl);
		gl.coord = coord; // leave ?
// 		if (gl instanceof Similarity) {
// 			Similarity sim = (Similarity) gl;
// 			if (gl.getTier() > 0) {
// // 				System.out.println(gl + ", nudging down");
// 				nudgeDown(sim);
// 				
// 			} else {
// 				nudgeUp(sim);
// 			}
// 		}
	}

  
  /**
   * Returns the provided string masked with N's at positions
   * specified by ranges in vector
   * @return masked sequence
   * @param seq sequence string to be masked
   * @param ranges vector of ranges to be masked
   */
  private String maskStringSeq(String seq, Vector ranges) {
    char [] seqArr = seq.toCharArray();
    Enumeration ren = ranges.elements();
    while(ren.hasMoreElements()) {
      SeqRange r = (SeqRange) ren.nextElement();
      int start = r.getStart() - 1;
      int stop = r.getStop() - 1;
//       System.out.println("start: " + start + ", stop: " + stop);
      for (int i = start; i <= stop ; i++) {
        seqArr[i] = 'N';
      } 
    }
    return new String(seqArr);
  }
  
  /**
   * Returns the sequence string of the provided SeqRange masked with N's at positions
   * specified by ranges in vector
   * @return masked sequence
   * @param range ranges to be masked
   * @param ranges vector of ranges to be masked
   */
  private String maskStringSeq(SeqRange range, Vector ranges) {
    String seq = this.getSequence(range);
    char [] seqArr = seq.toCharArray();
//     System.out.println("seqArr length: " + seqArr.length);
    Enumeration ren = ranges.elements();
    while(ren.hasMoreElements()) {
      SeqRange r = (SeqRange) ren.nextElement();
      if (r.overlap(range)) {
        int start = r.getStart() - range.getStart();
        if (start < 0) start = 0;
        int stop = r.getStop() - range.getStart();
//         System.out.println("start: " + start + ", stop: " + stop);
        for (int i = start; i <= stop && i < seqArr.length ; i++) {
          seqArr[i] = 'N';
        } 
      }
//       int start = r.getStart() - 1;
//       int stop = r.getStop() - 1;
//       System.out.println("start: " + start + ", stop: " + stop);
//       for (int i = start; i <= stop ; i++) {
//         seqArr[i] = 'N';
//       } 
    }
    return new String(seqArr);
  }
/**
 * Finds N's in sequence and creates Nrepeats
 */
	public void maskN() {
//     System.out.println("Starting to mask N's");
		int start = 0; 
		int stop = -1;
		while (start > -1 ) {
			start = seq.indexOf('N',stop+1);
// 			System.out.println("start: " + start);
			if ( start > -1 ) {
				stop = start;
				int pos = start + 1;
				while (pos < length && seq.charAt(pos) == 'N'){
					stop++;
          pos++;
				}
			} else {
        continue;
      }
// 				System.out.println("Nrepeat start: " + (start+1) + " stop: " + (stop+1));
				SeqRange r = new SeqRange(start+1, stop+1);
				addChild(new Nrepeat(this, r));
		}
	}
	
	/**
   * Description
   * @return descr
   * @param param descr
   */
  public void addATGs() {
    int pos = 0;
    int prev = 0;
    while (pos > -1 ) {
      pos = seq.indexOf("ATG", prev);
      if (pos > -1) {
        ATG atg = new ATG(this, pos + 1, pos + 3, false);
        atg.setTier(1);
        addChild(atg);
        prev = pos + 3;
      }
    }
    pos = prev = 0;
    while (pos > -1 ) {
      pos = seq.indexOf("CAT", prev);
      if (pos > -1) {
        ATG atg = new ATG(this, pos + 1, pos + 3, true);
        atg.setTier(1);
        addChild(atg);
        prev = pos + 3;
      }
    }
    
  }
/**
 * Gets comment line
 * @return comment
 */
	public String getComment() {
		return comment;
	}
	
/**
 * Gets sequence length
 * @return length
 */
	public int getLength() {
		return length;
	}
	
	/**
	 * Gets the entryFile of the Entry
	 * @return File of entry sequence
	 */
	public File getEntryFile() {
		return entryFile;
	}
	
/**
 * Gets sequence filename
 * @return sequence file name
 */
	public String getFilename() {
		if (entryFile != null) {
			return entryFile.getName();
		} else {
			return name;
		}
	}
/**
 * Returns a name for a FastaFile of a subsequence of the Entry specified by the range
 * @return subsequence file name
 * @param r the range
 */
	public String getSubSeqFileName(SeqRange r) {
		return "." + entryFile.getName() + "_" + r.getStart() + "-" + r.getStop();
	}
// 	public Vector getExclude()  {
// 		Vector exclude = new Vector();
// 		Enumeration entryChildren = children.elements();
// 		while(entryChildren.hasMoreElements()) {
// 			Glyph child = (Glyph) entryChildren.nextElement();
// 			if (child instanceof Repeat) {
// 				Repeat r = (Repeat) child;
// 				exclude.addElement(r.getSeqRange());
// 			}
// 			if (child instanceof Gene){
// 				Gene g = (Gene) child;
// 				Enumeration geneChildren = g.children.elements();
// 				while (geneChildren.hasMoreElements()) {
// 					Glyph gChild = (Glyph) geneChildren.nextElement();
// 					if (gChild instanceof CDS) {
// 						Enumeration cdsChildren = gChild.children.elements();
// 						while (cdsChildren.hasMoreElements()) {
// 							Exon ex = (Exon) cdsChildren.nextElement();
// 	 						exclude.addElement(ex.getSeqRange());
// 							
// 						}
// 					}
// 				}
// 			}
// 		}
// 		return exclude;
// 	}	

/**
 * Gets ranges of all Exons of the Entry
 * @return Vector of ranges
 */
	public Vector getExonRanges()  {
		Vector exclude = new Vector();
		Enumeration entryChildren = children.elements();
		while(entryChildren.hasMoreElements()) {
			Glyph child = (Glyph) entryChildren.nextElement();
			if (child instanceof Gene){
				Gene g = (Gene) child;
				Enumeration geneChildren = g.children.elements();
				while (geneChildren.hasMoreElements()) {
					Glyph gChild = (Glyph) geneChildren.nextElement();
					if (gChild instanceof CDS) {
						Enumeration cdsChildren = gChild.children.elements();
						while (cdsChildren.hasMoreElements()) {
							Exon ex = (Exon) cdsChildren.nextElement();
// 	 						exclude.addElement(ex.getSeqRange());
	 						exclude.insertElementAt(ex.getSeqRange(),0); // 'cause they are stored backwards
							
						}
					}
				}
			} else if (child instanceof Exon) {
				exclude.insertElementAt(((Exon)child).getSeqRange(),0);
			}
		}
		return exclude;
	}
	
	/**
	 * Gets a Vector the Genes of the enty
	 * @return Vector of Genes
	 */
	public Vector getGenes() {
		Vector genes = new Vector();
		Enumeration entryChildren = children.elements();
		while(entryChildren.hasMoreElements()) {
			Object child = entryChildren.nextElement();
			if (child instanceof Gene){
				genes.addElement(child);
			}
		}
		return genes;
	}
	
/**
 * Gets all Exons of the Entry
 * @return Vector of Exons
 */
	public Vector getExons()  { // Should use getGenes() above
		Vector exons = new Vector();
		Enumeration entryChildren = children.elements();
		while(entryChildren.hasMoreElements()) {
			Glyph child = (Glyph) entryChildren.nextElement();
			if (child instanceof Gene){
				Gene g = (Gene) child;
				Enumeration geneChildren = g.children.elements();
				while (geneChildren.hasMoreElements()) {
					Glyph gChild = (Glyph) geneChildren.nextElement();
					if (gChild instanceof CDS) {
						Enumeration cdsChildren = gChild.children.elements();
						while (cdsChildren.hasMoreElements()) {
							Exon ex = (Exon) cdsChildren.nextElement();
	 						exons.addElement(ex);
							
						}
					}
				}
			} else if ( child instanceof Exon) {
				Exon ex = (Exon) child;
				exons.addElement(ex);
			}
		}
		return exons;
	}
	
/**
 * Gets ranges of all UTRs of the Entry
 * @return Vector of ranges
 */
	public Vector getUTRs() {
		Vector utrs = new Vector();
		Enumeration entryChildren = children.elements();
		while(entryChildren.hasMoreElements()) {
			Glyph child = (Glyph) entryChildren.nextElement();
			if (child instanceof Gene){
				Gene g = (Gene) child;
				Enumeration geneChildren = g.children.elements();
				while (geneChildren.hasMoreElements()) {
					Glyph gChild = (Glyph) geneChildren.nextElement();
					if (gChild instanceof UTR) {
						UTR utr = (UTR) gChild;
						utrs.addElement(utr.getSeqRange());
					}
				}
			}
		}
		return utrs;
	}
	
/**
 * Gets ranges of all Introns of the Entry
 * @return Vector of ranges
 */
	public Vector getIntrons() {
		Vector intr = new Vector();
		Enumeration entryChildren = children.elements();
		while(entryChildren.hasMoreElements()) {
			Glyph child = (Glyph) entryChildren.nextElement();
			if (child instanceof Gene){
				Gene g = (Gene) child;
				Enumeration geneChildren = g.children.elements();
				while (geneChildren.hasMoreElements()) {
					Glyph gChild = (Glyph) geneChildren.nextElement();
					if (gChild instanceof Intron) {
						Intron i = (Intron) gChild;	
						intr.addElement(i.getSeqRange());
					}
				}
			}
		}
		return intr;
	}
	
/**
 * Gets ranges of all Repeats of the Entry
 * @return Vector of ranges
 */
	public Vector getRepeats()  {
		Vector exclude = new Vector();
		Enumeration entryChildren = children.elements();
		while(entryChildren.hasMoreElements()) {
			Glyph child = (Glyph) entryChildren.nextElement();
			if (child instanceof Repeat) {
				Repeat r = (Repeat) child;
				exclude.addElement(r.getSeqRange());
			}
		}
		return exclude;
	}

/**
 * Gets ranges of all Repeats of the Entry, except repeats of the class specified
 * @return Vector of ranges
 * @param notInclClass type of repeat not to include
 */
	public Vector getRepeats(Class notInclClass)  {
		Vector exclude = new Vector();
		Enumeration entryChildren = children.elements();
		while(entryChildren.hasMoreElements()) {
			Glyph child = (Glyph) entryChildren.nextElement();
			if (child instanceof Repeat) {
				Repeat r = (Repeat) child;
				if (!notInclClass.isInstance(r)) {
					exclude.addElement(r.getSeqRange());
				} else {
					System.out.println("Excluded " + r);
				}
			}
		}
		return exclude;
	}
/**
 * Gets ranges of all Repeats of the Entry, except low complexity regions
 * @return Vector of ranges
 */
	public Vector getRepeatsNoLC()  {
		Vector exclude = new Vector();
		Enumeration entryChildren = children.elements();
		while(entryChildren.hasMoreElements()) {
			Glyph child = (Glyph) entryChildren.nextElement();
			if (child instanceof Repeat) {
				Repeat r = (Repeat) child;
				if ( !(r instanceof LowComplexityRepeat)) {
					exclude.addElement(r.getSeqRange());
				} else {
					System.out.println("Excluded " + r);
				}
			}
		}
		return exclude;
	}
	
	/**
	 * Gets Vector of all promoters
	 * @return vector of promoters
	 */
	public Vector getPromoters() {
		Vector prs = new Vector();
		Enumeration kids = getChildEnumeration();
		while(kids.hasMoreElements()) {
			Glyph kid = (Glyph) kids.nextElement();
			if(kid instanceof Promoter) {
				prs.addElement(kid);
			} else if (kid instanceof Gene) {
				Gene g = (Gene) kid;
				Enumeration gkids = g.getChildEnumeration();
				while (gkids.hasMoreElements()) {
					Glyph gkid = (Glyph) gkids.nextElement();
					if (gkid instanceof Promoter) {
						prs.addElement(gkid);
					}
				}
			}
		}
		return prs;
	}
	
	/**
	 * Gets Vector of all TFBSs
	 * @return vector
	 */
	public Vector getTFBSs() {
		Vector tfs = new Vector();
		Enumeration proms = getPromoters().elements();
		while (proms.hasMoreElements()){
			Enumeration pren = ((Promoter) proms.nextElement()).getChildEnumeration();
			while(pren.hasMoreElements()){
				Glyph kid = (Glyph) pren.nextElement();
				if (kid instanceof TFBS) {
					tfs.addElement(kid);
				}
			}
		}
		return tfs;
	}
	
	/**
	 * Gets Vector of all ATGs
	 * @return vector
	 */
	public Vector getATGs() {
		Vector atgs = new Vector();
    Enumeration kids = getChildEnumeration();
		while(kids.hasMoreElements()) {
			Glyph kid = (Glyph) kids.nextElement();
			if(kid instanceof ATG) {
				atgs.addElement(kid);
			}
    }
    return atgs;
	}
/**
 * Gets ranges of all CpGs of the Entry
 * @return Vector of ranges
 */
	public Vector getCpGs() {
		Vector exclude = new Vector();
		Enumeration entryChildren = children.elements();
		while(entryChildren.hasMoreElements()) {
			Glyph child = (Glyph) entryChildren.nextElement();
			if (child instanceof CpGIsland) {
				CpGIsland cpg = (CpGIsland) child;
				exclude.addElement(cpg.getSeqRange());
			}
		}
		return exclude;
		
	}
	
/**
 * Gets ranges of all Similarities of the Entry
 * @return Vector of ranges
 */
	public Vector getSimilarities() {
		Vector exclude = new Vector();
		Enumeration entryChildren = children.elements();
		while(entryChildren.hasMoreElements()) {
			Glyph child = (Glyph) entryChildren.nextElement();
			if (child instanceof Similarity) {
				Similarity sim = (Similarity) child;
				exclude.addElement(sim.getSeqRange());
			}
		}
		return exclude;
		
	}
/**
 * Gets ranges of all HCR of the Entry
 * @return Vector of ranges
 */
	public Vector getHCRs() {
		Vector exclude = new Vector();
		Enumeration entryChildren = children.elements();
		while(entryChildren.hasMoreElements()) {
			Glyph child = (Glyph) entryChildren.nextElement();
			if (child instanceof HCR) {
				HCR h = (HCR) child;
				exclude.addElement(h.getSeqRange());
			}
		}
		return exclude;
		
	}
  
/**
 * Gets ranges of all HCR of the Entry
 * @return Vector of ranges
 */
	public Vector getConservedElements() {
		Vector hcrs = getHCRs();
    Vector sims = getSimilarities();    
		Enumeration hcen = hcrs.elements();
		while(hcen.hasMoreElements()) {
      sims.addElement(hcen.nextElement());
		}
		return sims;
		
	}
	/**
	 * Gets the feature with the range and class name specified
	 * @return SeqFeature
	 * @param range range of feature
	 * @param classname class name of feature
	 */
	public SeqFeature getFeature(SeqRange range, String classname) {
		Enumeration kids = getChildEnumeration();
		while(kids.hasMoreElements()) {
			Glyph gl = (Glyph) kids.nextElement();
			if (gl instanceof SeqFeature) {
				if (gl.getClass().getName().equals("alfresco." + classname))
					if ( range.isEqual( ((SeqFeature) gl).getSeqRange() ) )
						return (SeqFeature) gl;
			} else if (gl instanceof CompositeSeqFeature){
				SeqFeature sf = ((CompositeSeqFeature) gl).getFeature(range, classname);
				if (sf != null) return sf;
			}
		}
		return null;
	}
	
/**
 * Gets a vector of feature lines in gff format for all features of the Entry
 * @return Vector of Strings
 */
	public Vector getGffLines() {
		Vector lines = new Vector();
		Vector kids = getChildren();
		Enumeration ke = kids.elements();
		while(ke.hasMoreElements()) {
			Glyph gl = (Glyph) ke.nextElement();
			if (gl instanceof Gene) {
				Vector genekids = gl.getChildren();
				Enumeration gke = genekids.elements();
				while(gke.hasMoreElements()) {
					Glyph gkgl = (Glyph) gke.nextElement();
					if (gkgl instanceof SeqFeature) {
						SeqFeature sf = (SeqFeature) gkgl;
// 						lines.addElement(sf.gffString());
						lines.insertElementAt(sf.gffString(),0);
					}
					if (gkgl instanceof CDS) {
						Vector cdskids = gkgl.getChildren();
						Enumeration cke = cdskids.elements();
						while(cke.hasMoreElements()) {
							Exon ex = (Exon) cke.nextElement();
//							lines.addElement(ex.gffString());
							lines.insertElementAt(ex.gffString(),0);
						}
					}
				}
			}
			if (gl instanceof SeqFeature) {
				if (!(gl instanceof Scale) && !(gl instanceof ATG)) {
					SeqFeature sf = (SeqFeature) gl;
// 					lines.addElement(sf.gffString());
						lines.insertElementAt(sf.gffString(),0);
				}
			}
		}
		return lines;
	}
	
/**
 * Whether to show Introns or not
 * @param show true to show, false not to show
 */
	public void showIntrons(boolean show) {
		Enumeration entryChildren = children.elements();
		while(entryChildren.hasMoreElements()) {
			Glyph child = (Glyph) entryChildren.nextElement();
			if (child instanceof Gene){
				Gene g = (Gene) child;
				Enumeration geneChildren = g.children.elements();
				while (geneChildren.hasMoreElements()) {
					Glyph gChild = (Glyph) geneChildren.nextElement();
					if (gChild instanceof Intron) {
						Intron i = (Intron) gChild;	
						i.setVisible(show);
					}
				}
			}
		}
	}
	  /**
   * Description
   * @return descr
   * @param param descr
   */
  public void showUnconfirmedExons(boolean show) {
    Vector ex = getExons();
    Enumeration exen = ex.elements();
    while(exen.hasMoreElements()) {
      Exon e = (Exon) exen.nextElement();
      if (!e.isConfirmed()) {
        e.setVisible(show);
      }
    }
  }

	/**
	 * Makes sure that overlapping genes are on different tiers
	 */
	public void nudgeOverlappingGenes() {
		Vector genes = getGenes();
		if(genes.size() > 1) {
			for (int i = 1; i < genes.size(); i++) {
				Gene thisgene = (Gene) genes.elementAt(i);
				Gene prevgene = (Gene) genes.elementAt(i-1);
// 				System.out.println(thisgene + ", " + prevgene);
				if (thisgene.overlap(prevgene)) {
					if (tier > 0) {
						thisgene.setTier(prevgene.getTier() + 1);
					} else {
						thisgene.setTier(prevgene.getTier() - 1);
					}
				}
			}
		}
	}
	
	/**
	 * Reduces the tier of SeqFeatures of the same class
	 * @param param descr
	 */
	public void nudgeDown(SeqFeature sf) {
		Class sfc = sf.getClass();
		Vector features = getChildrenByClass(sfc);
// 		Vector overlapping = new Vector();
		Enumeration fen = features.elements();
		int mintier = sf.getTier();
		while(fen.hasMoreElements()) {
			SeqFeature feature = (SeqFeature) fen.nextElement();
			if (sf.overlap(feature)) {
				System.out.println("Found overlap to " + sf);
				int tier = feature.getTier();
				mintier = (tier<=mintier)?tier - 1:mintier;
				System.out.println("mintier: " + mintier);
			}
		}
		if (mintier < sf.getTier()) {
			sf.setTier(mintier);
		}
	}
	
	/**
	 * Gets the scale glyph of the entry
	 * @return scale glyph
	 */
	public Scale getScale() {
		Enumeration kids = getChildEnumeration();
		while(kids.hasMoreElements()) {
			Glyph gl = (Glyph) kids.nextElement();
			if (gl instanceof Scale)
				return (Scale) gl;
		}
		return null;
	}
	
	/**
	 * Increases the tier of SeqFeatures of the same class
	 * @param param descr
	 */
	public void nudgeUp(SeqFeature sf) {
		Class sfc = sf.getClass();
		Vector features = getChildrenByClass(sfc);
		Vector overlapping = new Vector();
		Enumeration fen = features.elements();
		int maxtier = sf.getTier();
		while(fen.hasMoreElements()) {
			SeqFeature feature = (SeqFeature) fen.nextElement();
			if (sf.overlap(feature)) {
				int tier = feature.getTier();
				maxtier = (tier>=maxtier)?tier+1:maxtier;
			}
		}
		if (maxtier > sf.getTier()) {		
			sf.setTier(maxtier);
		}
	}
	
/**
 * Gets true width of Entry in pixels
 * @return width
 */
	public int getPixelWidth() {
// 		return canvWidth;
		return realWidth;
	}
	
/**
 * Sets x positions of Entry
 * @param x x position
 */
	public void setX(int x) {
		coord.setX(x);
	}
	
/**
 * Changes x position with nuber of pixels specified
 * @param pixelDiff number of pixels to move
 */
	public void scroll(int pixelDiff) {
		int xval = coord.getX()-pixelDiff;
		coord.setX(xval);
	}
	
/**
 * Draws Entry and child Glyphs on specified Graphics
 * @param g Graphics to draw to
 */
	public void draw(Graphics g) {
		canvY = coord.originY;
// 		canvX = coord.originX;
// 		canvWidth = (int) (length*coord.zoom);
		realX = coord.originX;
		realWidth = (int) (length*coord.zoom);
		realXend = realX + realWidth;
		if (realX > ABSXMAX || realXend < -ABSXMAX) { return; }
		if (realX < -ABSXMAX) {
			canvX = -ABSXMAX;
		} else {
			canvX = realX;
		}
		if (realXend > ABSXMAX) {
			canvWidth = ABSXMAX - canvX;
		} else {
// 			canvWidth = realWidth;
			canvWidth = realXend - canvX;
		}
		
		// set initial boundingRect to the line
		boundingRect = new Rectangle(canvX, canvY, canvWidth, 1); 
		// draw line for sequence
		g.setColor(fillColor);
		g.drawLine(canvX, canvY, canvX+canvWidth, canvY);
		super.draw(g);
		boundingRect = getBounds();
// 		Font f = parentEntryPair.wc.DNAFONT;
// 		int fw = coord.canv.getFontMetrics(f).getMaxAdvance();
// 		if (coord.zoom >= fw) {
		if (coord.zoom >= parentEntryPair.wc.DNAFONTWIDTH) {
// 			int start = (int)(-realX/coord.zoom);
			int start;
			double adjust;
			if (realX < 0) {
				start = (int) (-realX/coord.zoom);
				adjust = realX%coord.zoom;
			} else {
				start = 1;
				adjust = realX;
			}
			if (start == 0) { start = 1; }
// 			double start = -canvX/coord.zoom;
// 			System.out.print("Start: " + start);
			int w = (int)(coord.canvWidth/coord.zoom);
// 			int w = (int)((coord.canvWidth-realX)/coord.zoom);
// 			System.out.println("w: " + w);
			
			int end = getEnd(start + w);
			if (end >= start) { 
				Font pf = g.getFont();
				Font f = parentEntryPair.wc.DNAFONT;
				g.setFont(f);
				g.setColor(fillColor);
				String dSeq = getSequence(start, end);
// 				if (adjust >= 0) {
// 					dSeq = " " + dSeq;
// 				}
				g.drawString(dSeq, (int) adjust, canvY);
				g.setFont(pf);
			}
		}
		// include children in boundingRect
//		System.out.print("centerY: " + centerY);
//		System.out.print(" Tier: " + tier );
//		System.out.println(" canvY: " + canvY);
	}
	
	/**
	 * Returns the input position if it is less than the length otherwise the length
	 * @return the input pos if it is less than the length otherwise the length
	 * @param pos position
	 */
	private int getEnd(int pos) {
		if (pos > length) { pos = length; }
		return pos;
	}
	
/**
 * Centers Entry on drawing area
 */
	public void centerX() {
// 		canvWidth = (int) (length*coord.zoom);
		realWidth = (int) (length*coord.zoom);
// 		canvX = coord.getCanvCenterX() - canvWidth/2;
		realX = coord.getCanvCenterX() - realWidth/2;
		coord.setX(realX);		
	}
	
/**
 * String representation of Entry
 * @return string representation
 */
	public String toString() {
// 		String s = "Entry " + comment.substring(0,9);
		String s = name;
		return s;
	}
  
  /**
   * Divides sequence in 32K pieces for saving
   */
  public void chopUpSeqs() {
    int p = seq.length()/32000 + 1;
    seqPieces = new String[p];
//     char [] seqchars = seq.toCharArray();
    int chunksize = 32000;
    int pc = 0;
    for (int i = 0; i < seq.length(); i += chunksize) {
//       int end = (i + chunksize -1)< seq.length()?(i + chunksize -1):seq.length();
//       int start = i == 0?0:i-1;
      int end = (i + chunksize) < seq.length()?(i + chunksize):seq.length();
      int start = i;
      String piece = seq.substring(start, end);
      seqPieces[pc++] = piece;
    }
  }
  
  /**
   * Merges sequence pieces after loading from file
   */
  public void mergeSeqPieces() {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < seqPieces.length; i++) {
      sb.append(seqPieces[i]);
    }
    seq = new String(sb);
    seqPieces = null;
  }
// /**
//  * Calls RepeatMasker to find repeats. Repeat Glyphs are added to Entry
//  */
// 	public void callRepeatMasker(String option) { 
// 		Runtime rt = Runtime.getRuntime();
// 		Process rmp = null;
// // 		String option = "";
// // 		if (entryFile.getName().startsWith("MM")) { option = "-m "; }
// 		String cmnd = SystemConstants.REPEATMASKERPATH + "RepeatMasker " + option + " " + entryFile.getPath();
// // 		System.out.println("Calling " + cmnd);
// 		try {
// 			rmp = rt.exec(cmnd);
// 			rmp. waitFor();
// // 		String rmfn = entryFile.getPath() + ".RepMask";
// 		String rmfn = entryFile.getPath() + ".out";
// //		System.out.println(rmfn);
// // 		String path = System.getProperty("user.dir");
// // 		RepeatMaskerFile rmFile = new RepeatMaskerFile(USRDIR, rmfn, this);
// 		RepeatMaskerFile rmFile = new RepeatMaskerFile(rmfn, this);
// 		rmFile.open();
// 		rmFile.parse();
// 		}catch (InterruptedException ie) { System.out.println("RepeatMasker interupted:\n" + ie); }
// 			catch (IOException ioe) { System.out.println(ioe); }
// // 			catch (FileNotFoundException fnf) { System.out.println(fnf); }
// 	}
	
	// main, to test class
	public static void main(String[] arg) {
		String fn = arg[0];
		File f = new File(fn);
		Entry ent = null;
		try {
		ent = new Entry(f);
		} catch (FileNotFoundException fnf) { System.out.println(fnf); }
		System.out.println(ent);
	}
}

