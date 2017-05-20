package cn.sinjinsong.common.validator.time;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;

/**
 * Created by SinjinSong on 2017/5/5.
 */
public class PastValidator implements ConstraintValidator<Past, Temporal> {

    @Override
    public void initialize(Past constraintAnnotation) {
    }

    @Override
    public boolean isValid(Temporal value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        LocalDateTime ld = LocalDateTime.from(value);
        if (ld.isBefore(LocalDateTime.now())) {
            return true;
        }
        return false;
    }

}