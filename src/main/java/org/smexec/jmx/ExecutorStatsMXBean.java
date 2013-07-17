/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.smexec.jmx;

/**
 * represents one smart executor
 * 
 * @author armang
 */
public interface ExecutorStatsMXBean {

    String getName();

    String getDescription();

    int getActivePools();
}
