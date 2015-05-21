/*
 * $Revision: 1.1 $
 * $Id: FastaFile.java,v 1.1 2003/04/04 10:14:20 niclas Exp $
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
 * FastaFile represents a sequence file in fasta format
 * @see java.io.File
 * @see alfresco.UsefulConstants
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */ 
 
public class FastaFile extends File implements UsefulConstants {
/**
 * Entry the sequence comes from
 */
	Entry entry;
/**
 * Range of the sequence in entry
 */
	SeqRange range;
/**
 * Comment line of fasta file
 */
	String comment;
/**
 * DNA sequence
 */
	String seq;
/**
 * Vector of ranges
 */
	Vector ranges;
/**
 * Vector of comments
 */
	Vector comments;
/**
 * Vector of sequences
 */
	Vector seqs;

/**
 * Creates FastaFile with the specified name from the specified Entry and range
 * @param path directory path
 * @param filename name of file
 * @param ent source of the sequence
 * @param range range in the sequence specified by Entry
 */
	public FastaFile (String path, String filename, Entry ent, SeqRange range) {
		super(path, filename + (range.isComplement()?"REV":""));
		this.entry = ent;
		this.range = range;
		String tmpcomment = entry.getComment();
		int spaceindex = tmpcomment.indexOf(" ");
// 		comment = tmpcomment.substring(0,spaceindex) + " " + range + tmpcomment.substring(spaceindex);
		StringBuffer sb = null;
    if (spaceindex != -1 ) {
//       sb = new StringBuffer(tmpcomment.substring(0,spaceindex) + " ");
      sb = new StringBuffer(tmpcomment.substring(0,spaceindex));
    } else {
      sb = new StringBuffer(tmpcomment);
    }
// 		System.out.println("before Appending: " + range.toSuffixString());
    if (sb != null) {
// 		  System.out.println("Appending: " + range.toSuffixString());
      sb.append(range.toSuffixString());
      if (range.isComplement()) {
// 			  sb.append("REVERSE ");
			  sb.append("_REVERSE");
  // 			System.out.println("Name of file: " + this.getPath());
		  }
      if (spaceindex != -1 ) {
		    sb.append(tmpcomment.substring(spaceindex) );
      }
		  comment = new String(sb);
    } else {
      
      comment = tmpcomment;
      
    }
		seq = entry.getSequence(range);
	}

/**
 * Creates a multiple sequence FastaFile with the specified name from the specified Entry and ranges
 * @param path directory path
 * @param filename name of file
 * @param ent source of the sequence
 * @param ranges vector of ranges in the sequence specified by Entry
 */
  
  // Take a closer look at this !!!!!!!!!!!!!!!!!!!!!!
	public FastaFile (String path, String filename, Entry ent, Vector ranges) {
		super(path, filename);
		this.entry = ent;
		this.ranges = ranges;
		this.comments = new Vector();
		this.seqs = new Vector();
		String tmpcomment = entry.getComment();
		
		int spaceindex = tmpcomment.indexOf(" ");
		StringBuffer sb = null;
    if (spaceindex != -1 ) {
      sb = new StringBuffer(tmpcomment.substring(0,spaceindex));
    }
		Enumeration ren = ranges.elements();
		while (ren.hasMoreElements()) {
			SeqRange r = (SeqRange) ren.nextElement();
			StringBuffer tmpsb = new StringBuffer(new String(sb)); // Ugly!!!
			if (tmpsb != null) {
        if (r.isComplement()) {
				  tmpsb.append("REVERSE ");
			  }
			  tmpsb.append(r + ((spaceindex != -1)?tmpcomment.substring(spaceindex):""));
  // 			comment = new String(sb);
			  comments.addElement(new String(tmpsb));
      } else {
        comments.addElement(new String(tmpcomment));
      }
// 			seq = entry.getSequence(range);
			seqs.addElement(entry.getSequence(r));
		}
	}

/**
 * Creates FastaFile from the specified SeqFeature in TMPDIR
 * @param feature the feature
 */
	public FastaFile (SeqFeature feature) {
		super(SystemConstants.TMPDIR, feature.getSubSeqFileName() + (feature.getSeqRange().isComplement()?"REV":""));
		this.entry = feature.getEntry();
		this.range = feature.getSeqRange();
		String tmpcomment = entry.getComment();
		int spaceindex = tmpcomment.indexOf(" ");
// 		comment = tmpcomment.substring(0,spaceindex) + " " + range + tmpcomment.substring(spaceindex);
		StringBuffer sb = null;
    if (spaceindex != -1 ) {
//   		sb = new StringBuffer(tmpcomment.substring(0,spaceindex) + " ");
      sb = new StringBuffer(tmpcomment.substring(0,spaceindex));
    } else {
      sb = new StringBuffer(tmpcomment);
    }
    if (sb != null) {
		  sb.append(range.toSuffixString());
		  if (feature.getSeqRange().isComplement()) {
			  sb.append("_REVERSE ");
		  }
      if (spaceindex != -1 ) {
		    sb.append(tmpcomment.substring(spaceindex) );
      }
		  comment = new String(sb);
    } else {
      comment = tmpcomment;
    }
		seq = entry.getSequence(range);
	}
	
/**
 * Creates FastaFile with the specified name from the whole specified Entry
 * @param path directory path
 * @param filename name of file
 * @param ent source of the sequence
 */
	public FastaFile (String path, String filename, Entry ent) {
		super(path, filename);
		this.entry = ent;
		seq = entry.getSequence();
		comment = entry.getComment();
		range = new SeqRange(1,entry.getLength());
	}
	
/**
 * Creates FastaFile with the specified File from the whole specified Entry
 * @param path directory path
 * @param filename name of file
 * @param ent source of the sequence
 */
	public FastaFile (File file, Entry ent) {
		super(file.getPath());
		this.entry = ent;
		seq = entry.getSequence();
		comment = entry.getComment();
		range = new SeqRange(1,entry.getLength());
	}
	
/**
 * Creates FastaFile with the specified File from the specified input stream
 * @param path directory path
 * @param filename name of file
 * @param is input stream source of the sequence in fasta format
 */
	public FastaFile (File file, InputStream is) {
		super(file.getPath());
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader ibr = new BufferedReader(isr);
		char buffer[] = new char[MAXSEQLEN];
		try {
			comment = ibr.readLine();
      System.out.println("Read " + comment);
			int r;
			int i=0;
			while ((r=ibr.read()) != EOF) {
				if ( r == '\n' ) r=ibr.read();
				buffer[i++] = (char) r;
			}
			seq = new String(buffer,0,i);
		} catch (IOException ioe) { System.out.println(ioe); }
	}
	
/**
 * Creates FastaFile with the specified File from the specified comment and sequence
 * @param File directory path
 * @param filename name of file
 * @param comment sequence comment
 * @param seq sequence
 */
	public FastaFile (String path, String filename, String comment, String seq) {
		super(path, filename);
		this.comment = comment;
		this.seq = seq;

	}
/**
 * Creates FastaFile for existing sequence file. 
 * To be able to read multi-sequence files
 * @param File directory path
 * @param filename name of file
 */
	public FastaFile (String path, String filename) {
		super(path, filename);
	}
	
/**
 * Writes sequence to file in fasta format
 */
	public void write() {
		FileWriter fw = null;
		try {
			fw = new FileWriter(this);
// // 			fw.write(comment+range+"\n");
// 			fw.write(comment+"\n");
// // 			fw.write(">" + entry.getFilename() + "_" + range.getStart() + "-" + range.getStop() + "\n");
// 			fw.write(seq.charAt(0));
// 			for (int i = 1; i < seq.length(); i++) {
// 				fw.write(seq.charAt(i));
// 				if (i%60 == 0) { fw.write("\n"); }
// 			}
// // 			fw.write(seq);
			if (ranges == null) {
				writeRoutine(fw, comment, seq);
			} else {
				int len = ranges.size();
				for (int i = 0; i < len; i++) {
					writeRoutine(fw, ((String)comments.elementAt(i)), ((String)seqs.elementAt(i)));
					fw.write("\n");
				}
			}
			fw.flush();
			fw.close();
		} catch (IOException ioe) { System.out.println(ioe); }
	}
	
