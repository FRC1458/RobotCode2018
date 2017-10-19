package org.usfirst.frc.team1458.lib.util.annotation;

import java.lang.annotation.*;

/**
 * Marks a class which is for internal use only
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Internal {

}