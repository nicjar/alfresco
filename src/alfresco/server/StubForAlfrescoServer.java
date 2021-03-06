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
public class StubForAlfrescoServer extends org.omg.CORBA.portable.ObjectImpl
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

    //
    // IDL:alfresco/server/AlfrescoServer/getNames:1.0
    //
    public String[]
    getNames()
    {
        org.omg.CORBA.Request _ob_req = _request("getNames");
        org.omg.CORBA.ORB _ob_orb = _orb();

        _ob_req.set_return_type(alfresco.server.AlfrescoServerPackage.namesequenceHelper.type());

        _ob_req.invoke();

        java.lang.Exception _ob_ex = _ob_req.env().exception();
        if(_ob_ex != null)
            throw (org.omg.CORBA.SystemException)_ob_ex;

        org.omg.CORBA.Any _ob_ra = _ob_req.return_value();
        return alfresco.server.AlfrescoServerPackage.namesequenceHelper.extract(_ob_ra);
    }

    //
    // IDL:alfresco/server/AlfrescoServer/getPair:1.0
    //
    public PairStruct
    getPair(String _ob_a0)
    {
        org.omg.CORBA.Request _ob_req = _request("getPair");
        org.omg.CORBA.ORB _ob_orb = _orb();

        org.omg.CORBA.Any _ob_aa0 = _ob_req.add_in_arg();
        _ob_aa0.insert_string(_ob_a0);

        _ob_req.set_return_type(PairStructHelper.type());

        _ob_req.invoke();

        java.lang.Exception _ob_ex = _ob_req.env().exception();
        if(_ob_ex != null)
            throw (org.omg.CORBA.SystemException)_ob_ex;

        org.omg.CORBA.Any _ob_ra = _ob_req.return_value();
        return PairStructHelper.extract(_ob_ra);
    }
}
