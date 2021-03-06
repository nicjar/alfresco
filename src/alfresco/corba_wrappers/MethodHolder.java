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

package alfresco.corba_wrappers;

//
// IDL:alfresco/corba_wrappers/Method:1.0
//
final public class MethodHolder implements org.omg.CORBA.portable.Streamable
{
    public Method value;

    public
    MethodHolder()
    {
    }

    public
    MethodHolder(Method initial)
    {
        value = initial;
    }

    public void
    _read(org.omg.CORBA.portable.InputStream in)
    {
        value = MethodHelper.read(in);
    }

    public void
    _write(org.omg.CORBA.portable.OutputStream out)
    {
        MethodHelper.write(out, value);
    }

    public org.omg.CORBA.TypeCode
    _type()
    {
        return MethodHelper.type();
    }
}
