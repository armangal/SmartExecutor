package org.smexec;

/**
 * A wrapper Runnble for internal usage. <br>
 * I allows us to better control the thread execution, hooks and thread naming.
 */
public class SmartRunnble
    implements Runnable {

    private Runnable runnable;
    private String threadNameSuffix;

    public SmartRunnble(Runnable runnable, String threadNameSuffix) {
        this.runnable = runnable;
        this.threadNameSuffix = threadNameSuffix;
    }

    public SmartRunnble(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void run() {
        String orgName = null;
        try {
            if (threadNameSuffix != null) {
                orgName = Thread.currentThread().getName();
                Thread.currentThread().setName(orgName + "_" + threadNameSuffix);
            }
            // TODO Pre execution HOOK

            runnable.run();

            // TODO Post execution HOOK

        } catch (Exception e) {

        } finally {
            if (orgName != null) {
                Thread.currentThread().setName(orgName);
            }
        }
    }
}
