/*
* $Revision: 1.1 $
* $Id: CorbaMethodRunner.java,v 1.1 2003/04/04 10:13:59 niclas Exp $
*
* This file is part of Alfresco
* Copyright (C) 1998-1999  Niclas Jareborg
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
*
*/
package alfresco;
import java.io.*;
import java.net.*;
import java.util.*;
import alfresco.corba_wrappers.*;

/**
 * Client class for running CORBA methods
 * @see alfresco.corba_wrappers.Method
 * @see java.lang.Thread
 * @version 1
 * @author Niclas Jareborg, Center for Genomics Research
 */

public class CorbaMethodRunner extends Thread {
  GffDataStruct result;
  Method method;
  
  /**
   * Creates CorbaMethodRunner which creates a CORBA method object
   * with the specified name and input struct
   * @param methodname method name string
   * @param input input struct
   * @param prop properties object for the method
   */
  public CorbaMethodRunner (String methodname, InputStruct input, Properties prop){
//     String[] args = new String[1];
//     args[0] = "";
    String[] args = { "" };
    java.util.Properties props = System.getProperties();
    props.put("org.omg.CORBA.ORBClass",
              "com.ooc.CORBA.ORB");
    props.put("org.omg.CORBA.ORBSingletonClass",
              "com.ooc.CORBA.ORBSingleton");
    System.setProperties(props);



//     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, new java.util.Properties());
    org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
//     com.ooc.CORBA.ORB orb = com.ooc.CORBA.ORB.init(args, new java.util.Properties());
//     org.omg.CORBA.ORB orb = com.ooc.CORBA.ORB.init(args, new java.util.Properties());
// 		System.out.println("ORB object: " + orb);
    org.omg.CORBA.Object obj = null;
    
		try {
//       URL u = new URL(SystemConstants.METHODS_SERVER_IOR_URL);
      URL u = new URL(prop.getProperty("iorurl"));
//       System.out.println("URL: " + u);
      BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));
			obj = orb.string_to_object(in.readLine());
//       System.out.println("CORBA object: " + obj);
		} catch (Exception ex) { ex.printStackTrace(); }	
    Server server = ServerHelper.narrow(obj);
    method = null;
    try {
      method = server.getMethod(methodname);
    }catch (alfresco.corba_wrappers.NoSuchMethodException nsme) { nsme.printStackTrace(); }
    
    method.input(input);
  }
  
  /**
   * Creates CorbaMethodRunner which creates a CORBA method object
   * with the specified name and input struct.  OLD!
   * @param methodname method name string
   * @param input input struct
   */
  public CorbaMethodRunner (String methodname, InputStruct input){
//     String[] args = new String[1];
//     args[0] = "";
    String[] args = { "" };
    org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, new java.util.Properties());
		org.omg.CORBA.Object obj = null;
    
		try {
      URL u = new URL(SystemConstants.METHODS_SERVER_IOR_URL);
      BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));
			obj = orb.string_to_object(in.readLine());
		} catch (Exception ex) { ex.printStackTrace(); }	
    Server server = ServerHelper.narrow(obj);
    method = null;
    try {
      method = server.getMethod(methodname);
    }catch (alfresco.corba_wrappers.NoSuchMethodException nsme) { nsme.printStackTrace(); }
    
    method.input(input);
  }
  
  /**
   * Implements Runnable.run()
   */
  public void run(){
    method.run();
  }
  
  /**
   * Gets result as a GffDataStruct
   * @return gff data struct
   */
  public GffDataStruct getGffData() throws NoOutputException{
    result = null; 
    try {
      result = method.getGffData();
    } catch (NoOutputException noe) { throw noe; }
    return result;
  }

}
