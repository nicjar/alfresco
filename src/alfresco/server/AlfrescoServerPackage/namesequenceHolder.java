// **********************************************************************
//
// Generated by the ORBacus IDL to Java Translator
//
// Copyright (c) 1998
// Object-Oriented Concepts, Inc.
// Billerica, MA, USA
//
// Copyright (c) 1998
// Object-Oriented Concepts GmbH
// Ettlingen, Germany
//
// All Rights Reserved
//
// **********************************************************************

// Version: 3.1
// License: non-commercial

package alfresco.server.AlfrescoServerPackage;

//
// IDL:alfresco/server/AlfrescoServer/namesequence:1.0
//
final public class namesequenceHolder implements org.omg.CORBA.portable.Streamable
{
    public String[] value;

    public
    namesequenceHolder()
    {
    }

    public
    namesequenceHolder(String[] initial)
    {
        value = initial;
    }

    public void
    _read(org.omg.CORBA.portable.InputStream in)
    {
        value = namesequenceHelper.read(in);
    }

    public void
    _write(org.omg.CORBA.portable.OutputStream out)
    {
        namesequenceHelper.write(out, value);
    }

    public org.omg.CORBA.TypeCode
    _type()
    {
        return namesequenceHelper.type();
    }
}
