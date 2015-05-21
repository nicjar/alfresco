/*
* $Revision: 1.1 $
* $Id: Tssw.java,v 1.1 2003/04/04 10:15:22 niclas Exp $
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
import java.awt.Rectangle;
import java.io.*;
import java.util.*;
/**
 * A class to call tssw and parse the result
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
public class Tssw {
/**
 * feature to run tssw on
 */
	SeqFeature feature;
/**
 * vector holding the result from tssw 
 */
	Vector lines;
	
	Hashtable promHash;
	
	/**
	 * Creates new Tssw object, calls tssw, and stores the result
	 * @return descr
	 * @param param descr
	 */
	public Tssw(SeqFeature f) {
		feature = f;
		lines = new Vector();
		FastaFile infile = new FastaFile(feature);
		Vector maskV = feature.getEntry().getRepeats();
		infile.maskRanges(maskV);
		infile.write();
		String command = SystemConstants.TSSWPATH + "tssw " + infile.getPath() + " " + infile.getPath() + ".tssw";
		Runtime rt = Runtime.getRuntime();
		try {
			System.out.println("calling " + command);
			Process p = rt.exec(command);
			p.waitFor();
			FileReader fr = new FileReader(new File(infile.getPath() + ".tssw"));
			BufferedReader br = new BufferedReader(fr);
			String line = null;
		  while ((line = br.readLine()) != null) {
				lines.addElement(line);
			}
// 			System.out.println("tssw file read: " + lines.size() + " lines");
		} catch (Exception e) { e.printStackTrace(); }
		parse();
	}
	
	/**
	 * Description
	 * @return descr
	 * @param param descr
	 */
	public void parse() {

		Enumeration len = lines.elements();
		String line = null;
		while (len.hasMoreElements()) {
			line = (String) len.nextElement();
			if (line.indexOf("Pos") > 0) {
// 				System.out.println("Found \"Pos.:\" line.");
				break;
			}
		}
		// get promoter posistions, create promoters, TATA, and TrxStarts
		promHash = new Hashtable();
		int featureStart = feature.getSeqRange().getStart() - 1 ;
		while (len.hasMoreElements() && line.indexOf("Pos.:") > 0) {
			StringTokenizer st = new StringTokenizer(line);
			String crap = st.nextToken();
			String startS = st.nextToken();
			crap = st.nextToken();
			String score = st.nextToken();
			Promoter pr = new Promoter(feature.getEntry());
			int start = Integer.parseInt(startS) + featureStart;
			TrxStart trxs = new TrxStart(feature.getEntry(), start, start);
			pr.addChild(trxs);
			if(st.hasMoreTokens()) {
				String tataPos = null;
				for (int i = 0; i < 5; i++) tataPos = st.nextToken();
				int pos = Integer.parseInt(tataPos) + featureStart + 1;
				TATA tata = new TATA(feature.getEntry(), pos, pos + 4);
				pr.addChild(tata);
			}
			promHash.put(startS, pr);
			line = (String) len.nextElement();
		}
		if (promHash.isEmpty()) return;
// 		System.out.println(promHash.size() + " promoters added");
// 		System.out.println("line: " + line);
// 		line = (String) len.nextElement(); // get crap line
		// get TFBS positions
		PROM: while (len.hasMoreElements()) {
// 			line = (String) len.nextElement();
// 			if (line.indexOf("for promoter at position") > 0) {
			if (line.indexOf("promoter") > 0) {
// 				System.out.println("\"for promoter at position\" line found");
				StringTokenizer st = new StringTokenizer(line);
				String posS = null;
				for (int i = 0; i < 6; i++) posS = st.nextToken();
				// get position and appropriate promoter object
// 				System.out.println("promoter at " + posS);
				Promoter pr = (Promoter) promHash.get(posS);
// 				System.out.println("TFBS for " + pr);
				while (len.hasMoreElements()) {
					line = (String) len.nextElement();
					if (line.indexOf("promoter") > 0) continue PROM;
					// parse each TFBS line and add TFBS to Promoter
					StringTokenizer tfst = new StringTokenizer(line);
					String pos = tfst.nextToken();
					String or = tfst.nextToken();
					boolean compl = or.indexOf("-") > 0? true: false;
					String name = tfst.nextToken();
					if (name.startsWith("Y$") ||	//yeast
							name.startsWith("DROME$") ||	//drosophila
							name.startsWith("AD$") ||	//adenovirus
							name.startsWith("PA$") ||	//polyomavirus
							name.startsWith("SV$") ||	//SV40
							name.startsWith("EBV$") ||	//EBV
							name.startsWith("MOMLV$") ||	//MoMULV
							name.startsWith("MULV$") ||	//MULV
							name.startsWith("E74A$") ||	//drosophila factor
							name.startsWith("WHEAT$") ||	//wheat
							name.startsWith("OAT$") ||	//oat
							name.startsWith("MAIZE$") ||	//maize
							name.startsWith("RICE$")	//rice
							) continue;
					String seq = tfst.nextToken();
// 					System.out.println(pos + "\t" + or + "\t" + name + "\t" + seq + "\t" + seq.length());
					int tfstart = Integer.parseInt(pos) + featureStart;
					int tflen = seq.length() - 1;
					SeqRange sr = null;
					if(compl) {
						sr = new SeqRange(tfstart - tflen, tfstart, compl);
					} else {
						sr = new SeqRange(tfstart, tfstart + tflen, compl);
					}
					TFBS tf = new TFBS(feature.getEntry(), sr);
					tf.predicted(true);
					tf.setPredMethod("tssw");
					tf.setId(name);
					tf.setConsensus(seq);
					tf.setVisible(false);
					nudge(tf, pr);
					pr.addChild(tf);
					// yuck!
					((EntryPair)feature.getEntry().getParent()).wc.getTFBSDialog().addTFBS(name);
				}

			}
			if (!len.hasMoreElements()) return;
			line = (String) len.nextElement();
// 			System.out.println("line: " + line);
		}

	}
	
	/**
	 * Gets vector of promoter objects
	 * @return promoter vector
	 */
	public Vector getPromoters() {
		Enumeration keys = promHash.keys();
		Vector proms = new Vector(promHash.size());
		while (keys.hasMoreElements()) {
			proms.addElement(promHash.get(keys.nextElement()));
		}
		return proms;
	}
	
	/**
	 * Adds the promoters to the entry object
	 */
	public void addPromotersToEntry() {
		feature.getEntry().addChildren(getPromoters());
	}
	
	/**
	 * Description
	 * @return descr
	 * @param param descr
	 */
	public void nudge(TFBS tf, Promoter pr) {
// 		Enumeration en = pr.getChildEnumeration();
		Vector children = pr.getChildren();
		for(int i = children.size() -1; i >= 0; i--) {
// 		while(en.hasMoreElements()) {
			Object o = children.elementAt(i);
// 			Object o = en.nextElement();
			if (o instanceof TFBS) {
				TFBS currTf = (TFBS) o;
					if(tf.overlap(currTf)) {
// 						System.out.println(tf.getSeqRange() + (tf.complement?" complement,":" not complement,") + " tier " + tf.getTier() + 
// 															" overlaps " + 
// 															currTf.getSeqRange() + (currTf.complement?" complement,":" not complement,") + " tier " + currTf.getTier());
						tf.setTier(Math.abs(tf.getTier()) + 1);
						Rectangle r = tf.getBounds();
// 						System.out.println("New tier set to " + tf.getTier()+ ", rect: " + r);
// 					} else {
// 						System.out.println("\t" + tf.getSeqRange() + (tf.complement?" complement,":" not complement,") + " tier " + tf.getTier() + 
// 															" doesn't overlap " + 
// 															currTf.getSeqRange() + (currTf.complement?" complement,":" not complement,") + " tier " + currTf.getTier());
// 						
					}
			}
		}
// 		System.out.println();
	}
// 	/**
// 	 * Description
// 	 * @return descr
// 	 * @param param descr
// 	 */
// 	public static void main(String[] args) {
// 		try {
// 			File f = new File(args[0]);
// 			Entry ent = new Entry(f);
// 			SeqRange sr = new SeqRange(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
// 			Misc m = new Misc(ent, sr);
// 			Tssw tssw = new Tssw(m); 
// 		} catch (Exception e)  { e.printStackTrace(); }
// 	}
}
