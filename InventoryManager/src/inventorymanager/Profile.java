/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorymanager;

/**
 *
 * @author shubh
 */
public class Profile {
    private static String _id;
    private static String salutation;
    private static String fname;
    private static String lname;
    private static String email;
    private static String mobile;
    private static String gender;
    private static int role;
    private static String address;

    public static String getId() {
        return _id;
    }

    public static void setId(String _id) {
        Profile._id = _id;
    }

    public static void setSalutation(String salutation) {
        Profile.salutation = salutation;
    }

    public static void setFname(String fname) {
        Profile.fname = fname;
    }

    public static void setLname(String lname) {
        Profile.lname = lname;
    }

    public static void setEmail(String email) {
        Profile.email = email;
    }

    public static void setMobile(String mobile) {
        Profile.mobile = mobile;
    }

    public static void setGender(String gender) {
        Profile.gender = gender;
    }

    public static void setRole(int role) {
        Profile.role = role;
    }

    public static void setAddress(String address) {
        Profile.address = address;
    }

    public static String getSalutation() {
        return salutation;
    }

    public static String getFname() {
        return fname;
    }

    public static String getLname() {
        return lname;
    }

    public static String getEmail() {
        return email;
    }

    public static String getMobile() {
        return mobile;
    }

    public static String getGender() {
        return gender;
    }

    public static int getRole() {
        return role;
    }

    public static String getAddress() {
        return address;
    }

    static void setAll(String _id, String salutation, String fname, String lname, String email, String mobile, String gender, int role, String address) {
        Profile._id = _id;
        Profile.salutation = salutation;
        Profile.fname = fname;
        Profile.lname = lname;
        Profile.email = email;
        Profile.mobile = mobile;
        Profile.gender = gender;
        Profile.role = role;
        Profile.address = address;
    }
}
