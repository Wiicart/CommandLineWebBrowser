package net.wiicart.webcli;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Signifies what errors may be thrown via LoadFailureException
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ErrorStatus {

    /**
     * What codes may be thrown.
     * Includes all HTTP codes, as well as custom codes from 700 & above.
     * Custom Codes: 750 - file not found, 700 - unknown host, 710 - image error
     * @return An array of possible codes.
     */
    int[] codes();

}
