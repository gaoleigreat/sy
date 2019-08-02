package com.lego.survey.zuul.predicate;

/**
 * @author yanglf
 * @description
 * @since 2019/7/12
 **/
public class RibbonVersionHolder {

    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();


    public static String getContext() {
        return CONTEXT.get();
    }

    public static void setContext(String value) {
        CONTEXT.set(value);
    }

    public static void clearContext() {
        CONTEXT.remove();
    }

}
