/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.smexec;

/**
 * One of the options to control the destination thread pool is to implement this interface by Runnable or
 * Callble and provide the pool name where the task should be executed.
 */
public interface IThreadPoolAware {

    /**
     * @return the destination pool name.
     */
    IPoolName getPoolName();
}
