package dev.revere.alley.core.annotation;

import dev.revere.alley.core.lifecycle.IService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Service {
    /**
     * The interface that this service provides. This is crucial for dependency injection.
     */
    Class<? extends IService> provides();

    /**
     * The priority for initialization (lower values run first).
     * Use this to manage dependencies, e.g., ConfigService = 0, MongoService = 10, ProfileService = 20.
     */
    int priority() default 100;
}