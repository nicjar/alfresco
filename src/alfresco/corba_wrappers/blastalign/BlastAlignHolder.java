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

package alfresco.corba_wrappers.blastalign;

//
// IDL:alfresco/corba_wrappers/blastalign/BlastAlign:1.0
//
final public class BlastAlignHolder implements org.omg.CORBA.portable.Streamable
{
    public BlastAlign value;

    public
    BlastAlignHolder()
    {
    }

    public
    BlastAlignHolder(BlastAlign initial)
    {
        value = initial;
    }

    public void
    _read(org.omg.CORBA.portable.InputStream in)
    {
        value = BlastAlignHelper.read(in);
    }

    public void
    _write(org.omg.CORBA.portable.OutputStream out)
    {
        BlastAlignHelper.write(out, value);
    }

    public org.omg.CORBA.TypeCode
    _type()
    {
        return BlastAlignHelper.type();
    }
}
