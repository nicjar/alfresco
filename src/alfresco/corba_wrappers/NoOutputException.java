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
// IDL:alfresco/corba_wrappers/NoOutputException:1.0
//
final public class NoOutputException extends org.omg.CORBA.UserException
{
    public
    NoOutputException()
    {
    }

    public
    NoOutputException(String _ob_a0)
    {
        reason = _ob_a0;
    }

    public String reason;
}
