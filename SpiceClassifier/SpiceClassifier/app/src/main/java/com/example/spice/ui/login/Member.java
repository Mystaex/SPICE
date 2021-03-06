package com.example.spice.ui.login;

//Used in the Creating Account Page. This is the inputted object that will be in firebase database.
public class Member
{
    private String userid;
    private String email;
    private String password;
    private String genre;
    private String profession;
    private String utaid;
    private String name;

    //Empty Constructor
    public Member()
    {

    }

    //Constructor that sets values when at least one attribute is entered.
    public Member(String userid, String email, String password, String genre, String profession, String utaid, String name)
    {
        this.userid = userid;
        this.email = email;
        this.password = password;
        this.genre = genre;
        this.profession = profession;
        this.utaid = utaid;
        this.name = name;
    }

    //getter and setter functions
    public String getuserid(){return userid;}
    public void setuserid(String userid){this.userid = userid;}
    public String getemail(){return email;}
    public void setemail(String email){this.email = email;}
    public String getpassword(){return password;}
    public void setpassword(String password){this.password = password;}
    public String getgenre(){return genre;}
    public void setgenre(String genre){this.genre = genre;}
    public String getprofession(){return profession;}
    public void setprofession(String profession){this.profession = profession;}
    public String getutaid(){return utaid;}
    public void setutaid(String utaid){this.utaid = utaid;}
    public String getname(){return name;}
    public void setname(String name){this.name = name;}
}
