package org.smexec;

/**
 * One of the options to control the destination thread pool is to implement this interface by Runnable or Callble and provide the
 * pool name
 */
public interface IThreadPoolAware {

    /**
     * @return the destination pool name.
     */
    IPoolName getPoolName();
}
