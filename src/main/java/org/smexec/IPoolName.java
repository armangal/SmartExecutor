/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.smexec;

/**
 * In order to make it easy for the library to define it's own pool names but still keep it in order and easy
 * manageable, the framework limits the api the enums implementing this interface.
 * 
 * @author armang
 */
public interface IPoolName {

    String getPoolName();
}
