/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.clevermore;

/**
 * The framework collect statistics about tasks executed by it. The default task identification is it's full
 * class name but here we give ability to provide other name under it those statistics will be collected and
 * shown in monitoring system and logs.
 */
public interface ITaskIdentification {

    /**
     * @return the task ID
     */
    String getTaskId();
}
