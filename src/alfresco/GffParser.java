/*
 * $Revision: 1.1 $
 * $Id: GffParser.java,v 1.1 2003/04/04 10:14:30 niclas Exp $
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
 * Class for parsing gff files to SeqFeatures or CompositeSeqFeatures
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class GffParser {
/**
 * The entry pair holding the entries that features should be added to
 */
	EntryPair entries;
/**
 * The content of the file as Vector of lines
 */
	Vector lines;
/**
 * A hashtable holding the features found	
 */
	Hashtable features;
	
	/**
	 * Creates a GffParser from the specified File
	 * @param f gff file
	 */
	public GffParser(EntryPair entp, File f) throws FileNotFoundException, IOException, FormatException {
		entries = entp;
		FileReader fr=null;
		try {
		  fr = new FileReader(f);
		} catch(FileNotFoundException fnf) { throw fnf;}
		try {
			parse(fr);
		} catch (IOException io) {throw io;}
		  catch (FormatException fe) {throw fe;}
			
	}

	/**
	 * Creates a GffParser from the specified InputStream
	 * @param is input stream
	 */
	public GffParser(EntryPair entp, InputStream is) throws IOException, FormatException {
		entries = entp;
		InputStreamReader isr = new InputStreamReader(is);
		try {
			parse(isr);
		} catch (IOException io) {throw io;}
		  catch (FormatException fe) {throw fe;}
	}
	
	/**
	 * Creates a GffParser from the specified String
	 * @param s input string
	 */
	public GffParser(EntryPair entp, String s) throws IOException, FormatException {
		entries = entp;
    if (s == null) return;
		StringReader sr = new StringReader(s);
		try {
			parse(sr);
		} catch (IOException io) {throw io;}
		  catch (FormatException fe) {throw fe;}
	}
	
	/**
	 * Parses the input
	 * @param r Reader
	 */
	private void parse(Reader r) throws IOException, FormatException {
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
// 		while (lineEnum.hasMoreElements()) {
		while (line != null) {
			if (line.startsWith("##") || line.startsWith("//")) {
				if (lineEnum.hasMoreElements() ) {
					line = (String) lineEnum.nextElement();
					linenum++;
					continue;
				} else {
					line = null;
					continue;
				}
				
			}
			String seqname = null;
			String source = null;
			String feature = null;
			int start = 0;
			int end = 0;
			double score = 0.0;
			String strand = null;
			int frame = -1;
			String group = null;
// 			String line = (String) lineEnum.nextElement();
// 			linenum++;
// 			StringTokenizer st = new StringTokenizer(line, "\t"); // tokenize on tab
			StringTokenizer st = new StringTokenizer(line); // tokenize on white space
			if (st.hasMoreElements()) {
				try {
					seqname = st.nextToken();
					source = st.nextToken();
					source = source.toLowerCase();
					feature = st.nextToken();
					feature = feature.toLowerCase();
					start = Integer.parseInt(st.nextToken());
					end = Integer.parseInt(st.nextToken());
					score = Double.valueOf(st.nextToken()).doubleValue();
					strand = st.nextToken();
					String frameStr = st.nextToken();
					if (!frameStr.equals(".")) {
						frame = Integer.parseInt(frameStr);
					}
// 					if (st.hasMoreElements()) {
// 						group = st.nextToken();
// 					}
					StringBuffer gsb = null;
					if (st.hasMoreElements()) {
						gsb = new StringBuffer();
						gsb.append(st.nextToken());
						while (st.hasMoreElements()) {
							gsb.append(" " + st.nextToken());
						}
					}
					if (gsb != null) {
						group = gsb.toString();
					}
				} catch (NumberFormatException nfe) { 
					System.out.println(nfe);
					continue; 
				} catch (NoSuchElementException nse) {
					System.out.println("line: " + linenum);
					nse.printStackTrace();
				}
				
			}
			// Things to ignore
			if (feature.equals("sequence") || feature.equals("atg") || feature.equals("match")) {
				if (lineEnum.hasMoreElements()) {
					line = (String) lineEnum.nextElement();
					linenum++;
				} else {
					line = null;
				}
				continue;
			}
			// Fix name of exons if source is genewise
      if (source.indexOf("genewise")!= -1) {
        if (feature.indexOf("cds") != -1) {
          feature = "exon";
        }
      }
      
			// get entry
			int cind = seqname.indexOf(":");
			if (cind != -1 ) {
				seqname = seqname.substring(cind+1);
//				System.out.println(seqname);
			}
			Entry ent = entries.getEntry(seqname);
			
			
			if (ent == null) {
				throw new FormatException(linenum, line);
			}
			// create feature, try to find out if it could fit Exon, Intron
			// etc., otherwise set to Misc(?) with name=feature
			// set all attributes
			// check if feature belongs to gene, if so, if that gene has 
			// been created before ie look in entry to get gene and add feature
			// to it. addChild should be changed so that Exons and Introns are added to a CDS
			// instead of directly to the Gene
			// Methods needed:
			// Entry.getGenes()
			// Gene.addChild();
			// 
			
			boolean reverse = strand.equals("-")? true: false;
			SeqRange range = new SeqRange(start, end, reverse);
			SeqFeature sf = null;
			if (feature.indexOf("exon") != -1) {
				sf = new Exon(ent, range);
        if (source.indexOf("genscan") != -1) {
          sf.predicted(true);
          sf.setPredMethod("Genscan");
          sf.setScore(score);
        }
        if (source.indexOf("genewise") != -1) {
          sf.predicted(true);
          sf.setPredMethod("GeneWise");
          sf.setScore(score);
        }
        if (source.indexOf("est_genome") != -1) {
          sf.predicted(true);
          sf.setPredMethod("est_genome");
          sf.setScore(score);
        }
        if (source.indexOf("spidey") != -1) {
          sf.predicted(true);
          sf.setPredMethod("spidey");
          sf.setScore(score);
        }
        if (source.indexOf("sim4") != -1) {
          sf.predicted(true);
          sf.setPredMethod("sim4");
          sf.setScore(score);
        }
				if (group != null) {
					getGeneToBelongTo(ent, group).addChild(sf);
					if (lineEnum.hasMoreElements()) {
						line = (String) lineEnum.nextElement();
						linenum++;
					} else {
						line = null;
					}
					continue;
				} else {
          sf.setTier(4);
          ent.nudgeUp(sf);
        }
			} else if (feature.indexOf("cds") != -1) {
				sf = new Exon(ent, range);
				if (group != null) {
					getGeneToBelongTo(ent, group).addChild(sf);
					if (lineEnum.hasMoreElements()) {
						line = (String) lineEnum.nextElement();
						linenum++;
					} else {
						line = null;
					}
					continue;
				}
			} else if (feature.indexOf("utr") != -1){
				sf = new UTR(ent, range);
				if (group != null) {
					getGeneToBelongTo(ent, group).addChild(sf);
					if (lineEnum.hasMoreElements()) {
						line = (String) lineEnum.nextElement();
						linenum++;
					} else {
						line = null;
					}
					continue;
				}
			} else if (feature.indexOf("intron") != -1){
				sf = new Intron(ent, range);
				if (group != null) {
					getGeneToBelongTo(ent, group).addChild(sf);
					if (lineEnum.hasMoreElements()) {
						line = (String) lineEnum.nextElement();
						linenum++;
					} else {
						line = null;
					}
					continue;
				}
			} else if (feature.indexOf("repeat") != -1){
				if(source.indexOf("repeatmasker") != -1) {
					if (group != null) {
						if (group.indexOf("LINE") != -1) {
							sf = new LINERepeat(ent, range);
						} else if (group.indexOf("SINE") != -1) {
							sf = new SINERepeat(ent, range);						
						} else if (group.indexOf("LTR") != -1) {
							sf = new LTRRepeat(ent, range);
						} else if (group.indexOf("Simple") != -1) {
							sf = new SimpleRepeat(ent, range);
						} else if (group.indexOf("DNA") != -1) {
							sf = new DNAtranspRepeat(ent, range);
						} else if (source.indexOf("complexity") != -1) {
							sf = new LowComplexityRepeat(ent, range);
						} else {
							sf = new Repeat(ent, range);
						}							
					} else {
            sf = new Repeat(ent, range);
          }
				} else { // This could be fleshed out to try to determine what type of repeat
					sf = new Repeat(ent, range);
//           System.out.println("unknown repeat" + feature);
				}
				// things common for repeats
				if (group != null) {
					Repeat rep = (Repeat) sf;
					if (group.indexOf(",") != -1) {
// 								System.out.println(group);
						StringTokenizer gst = new StringTokenizer(group, ",");
						String type = gst.nextToken();
						String name = gst.nextToken();
						rep.setAttributes(name, type);
					}
				}
			} else if (feature.indexOf("feature") != -1) {	//Duuh! RepeatMasker, Grail, ...
				if(source.indexOf("repeatmasker") != -1) {
					if (group.indexOf("LINE") != -1) {
						sf = new LINERepeat(ent, range);
					} else if (group.indexOf("SINE") != -1) {
						sf = new SINERepeat(ent, range);						
					} else if (group.indexOf("LTR") != -1) {
						sf = new LTRRepeat(ent, range);
					} else if (group.indexOf(")n") != -1) {
						sf = new SimpleRepeat(ent, range);
					} else if (group.indexOf("DNA") != -1) {
						sf = new DNAtranspRepeat(ent, range);
					} else {
						sf = new Repeat(ent, range);
					}
					if (group != null) {
						Repeat rep = (Repeat) sf;
						if (group.indexOf(",") != -1) {
	// 								System.out.println(group);
							StringTokenizer gst = new StringTokenizer(group, ",");
							String type = gst.nextToken();
							String name = gst.nextToken();
							rep.setAttributes(name, type);
						} else if (group.indexOf(")n") != -1) {
							rep.setAttributes(group, "Simple_Repeat");
						}
					}
				} else if(source.indexOf("complexity") != -1) {
					sf = new LowComplexityRepeat(ent, range);
					if (group.indexOf(",") != -1) {
						Repeat rep = (Repeat) sf;
						rep.setAttributes(group, "Low_complexity");
					}
				} else if (source.indexOf("grail") != -1) {
					sf = new Exon(ent, range);
					sf.predicted(true);
					sf.setTier(8);
// 					if(sf.getTier() > 0) {
// 						sf.setTier(8);
// 					} else {
// 						sf.setTier(-8);
// 					}
				} else if (source.indexOf("cpg") != -1) {
					CpGIsland cpg = new CpGIsland(ent, range);
					if (group != null){
						cpg.setInfo(group);
					}
					sf = cpg;
					sf.setTier(7);
// 					if(sf.getTier() > 0) {
// 						sf.setTier(7);
// 					} else {
// 						sf.setTier(-7);
// 					}
				} else {
					sf = new Misc(ent, range);
					Misc m = (Misc) sf;
					m.setType(source);
				}
			} else if (feature.indexOf("cpg") != -1) {
				CpGIsland cpg = new CpGIsland(ent, range);
				if (group != null){
					cpg.setInfo(group);
				}
				sf = cpg;
				sf.setTier(4);
// 					System.out.println("Added CpG island at tier " + sf.getTier());
			} else if (feature.indexOf("similarity") != -1){
				sf = new Similarity(ent, range);
				if (group != null) {
					Similarity sim = (Similarity) sf;
					sim.setMatch(group);
				}
				sf.setTier(1);
			} else if (feature.indexOf("polya") != -1){
// 				System.out.println("Found PolyA site in gff file");
        sf = new PolyA(ent, range);
				if (group != null) {
					getGeneToBelongTo(ent, group).addChild(sf);
        }
			} else if (feature.indexOf("dbablock") != -1){
				sf = new DbaBlock(ent, range);
			} else if (feature.indexOf("hcr") != -1){
				sf = new HCR(ent, range);
				if (group != null) {
					StringTokenizer hst = new StringTokenizer(group, ",");
					((HCR) sf).setLevel(Integer.parseInt(hst.nextToken()));
					((HCR) sf).setId(Double.valueOf(hst.nextToken()).doubleValue());
					((HCR) sf).setGaps(Integer.parseInt(hst.nextToken()));
				}
			} else if (feature.indexOf("tfbs") != -1){
				sf = new TFBS(ent, range);
        sf.setTier(2);
			} else {
				sf = new Misc(ent, range);
				Misc m = (Misc) sf;
				m.setType(feature);
			}
			// things common for all SeqFeatures
// 			System.out.println(sf);
//       System.out.println("Source: " + source);
			sf.setPredMethod(source);
			sf.setScore(score);
			
			ent.addChild(sf);
			if(lineEnum.hasMoreElements()){
				line = (String) lineEnum.nextElement();
				linenum++;
			} else {
				line = null;
			}
		}
		entries.nudgeOverlappingGenes();
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

