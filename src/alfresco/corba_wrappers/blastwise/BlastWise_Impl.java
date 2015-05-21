/*
* $Revision: 1.1 $
* $Id: BlastWise_Impl.java,v 1.1 2003/04/04 11:38:37 niclas Exp $
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
 * For more information contact Niclas at niclas.jareborg@cgr.ki.se
*/
package alfresco.corba_wrappers.blastwise;
import java.io.*;
import java.util.*;
import org.omg.CORBA.*;
import alfresco.*;
import alfresco.corba_wrappers.*;


/**
 * Implements the BlastWise:Method interface
 * @see alfresco.corba_wrappers.genscan.BlastWise
 * @version 1
 * @author Niclas Jareborg, Center for Genomics Research
 */

public class BlastWise_Impl extends _BlastWiseImplBase implements alfresco.corba_wrappers.Method, TmpDirWriter {

private String seq;
private String seqName;
private String [] parameters;
private GffDataStruct data;
private File tmpDir;
private Properties prop;

/**
 * Constructor
 */ 
  public BlastWise_Impl() {
    String [] supl = { "" };
    data = new GffDataStruct("", "", supl);
  }
/**
 * Sets temporary directory for writing files
 * @param td temporary directory file object
 */
  public void setTmpDir(File td) {
    tmpDir = td;
  }
/**
 * Sets property object for method
 * @param prop Properties object
 */
  public void setProperties(Properties prop) {
    this.prop = prop;    
  }
/**
 * Takes the input necessary for running the method
 * @param input input struct
 */
  public void input(InputStruct input) {
    this.seq = input.seqs[0];
    this.seqName = input.names[0];
    this.parameters = input.parameters;
  }
/**
 * Runs the analysis. 
 */
  public void run() {
    // Do the dirty stuff !!
    if (tmpDir == null) {
      System.out.println("alfresco.corba_wrappers.blastwise.BlastWise_Impl: tmpDir not set!");
    }
    FastaFile file = new FastaFile(tmpDir.getPath(), seqName, ">" + seqName, seq);
    file.write();
    parseParameters();
    alfresco.BlastWise bwr = new alfresco.BlastWise(file, prop);
    bwr.start();
    try {
      bwr.join();
      data.gff = bwr.getGff();
    } catch (Exception e) { e.printStackTrace(); }
    
  }
/**
 * Gets the resultant data
 * @return gff data structure
 */
  public GffDataStruct getGffData() throws alfresco.corba_wrappers.NoOutputException{
    if (data == null) { 
      throw new alfresco.corba_wrappers.NoOutputException("BlastWise produced no output"); 
    }
    return data;
  }
/**
 * Parses the parameter array in the InputStruct and changes the
 * Properties object accordingly
 */
  private void parseParameters(){
    for (int i=0; i < parameters.length; i++) {
      StringTokenizer st = new StringTokenizer(parameters[i], "=");
      prop.put(st.nextToken(), st.nextToken());
    }
  }
}
