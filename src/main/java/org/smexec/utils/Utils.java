/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.smexec.utils;

import org.smexec.DefaultPoolNames;
import org.smexec.IPoolName;
import org.smexec.ITaskIdentification;
import org.smexec.IThreadNameSuffixAware;
import org.smexec.IThreadPoolAware;
import org.smexec.TaskMetadata;
import org.smexec.annotation.ThreadNameSuffix;

public class Utils {

    private static final String EMPTY_STRING = "";

/*    static TaskMetadata newDefaultMetadata(Object task) {
        return fillMetaData(TaskMetadata.newDefaultMetadata(), task);
    }

    static TaskMetadata newMetadata(IPoolName poolName, Object task) {
        return fillMetaData(TaskMetadata.newMetadata(poolName), task);
    }

    static TaskMetadata newMetadata(IPoolName poolName, String threadNameSuffix, Object task) {
        return fillMetaData(TaskMetadata.newMetadata(poolName, threadNameSuffix), task);
    }

    static TaskMetadata newMetadata(IPoolName poolName, String threadNameSuffix, String taskId, Object task) {
        return fillMetaData(TaskMetadata.newMetadata(poolName, threadNameSuffix, taskId), task);
    }
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
