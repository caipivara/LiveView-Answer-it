package com.makingiants.answerit.model.calls;

/**
 * Represent a Call from someone
 */
public class Call {

    // ****************************************************************
    // Attributes
    // ****************************************************************

    private String name;// Ex: James Tomas
    private String number; // Ex: 300 499 1234

    // ****************************************************************
    // Constructor
    // ****************************************************************

    public Call(String name, String callLogs) {
        super();

        this.name = name;
        this.number = callLogs;

    }

    // ****************************************************************
    // Accessor methods
    // ****************************************************************

    public String getName() {
        if (name != null) {
            return name;
        } else {
            return "Unknown";
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String call) {
        this.number = call;
    }

}
