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
final public class SpideyHelper
{
    public static void
    insert(org.omg.CORBA.Any any, Spidey val)
    {
        org.omg.CORBA.portable.OutputStream out = any.create_output_stream();
        write(out, val);
        any.read_value(out.create_input_stream(), type());
    }

    public static Spidey
    extract(org.omg.CORBA.Any any)
    {
        if(any.type().equal(type()))
            return read(any.create_input_stream());
        else
            throw new org.omg.CORBA.BAD_OPERATION();
    }

    private static org.omg.CORBA.TypeCode typeCode_;

    public static org.omg.CORBA.TypeCode
    type()
    {
        if(typeCode_ == null)
        {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            typeCode_ = orb.create_interface_tc(id(), "Spidey");
        }

        return typeCode_;
    }

    public static String
    id()
    {
        return "IDL:alfresco/corba_wrappers/spidey/Spidey:1.0";
    }

    public static Spidey
    read(org.omg.CORBA.portable.InputStream in)
    {
        org.omg.CORBA.Object val = in.read_Object();
        return narrow(val);
    }

    public static void
    write(org.omg.CORBA.portable.OutputStream out, Spidey val)
    {
        out.write_Object(val);
    }

    public static Spidey
    narrow(org.omg.CORBA.Object obj)
    {
        if(obj != null)
        {
            try
            {
                return (Spidey)obj;
            }
            catch(ClassCastException ex)
            {
            }

            if(obj._is_a(id()))
            {
                org.omg.CORBA.portable.ObjectImpl impl;
                impl = (org.omg.CORBA.portable.ObjectImpl)obj;
                StubForSpidey stub = new StubForSpidey();
                stub._set_delegate(impl._get_delegate());
                return stub;
            }
        }

        return null;
    }
}
