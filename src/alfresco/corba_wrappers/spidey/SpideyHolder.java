// **********************************************************************
//
// Generated by the ORBacus IDL to Java Translator
//
// Copyright (c) 1999
// Object-Oriented Concepts, Inc.
// Billerica, MA, USA
//
// Copyright (c) 1999
// Object-Oriented Concepts GmbH
// Ettlingen, Germany
//
// All Rights Reserved
//
// **********************************************************************

// Version: 3.1.2
// License: non-commercial

package alfresco.corba_wrappers.spidey;

//
// IDL:alfresco/corba_wrappers/spidey/Spidey:1.0
//
final public class SpideyHolder implements org.omg.CORBA.portable.Streamable
{
    public Spidey value;

    public
    SpideyHolder()
    {
    }

    public
    SpideyHolder(Spidey initial)
    {
        value = initial;
    }

    public void
    _read(org.omg.CORBA.portable.InputStream in)
    {
        value = SpideyHelper.read(in);
    }

    public void
    _write(org.omg.CORBA.portable.OutputStream out)
    {
        SpideyHelper.write(out, value);
    }

    public org.omg.CORBA.TypeCode
    _type()
    {
        return SpideyHelper.type();
    }
}
