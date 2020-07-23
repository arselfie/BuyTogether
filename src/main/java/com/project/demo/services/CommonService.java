package com.project.demo.services;

import com.project.demo.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Constraint;
import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CommonService {

    @Autowired
    private LocalValidatorFactoryBean localValidatorFactoryBean;

    public <E> void validate(E object) {

        List<String> errors = new ArrayList<>();
        Set<ConstraintViolation<E>> violations = localValidatorFactoryBean.validate(object);

        violations.forEach(v -> errors.add(v.getMessage()));

        if (!errors.isEmpty()) {

            throw new ValidationException(errors);

        }
    }

    public static <E> boolean objectIn(E object, E... collection){
        if (object == null){
            throw new IllegalArgumentException("Object is null");
        }
        if (collection == null){
            return false;
        }
        if (collection.length == 0){
            return false;
        }
        for (E obj : collection){
            if (obj != null && obj.equals(object)){
                return true;
            }
        }
        return false;
    }

}
