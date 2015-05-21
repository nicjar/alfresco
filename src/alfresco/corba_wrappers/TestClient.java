/*
* $Revision: 1.1 $
* $Id: TestClient.java,v 1.1 2003/04/04 11:32:57 niclas Exp $
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
package alfresco.corba_wrappers;
import java.io.*;
import org.omg.CORBA.*;
import alfresco.*;


/**
 * For testing alfresco.corba_wrappers.blastalign.BlastAlign
 * @see alfresco.corba_wrappers.blastalign.BlastAlign
 * @version 1
 * @author Niclas Jareborg, Center for Genomics Research
 */

public class TestClient {
	
	
	public static void main(String [] args) {
		if (args.length < 3) { usage(); }
    String iorf = args[0];
		org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, new java.util.Properties());
		org.omg.CORBA.Object obj = null;

		try {
			FileReader fr = new FileReader(iorf);
			BufferedReader in = new BufferedReader(fr);
			obj = orb.string_to_object(in.readLine());
		} catch (Exception ex) { ex.printStackTrace(); }	

		Server server = ServerHelper.narrow(obj);
//     alfresco.corba_wrappers.Method cpg = null;
    alfresco.corba_wrappers.Method blastal = null;
//     alfresco.corba_wrappers.cpg.CpG cpg = null;
    try {
//       cpg = server.getMethod("CpG");
      blastal = server.getMethod("BlastAlign");
    } catch (NoSuchMethodException nsme) {
//       System.out.println("TestClient: No method CpG");
      System.out.println("TestClient: No method BlastAlign");
    } catch (Exception e) {
      System.out.println("TestClient: exception raised");
      e.printStackTrace();
      System.out.println(e.getMessage());
    }
    Class cl = blastal.getClass();
    System.out.println("Class: " + cl.getName());
		StringBuffer sb1 = new StringBuffer();
		String sn1 = args[1];
		File f1 = new File (sn1);
		System.out.println("File 1: " + f1.getPath());
		StringBuffer sb2 = new StringBuffer();
		String sn2 = args[2];
		File f2 = new File (sn2);
		System.out.println("File 2: " + f2.getPath());
		try {
			FileReader fr = new FileReader(f1);
			BufferedReader in = new BufferedReader(fr);
			String line = in.readLine();
			System.out.println(line);
			line = in.readLine();
			while (line != null) {
				sb1.append(line);
				line = in.readLine();
			}
			fr = new FileReader(f2);
			in = new BufferedReader(fr);
			line = in.readLine();
			System.out.println(line);
			line = in.readLine();
			while (line != null) {
				sb2.append(line);
				line = in.readLine();
			}
			
		} catch (Exception ex) { ex.printStackTrace(); }	
		String seq1 = null;
		String seq2 = null;
		if (sb1 != null) {
			seq1 = sb1.toString();	
		} else {
			System.out.println("No sequence in " + sn1);
			System.exit(1);
		}
		if (sb2 != null) {
			seq2 = sb2.toString();	
		} else {
			System.out.println("No sequence in " + sn2);
			System.exit(1);
		}
		
		GffDataStruct gff = null;
    String[] in_name = new String[2];
    in_name[0] = sn1;
    in_name[1] = sn2;
    String[] in_seq = new String[2];
    in_seq[0] = seq1;
    in_seq[1] = seq2;
    String[] params = new String[1];
    params[0] = "";
    InputStruct inp = new InputStruct(in_name, in_seq, params);

		try {
// 			cpg.input(inp);
//       cpg.run();
//       gff = cpg.getGffData();
			blastal.input(inp);
      blastal.run();
      gff = blastal.getGffData();
		} catch (NoOutputException e) {
			System.out.println("No output: " + e.reason);
			System.exit(1);
		}
		System.out.println(gff.gff);
		System.out.println(gff.cgff);
	
	}
	/**
   * Description
   * @return descr
   * @param param descr
   */
  public static void usage() {
    System.out.println("usage: java alfresco.corba_wrappers.TestClient <iorfile> <seqfile1> <seqfile>");
    System.exit(1);
  }

}
