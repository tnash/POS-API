package com.example.controllers;


import java.io.Serializable;
import com.example.services.BaseService;


/**
 * This is a base controller that offers some default methods to allow for consolidation and tuning of code.
 */
abstract public class BaseController<T, I extends Serializable> {

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final String DEFAULT_SORT = "id";

    /**
     * The getter for the main service for this class.
     *
     * @return The main service for for this class.
     */
    abstract protected BaseService<T, I> getService();

    /**
     * This is a method that will get an object by its primary id.
     *
     * @param id The primary id to use
     * @return The populated object.
     */
    public T get(final I id) {
        return getService().get(id);
    }

}
