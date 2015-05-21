/*
 * $Revision: 1.1 $
 * $Id: Genscan.java,v 1.1 2003/04/04 10:14:26 niclas Exp $
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
import java.lang.reflect.*;

/**
 * A class to call genscan and parse the result
 * @see alfresco.UsefulConstants
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class Genscan extends Thread{
/**
 * Entry to run genscan on
 */
	Entry entry;
/**
 * Sequence fasta file	
 */  
  File seqFile;
  
  String seqName;
/**
 * Output result from genscan
 */
	Vector result;
/**
 * Vector for predicted genes	
 */
	Vector genes;
	
  String gff;
  Properties properties;
  
  static String[] executables = { "genscan", "HumanIso.smat" }; // HuamnIso.smat isn't really an executable, but the path has to be defined
  static String subopt = "1.0";

  
  /**
  * Description
  * @return descr
  * @param param descr
  */
  public Genscan(Entry ent, Properties props) {
		entry = ent;
    seqName = ent.getFilename();
		result = new Vector();
		Class simpleRepeat = null;
		try {
			simpleRepeat = Class.forName("alfresco.SimpleRepeat");
		} catch (ClassNotFoundException cnfe) { System.out.println(cnfe); }
		Vector maskV = entry.getRepeats(simpleRepeat);
		FastaFile maskedf = new FastaFile(SystemConstants.TMPDIR, entry.getFilename(), entry);
		maskedf.maskRanges(maskV);
		maskedf.write();
    seqFile = maskedf;
    properties = props;
  }

  /**
  * Description
  * @return descr
  * @param param descr
  */
  public Genscan(File file, Properties props) {
		result = new Vector();
		seqFile =  file;
    seqName = seqFile.getName();
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
    hash.put("subopt",subopt);
    return hash;
  }
  
  /**
   * Description
   * @return descr
   * @param param descr
   */
  public void run() {
// 		String command = SystemConstants.GENSCANPATH + "genscan " + SystemConstants.GENSCANPATH + "HumanIso.smat " + seqFile.getPath() + " -subopt 0.02";
		String command = properties.getProperty("genscan") + " " + properties.getProperty("HumanIso.smat") + " " + seqFile.getPath() + " -subopt " + properties.getProperty("subopt");
		Runtime rt = Runtime.getRuntime();
		try {
			System.out.println("calling " + command);
			Process p = rt.exec(command);
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
			while (outline != null && !outline.startsWith("Gn.Ex")) {
				outline = stdoutbr.readLine();
			}
			outline = stdoutbr.readLine();
			outline = stdoutbr.readLine();
			while (outline != null && !outline.startsWith("Predicted peptide")) {
				if(!outline.equals("") && outline.indexOf("optimal") < 0 && outline.indexOf("Type") < 0  && outline.indexOf("---") < 0  && outline.indexOf("NO") < 0) {
// 					String line = outline.substring(1);
					String line = outline;
// 					result.addElement(outline);
					result.addElement(line);
				}
				outline = stdoutbr.readLine();
			}
		}catch (IOException ioe) { System.out.println(ioe); }
			catch (InterruptedException ie) { System.out.println(ie); }
		Enumeration renum = result.elements();
    GffHeader gh = new GffHeader();
    String ghs = gh.getHeader();
//     System.out.println("Gff header:\n" + ghs);
    StringBuffer gffb = new StringBuffer(ghs);
		while (renum.hasMoreElements()) {
			String line = (String) renum.nextElement();
// 			System.out.println("Line " + l++ + ": " + line);
			Hashtable values = parseLine(line);
// 			System.out.println("Type: " + values.get("type"));
			String type = (String) values.get("type");
			if (type.equals("Promoter")) continue;
      gffb.append(seqName + "\tGenscan\t" + type);
      gffb.append("\t" + values.get("start").toString() + "\t" + ((Integer)values.get("stop")).toString());
      gffb.append("\t" + (type.equals("Exon")?values.get("prob").toString():"0") + "\t" + values.get("strand").toString());
      gffb.append("\t" + (type.equals("Exon")?values.get("frame").toString():"."));
      int gennum = ((Integer)values.get("gennum")).intValue();
      if (gennum > 0) {
        gffb.append("\t" + "Predicted gene " + gennum);
      }
      gffb.append("\n");
    }
    gff = new String(gffb);
//     System.out.println(gff);
  }
  
  /**
   * Returns result gff String
   * @return gff String
   */
  public String getGff() {
    return gff;
  }
  
