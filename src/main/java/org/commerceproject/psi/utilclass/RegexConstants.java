package org.commerceproject.psi.utilclass;

public abstract class RegexConstants {
    public static final String PINCODE = "^[1-9]{1}[0-9]{3}\\s{0,1}[0-9]{3}$";
    public static final String PAN_NUMBER = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$";
    public static final String AADHAR_NUMBER = "^[2-9]{1}[0-9]{3}\\s{0,1}[0-9]{4}\\s{0,1}[0-9]{4}$";
    //place other regex constants here
}
