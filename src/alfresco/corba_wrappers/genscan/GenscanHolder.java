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

package alfresco.corba_wrappers.genscan;

//
// IDL:alfresco/corba_wrappers/genscan/Genscan:1.0
//
final public class GenscanHolder implements org.omg.CORBA.portable.Streamable
{
    public Genscan value;

    public
    GenscanHolder()
    {
    }

    public
    GenscanHolder(Genscan initial)
    {
        value = initial;
    }

    public void
    _read(org.omg.CORBA.portable.InputStream in)
    {
        value = GenscanHelper.read(in);
    }

    public void
    _write(org.omg.CORBA.portable.OutputStream out)
    {
        GenscanHelper.write(out, value);
    }

    public org.omg.CORBA.TypeCode
    _type()
    {
        return GenscanHelper.type();
    }
}
