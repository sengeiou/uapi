package uapi.kernel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import uapi.kernel.helper.Null;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Attribute {

    /**
     * Custom service name
     * 
     * @return Service name
     */
    public String name() default "";

    public Class<?> type() default Null.class;

    /**
     * Indicate the service should be initialized at launch time
     * 
     * @return
     */
    public boolean initAtLaunching() default false;
}