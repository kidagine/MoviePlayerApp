/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.bll;

import java.time.LocalDate;
import java.util.List;
import privatemoviecollection.be.Category;
import privatemoviecollection.be.Movie;
import privatemoviecollection.be.User;
import privatemoviecollection.bll.exceptions.BllException;
import privatemoviecollection.dal.DalController;
import privatemoviecollection.dal.IDalFacade;
import privatemoviecollection.dal.exceptions.DalException;

/**
 *
 * @author Acer
 */
public class BllManager implements IBllFacade{
    
    private IDalFacade dalController;
    
    public BllManager()
    {
        dalController = new DalController();
    }

    @Override
    public Movie createMovie(User user, String title, List<Category> categories, String path, int time, Integer rating, String pathToImage, LocalDate lastView) throws BllException
    {
        try
        {
            return dalController.createMovie(user, title, categories, path, time, rating, pathToImage, lastView);
        }
        catch(DalException e)
        {
            throw new BllException(e.getMessage());
        }
    }
    
    @Override
    public List<Movie> getAllMovies(User user) throws BllException
    {
        try
        {
            return dalController.getAllMovies(user);
        }
        catch(DalException e)
        {
            throw new BllException(e.getMessage());
        }
    }
    
    @Override
    public Movie updateMovie(Movie movie, String title, List<Category> categories, String path, int time, Integer rating, String pathToImage) throws BllException
    {
        try
        {
            return dalController.updateMovie(movie, title, categories, path, time, rating, pathToImage);
        }
        catch(DalException e)
        {
            throw new BllException(e.getMessage());
        }
    }
    
    @Override
    public void updateMovieLastView(Movie movie, LocalDate lastView) throws BllException
    {
        try
        {
            dalController.updateMovieLastView(movie, lastView);
        }
        catch(DalException e)
        {
            throw new BllException(e.getMessage());
        }
    }
    
    @Override
    public void deleteMovie(Movie movieToDelete) throws BllException
    {
        try
        {
            dalController.deleteMovie(movieToDelete);
        }
        catch(DalException e)
        {
            throw new BllException(e.getMessage());
        }
    }

    @Override
    public Category createCategory(User user, String name) throws BllException
    {
        try
        {
            return dalController.createCategory(user, name);
        }
        catch(DalException e)
        {
            throw new BllException(e.getMessage());
        }
    }
    
    @Override
    public List<Category> getAllCategories(User user) throws BllException
    {
        try
        {
            return dalController.getAllCategories(user);
        }
        catch(DalException e)
        {
            throw new BllException(e.getMessage());
        }
    }
    
    @Override
    public void deleteCategory(Category categoryToDelete) throws BllException
    {
        try
        {
            dalController.deleteCategory(categoryToDelete);
        }
        catch(DalException e)
        {
            throw new BllException(e.getMessage());
        }
    }
    
    @Override
    public User createUser(String email, String password) throws BllException{
        try
        {
        return dalController.createUser(email,password);
        }
        catch(DalException e)
        {
            throw new BllException(e.getMessage());
        }
    }

    @Override
    public User getUser(String email, String password) throws BllException{
        try
        {
            return dalController.getUser(email, password);
            
        }
        catch(DalException e)
        {
            throw new BllException(e.getMessage());
        }
    }
    
}
