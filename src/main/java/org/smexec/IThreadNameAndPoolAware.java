package org.smexec;

/**
 * A combined interface of both IThreadNameSuffixAware and IThreadPoolAware
 */
public interface IThreadNameAndPoolAware
    extends IThreadNameSuffixAware, IThreadPoolAware {

}
