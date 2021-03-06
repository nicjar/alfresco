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

package alfresco.server;

//
// IDL:alfresco/server/AlfrescoServer:1.0
//
public abstract class _AlfrescoServerImplBase extends org.omg.CORBA.DynamicImplementation
                                              implements AlfrescoServer
{
    static final String[] _ob_ids_ =
    {
        "IDL:alfresco/server/AlfrescoServer:1.0",
    };

    public String[]
    _ids()
    {
        return _ob_ids_;
    }

    final public void
    invoke(org.omg.CORBA.ServerRequest _ob_req)
    {
        org.omg.CORBA.ORB _ob_orb = _orb();
        org.omg.CORBA.NVList _ob_list = _ob_orb.create_list(0);
        String _ob_op = _ob_req.op_name();

        if(_ob_op.equals("getNames"))
        {
            _ob_req.params(_ob_list);

            String[] _ob_r = getNames();

            org.omg.CORBA.Any _ob_ra = _ob_orb.create_any();
            alfresco.server.AlfrescoServerPackage.namesequenceHelper.insert(_ob_ra, _ob_r);
            _ob_req.result(_ob_ra);
        }
        else if(_ob_op.equals("getPair"))
        {
            org.omg.CORBA.Any _ob_aa0 = _ob_orb.create_any();
            _ob_aa0.type(_ob_orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string));
            _ob_list.add_value("", _ob_aa0, org.omg.CORBA.ARG_IN.value);
            _ob_req.params(_ob_list);

            String _ob_a0 = _ob_aa0.extract_string();

            PairStruct _ob_r = getPair(_ob_a0);

            org.omg.CORBA.Any _ob_ra = _ob_orb.create_any();
            PairStructHelper.insert(_ob_ra, _ob_r);
            _ob_req.result(_ob_ra);
        }
        else
            throw new org.omg.CORBA.BAD_OPERATION();
    }
}
