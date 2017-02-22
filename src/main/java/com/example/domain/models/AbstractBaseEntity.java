package com.example.domain.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Base class for model objects to provide base methods and properties common to all models
 */
@MappedSuperclass
public abstract class AbstractBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

//    @Override
//    public String toString() {
//        return ReflectionToStringBuilder.toString(this);
//    }

}
