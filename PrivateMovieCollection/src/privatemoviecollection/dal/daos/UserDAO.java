/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.dal.daos;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import privatemoviecollection.be.User;
import privatemoviecollection.dal.DbConnectionProvider;
import privatemoviecollection.dal.exceptions.DalException;

/**
 *
 * @author Acer
 */
public class UserDAO {
    
    private DbConnectionProvider connector;
    private CategoriesDAO cDao;
    
    /**
     * Creates connector with database and DAO for PlaylistSongs.
     */
    public UserDAO()
    {
        connector = new DbConnectionProvider();
        cDao = new CategoriesDAO();
    }
    
    /**
     * Creates user in database.
     * 
     * @param email The users e-mail address.
     * @param password The users password.
     * @return Created user.
     * @throws SQLException if connection with database cannot be established.
     */
    public User createUser(String email, String password) throws SQLException, DalException
    {
        if(isEmailTaken(email))
        {
            throw new DalException("This e-mail address is already in use");
        }
        else
        {
            String sqlStatement = "INSERT INTO Users(email, password) VALUES(?,?)";
            try(Connection con = connector.getConnection();
                    PreparedStatement statement = con.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS))
                    
            {
                statement.setString(1, email);
                statement.setString(2, password);
                ResultSet rs = statement.executeQuery();
                rs.next();
                int id = rs.getInt(1);
                User user = new User(id,email,password);
                cDao.addDefaultCategories(user);
                return user;               
            }
        }
    }
    
    /**
     * Checks if given e-mail address is already in use by some user in database.
     * 
     * @param email The e-mail to check.
     * @return true if e-mail is not in use.
     * @throws SQLException if connection with database cannot be established.
     */
    private boolean isEmailTaken(String email) throws SQLException
    {
        String sqlStatement = "SELECT * FROM Users WHERE email=?";
        try(Connection con = connector.getConnection();
                    PreparedStatement statement = con.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS))
                    
            {
                statement.setString(1, email);
                ResultSet rs = statement.executeQuery();
                if(rs.next()==false)
                {
                    return false;
                }
                else
                {
                    return true;
                }
                
            }
    }
    
    /**
     * Gets the user with given e-mail address and password from database.
     * 
     * @param email The users e-mail address.
     * @param password The users password.
     * @return The user.
     * @throws SQLException if connection with database cannot be established. 
     */
    public User getUser(String email, String password) throws SQLServerException, SQLException, DalException
    {
        String sqlStatement = "SELECT * FROM Users WHERE email=? and password=?";
        try(Connection con = connector.getConnection();
                    PreparedStatement statement = con.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS))
                    
            {
                statement.setString(1, email);
                statement.setString(2, password);
                ResultSet rs = statement.executeQuery();
                if(rs.next()==false)
                {
                    throw new DalException("Invalid e-mail address or password.");
                }
                else
                {
                    int id = rs.getInt("id");
                    return new User(id,email,password);
                }
                
            }
    }
    
}
