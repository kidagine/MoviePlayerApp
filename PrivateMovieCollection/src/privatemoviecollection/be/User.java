/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.be;

/**
 *
 * @author Acer
 */
public class User {

    private int id;
    private String email;
    private String password;

    public User(int id, String email, String password)
    {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public int getId()
    {
        return id;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }

}
