/*
 * $Revision: 1.1 $
 * $Id: Genewise.java,v 1.1 2003/04/04 10:14:25 niclas Exp $
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
 * A class to call genewise and parse the result
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */

public class Genewise extends Thread{
/**
 * Entry to run Genewise on
 */
	Entry entry;
/**
 * Sequence fasta file	
 */  
  File seqFile;
  
  String seqName;

  FastaFile pFile;
/**
 * Output result from Genewise
 */
	StringBuffer result;
	
  String gff;
  Properties properties;
  
  static String[] executables = { "genewise" }; // 

  
  /**
  * Description
  * @return descr
  * @param param descr
  */
  public Genewise(Entry ent, FastaFile pFile, Properties props) {
		entry = ent;
    seqName = ent.getFilename();
		result = new StringBuffer();
// 		Class simpleRepeat = null;
// 		try {
// 			simpleRepeat = Class.forName("alfresco.SimpleRepeat");
// 		} catch (ClassNotFoundException cnfe) { System.out.println(cnfe); }
// 		Vector maskV = entry.getRepeats(simpleRepeat);
		Vector maskV = entry.getRepeats();
		FastaFile maskedf = new FastaFile(SystemConstants.TMPDIR, entry.getFilename(), entry);
		maskedf.maskRanges(maskV);
		maskedf.write();
    seqFile = maskedf;
    this.pFile = pFile;
    properties = props;
  }

  /**
  * Description
  * @return descr
  * @param param descr
  */
  public Genewise(File file, FastaFile pFile, Properties props) {
		result = new StringBuffer();
		seqFile =  file;
    seqName = seqFile.getName();
    this.pFile = pFile;
    properties = props;
  }
  /**
   * Returns the defaults for the Method in a Hashtable
   * @return Hashtable of defaults
   */
  public static Hashtable getDefaults() {
    Hashtable hash = new Hashtable(1);
    Vector exv = new Vector(executables.length);
    for (int i =0; i<executables.length; i++) {
      exv.addElement(executables[i]);
    }
    hash.put("executables",exv);
//     hash.put("subopt",subopt);
    return hash;
  }
  
  /**
   * Description
   * @return descr
   * @param param descr
   */
  public void run() {
    GffHeader gh = new GffHeader();
    String ghs = gh.getHeader();
    result.append(ghs);
		String command = properties.getProperty("genewise") + " -quiet -gff -both -divide \"\" " + pFile.getPath() + " " + seqFile.getPath();
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
			while (outline != null) {
// 				result.addElement(line);
				result.append(outline + "\n");
				outline = stdoutbr.readLine();
			}
		}catch (IOException ioe) { System.out.println(ioe); }
			catch (InterruptedException ie) { System.out.println(ie); }
    gff = new String(result);
//     System.out.println(gff);
  }
  
  /**
   * Returns result gff String
   * @return gff String
   */
  public String getGff() {
    return gff;
  }
  

}
