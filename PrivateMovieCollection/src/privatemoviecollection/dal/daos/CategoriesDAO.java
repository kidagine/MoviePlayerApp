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
import java.util.ArrayList;
import java.util.List;
import privatemoviecollection.be.Category;
import privatemoviecollection.be.User;
import privatemoviecollection.dal.DbConnectionProvider;

/**
 *
 * @author Acer
 */
public class CategoriesDAO {
    
    private DbConnectionProvider connector;
    private MovieCategoriesDAO mcDao;
    
    /**
     * Creates connector with database.
     */
    public CategoriesDAO()
    {
        connector = new DbConnectionProvider();
        mcDao = new MovieCategoriesDAO();
    }
    
    public Category createCategory(User user, String name) throws SQLServerException, SQLException
    {
        String sqlStatement = "INSERT INTO Categories(userId, name) VALUES(?,?)";
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setInt(1, user.getId());
            statement.setString(2, name);
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            return new Category(id, name);
            
        }
    }
    
    public List<Category> getAllCategories(User user) throws SQLServerException, SQLException
    {
        String sqlStatement = "SELECT * FROM Categories WHERE userId=?";
        List<Category> allCategories = new ArrayList();
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement))
        {
            statement.setInt(1, user.getId());
            ResultSet rs = statement.executeQuery();
            while(rs.next())
            {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                allCategories.add(new Category(id, name));
            }
            return allCategories;
        }
        
    }
    
    public void deleteCategory(Category categoryToDelete) throws SQLServerException, SQLException
    {
        mcDao.deleteCategoryFromAllMovies(categoryToDelete);
        String sqlStatement = "DELETE FROM Categories WHERE id=?";
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement))
        {
            statement.setInt(1, categoryToDelete.getId());
            statement.execute();
        }
    }
    
    public void addDefaultCategories(User user) throws SQLServerException, SQLException
    {
        List<String> defaultCategories = new ArrayList();
        String sqlStatement = "SELECT * FROM DefaultCategories";
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement))
        {
            ResultSet rs = statement.executeQuery();
            while(rs.next())
            {
                defaultCategories.add(rs.getString("name"));
            }
        }
        sqlStatement = "INSERT INTO Categories(userId, name) VALUES(?, ?)";
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement))
        {
            for(String categoryName : defaultCategories)
            {
                statement.setInt(1, user.getId());
                statement.setString(2, categoryName);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }
    
}
