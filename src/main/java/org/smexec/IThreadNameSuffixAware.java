package org.smexec;

/**
 * One of the options to control the destination thread suffix name is to implement this interface by Runnable
 * or Callble and provide the thread suffix name
 */
public interface IThreadNameSuffixAware {

    /**
     * @return the destination thread suffix name.
     */
    String getThreadNameSuffix();
}
