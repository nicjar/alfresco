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

// Version: 3.1.1
// License: non-commercial

package alfresco.corba_wrappers.genewise;

//
// IDL:alfresco/corba_wrappers/genewise/Genewise:1.0
//
public abstract class _GenewiseImplBase extends org.omg.CORBA.DynamicImplementation
                                        implements Genewise
{

    //
    // IDL:alfresco/corba_wrappers/Method/input:1.0
    //
    final private void
    _OB_op_input(org.omg.CORBA.ServerRequest _ob_req)
    {
        org.omg.CORBA.NVList _ob_list = _orb().create_list(0);
        org.omg.CORBA.Any _ob_aa0 = _orb().create_any();
        _ob_aa0.type(alfresco.corba_wrappers.InputStructHelper.type());
        _ob_list.add_value("", _ob_aa0, org.omg.CORBA.ARG_IN.value);
        _ob_req.params(_ob_list);

        alfresco.corba_wrappers.InputStruct _ob_a0 = alfresco.corba_wrappers.InputStructHelper.extract(_ob_aa0);

        input(_ob_a0);
    }

    //
    // IDL:alfresco/corba_wrappers/Method/run:1.0
    //
    final private void
    _OB_op_run(org.omg.CORBA.ServerRequest _ob_req)
    {
        org.omg.CORBA.NVList _ob_list = _orb().create_list(0);
        _ob_req.params(_ob_list);

        run();
    }

    //
    // IDL:alfresco/corba_wrappers/Method/getGffData:1.0
    //
    final private void
    _OB_op_getGffData(org.omg.CORBA.ServerRequest _ob_req)
    {
        org.omg.CORBA.NVList _ob_list = _orb().create_list(0);
        _ob_req.params(_ob_list);

        try
        {
            alfresco.corba_wrappers.GffDataStruct _ob_r = getGffData();

            org.omg.CORBA.Any _ob_ra = _orb().create_any();
            alfresco.corba_wrappers.GffDataStructHelper.insert(_ob_ra, _ob_r);
            _ob_req.result(_ob_ra);
        }
        catch(alfresco.corba_wrappers.NoOutputException _ob_ex)
        {
            org.omg.CORBA.Any _ob_exa = _orb().create_any();
            alfresco.corba_wrappers.NoOutputExceptionHelper.insert(_ob_exa, _ob_ex);
            _ob_req.except(_ob_exa);
        }
    }
    static final String[] _ob_ids_ =
    {
        "IDL:alfresco/corba_wrappers/genewise/Genewise:1.0",
        "IDL:alfresco/corba_wrappers/Method:1.0"
    };

    public String[]
    _ids()
    {
        return _ob_ids_;
    }

    final public void
    invoke(org.omg.CORBA.ServerRequest _ob_req)
    {
        String _ob_op = _ob_req.op_name();
        final String[] _ob_names =
        {
            "getGffData",
            "input",
            "run"
        };

        int _ob_left = 0;
        int _ob_right = _ob_names.length;
        int _ob_m;
        int _ob_index = -1;

        while(_ob_left < _ob_right)
        {
            _ob_m = (_ob_left + _ob_right) / 2;
            int _ob_res = _ob_names[_ob_m].compareTo(_ob_op);
            if(_ob_res == 0)
            {
                _ob_index = _ob_m;
                break;
            }
            if(_ob_res > 0)
                _ob_right = _ob_m;
            else
                _ob_left = _ob_m + 1;
        }

        switch(_ob_index)
        {
        case 0: // getGffData
            _OB_op_getGffData(_ob_req);
            return;

        case 1: // input
            _OB_op_input(_ob_req);
            return;

        case 2: // run
            _OB_op_run(_ob_req);
            return;
        }

        throw new org.omg.CORBA.BAD_OPERATION();
    }
}
