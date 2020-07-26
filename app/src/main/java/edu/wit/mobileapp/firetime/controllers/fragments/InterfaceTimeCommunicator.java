package edu.wit.mobileapp.firetime.controllers.fragments;



/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Interface for returning the returns of the time picker back to the class that opened it
public interface InterfaceTimeCommunicator {
    void sendDateRequestCode(int code, int resultCode, int hour, int minute, int second);
}
