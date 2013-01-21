package org.smexec.mbeans;

import java.util.Date;

public class Stats
    implements StatsMBean {

    public Stats() {}

    @Override
    public String getName() {
        return "Nameee:" + new Date();
    }

    @Override
    public void printStats() {

        printStats("blabla");
    }

    @Override
    public String printStats(String text) {
        System.out.println(text);
        return getName();
    }

}
