package org.smexec.mbeans;


public interface StatsMBean {
    String getName();
    
    void printStats();

    String printStats(String text);
    
}
