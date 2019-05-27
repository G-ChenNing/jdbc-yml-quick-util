package com.github.wangchenning.leaf.util.validator;

import com.github.wangchenning.leaf.util.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class isMobileValidator implements ConstraintValidator<isMobile,String> {
    private boolean required = false;
    @Override
    public void initialize(isMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (required) {
            return ValidatorUtil.isMobile(value);
        }else{
            if (StringUtils.isEmpty(value)) {
                return true;
            }else{
                return ValidatorUtil.isMobile(value);
            }

        }
    }
}
