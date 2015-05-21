/*
* $Revision: 1.1 $
* $Id: Dba_Impl.java,v 1.1 2003/04/04 11:39:08 niclas Exp $
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
package alfresco.corba_wrappers.dba;
import java.io.*;
import java.util.*;
import org.omg.CORBA.*;
import alfresco.*;
import alfresco.corba_wrappers.*;


/**
 * Implements the Dba:Method interface
 * @see alfresco.corba_wrappers.dba.Dba
 * @version 1
 * @author Niclas Jareborg, Center for Genomics Research
 */

public class Dba_Impl extends _DbaImplBase implements alfresco.corba_wrappers.Method, TmpDirWriter {

private String seq1;
private String seq2;
private String seqName1;
private String seqName2;
private String offset1;
private String offset2;
private String [] parameters;
private GffDataStruct data;
private File tmpDir;
private Properties prop;

/**
 * Constructor
 */ 
  public Dba_Impl() {
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
 * @param input input structure with the sequences
 */
  public void input(InputStruct input) {
    this.seq1 = input.seqs[0];
    this.seqName1 = input.names[0];
    this.seq2 = input.seqs[1];
    this.seqName2 = input.names[1];
    this.parameters = input.parameters;
  }
/**
 * Runs the analysis. 
 */
  public void run() {
    // Do the dirty stuff !!
    if (tmpDir == null) {
      System.out.println("alfresco.corba_wrappers.dba.Dba_Impl: tmpDir not set!");
    }
    if (prop == null) {
      System.out.println("alfresco.corba_wrappers.dba.Dba_Impl: Properties not set!");
    }
    
    parseParameters();
    
    if (offset1 == null) {
      System.out.println("alfresco.corba_wrappers.dba.Dba_Impl: Offset1 not set!");
    }
    if (offset2 == null) {
      System.out.println("alfresco.corba_wrappers.dba.Dba_Impl: Offset2 not set!");
    }
    FastaFile file1 = new FastaFile(tmpDir.getPath(), seqName1, ">" + seqName1, seq1);
    FastaFile file2 = new FastaFile(tmpDir.getPath(), seqName2, ">" + seqName2, seq2);
    file1.write();
    file2.write();
    alfresco.Dba dbar = new alfresco.Dba(file1, file2, offset1, offset2, prop);
    dbar.start();
    try {
      dbar.join();
      data.gff = dbar.getGff();
      data.cgff = dbar.getCgff();
      data.suplements = dbar.getPffBlocks();
    } catch (Exception e) { e.printStackTrace(); }
    
  }
/**
 * Gets the resultant data
 * @return gff data structure
 */
  public GffDataStruct getGffData() throws alfresco.corba_wrappers.NoOutputException{
    if (data == null) { 
      throw new alfresco.corba_wrappers.NoOutputException("Dba produced no output"); 
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
      String parameter = st.nextToken();
      String value = st.nextToken();
      if(parameter.equals("offset1")) {
        offset1 = value;
      } else if(parameter.equals("offset2")) {
        offset2 = value;
      } else {
        prop.put(parameter, value);
      }
    }
  }
}
