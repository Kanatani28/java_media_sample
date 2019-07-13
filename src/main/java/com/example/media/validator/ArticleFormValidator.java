package com.example.media.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.media.form.ArticleForm;

@Component
public class ArticleFormValidator  implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return ArticleForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		
	}

}
