/*
 * $Revision: 1.1 $
 * $Id: Alfresco.java,v 1.1 2003/04/04 10:13:40 niclas Exp $
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
 * The Alfresco class. Provides the main function<P>
 * @see alfresco.UsefulConstants
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class Alfresco {
/**
 * The application main method. <P>
 * Command line arguments: <PRE>[&lt;cmp_file_name&gt; | &lt;EntryPair_file_name&gt;]</PRE>
 * @param arg command line arguments  
 */
	public static void main(String[] arg) {
		boolean batch = false;
		boolean gff = false;
		boolean cgff = false;
		for (int i = 0; i < arg.length; i++) {
// 			System.out.println("arg["+i+"]: >" + arg[i]+ "<");
			if (arg[i].equals("-b")) { 
				batch = true; 
				System.out.println("Batch mode");
				break;
			} else if (arg[i].equals("-g")){
        		gff = true;
        		System.out.println("gff mode");
				break;
      		} else if (arg[i].equals("-c")){
      			cgff = true;
        		System.out.println("cgff mode");
				break;      			
      		}
		}
		
		if(batch) {
			String[] tmp = new String[arg.length-1];
			int j = 0;
			for (int i = 0; i < arg.length; i++) {
				if (!arg[i].equals("-b")) { 
					tmp[j++] = arg[i];
				}
			}
			arg = tmp;
		}
		if(gff) {
			String[] tmp = new String[arg.length-1];
			int j = 0;
			for (int i = 0; i < arg.length; i++) {
				if (!arg[i].equals("-g")) { 
					tmp[j++] = arg[i];
				}
			}
			arg = tmp;
		}
		if(cgff) {
			String[] tmp = new String[arg.length-1];
			int j = 0;
			for (int i = 0; i < arg.length; i++) {
				if (!arg[i].equals("-c")) { 
					tmp[j++] = arg[i];
				}
			}
			arg = tmp;
		}
		
		Wcomp application = new Wcomp(true, batch);
		if (!batch) {
// 			application.mw.show();
			if (arg.length == 0) { return; }
		} else if (arg.length == 2) { // We're in batch mode!
			Entry ent1 = null;
			Entry ent2 = null;
			try {
				ent1 = new Entry(new File(arg[0]));
			} catch (FileNotFoundException fnfe) {
				System.out.println("Can't find file " + arg[0]);
				System.exit(1);
			}
			try {
				ent2 = new Entry(new File(arg[1]));
			} catch (FileNotFoundException fnfe) {
				System.out.println("Can't find file " + arg[1]);
				System.exit(1);
			}
			application.setEntry1(ent1);
			application.setEntry2(ent2);
			EntryPair ep = application.getEntryPair();
      
      String path = System.getProperty("user.dir");
			String fname = ent1.getFilename() + "-" + ent2.getFilename() + ".pair";
      File outfile = new File(path,fname);
      
			// Run Cpg
			System.out.println("Running cpg");
			ep.callCpG();
			System.out.println("Saving after cpg in " + fname);
      saveForBatch(ep, outfile);
			// Run RepeatMasker
			System.out.println("Running RepeatMasker");
			ep.callRepeatMasker("-m ", "");
			System.out.println("Saving after RepeatMasker in " + fname);
      saveForBatch(ep, outfile);
      // Run blastalign
			System.out.println("Running BlastnAlign");
      ep.callBlastnAlign();
			System.out.println("Saving after BlastnAlign in " + fname);
      saveForBatch(ep, outfile);
      // Run blEst_genome
      System.out.println("Running blEst_genome");
      ep.callBlEst_Genome();
			System.out.println("Saving after blEst_genome in " + fname);
      saveForBatch(ep, outfile);
// 			// Run BlastWise
// 			System.out.println("Running BlastWise");
// 			ep.callBlastWise();
// 			System.out.println("Saving after BlastWise in " + fname);
//       saveForBatch(ep, outfile);
			// Run BlGenewise
			System.out.println("Running BlGenewise");
			ep.callBlGenewise();
			System.out.println("Saving after BlGenewise in " + fname);
      saveForBatch(ep, outfile);
			// Run Genscan
			System.out.println("Running Genscan");
			ep.callGenscan();
			System.out.println("Saving after Genscan in " + fname);
      saveForBatch(ep, outfile);
// 			// Find Reciprocals
// 			ep.findReciprocals();
			// Run regionsets
			
// 			try {
// // 				ep.save(fname);
// 				ep.save(outfile);
// 			} catch (IOException ioe) {
// 				System.out.println("Can't save file " + outfile.getPath() + ". Don't hit me!");
// 				System.exit(1);
// 			}
// 			System.exit(0); 
			application.quit();
		} else {
			usage();
		}
		if (arg[0] != null) {
			if (gff) {
				String gffFile = arg[0];
				String ent1File = arg[1];
				String ent2File = arg[2];
				Entry ent1 = null;
				Entry ent2 = null;
				try {
					ent1 = new Entry(new File(ent1File));
				} catch (FileNotFoundException fnfe) {
					System.out.println("Can't find file " + ent1File);
					System.exit(1);
				}
				try {
					ent2 = new Entry(new File(ent2File));
				} catch (FileNotFoundException fnfe) {
					System.out.println("Can't find file " + ent2File);
					System.exit(1);
				}
				application.setEntry1(ent1);
				application.setEntry2(ent2);
				EntryPair ep = application.getEntryPair();
				try {
					GffParser gp = new GffParser(ep, new File(gffFile));
				} catch (Exception e) { e.printStackTrace(); }
				application.pw.cdsCheckboxP.setEnabled(true);		// enable exclude checkboxes in region set parameter window
				application.drawEntries();
				application.mw.status.setText("OK");			// set status label to OK
				
			} else if (cgff) {
				String cgffFile = arg[0];
				String ent1File = arg[1];
				String ent2File = arg[2];
				Entry ent1 = null;
				Entry ent2 = null;
				try {
					ent1 = new Entry(new File(ent1File));
				} catch (FileNotFoundException fnfe) {
					System.out.println("Can't find file " + ent1File);
					System.exit(1);
				}
				try {
					ent2 = new Entry(new File(ent2File));
				} catch (FileNotFoundException fnfe) {
					System.out.println("Can't find file " + ent2File);
					System.exit(1);
				}
				application.setEntry1(ent1);
				application.setEntry2(ent2);
				EntryPair ep = application.getEntryPair();
				try {
					CgffParser gp = new CgffParser(ep, new File(cgffFile));
				} catch (Exception e) { e.printStackTrace(); }
				application.pw.cdsCheckboxP.setEnabled(true);		// enable exclude checkboxes in region set parameter window
				application.drawEntries();
				application.mw.status.setText("OK");			// set status label to OK
				
			} else if (arg[0].endsWith(".cmp")){
// 				application.mw.show();
				String path = "./";
				String filename = "";
				int index = arg[0].lastIndexOf("/");
				if (index >= 0) {
					path = arg[0].substring(0,index);
					filename = arg[0].substring(index+1);
				} else {
					filename = arg[0];
				}
				CmpFile cmpf = new CmpFile(path,filename);
				if (!cmpf.exists() ) {
					application.mw.status.setText(filename+": no such file");
					return;
				}
				application.mw.status.setText("Reading .cmp file...");	// set status label to "Reading..."
				try {
					cmpf.open();
					cmpf.parse(application.entries);
				} catch (FileNotFoundException fnf) {
					application.mw.status.setText(filename+": no such file");	// HMM, not sure this catching only catches filename problems
					return;
				}
				application.pw.cdsCheckboxP.setEnabled(true);		// enable exclude checkboxes in region set parameter window
				application.drawEntries();
				application.mw.status.setText("OK");			// set status label to OK
					
			} else {
// 				application.mw.show();
				try {
				  String path = "./";
				  String filename = "";
				  int index = arg[0].lastIndexOf("/");
				  if (index >= 0) {
					  path = arg[0].substring(0,index);
					  filename = arg[0].substring(index+1);
				  } else {
					  filename = arg[0];
				  }
          File infile = new File(path, filename);
// 					application.entries = EntryPair.load(arg[0], application);
					application.entries = EntryPair.load(infile, application);
					application.mw.entryGadget1.update(application.entries.entry1);
					application.mw.entryGadget2.update(application.entries.entry2);
					application.entries.zoom = -1;
					application.drawEntries();	
				} catch (IOException ioe) { System.out.println(ioe); }
				  catch (ClassNotFoundException cnfe) { System.out.println(cnfe); }			
			}
		}
	}
	
  /**
   * Save routine during batch analysis
   * @param outf file to save to
   */
  static private void saveForBatch(EntryPair ep, File outf) {
	  try {
		  ep.save(outf);
	  } catch (IOException ioe) {
		  System.out.println("Can't save file " + outf.getPath() + ". Don't hit me!");
// 		  System.exit(1);
	  }   
  }
  
	public static void usage() {
		System.out.println("Usage: java alfresco.Alfresco [<cmp_file_name> | <EntryPair_file_name>| -b <fasta_file_1> <fasta_file_2>]");
		System.exit(1);
	}
	
}
