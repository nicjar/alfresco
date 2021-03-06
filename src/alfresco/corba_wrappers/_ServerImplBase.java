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
// IDL:alfresco/corba_wrappers/Server:1.0
//
public abstract class _ServerImplBase extends org.omg.CORBA.DynamicImplementation
                                      implements Server
{
    static final String[] _ob_ids_ =
    {
        "IDL:alfresco/corba_wrappers/Server:1.0",
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

        if(_ob_op.equals("getMethod"))
        {
            org.omg.CORBA.Any _ob_aa0 = _ob_orb.create_any();
            _ob_aa0.type(_ob_orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_string));
            _ob_list.add_value("", _ob_aa0, org.omg.CORBA.ARG_IN.value);
            _ob_req.params(_ob_list);

            String _ob_a0 = _ob_aa0.extract_string();

            try
            {
                Method _ob_r = getMethod(_ob_a0);

                org.omg.CORBA.Any _ob_ra = _ob_orb.create_any();
                MethodHelper.insert(_ob_ra, _ob_r);
                _ob_req.result(_ob_ra);
            }
            catch(NoSuchMethodException _ob_ex)
            {
                org.omg.CORBA.Any _ob_exa = _ob_orb.create_any();
                NoSuchMethodExceptionHelper.insert(_ob_exa, _ob_ex);
                _ob_req.except(_ob_exa);
            }
        }
        else if(_ob_op.equals("getAvailableMethods"))
        {
            _ob_req.params(_ob_list);

            String[] _ob_r = getAvailableMethods();

            org.omg.CORBA.Any _ob_ra = _ob_orb.create_any();
            string_seqHelper.insert(_ob_ra, _ob_r);
            _ob_req.result(_ob_ra);
        }
        else
            throw new org.omg.CORBA.BAD_OPERATION();
    }
}
