package com.dc.redistool.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class KeyValidatorClass implements ConstraintValidator<KeyValidator,Integer>{

	@Override
	public boolean isValid(Integer arg0, ConstraintValidatorContext arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