// 	/**
// 	 * Creates a Genscan object, calls genscan, and stores the result
// 	 * (NOTE! Found exons are added directly to Gene, they should be added to a CDS first)
// 	 * @param ent Entry to run genscan on and add predicted exons to
// 	 */
// 	public Genscan(Entry ent) {
// 		entry = ent;
// 		result = new Vector();
// 		Class simpleRepeat = null;
// 		try {
// 			simpleRepeat = Class.forName("alfresco.SimpleRepeat");
// 		} catch (ClassNotFoundException cnfe) { System.out.println(cnfe); }
// 		Vector maskV = entry.getRepeats(simpleRepeat);
// 		FastaFile maskedf = new FastaFile(SystemConstants.TMPDIR, entry.getFilename(), entry);
// 		maskedf.maskRanges(maskV);
// 		maskedf.write();
// // 		String command = GENSCANPATH + "genscan " + GENSCANPATH + "HumanIso.smat " + path + "/" + filename;
// // 		String command = GENSCANPATH + "genscan " + GENSCANPATH + "HumanIso.smat " + entry.getEntryFile().getPath() + " -subopt 0.01";
// 		String command = SystemConstants.GENSCANPATH + "genscan " + SystemConstants.GENSCANPATH + "HumanIso.smat " + maskedf.getPath() + " -subopt 0.02";
// 		Runtime rt = Runtime.getRuntime();
// 		try {
// 			System.out.println("calling " + command);
// 			Process p = rt.exec(command);
// 			InputStream stdout = p.getInputStream();
// 			InputStreamReader stdoutr = new InputStreamReader(stdout);
// 			BufferedReader stdoutbr = new BufferedReader(stdoutr);
// // 			InputStream stderr = p.getInputStream();
// // 			InputStreamReader serrr = new InputStreamReader(stderr);
// // 			BufferedReader errbr = new BufferedReader(serrr);
// 			p.waitFor();
// // 			String errline = errbr.readLine();
// // 			System.out.println(errline);
// 			String outline = stdoutbr.readLine();
// 			while (outline != null && !outline.startsWith("Gn.Ex")) {
// 				outline = stdoutbr.readLine();
// 			}
// 			outline = stdoutbr.readLine();
// 			outline = stdoutbr.readLine();
// 			while (outline != null && !outline.startsWith("Predicted peptide")) {
// 				if(!outline.equals("") && outline.indexOf("optimal") < 0 && outline.indexOf("Type") < 0  && outline.indexOf("---") < 0) {
// // 					String line = outline.substring(1);
// 					String line = outline;
// // 					result.addElement(outline);
// 					result.addElement(line);
// 				}
// 				outline = stdoutbr.readLine();
// 			}
// 		}catch (IOException ioe) { System.out.println(ioe); }
// 			catch (InterruptedException ie) { System.out.println(ie); }
// 		genes = new Vector();
// 		Vector cdsChildren = null;
// 		Vector otherChildren = null;
// 		boolean compl = false;
// 		Enumeration renum = result.elements();
// 		int currentgene = 0;
// 		int l = 1;
// 		while (renum.hasMoreElements()) {
// 			String line = (String) renum.nextElement();
// // 			System.out.println("Line " + l++ + ": " + line);
// 			Hashtable values = parseLine(line);
// // 			System.out.println("Type: " + values.get("type"));
// 			String type = (String) values.get("type");
// 			if (type.equals("Promoter")) continue;
// 			Integer gnI = (Integer) values.get("gennum");
// 			if (gnI.intValue() > currentgene ) {
// 				Gene g = makeGene(currentgene, cdsChildren, otherChildren, compl);
// 				if ( g!= null ) {
// 					genes.addElement(g);
// 				}
// 				cdsChildren = new Vector();
// 				otherChildren = new Vector();
// 			} else if (gnI.intValue() < currentgene) { // gnI == 0 ie suboptimal exons
// 				Gene g = makeGene(currentgene, cdsChildren, otherChildren, compl);
// 				if ( g!= null ) {
// 					genes.addElement(g);
// 				}
// 			}
// 			currentgene = gnI.intValue();
// 			Class cl = null;
// 			Class paramarr[] = new Class[2];
// 			try {
// 				cl = Class.forName("alfresco." + type);
// 				paramarr[0] = Class.forName("alfresco.Entry");
// 				paramarr[1] = Class.forName("alfresco.SeqRange");
// 			} catch (ClassNotFoundException cnf) { System.out.println(cnf); }
// 			Constructor cons = null;
// 			try {
// 					cons = cl.getDeclaredConstructor(paramarr);
// 			} catch (NoSuchMethodException nsm) { System.out.println(nsm); }
// 			String strand = (String) values.get("strand");
// 			boolean reverse = strand.equals("-")?true:false;
// 			compl = reverse;
// 			Integer startI = (Integer) values.get("start");
// 			Integer stopI = (Integer) values.get("stop");
// 			SeqRange r = new SeqRange(startI.intValue(), stopI.intValue(), reverse);
// 			Object initargs[] = new Object[2];
// 			initargs[0] = entry;
// 			initargs[1] = r;
// 			SeqFeature feature = null;
// 			try {
// 				feature = (SeqFeature) cons.newInstance(initargs);
// 			}catch (IllegalAccessException ia) { System.out.println(ia); }
// 			 catch (InvocationTargetException it) { System.out.println(it); }
// 			 catch (InstantiationException ie) { System.out.println(ie); }
// 			feature.predicted(true);
// 			feature.setPredMethod("Genscan");
// 			if(type.equals("Exon")) {
// 				Double s = (Double) values.get("prob");
// 				feature.setScore(s.doubleValue());
// 				Integer f = (Integer) values.get("frame");
// 				feature.setFrame(f.intValue());
// 			} 
// // 			else if (type.equals("TATA")) {
// // 				Promoter p = new Promoter(entry);
// // 				p.addChild(feature);
// // 				otherChildren.addElement(p);
// // 				continue;
// // 			}
// 			if (entry.getTier() > 0) {
// 				if (currentgene == 0) {
// 					feature.setTier(4);
// 				} else {
// 					feature.setTier(3);
// 				}
// 			} else {
// 				if (currentgene == 0) {
// 					feature.setTier(-4);
// 				} else {
// 					feature.setTier(-3);
// 				}
// 
// 			}
// 			if (currentgene == 0) {
// 				int htier = getOverlapTier(feature);
// 				if (htier != 0) {
// 					if (htier > 0) {
// 						feature.setTier(++htier);
// 					} else {
// 						feature.setTier(--htier);
// 					}
// 				}
// 			}
// 			if (currentgene != 0) {
// 				if (feature instanceof Exon){
// 					cdsChildren.addElement(feature);
// 				} else {
// 					otherChildren.addElement(feature);
// 				}
// 			} else {
// 				if (!overlapOptimal(feature)) {
// 					genes.addElement(feature);
// 				}
// 			}
// 			
// 
// 		}
// 		if (currentgene == 1) {
// 			Gene g = makeGene(currentgene, cdsChildren, otherChildren, compl);
// 			if ( g!= null ) {
// 				genes.addElement(g);
// 			}
// 		}
// 	}
	
