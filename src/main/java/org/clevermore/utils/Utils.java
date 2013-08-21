/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.clevermore.utils;

import org.clevermore.DefaultPoolNames;
import org.clevermore.IPoolName;
import org.clevermore.ITaskIdentification;
import org.clevermore.IThreadNameSuffixAware;
import org.clevermore.IThreadPoolAware;
import org.clevermore.TaskMetadata;
import org.clevermore.annotation.ThreadNameSuffix;

public class Utils {

    private static final String EMPTY_STRING = "";

    /**
     * Tries to complete that metadata with parameters coming from annotations or interfaces implemented by
     * the task
     * 
     * @param taskMetadata
     * @param task
     * @return
     */
    public static TaskMetadata fillMetaData(TaskMetadata taskMetadata, Object task) {
        if (!taskMetadata.hasPoolName()) {
            taskMetadata.setPoolName(getPoolName(task));
        }

        if (!taskMetadata.hasTaskId()) {
            taskMetadata.setTaskId(getTaskIdentification(task));
        }

        if (!taskMetadata.hasSuffix()) {
            taskMetadata.setThreadNameSuffix(getThreadNameSuffix(task));
        }

        return taskMetadata;
    }

    /**
     * determine the pool name by reading the annotation or interface
     * 
     * @param task
     * @return
     */
    private static IPoolName getPoolName(Object task) {

        if (task instanceof IThreadPoolAware) {
            return ((IThreadPoolAware) task).getPoolName();
        }

        return DefaultPoolNames.DEFAULT;
    }

    private static String getTaskIdentification(Object task) {
        if (task instanceof ITaskIdentification) {
            return ((ITaskIdentification) task).getTaskId();
        } else {
            return task.getClass().getName();
        }
    }

    private static String getThreadNameSuffix(Object task) {
        ThreadNameSuffix tns = task.getClass().getAnnotation(ThreadNameSuffix.class);
        if (tns != null) {
            return tns.threadNameSuffix();
        }
        if (task instanceof IThreadNameSuffixAware) {
            return ((IThreadNameSuffixAware) task).getThreadNameSuffix();
        }
        return EMPTY_STRING;
    }
}
