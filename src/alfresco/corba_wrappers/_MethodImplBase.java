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
public abstract class _MethodImplBase extends org.omg.CORBA.DynamicImplementation
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

    final public void
    invoke(org.omg.CORBA.ServerRequest _ob_req)
    {
        org.omg.CORBA.ORB _ob_orb = _orb();
        org.omg.CORBA.NVList _ob_list = _ob_orb.create_list(0);
        String _ob_op = _ob_req.op_name();

        if(_ob_op.equals("input"))
        {
            org.omg.CORBA.Any _ob_aa0 = _ob_orb.create_any();
            _ob_aa0.type(InputStructHelper.type());
            _ob_list.add_value("", _ob_aa0, org.omg.CORBA.ARG_IN.value);
            _ob_req.params(_ob_list);

            InputStruct _ob_a0 = InputStructHelper.extract(_ob_aa0);

            input(_ob_a0);
        }
        else if(_ob_op.equals("run"))
        {
            _ob_req.params(_ob_list);

            run();
        }
        else if(_ob_op.equals("getGffData"))
        {
            _ob_req.params(_ob_list);

            try
            {
                GffDataStruct _ob_r = getGffData();

                org.omg.CORBA.Any _ob_ra = _ob_orb.create_any();
                GffDataStructHelper.insert(_ob_ra, _ob_r);
                _ob_req.result(_ob_ra);
            }
            catch(NoOutputException _ob_ex)
            {
                org.omg.CORBA.Any _ob_exa = _ob_orb.create_any();
                NoOutputExceptionHelper.insert(_ob_exa, _ob_ex);
                _ob_req.except(_ob_exa);
            }
        }
        else
            throw new org.omg.CORBA.BAD_OPERATION();
    }
}
