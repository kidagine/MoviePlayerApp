/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.gui.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import privatemoviecollection.be.Category;
import privatemoviecollection.be.Movie;
import privatemoviecollection.be.User;
import privatemoviecollection.bll.BllManager;
import privatemoviecollection.bll.IBllFacade;
import privatemoviecollection.bll.exceptions.BllException;
import privatemoviecollection.bll.util.MovieSearcher;
import privatemoviecollection.gui.exceptions.ModelException;

/**
 *
 * @author Acer
 */
public class MainModel {
    
    private static MainModel instance;
    private IBllFacade bllManager;
    private ObservableList<Movie> movies;
    private static User loggedInUser;
    
    private MainModel() throws ModelException
    {
        bllManager = new BllManager();
        try
        {
            movies = FXCollections.observableArrayList(bllManager.getAllMovies(loggedInUser));     
        }
        catch(BllException e)
        {
            throw new ModelException(e.getMessage());
        }
    }
    
    public static MainModel createInstance() throws ModelException
    {
        if(instance == null)
        {
            instance = new MainModel();
        }
        return instance;
    }
    
    public static void setUser(User user)
    {
        loggedInUser = user;
    }
    
    public ObservableList<Movie> getMovies()
    {
        return movies;
    }

    public void createMovie(String title, List<Category> categories, String path, int time, Integer rating, String pathToImage) throws ModelException
    {
        try
        {
            LocalDate currentDate = LocalDate.now();
            Movie createdMovie = bllManager.createMovie(loggedInUser, title, categories, path, time, rating, pathToImage, currentDate);
            movies.add(createdMovie);
        }
        catch(BllException e)
        {
            throw new ModelException(e.getMessage());
        }
    }
    
    public void updateMovie(Movie movie, String title, List<Category> categories, String path, int time, Integer rating, String pathToImage) throws ModelException
    {
        try
        {
            Movie updatedMovie = bllManager.updateMovie(movie, title, categories, path, time, rating, pathToImage);
            updateListOfMovies(updatedMovie);
        }
        catch(BllException e)
        {
            throw new ModelException(e.getMessage());
        }
    }
    
    public void updateMovieLastView(Movie movie) throws ModelException
    {
        try
        {
            LocalDate currentDate = LocalDate.now();
            bllManager.updateMovieLastView(movie, currentDate);
        }
        catch(BllException e)
        {
            throw new ModelException(e.getMessage());
        }
    }
    
    private void updateListOfMovies(Movie movie)
    {
        int index = movies.indexOf(movie);
        movies.set(index, movie);
    }
    
    public void deleteMovie(Movie movie) throws ModelException{
        try
        {
            bllManager.deleteMovie(movie);
            movies.remove(movie);
        }
        catch(BllException e)
        {
            throw new ModelException(e.getMessage());
        }
    }
    
    public void deleteCategoryFromAllMovies(Category category)
    {
        for(Movie movie : movies)
        {
            if(movie.removeCategory(category))
            {
                updateMovieCategories(movie);
                updateListOfMovies(movie);
            }
        }
    }
    
    private void updateMovieCategories(Movie movie)
    {
        List<Category> categories = new ArrayList();
        for(Category category : movie.getCategories())
        {
            categories.add(category);
        }
        movie.setCategories(categories);
    }
    
    public ObservableList<Movie> getFilteredMovies(List<Category> categories, String filter, Integer rating)
    {
        ObservableList<Movie> filteredMovies = FXCollections.observableArrayList(MovieSearcher.searchMoviesByCategory(movies, categories));
        filteredMovies = FXCollections.observableArrayList(MovieSearcher.searchMoviesByTitle(filteredMovies, filter));
        filteredMovies = FXCollections.observableArrayList(MovieSearcher.searchMoviesWithHigherRating(filteredMovies, rating));
        return filteredMovies;
    }
    
    public ObservableList<Movie> getMoviesToDelete()
    {
        ObservableList<Movie> moviesToDelete = FXCollections.observableArrayList(MovieSearcher.searchMoviesWithLowerRating(movies, 4));
        moviesToDelete = FXCollections.observableArrayList(MovieSearcher.searchMoviesNotSeenRecently(moviesToDelete, 2));
        return moviesToDelete;
    }
    
    
}
