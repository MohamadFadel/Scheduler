package com.wavemark.scheduler.schedule.validation.validator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.wavemark.scheduler.schedule.validation.annotation.EmailListValidation;

public class EmailListValidator implements ConstraintValidator<EmailListValidation, String> {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    @Override
    public boolean isValid(String emailList, ConstraintValidatorContext context) {

        if (Objects.isNull(emailList) || emailList.isEmpty())
            return true;

        String[] emails = emailList.split(",");
        List<String> invalidEmails = Arrays.stream(emails)
                .filter(email -> !isValidEmail(email.trim()))
                .collect(Collectors.toList());

        return invalidEmails.isEmpty();
    }

}
