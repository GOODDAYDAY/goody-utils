package com.goody.utils.xueya.bean;

/**
 * definition of bean
 *
 * @author Goody
 * @version 1.0, 2022/5/9
 * @since 1.0.0
 */
public class BeanDefinition {
    /** bean name */
    private String name;
    /** class of bean */
    private Class<?> clazz;
    /** scope of bean */
    private Scope scope;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }
}