// 	/**
// 	 * Adds found genes to entry
// 	 */
// 	public void addToEntry() {
// 		entry.addChildren(genes);
// // 		Enumeration kids = genes.elements();
// // 		while(kids.hasMoreElements()) {
// // 			Glyph g = (Glyph) kids.nextElement();
// // 			entry.addChild(g);
// // 		}
// 	}

// 	things to parse
// 	Init |
// 	Intr | Exons
// 	Term |
// 	Sngl |
// 	Prom
// 	PlyA
// gennum, exnum, type, strand, begin, end, [frame, prob]
	
	/**
	 * Parses an genscan output line into a hashtable of objects
	 * Contrary to the output of genscan the start position will always
	 * be less than the stop position. 
	 * @return Hashtable of objects <P> <b>Keys</b>:<br>
   * <pre>
   * "gennum"   Integer<br>
   * "exnum"    Integer<br>
   * "type"     String ("Promoter", "PolyA", "Exon")<br>
   * "strand"   String ("+","-")<br>
   * "start"    Integer<br>
   * "stop"     Integer<br>
   * ["frame"]  Integer<br>
   * ["prob"]   Double<br></pre>
	 * @param line line to be parsed
	 */
	private Hashtable parseLine(String line) {
		Hashtable out = new Hashtable();
		StringTokenizer st = new StringTokenizer(line);
		String gnex = st.nextToken();
		if (gnex == null) return null;
		StringTokenizer gt = new StringTokenizer(gnex, ".");
		String gennumS = gt.nextToken();
		Integer gennum = null;
		if (gennumS.equals("S")) {
			gennum = new Integer(0);
		} else {
			gennum = Integer.valueOf(gennumS);
		}
		Integer exnum = Integer.valueOf(gt.nextToken());
		out.put("gennum", gennum);
		out.put("exnum", exnum);
		String type = st.nextToken();
		if (type.equals("Prom")) {
			type = "Promoter";
// 			type = "TATA";
		} else if (type.equals("PlyA")) {
			type = "PolyA";
		} else {
			type = "Exon";
		}
		out.put("type", type);
		String strand = st.nextToken();
		out.put("strand", strand);
		Integer begin = Integer.valueOf(st.nextToken());
		Integer end = Integer.valueOf(st.nextToken());
		if (strand.equals("+")) {
			out.put("start", begin);
			out.put("stop", end);
		} else {
			out.put("start", end);
			out.put("stop", begin);		
		}
		st.nextElement(); // Len
		if (type.equals("Exon")) {
			Integer frame = Integer.valueOf(st.nextToken());
			out.put("frame", frame);
			st.nextElement(); //Ph
			st.nextElement(); //I/Ac
			st.nextElement(); //Do/T
			st.nextElement(); //CodRg
			Double prob = Double.valueOf(st.nextToken());
			out.put("prob", prob);
		}
		return out;
	}
	
