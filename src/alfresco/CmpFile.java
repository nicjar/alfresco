/*
 * $Revision: 1.1 $
 * $Id: CmpFile.java,v 1.1 2003/04/04 10:13:53 niclas Exp $
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
 * Represents a .cmp file.<p>
 * @see java.io.File
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class CmpFile extends File {
/**
 * Holds the content of the file	
 */
	Vector lines;	
/**
 * ReciprocalSet to hold reciprocal cds's
 */
	ReciprocalSet reciprocals;

/**
 * Creates CmpFile object
 * @param path directory path
 * @param name filename
 */ 
	CmpFile(String path, String name) {
		super(path, name);
	}

/**
 * Opens the file and reads the content into the lines Vector
 * @exception 	java.io.FileNotFoundException
 */
	public void open() throws FileNotFoundException {
		if (this.exists()) {
			lines = new Vector();
			FileReader fr=null;
			try {
		  	fr = new FileReader(this);
			} catch(FileNotFoundException fnf) {System.out.println(fnf);}
			try {
		  	while (fr.ready()) {
		  		char[] buff = new char[200];
		  		int i = 0;
		  		char s = (char) fr.read();
		  		while( s != '\n' ) {
					buff[i++] = s;
					s = (char) fr.read();
					// System.out.print(s);
				}
				lines.addElement(new String(buff));
		  	}
			} catch(IOException ioe) {System.out.println(ioe);}
			return;
		}
		throw new FileNotFoundException();
	}

/**
 * Parses the content of the lines Vector. Looks for sequence files if Entries 
 * aren't set, and creates
 * Entry objects and Gene, CDS, Exon, UTR, Repeat, and Intron objects as 
 * specified in the .cmp file. The two Entry obj are added to the EntryPair 
 * parameter.
 * @param ep EntryPair to hold Entries specified in file
 */	
	public void parse(EntryPair ep) throws FileNotFoundException{
		Gene gene1;
		Gene gene2;
		CDS cds1;
		CDS cds2;
		String genename1;
		String genename2;
		String id1 = null;
		String id2 = null;
		String ac1;
		String ac2;
		String line, crap;
		StringTokenizer words;
		Enumeration filelines = lines.elements();
		while (filelines.hasMoreElements() ) {
			StringTokenizer st = new StringTokenizer( (String) filelines.nextElement(), "\t");
			while (st.hasMoreElements() ) {
				String ft = (String) st.nextElement();
				if (ft.equals("ID")) {
					id1 = (String) st.nextElement();
					id2 = (String) st.nextElement();
					id2 = id2.trim();
					int spind = id2.indexOf(" ");
					if (spind >= 0) {
						id2 = id2.substring(0,spind);
					}
					int ci1 = -1;
					int ci2 = -1;
					ci1 = id1.indexOf(":");
					if (ci1 >= 0 ) { id1 = id1.substring(ci1+1, id1.length()); }
					ci2 = id2.indexOf(":");
					if (ci2 >= 0) { id2 = id2.substring(ci2+1, id2.length()); }
				} else if (ft.equals("AC")) {
					ac1 = (String) st.nextElement();
					ac2 = (String) st.nextElement();
					ac2 = ac2.trim();
				}
			}
		}
		if (!ep.entriesSet()) { // have to set entry1 and entry2 before parsing CDSs
			File f1 = new File(this.getParent(), id1);
			File f2 = new File(this.getParent(), id2);
			// handle errors here or maybe inside Entry()...
			ep.setEntry1(new Entry(f1));
// 			ep.entry1.maskN();
			ep.setEntry2(new Entry(f2));
// 			ep.entry2.maskN();
		}
// 		reciprocals = new ReciprocalSet(ep);
		reciprocals = ep.reciprocals;
// 		ep.setReciprocals(reciprocals);
		Entry ent1 = ep.entry1;
		Entry ent2 = ep.entry2;
		filelines = lines.elements();
		try {
		line = (String) filelines.nextElement();
		TOP: while (line != null) {
			if (line.startsWith("GENE")) {
					GENE: while (line.startsWith("GENE")) {
						words = new StringTokenizer(line,"\t");
						crap = (String) words.nextElement();
						genename1 = (String) words.nextElement();
						genename2 = (String) words.nextElement();
						genename2 = genename2.trim();
						int cindx1 = genename1.indexOf(",");
						int cindx2 = genename2.indexOf(",");
						if (cindx1 != -1) {
							genename1 = genename1.substring(0, cindx1);
						}
						if (cindx2 != -1) {
							genename2 = genename2.substring(0, cindx2);
						}
						gene1 = new Gene(ent1, genename1);
						gene2 = new Gene(ent2, genename2);
						ent1.addChild(gene1);
						ent2.addChild(gene2);
// 						cds1 = new CDS (ent1);
// 						cds2 = new CDS (ent2);
// 						gene1.addChild(cds1);
// 						gene2.addChild(cds2);
						if (filelines.hasMoreElements() ) {
							line = (String) filelines.nextElement();
						}
						while (line != null) {
							if (line.startsWith("CDS")) {
								SeqRange ranges[] = parseLine(line);
								SeqRange r1 = ranges[0];
								SeqRange r2 = ranges[1];
								Exon exon1 = new Exon(ep.entry1, r1);
								Exon exon2 = new Exon(ep.entry2, r2);
// 								cds1.addChild(exon1);
// 								cds2.addChild(exon2);
								gene1.addChild(exon1);
								gene2.addChild(exon2);
								reciprocals.addChild(new Reciprocal(exon1, exon2));
							} else if (line.startsWith("PROM")) {
								// deal with it
							} else if (line.startsWith("UPST")) {
								// deal with it
							} else if (line.startsWith("5UTR") || line.startsWith("3UTR")) {
								// deal with it
								SeqRange ranges[] = parseLine(line);
								SeqRange r1 = ranges[0];
								SeqRange r2 = ranges[1];
								UTR utr1 = new UTR(ep.entry1, r1);
								UTR utr2 = new UTR(ep.entry2, r2);
								gene1.addChild(utr1);
								gene2.addChild(utr2);
								ep.reciprocals.addChild(new Reciprocal(utr1, utr2));
								
							} else if (line.startsWith("INT")) {
								SeqRange ranges[] = parseLine(line);
								SeqRange r1 = ranges[0];
								SeqRange r2 = ranges[1];
								Intron intr1 = new Intron(ep.entry1, r1);
								Intron intr2 = new Intron(ep.entry2, r2);
								gene1.addChild(intr1);
								gene2.addChild(intr2);
								Reciprocal rec = new Reciprocal(intr1, intr2);
								rec.setVisible(false);
								reciprocals.addChild(rec);
							} else if (line.startsWith("GENE")) {
								line = (String) filelines.nextElement();
								continue GENE;
							}	else {
								line = (String) filelines.nextElement();
								continue TOP;
							}
							// read next line for loop
							line = (String) filelines.nextElement();
						}
					}
			}
			if (line.startsWith("REP")) {
				SeqRange ranges[] = parseLine(line);
				SeqRange r1 = ranges[0];
				SeqRange r2 = ranges[1];
				if (r1 != null) {
					Repeat rep1 = new Repeat(ep.entry1, r1);
					ep.entry1.addChild(rep1);
				}
				if (r2 != null) {
					Repeat rep2 = new Repeat(ep.entry2, r2);
					ep.entry2.addChild(rep2);
					//System.out.println("Added " + rep2 + " to " + ep.entry2);
				}
			}
			line = (String) filelines.nextElement();
		}		
		} catch (NoSuchElementException e) { } // just ignore ;-)
	}

