/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.gui.model;

import privatemoviecollection.be.User;
import privatemoviecollection.bll.BllManager;
import privatemoviecollection.bll.IBllFacade;
import privatemoviecollection.bll.exceptions.BllException;
import privatemoviecollection.gui.exceptions.ModelException;

/**
 *
 * @author Acer
 */
public class UserModel {
    
    private static UserModel instance;
    private IBllFacade bllManager;

    private UserModel()
    {
        bllManager = new BllManager();
    }

    public static UserModel createInstance()
    {
        if(instance == null)
        {
            instance = new UserModel();
        }
        return instance;
    }
    
    public void setUser(User user)
    {
        MainModel.setUser(user);
        CategoriesModel.setUser(user);
    }

    public User createUser(String email, String password) throws ModelException
    {
        try
        {
            return bllManager.createUser(email, password);
        }
        catch(BllException e)
        {
            throw new ModelException(e.getMessage());
        }
    }

    public User getUser(String email, String password) throws ModelException
    {
        try
        {
            return bllManager.getUser(email, password);
        }
        catch(BllException e)
        {
            throw new ModelException(e.getMessage());
        }
    }
}
