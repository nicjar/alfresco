/*
 * $Revision: 1.1 $
 * $Id: CgffParser.java,v 1.1 2003/04/04 10:13:52 niclas Exp $
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
 * Class for parsing cgff files to Reciprocals. Also parses the gff file(s)
 * specied in the cgff file
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class CgffParser implements UsefulConstants {
/**
 * The entry pair holding the entries that features should be added to
 */
	EntryPair entries;
/**
 * The content of the file as Vector of lines
 */
	Vector lines;
/**
 * File
 */
	File cgffFile;
/**
 * PairStruct when using CORBA
 */
	alfresco.server.PairStruct pair;
/**
 * GffDataStruct when using CORBA
 */  
  alfresco.corba_wrappers.GffDataStruct gffstruct;
	
	/**
	 * Creates a CgffParser from the specified File
	 * @param f cgff file
	 */
	public CgffParser(EntryPair entp, File f) throws FileNotFoundException, IOException, FormatException {
		entries = entp;
		cgffFile = f;
		FileReader fr=null;
		try {
		  fr = new FileReader(cgffFile);
		} catch(FileNotFoundException fnf) { throw fnf;}
		try {
			parse(fr);
		} catch (IOException io) {throw io;}
		  catch (FormatException fe) {throw fe;}
// 			catch(FileNotFoundException fnf) { throw fnf;}
	}

// 	/**
// 	 * Creates a CgffParser from the specified InputStream
// 	 * @param is input stream
// 	 */
// 	public CgffParser(EntryPair entp, InputStream is) throws IOException, FormatException {
// 		entries = entp;
// 		InputStreamReader isr = new InputStreamReader(is);
// 		try {
// 			parse(isr);
// 		} catch (IOException io) {throw io;}
// 		  catch (FormatException fe) {throw fe;}
// 	}
	
	public CgffParser(EntryPair entp, alfresco.server.PairStruct p) throws IOException, FormatException {
		entries = entp;
		this.pair = p;
		StringReader sr = new StringReader(pair.cgff);
		try {
			parse(sr);
		} catch (IOException io) {throw io;}
		  catch (FormatException fe) {throw fe;}
// 			catch(FileNotFoundException fnf) { throw fnf;}
	}

	public CgffParser(EntryPair entp, alfresco.corba_wrappers.GffDataStruct gds) throws IOException, FormatException {
		entries = entp;
		this.gffstruct = gds;
		StringReader sr = new StringReader(gds.cgff);
		try {
			parse(sr);
		} catch (IOException io) {throw io;}
		  catch (FormatException fe) {throw fe;}
// 			catch(FileNotFoundException fnf) { throw fnf;}
	}
	
	/**
	 * Parses the input
	 * @param r Reader
	 */
	private void parse(Reader r) throws FileNotFoundException, IOException, FormatException {
		BufferedReader br = new BufferedReader(r);
		lines = new Vector();
		String inline = null;
		try {
		  while ((inline = br.readLine()) != null) {
				lines.addElement(inline);
		  }
		} catch(IOException ioe) { throw ioe;}
		if (lines.isEmpty()) return;
		Enumeration lineEnum = lines.elements();
		String line = (String) lineEnum.nextElement();
		int linenum = 1;
		String seqname1 = null;
		String seqname2 = null;
		while (line != null) {
			if (line.startsWith("//")) {
				line = (String) lineEnum.nextElement();
				linenum++;
				continue;
			} else if (line.startsWith("##")) {
				if (line.startsWith("##gff")) {
					if (pair == null && gffstruct == null) { // Cgff comes from a local file
						try {
							parseGff(line);
						} catch (FileNotFoundException fnfe) { throw fnfe; }
							catch (IOException io) {throw io;}
							catch (FormatException fe) {throw fe;}
					} else  {	// Cgff comes as a string in a CORBA struct
						parseGffCORBA(line);
					} 
				} else if (line.startsWith("##seq")) {
					//parseSeq(line);
					StringTokenizer st = new StringTokenizer(line);
					st.nextToken();
					seqname1 = st.nextToken();
					seqname2 = st.nextToken();
				}
				line = (String) lineEnum.nextElement();
				linenum++;
				continue;
				
			}
			if (seqname1 == null || seqname2 == null) 
				throw new FormatException(linenum);
			
			StringTokenizer st = new StringTokenizer(line);
			String f1 = st.nextToken();
			int start1 = Integer.parseInt(st.nextToken());
			int end1 = Integer.parseInt(st.nextToken());
			String f2 = st.nextToken();
			int start2 = Integer.parseInt(st.nextToken());
			int end2 = Integer.parseInt(st.nextToken());
			Entry ent1 = entries.getEntry(seqname1);
			Entry ent2 = entries.getEntry(seqname2);
			SeqFeature feature1 = ent1.getFeature(new SeqRange(start1, end1), f1);
			SeqFeature feature2 = ent2.getFeature(new SeqRange(start2, end2), f2);
// 			System.out.println(feature1 + " and " + feature2);
      if (feature1 == null) {
        Misc m1 = new Misc(ent1, new SeqRange(start1, end1));
        m1.setType(f1);
        feature1 = m1;
      }
      if (feature2 == null) {
        Misc m2 = new Misc(ent2, new SeqRange(start2, end2));
        m2.setType(f2);
        feature2 = m2;
      }
      // just to make sure
			if (feature1 == null || feature2 == null) {
        
				if(lineEnum.hasMoreElements()){
					line = (String) lineEnum.nextElement();
					linenum++;
					continue;
				} else {
					line = null;
				}
			}
			Reciprocal rec = new Reciprocal(feature1, feature2);
			entries.addReciprocal(rec);
			if(lineEnum.hasMoreElements()){
				line = (String) lineEnum.nextElement();
				linenum++;
			} else {
				line = null;
			}
		}
// 		entries.nudgeOverlappingGenes();
	}
	
	/**
	 * Parses gff defining line and creates new GffParser
	 * @param line the #gff line
	 */
	public void parseGff(String line) throws FileNotFoundException, IOException, FormatException{
		StringTokenizer st = new StringTokenizer(line);
		st.nextToken();
		while (st.hasMoreElements()) {
			String fname = st.nextToken();
			File f = new File(cgffFile.getParent(), fname);
			try {
				GffParser p = new GffParser(entries, f);
			} catch (FileNotFoundException fnfe) { throw fnfe; }
				catch (IOException io) {throw io;}
				catch (FormatException fe) {throw fe;}
		}
	}

	/**
	 * Parses gff defining line and creates new GffParser from a CORBA struct
	 * @param line the #gff line
	 */
	public void parseGffCORBA(String line) throws IOException, FormatException{
    ByteArrayInputStream bais = null;
    if (pair != null) {
		  bais = new ByteArrayInputStream(pair.gff.getBytes());
    } else if (gffstruct != null) {
      bais = new ByteArrayInputStream(gffstruct.gff.getBytes());
    }
		try {
			GffParser p = new GffParser(entries, bais);
		} catch (IOException io) {throw io;}
			catch (FormatException fe) {throw fe;}
	}

	
	/**
	 * Returns a gene from specified Entry with specified name
	 * @return Gene
	 * @param ent entry
	 * @param name gene name
	 */
	private Gene getGeneToBelongTo(Entry ent, String name) {
		Vector genes = ent.getGenes();
		Enumeration gen = genes.elements();
		while(gen.hasMoreElements()) {
			Gene g = (Gene) gen.nextElement();
			if (name.equals(g.getName())) {
				return g;
			}
		}
		Gene newGene = new Gene(ent, name);
		ent.addChild(newGene);
		return newGene;
	}

}

