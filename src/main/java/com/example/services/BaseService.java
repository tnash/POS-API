package com.example.services;


import java.io.Serializable;
import org.springframework.data.repository.CrudRepository;

/**
 * This a helper class that offers some default methods to operate on objects via repositories.
 */
abstract public class BaseService<T, I extends Serializable> {

    /**
     * The repository to support the object
     *
     * @return The repository.
     */
    abstract protected CrudRepository<T, I> getRepository();

    /**
     * This will return the object based on the id.
     *
     * @param id The id to search for.
     * @return The object with the passed in id.
     */
    public T get(final I id) {
        return getRepository().findOne(id);
    }

    /**
     * This will delete the object in question.
     *
     * @param id The id of the object to delete.
     */
    public void delete(final I id) {
        getRepository().delete(id);
    }

    /**
     * This will save the object in question.
     *
     * @param object The object to save.
     */
    public T save(final T object) {
        return getRepository().save(object);
    }
}
