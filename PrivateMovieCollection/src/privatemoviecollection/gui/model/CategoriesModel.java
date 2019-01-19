/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import privatemoviecollection.be.Category;
import privatemoviecollection.be.User;
import privatemoviecollection.bll.BllManager;
import privatemoviecollection.bll.IBllFacade;
import privatemoviecollection.bll.exceptions.BllException;
import privatemoviecollection.gui.exceptions.ModelException;
import privatemoviecollection.gui.util.WarningDisplayer;

/**
 *
 * @author Acer
 */
public class CategoriesModel {
    
    private static CategoriesModel instance;
    private IBllFacade bllManager;
    private ObservableList<Category> categories;
    private MainModel mainModel;
    private static User loggedInUser;
    private WarningDisplayer warningDisplayer;
    
    private CategoriesModel() throws ModelException
    {
        bllManager = new BllManager();
        warningDisplayer = new WarningDisplayer();
        try
        {
            categories = FXCollections.observableArrayList(bllManager.getAllCategories(loggedInUser));
        }
        catch(BllException e)
        {
            throw new ModelException(e.getMessage());
        }
        mainModel = MainModel.createInstance();
    }
    
    public static CategoriesModel createInstance() throws ModelException
    {
        if(instance == null)
        {
            instance = new CategoriesModel();
        }
        return instance;
    }
    
    public static void setUser(User user)
    {
        loggedInUser = user;
    }
    
    public ObservableList<Category> getCategories()
    {
        return categories;
    }
    
    public void createCategory(String name) throws ModelException
    {
        if(isCategoryExisting(name))
        {
            throw new ModelException(name + " is already in your categories");
        }
        try
        {
            Category createdCategory = bllManager.createCategory(loggedInUser, name);
            categories.add(createdCategory);
        }
        catch(BllException e)
        {
            throw new ModelException(e.getMessage());
        }
    }
    
    private boolean isCategoryExisting(String name)
    {
        for(Category category : categories)
        {
            if(category.getName().equals(name))
            {
                return true;
            }
        }
        return false;
    }
    
    public void deleteCategory(Category category) throws ModelException
    {
        try
        {
            bllManager.deleteCategory(category);
            mainModel.deleteCategoryFromAllMovies(category);
            categories.remove(category);
        }
        catch(BllException e)
        {
            throw new ModelException(e.getMessage());
        }
    }
    
}
