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

/**
 *
 * @author Acer
 */
public interface IBllFacade {
    
    Movie createMovie(User user, String title, List<Category> categories, String path, int time, Integer rating, String pathToImage, LocalDate lastView) throws BllException;
    
    List<Movie> getAllMovies(User user) throws BllException;
    
    Movie updateMovie(Movie movie, String title, List<Category> categories, String path, int time, Integer rating, String pathToImage) throws BllException;
    
    void updateMovieLastView(Movie movie, LocalDate lastView) throws BllException;
    
    void deleteMovie(Movie movieToDelete) throws BllException;
    
    Category createCategory(User user, String name) throws BllException;
    
    List<Category> getAllCategories(User user) throws BllException;
    
    void deleteCategory(Category categoryToDelete) throws BllException;
    
    User createUser(String email, String password) throws BllException;

    User getUser(String email, String password) throws BllException;
    
}
