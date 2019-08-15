package com.dc.redistool.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class KeyValidatorClass implements ConstraintValidator<KeyValidator,String>{
	
	@Override
	public void initialize(KeyValidator constraintAnnotation) {
	}

	@Override
	public boolean isValid(String arg0, ConstraintValidatorContext arg1) {
		if(arg0==null||arg0.isEmpty()) return false;
		return true;
	}

}
