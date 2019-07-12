package com.lego.survey.zuul.predicate;

/**
 * @author yanglf
 * @description
 * @since 2019/7/12
 **/
public class RibbonVersionHolder {

    private static final ThreadLocal<String> context = new ThreadLocal<>();


    public static String getContext() {
        return context.get();
    }

    public static void setContext(String value) {
        context.set(value);
    }

    public static void clearContext() {
        context.remove();
    }

}
