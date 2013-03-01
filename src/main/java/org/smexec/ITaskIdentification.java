package org.smexec;

/**
 * The framework collect statistics about tasks executed by it. The deafult task identification is it's full
 * class name but here we give ability to use other name for those statistics
 */
public interface ITaskIdentification {

    /**
     * @return the task ID
     */
    String getTaskId();
}
