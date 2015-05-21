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
// IDL:alfresco/corba_wrappers/GffDataStruct:1.0
//
final public class GffDataStructHelper
{
    public static void
    insert(org.omg.CORBA.Any any, GffDataStruct val)
    {
        org.omg.CORBA.portable.OutputStream out = any.create_output_stream();
        write(out, val);
        any.read_value(out.create_input_stream(), type());
    }

    public static GffDataStruct
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
            org.omg.CORBA.StructMember[] members = new org.omg.CORBA.StructMember[3];

            members[0] = new org.omg.CORBA.StructMember();
            members[0].name = "gff";
            members[0].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);

            members[1] = new org.omg.CORBA.StructMember();
            members[1].name = "cgff";
            members[1].type = orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string);

            members[2] = new org.omg.CORBA.StructMember();
            members[2].name = "suplements";
            members[2].type = string_seqHelper.type();

            typeCode_ = orb.create_struct_tc(id(), "GffDataStruct", members);
        }

        return typeCode_;
    }

    public static String
    id()
    {
        return "IDL:alfresco/corba_wrappers/GffDataStruct:1.0";
    }

    public static GffDataStruct
    read(org.omg.CORBA.portable.InputStream in)
    {
        GffDataStruct val = new GffDataStruct();
        val.gff = in.read_string();
        val.cgff = in.read_string();
        val.suplements = string_seqHelper.read(in);
        return val;
    }

    public static void
    write(org.omg.CORBA.portable.OutputStream out, GffDataStruct val)
    {
        out.write_string(val.gff);
        out.write_string(val.cgff);
        string_seqHelper.write(out, val.suplements);
    }
}