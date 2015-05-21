/*
* $Revision: 1.3 $
* $Id: Server_Impl.java,v 1.3 2003/08/21 08:59:48 niclas Exp $
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
import java.util.*;
import java.io.*;
import org.omg.CORBA.*;
import alfresco.*;

/**
 * Implements the Server interface
 * @see alfresco.corba_wrappers.Server
 * @version 1
 * @author Niclas Jareborg, Center for Genomics Research
 */

public class Server_Impl extends _ServerImplBase {

private Vector methods;
private File writedir;
private AlfrescoResources alfrescoResources;
// /**
//  * Constructor
//  */ 
//   public Server_Impl(Vector methodNames, File tmpDir) {
//     super();
//     writedir = tmpDir;
//     methods = new Vector();
//     Enumeration names = methodNames.elements();
//     while(names.hasMoreElements()) {
//       String n = (String) names.nextElement();
//       String fullName = "alfresco.corba_wrappers." + n + "Factory";
//       MethodFactory mf = null;
//       try {
//         Class cl = Class.forName(fullName);
//         mf = (MethodFactory) cl.newInstance();
//       } catch (Exception e) { e.printStackTrace(); }
//       if (mf == null) {
//         System.out.println("alfresco.corba_wrappers.Server: " + n + ", no such method");
//         continue;
//       }
//       mf.setTmpDir(writedir);
//       methods.addElement(mf);
//     }
//   }
/**
 * Constructor
 */ 
  public Server_Impl(File alfrescoResourceFile, Vector methodNames, File tmpDir) {
    super();
    writedir = tmpDir;
    this.alfrescoResources = new AlfrescoResources(alfrescoResourceFile);
    methods = new Vector();
    Enumeration names = methodNames.elements();
    while(names.hasMoreElements()) {
      String n = (String) names.nextElement();
      methods.addElement(n);
      System.out.println("Registering method: " + n);
    }
  }

  
// /**
//  * Returns a specified method
//  * @param methodName the name of method
//  * @return Method
//  */
//   public Method getMethod(String methodName) throws alfresco.corba_wrappers.NoSuchMethodException {
//     Enumeration methen = methods.elements();
//     while(methen.hasMoreElements() ) {
//       MethodFactory mf = (MethodFactory) methen.nextElement();
//       if (methodName.equals(mf.getMethodName())) {
//         return mf.createMethod();
//       }
//     }
//     throw new alfresco.corba_wrappers.NoSuchMethodException();
//   }
/**
 * Returns a specified method
 * @param methodName the name of method
 * @return Method
 */
  public Method getMethod(String methodName) throws alfresco.corba_wrappers.NoSuchMethodException {
    Enumeration methen = methods.elements();
    while(methen.hasMoreElements() ) {
      String mn = (String) methen.nextElement();
      if (methodName.equals(mn)) {
        String fullName = "alfresco.corba_wrappers." + methodName.toLowerCase() + "." + methodName + "_Impl";
        System.out.println("Method fullname: " + fullName);
        Method m = null;
        try {
          Class cl = Class.forName(fullName);
    //       System.out.println("class name: " + cl.getName());
    //       Class [] interfaces = cl.getInterfaces();
    //       for (int i = 0; i < interfaces.length; i++) {
    //         System.out.println("implements: " + interfaces[i].getName()); 
    //       }
    //       Class scl = cl.getSuperclass();
    //       System.out.println("super class name: " + scl.getName());
    //       Class sscl = scl.getSuperclass();
    //       System.out.println("super class name: " + sscl.getName());
          m = (Method) cl.newInstance();
          if (m == null) {
            System.out.println("Could not get Method " + fullName);
          }
        } catch (Exception e) { e.printStackTrace(); }
        TmpDirWriter tdr = (TmpDirWriter) m;
        tdr.setTmpDir(writedir);
        tdr.setProperties((Properties)alfrescoResources.get(methodName));
        return m;
      }
    }
    System.out.println("Server_Impl: something went wrong");
    throw new alfresco.corba_wrappers.NoSuchMethodException();
  }
  
 
// /**
//  * Gets the available method names
//  * @return array of method names
//  */
//   public String[] getAvailableMethods() {
//     String [] names = new String[methods.size()];
//     int i = 0;
//     Enumeration methen = methods.elements();
//     while(methen.hasMoreElements() ) {
//       MethodFactory mf = (MethodFactory) methen.nextElement();
//       names[i++] = mf.getMethodName();
//     }
//     return names;
//   }
/**
 * Gets the available method names
 * @return array of method names
 */
  public String[] getAvailableMethods() {
    String [] names = new String[methods.size()];
    int i = 0;
    Enumeration methen = methods.elements();
    while(methen.hasMoreElements() ) {
      String n = (String) methen.nextElement();
      names[i++] = n;
    }
    return names;
  }
  
/**
 * Main function
 * @param args command line arguments
 */
  public static void main(String [] args) {
    if (args.length != 3) usage();
    File rcf = new File(args[0]);
    File tmpd = new File(args[1]);
    String refFile = args[2];
    if (!rcf.isFile()) usage();
    if (!tmpd.isDirectory()) usage();
    Vector names = new Vector();
    FileReader fr=null;
		BufferedReader br = null;
		try {
		  fr = new FileReader(rcf);
		  br = new BufferedReader(fr);
		} catch(FileNotFoundException fnf) { fnf.printStackTrace();}
		String line = null;
		try {
		  while ((line = br.readLine()) != null) {
        System.out.println("Method names file: " + line);
				names.addElement(line);
		  }
		} catch(IOException ioe) { ioe.printStackTrace();}
    System.out.println("Resource file read");
    
    // 030821 new bit to try to get the Orbacus orb to work with JDK1.2 and later
    java.util.Properties props = System.getProperties();
    props.put("org.omg.CORBA.ORBClass",
              "com.ooc.CORBA.ORB");
    props.put("org.omg.CORBA.ORBSingletonClass",
              "com.ooc.CORBA.ORBSingleton");
    System.setProperties(props);
    
    
// 		org.omg.CORBA.ORB orb = com.ooc.CORBA.ORB.init(args, new java.util.Properties());
		org.omg.CORBA.ORB orb = com.ooc.CORBA.ORB.init(args, props);
		BOA boa = ((com.ooc.CORBA.ORB)orb).BOA_init(args, new java.util.Properties());
    System.out.println("ORB initalized");
		Server_Impl server = new Server_Impl(new File("alfresco_resources"), names, tmpd);
    System.out.println("Server object created");
// 		String refFile = null;
		try {
			String ref = orb.object_to_string(server);
// 			refFile = "/dna2/nic/www/methods_server.ior";
			FileWriter fw = new FileWriter(refFile);
			fw.write(ref);
			fw.flush();
			fw.close();
		} catch (IOException ioe) { ioe.printStackTrace(); }
    System.out.println("IOR file written to " + refFile);
    
    System.out.println("Calling boa.impl_is_ready()");
		
		boa.impl_is_ready(null);
    

  }
  /**
	 * Prints a usage statement and exits
	 */
	private static void usage() {
		System.err.println("usage: java alfresco.corba_wrappers.Server_Impl <resource file> <tmp dir> <IOR file path>");
		System.exit(1);
	}

}
