package edu.wit.mobileapp.firetime.controllers.fragments;

//region Imports
import java.util.Date;
//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Interface for returning the returns of the date picker back to the class that opened it
public interface InterfaceDateCommunicator {
    void sendDateRequestCode(int code, int resultCode, Date date);
}

