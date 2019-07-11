package com.lego.survey.base.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
// 可以出现在java文档中
@Documented
// 可以被子注解继承
@Inherited
public @interface Operation {

    String value() default "";

    String desc() default "";
}
