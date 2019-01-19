/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import privatemoviecollection.be.Category;
import privatemoviecollection.be.Movie;
import privatemoviecollection.be.User;
import privatemoviecollection.dal.daos.CategoriesDAO;
import privatemoviecollection.dal.daos.MovieDAO;
import privatemoviecollection.dal.daos.UserDAO;
import privatemoviecollection.dal.exceptions.DalException;

/**
 *
 * @author Acer
 */
public class DalController implements IDalFacade{
    
    private MovieDAO mDao;
    private CategoriesDAO cDao;
    private UserDAO userDao;
    private final static Logger LOGGER = Logger.getLogger(DalController.class.getName());
    
    public DalController()
    {
        mDao = new MovieDAO();
        cDao = new CategoriesDAO();
        userDao = new UserDAO();
    }

    @Override
    public Movie createMovie(User user, String title, List<Category> categories, String path, int time, Integer rating, String pathToImage, LocalDate lastView) throws DalException
    {
        Movie createdMovie = null;
        try
        {
            createdMovie = mDao.createMovie(user, title, categories, path, time, rating, pathToImage, lastView);
        }
        catch(SQLServerException e)
        {
            throw new DalException("Cannot connect to server. Check your internet connection.");
        }
        catch(SQLException e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return createdMovie;
        
    }
    
    @Override
    public List<Movie> getAllMovies(User user) throws DalException
    {
        List<Movie> allMovies = null;
        try
        {
            allMovies = mDao.getAllMovies(user);
        }
        catch(SQLServerException e)
        {
            throw new DalException("Cannot connect to server. Check your internet connection.");
        }
        catch(SQLException e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return allMovies;
    }
    
    @Override
    public Movie updateMovie(Movie movie, String title, List<Category> categories, String path, int time, Integer rating, String pathToImage) throws DalException
    {
        Movie updatedMovie = null;
        try
        {
            updatedMovie = mDao.updateMovie(movie, title, categories, path, time, rating, pathToImage);
        }
        catch(SQLServerException e)
        {
            throw new DalException("Cannot connect to server. Check your internet connection.");
        }
        catch(SQLException e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return updatedMovie;
    }
    
    @Override 
    public void updateMovieLastView(Movie movie, LocalDate lastView) throws DalException
    {
        try
        {
            mDao.updateMovieLastView(movie, lastView);
        }
        catch(SQLServerException e)
        {
            throw new DalException("Cannot connect to server. Check your internet connection.");
        }
        catch(SQLException e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteMovie(Movie movieToDelete) throws DalException
    {
        try
        {
            mDao.deleteMovie(movieToDelete);
        }
        catch(SQLServerException e)
        {
            throw new DalException("Cannot connect to server. Check your internet connection.");
        }
        catch(SQLException e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public Category createCategory(User user, String name) throws DalException
    {
        Category category = null;
        try
        {
            category = cDao.createCategory(user, name);
        }
        catch(SQLServerException e)
        {
            throw new DalException("Cannot connect to server. Check your internet connection.");
        }
        catch(SQLException e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return category;
    }
    
    @Override
    public List<Category> getAllCategories(User user) throws DalException
    {
        List<Category> allCategories = null;
        try
        {
            allCategories = cDao.getAllCategories(user);
        }
        catch(SQLServerException e)
        {
            throw new DalException("Cannot connect to server. Check your internet connection.");
        }
        catch(SQLException e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return allCategories;
    }
    
    @Override
    public void deleteCategory(Category categoryToDelete) throws DalException
    {
        try
        {
            cDao.deleteCategory(categoryToDelete);
        }
        catch(SQLServerException e)
        {
            throw new DalException("Cannot connect to server. Check your internet connection.");
        }
        catch(SQLException e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
    
    @Override
    public User createUser(String email, String password) throws DalException
    {
        User user = null;
        try
        {
            user = userDao.createUser(email, password);
        }
        catch(SQLServerException e)
        {
            throw new DalException("Cannot connect to server. Check your internet connection.");
        }
        catch(SQLException e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return user;
    }

    @Override
    public User getUser(String email, String password) throws DalException
    {
        User user = null;
        try
        {
            user = userDao.getUser(email, password);
        }
        catch(SQLServerException e)
        {          
            throw new DalException("Cannot connect to server. Check your internet connection.");
        }
        catch(SQLException e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }   
        return user;
    }
    
}