// 	/**
// 	 * Description
// 	 * @return descr
// 	 * @param param descr
// 	 */
// 	private Gene makeGene(int currGene, Vector cdsKids, Vector otherKids, boolean compl) {
// 		if ( cdsKids == null && otherKids == null) return null;
// 		Gene g = new Gene(entry, "Predicted Gene" + currGene);
// 		if ( cdsKids != null) {
// 			CDS cds = new CDS(entry, cdsKids);
// 			g.addChild(cds);
// 		}
// 		if (otherKids != null) {		
// 			g.addChildren(otherKids);
// 		}
// 		g.complement(compl);
// 		return g;
// 	}
// 	
// 	/**
// 	 * Determines if a feature overlaps any other feature in the genes vector
// 	 * @return true if feature overlaps other SeqFeature
// 	 * @param f feature
// 	 */	
// 	private boolean geneOverlap(SeqFeature f) {
// 		 boolean overlap = false;
// 		 Enumeration ge = genes.elements();
// 		 while (ge.hasMoreElements()) {
// 			Object o = ge.nextElement();
// 			if (o instanceof CompositeSeqFeature) {
// 				CompositeSeqFeature csf = (CompositeSeqFeature) o;
// 				overlap = csf.overlap(f);
// 			} else if (o instanceof SeqFeature) {
// 				SeqFeature tsf = (SeqFeature) o;
// 				overlap = tsf.overlap(f);
// 			}
// 			if (overlap) return true;
// 		 }
// 		 return false;
// 	}
// 
// 	/**
// 	 * Gets the tier of an overlaping feature, if there is one in the genes vector
// 	 * (Maybe this should be implemented in CompositeSeqFeature)
// 	 * @return tier, 0 if no overlapping feature is found
// 	 * @param f feature
// 	 */	
// 	private int getOverlapTier(SeqFeature f) {
// 		int hTier = 0; 
// 		Enumeration ge = genes.elements();
// 		while (ge.hasMoreElements()) {
// 			Object o = ge.nextElement();
// 			if (o instanceof CompositeSeqFeature) {
// 				CompositeSeqFeature csf = (CompositeSeqFeature) o;
// 				Enumeration compen = csf.getChildEnumeration();
// 				while(compen.hasMoreElements()) {
// 					Object ob = (Object) compen.nextElement();
// 					if (ob instanceof CompositeSeqFeature) {
// 						CompositeSeqFeature comsf = (CompositeSeqFeature) ob;
// 						Enumeration compsfen = comsf.getChildEnumeration();
// 						while(compsfen.hasMoreElements()) {
// 							SeqFeature seqf = (SeqFeature) compsfen.nextElement();
// 							if (seqf.overlap(f)) {
// 								int tier = seqf.getTier();
// 								if (Math.abs(tier) > Math.abs(hTier)) {
// 									hTier = tier;
// 								}
// 							}
// 						}
// 					} else if (ob instanceof SeqFeature) {
// 						SeqFeature sf = (SeqFeature) ob;
// 						if (sf.overlap(f)) {
// 							int tier = sf.getTier();
// 							if (Math.abs(tier) > Math.abs(hTier)) {
// 								hTier = tier;
// 							}
// 						}
// 					}
// 				}
// 			} else if (o instanceof SeqFeature) {
// 				SeqFeature sf = (SeqFeature) o;
// 				if (sf.overlap(f)) {
// 					int tier = sf.getTier();
// 					if (Math.abs(tier) > Math.abs(hTier)) {
// 						hTier = tier;
// 					}
// 				}
// 			}
// 		}
// 		return hTier;
// 	}
// 	
// 	/**
// 	 * Determines if feature overlaps optimal features
// 	 * @return true if feature overlaps optimal
// 	 * @param sf feature
// 	 */	
// 	private boolean overlapOptimal(SeqFeature sf) {
// 		 boolean overlap = false;
// 		 Enumeration ge = genes.elements();
// 		 while (ge.hasMoreElements()) {
// 			Object o = ge.nextElement();
// 			if (o instanceof CompositeSeqFeature) {
// 				CompositeSeqFeature csf = (CompositeSeqFeature) o;
// 				overlap = csf.overlap(sf);
// 			}
// 			if (overlap) return true;
// 		 }
// 		 return false;
// 	}
	
// 	public static void main(String[] arg) {
// 		String fname = "MMU58105";
// 		File f = new File(fname);
// 		Entry ent = null;
// 		try {
// 			ent = new Entry(f);
// 		} catch (FileNotFoundException fnf) { System.out.println(fnf); }
// 		Genscan g = new Genscan(ent);
// 		Enumeration ge = g.result.elements();
// 		while (ge.hasMoreElements()) {
// 			String s = (String) ge.nextElement();
// 			System.out.println(s);
// 		}
// 	}

}
