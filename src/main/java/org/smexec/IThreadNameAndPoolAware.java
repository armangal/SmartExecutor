/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.smexec;

/**
 * A combined interface of both IThreadNameSuffixAware and IThreadPoolAware
 */
public interface IThreadNameAndPoolAware
    extends IThreadNameSuffixAware, IThreadPoolAware {

}
