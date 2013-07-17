/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.smexec;

/**
 * One of the options to control the destination thread name suffix is to implement this interface by Runnable
 * or Callble and provide the thread name suffix.
 */
public interface IThreadNameSuffixAware {

    /**
     * @return the destination thread suffix name.
     */
    String getThreadNameSuffix();
}