/**
 * Parses start,stop String to SeqRange object
 * @return SeqRange object
 * @param s the String start,stop
 */
	private SeqRange parseStartStop(String s) {
		boolean compl = false;
		StringTokenizer st;
		String startString, stopString;
		SeqRange r=null;
		if (!s.startsWith("---")) {
			st = new StringTokenizer(s, ",");
			startString = (String) st.nextElement();
			if (startString.startsWith(">") || startString.startsWith("<"))
				startString = startString.substring(1);
			if (startString.startsWith("c")) {
				startString = startString.substring(1);
				compl = true;
			}
			stopString = (String) st.nextElement();
			if (stopString.startsWith(">") || stopString.startsWith("<"))
				stopString = stopString.substring(1);
			if (stopString.startsWith("c")) {
				stopString = stopString.substring(1);
				compl = true;
			}			
			r = new SeqRange(Integer.parseInt(startString), Integer.parseInt(stopString), compl);
		}
		return r;
	}	

/**
 * Parses a line with two ranges
 * @return an array of two SeqRange objects
 * @param line the line to be parsed	
 */
	private SeqRange[]	parseLine(String line) {
		StringTokenizer words = new StringTokenizer(line,"\t");
		String id = (String) words.nextElement();
		String seq1 = (String) words.nextElement();
		String seq2 = (String) words.nextElement();
		seq2 = seq2.trim();
		SeqRange r1 = parseStartStop(seq1);
		SeqRange r2 = parseStartStop(seq2);
		SeqRange rangeArray[] = {r1,r2};
		return rangeArray;
	}
	
}


