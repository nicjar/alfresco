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
// IDL:alfresco/corba_wrappers/InputStruct:1.0
//
final public class InputStructHelper
{
    public static void
    insert(org.omg.CORBA.Any any, InputStruct val)
    {
        org.omg.CORBA.portable.OutputStream out = any.create_output_stream();
        write(out, val);
        any.read_value(out.create_input_stream(), type());
    }

    public static InputStruct
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
            members[0].name = "names";
            members[0].type = string_seqHelper.type();

            members[1] = new org.omg.CORBA.StructMember();
            members[1].name = "seqs";
            members[1].type = string_seqHelper.type();

            members[2] = new org.omg.CORBA.StructMember();
            members[2].name = "parameters";
            members[2].type = string_seqHelper.type();

            typeCode_ = orb.create_struct_tc(id(), "InputStruct", members);
        }

        return typeCode_;
    }

    public static String
    id()
    {
        return "IDL:alfresco/corba_wrappers/InputStruct:1.0";
    }

    public static InputStruct
    read(org.omg.CORBA.portable.InputStream in)
    {
        InputStruct val = new InputStruct();
        val.names = string_seqHelper.read(in);
        val.seqs = string_seqHelper.read(in);
        val.parameters = string_seqHelper.read(in);
        return val;
    }

    public static void
    write(org.omg.CORBA.portable.OutputStream out, InputStruct val)
    {
        string_seqHelper.write(out, val.names);
        string_seqHelper.write(out, val.seqs);
        string_seqHelper.write(out, val.parameters);
    }
}