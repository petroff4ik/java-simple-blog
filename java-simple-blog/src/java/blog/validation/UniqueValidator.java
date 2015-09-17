/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blog.validation;

import blog.system.loader.Load;
import blog.system.model.Model;
import blog.validation.annotation.Unique;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author petroff
 */
public class UniqueValidator implements ConstraintValidator<Unique, String> {

    private String model_name;
    private String errorMessagename;

    @Override
    public void initialize(Unique constraintAnnotation) {
        model_name = constraintAnnotation.model_name();
        errorMessagename = constraintAnnotation.message();
    }

    /**
     * 1. The address should not be null. 2. The address should have all the
     * data values specified. 3. Pin code in the address should be of atleast 6
     * characters. 4. The country in the address should be of atleast 4
     * characters.
     */
    @Override
    public boolean isValid(String uniue, ConstraintValidatorContext context) {
        Model model = (Model) Load.model.name(model_name);
        if (model == null) {
            return false;
        } else {
            return model.checkUnique(uniue);
        }
    }
}
