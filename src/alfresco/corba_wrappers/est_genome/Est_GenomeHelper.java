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

package alfresco.corba_wrappers.est_genome;

//
// IDL:alfresco/corba_wrappers/est_genome/Est_Genome:1.0
//
final public class Est_GenomeHelper
{
    public static void
    insert(org.omg.CORBA.Any any, Est_Genome val)
    {
        org.omg.CORBA.portable.OutputStream out = any.create_output_stream();
        write(out, val);
        any.read_value(out.create_input_stream(), type());
    }

    public static Est_Genome
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
            typeCode_ = orb.create_interface_tc(id(), "Est_Genome");
        }

        return typeCode_;
    }

    public static String
    id()
    {
        return "IDL:alfresco/corba_wrappers/est_genome/Est_Genome:1.0";
    }

    public static Est_Genome
    read(org.omg.CORBA.portable.InputStream in)
    {
        org.omg.CORBA.Object val = in.read_Object();
        return narrow(val);
    }

    public static void
    write(org.omg.CORBA.portable.OutputStream out, Est_Genome val)
    {
        out.write_Object(val);
    }

    public static Est_Genome
    narrow(org.omg.CORBA.Object obj)
    {
        if(obj != null)
        {
            try
            {
                return (Est_Genome)obj;
            }
            catch(ClassCastException ex)
            {
            }

            if(obj._is_a(id()))
            {
                org.omg.CORBA.portable.ObjectImpl impl;
                impl = (org.omg.CORBA.portable.ObjectImpl)obj;
                StubForEst_Genome stub = new StubForEst_Genome();
                stub._set_delegate(impl._get_delegate());
                return stub;
            }
        }

        return null;
    }
}
