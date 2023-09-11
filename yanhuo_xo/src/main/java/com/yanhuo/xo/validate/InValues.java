package com.yanhuo.xo.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented//
@Constraint(validatedBy = {InValueConstraintValidator.class})// 使用哪个校验器进行校验的（这里不指定，在初始化的时候指定）
public @interface InValues {
    // 默认会找ValidationMessages.properties
    String message() default "类型只能是0，1";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // 可以指定数据只能是vals数组指定的值
    int[] vals() default {};
}
