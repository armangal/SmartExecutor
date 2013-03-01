package org.smexec.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Any Runnable or Callable class might be annotated with this annotation to instruct the framework what
 * thread name suffix to add
 * 
 * @author armang
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ThreadNameSuffix {

    String threadNameSuffix();
}