	/**
	 * Does the actual writing
	 * @param param descr
	 */
	private void writeRoutine(FileWriter fw, String com, String sequence) throws IOException {
		try {
			fw.write(com+"\n");
// 			fw.write(">" + entry.getFilename() + "_" + range.getStart() + "-" + range.getStop() + "\n");
			fw.write(sequence.charAt(0));
			for (int i = 1; i < sequence.length(); i++) {
				fw.write(sequence.charAt(i));
				if (i%60 == 0) { fw.write("\n"); }
			}
		} catch (IOException ioe) { throw ioe; }
	}
/**
 * Masks the specified range in the sequence with Ns
 * @param r range to be masked
 */
	public void mask(SeqRange r) {
		if (r.overlap(range)) {
			int start = r.getStart() - range.getStart();
			if (start < 0) { start = 0; }
			int stoppos = r.getStop();
			if (stoppos > range.getStop()) { stoppos = range.getStop(); }
			int stop = stoppos - range.getStart();
			char[] seqArr = seq.toCharArray();
			for (int i = start; i <= stop; i++) {
				seqArr[i] = 'N';
			}
			seq = new String(seqArr);
		}
	}
	
	/**
	 * Maskes the ranges specified in the vector with Ns
	 * @param ranges Vector of SeqRanges to be masked
	 */
	public void maskRanges(Vector ranges) {
		Enumeration ren = ranges.elements();
		while (ren.hasMoreElements()) {
			SeqRange r = (SeqRange) ren.nextElement();
			mask(r);
		}
	}
	
	/**
	 * Sets the comment line of the Fasta file
	 * @param comm comment string
	 */
	public void setComment(String comm) {
		comment = comm;
	}
	
/**
 * Gets range
 * @return range
 */
	public SeqRange getRange() {
		return range;
	}

/**
 * Gets SequenceTokenizer to get sequences one at a time
 * @return tokenizer
 */
	public SequenceTokenizer sequences() {
    return new SequenceTokenizer(this);
	}
	
	public static void main (String[] arg) {
// 		File f = new File(arg[0]);
// 		Entry ent = null;
// 		try {
// 			ent = new Entry(f);
// 		} catch (FileNotFoundException fnf) { System.out.println(fnf); }
// 		SeqRange r = new SeqRange(1,100);
// 		FastaFile ff = new FastaFile(".", "myfile.tfa", ent, r);
// 		ff.write();
		FastaFile ff = new FastaFile(".", arg[0]);
		SequenceTokenizer st = ff.sequences();
    while (st.hasMoreElements()) {
      System.out.println("Sequence: " + st.nextSequence());
    }
	}
}
