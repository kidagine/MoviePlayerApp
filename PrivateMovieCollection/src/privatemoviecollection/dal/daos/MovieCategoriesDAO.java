/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.dal.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import privatemoviecollection.be.Category;
import privatemoviecollection.be.Movie;
import privatemoviecollection.be.User;
import privatemoviecollection.dal.DbConnectionProvider;

/**
 *
 * @author Acer
 */
public class MovieCategoriesDAO {
    
    private DbConnectionProvider connector;
    
    /**
     * Creates connector with database.
     */
    public MovieCategoriesDAO()
    {
        connector = new DbConnectionProvider();
    }
    
    public void addCategoriesToMovie(Movie movie, List<Category> categories) throws SQLException
    {
        String sqlStatement = "INSERT INTO MovieCategories(movieId, categoryId) VALUES(?,?)";
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS))
        {
            for(Category category: categories)
            {
                statement.setInt(1, movie.getId());
                statement.setInt(2, category.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }
    
    public void addCategoriesToAllMovies(User user, List<Movie> allMovies) throws SQLException
    {
        String sqlStatement = "SELECT Movies.id as movieId, Categories.* FROM MovieCategories " +
                                        "INNER JOIN Movies on MovieCategories.movieId=Movies.id " +
                                        "INNER JOIN Categories on MovieCategories.categoryId=Categories.id " + 
                                        "WHERE Categories.userId=?";
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement))
        {
            statement.setInt(1, user.getId());
            ResultSet rs = statement.executeQuery();
            if(rs.next())
            {
                for(Movie m : allMovies)
                {
                    while(!rs.isAfterLast() && rs.getInt("movieId") == m.getId())
                    {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        m.addCategory(new Category(id, name));
                        rs.next();
                    }
                }
            }
        }
    }
    
    public void deleteAllCategoriesFromMovie(Movie movie) throws SQLException
    {
        String sqlStatement = "DELETE FROM MovieCategories WHERE movieId=?";
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement))
        {
            statement.setInt(1, movie.getId());
            statement.execute();
        }
    }
    
    public void deleteCategoryFromAllMovies(Category category) throws SQLException
    {
        String sqlStatement = "DELETE FROM MovieCategories WHERE categoryId=?";
        try(Connection con = connector.getConnection();
                PreparedStatement statement = con.prepareStatement(sqlStatement))
        {
            statement.setInt(1, category.getId());
            statement.execute();
        }
    }
    
}
