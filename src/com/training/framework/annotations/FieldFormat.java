package com.training.framework.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.FIELD;

@Retention(RUNTIME)
@Target(FIELD)
public @interface FieldFormat {
    String value() default "yyyy-MM-dd"; // 日期格式
}