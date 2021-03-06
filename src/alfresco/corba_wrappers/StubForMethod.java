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
public class StubForMethod extends org.omg.CORBA.portable.ObjectImpl
                           implements Method
{
    static final String[] _ob_ids_ =
    {
        "IDL:alfresco/corba_wrappers/Method:1.0",
    };

    public String[]
    _ids()
    {
        return _ob_ids_;
    }

    //
    // IDL:alfresco/corba_wrappers/Method/input:1.0
    //
    public void
    input(InputStruct _ob_a0)
    {
        org.omg.CORBA.Request _ob_req = _request("input");
        org.omg.CORBA.ORB _ob_orb = _orb();

        org.omg.CORBA.Any _ob_aa0 = _ob_req.add_in_arg();
        InputStructHelper.insert(_ob_aa0, _ob_a0);

        _ob_req.invoke();

        java.lang.Exception _ob_ex = _ob_req.env().exception();
        if(_ob_ex != null)
            throw (org.omg.CORBA.SystemException)_ob_ex;
    }

    //
    // IDL:alfresco/corba_wrappers/Method/run:1.0
    //
    public void
    run()
    {
        org.omg.CORBA.Request _ob_req = _request("run");
        org.omg.CORBA.ORB _ob_orb = _orb();

        _ob_req.invoke();

        java.lang.Exception _ob_ex = _ob_req.env().exception();
        if(_ob_ex != null)
            throw (org.omg.CORBA.SystemException)_ob_ex;
    }

    //
    // IDL:alfresco/corba_wrappers/Method/getGffData:1.0
    //
    public GffDataStruct
    getGffData()
        throws NoOutputException
    {
        org.omg.CORBA.Request _ob_req = _request("getGffData");
        org.omg.CORBA.ORB _ob_orb = _orb();

        _ob_req.exceptions().add(NoOutputExceptionHelper.type());

        _ob_req.set_return_type(GffDataStructHelper.type());

        _ob_req.invoke();

        java.lang.Exception _ob_ex = _ob_req.env().exception();
        if(_ob_ex != null)
        {
            org.omg.CORBA.UnknownUserException _ob_uex;
            try
            {
                _ob_uex = (org.omg.CORBA.UnknownUserException)_ob_ex;
            }
            catch(ClassCastException _ob_dummy)
            {
                throw (org.omg.CORBA.SystemException)_ob_ex;
            }

            try
            {
                throw NoOutputExceptionHelper.extract(_ob_uex.except);
            }
            catch(org.omg.CORBA.BAD_OPERATION _ob_dummy)
            {
            }

            throw new org.omg.CORBA.UNKNOWN();
        }

        org.omg.CORBA.Any _ob_ra = _ob_req.return_value();
        return GffDataStructHelper.extract(_ob_ra);
    }
}
