package org.smexec;

public enum DefaultPoolNames implements IPoolName {

    DEFAULT("default");

    private String name;

    private DefaultPoolNames(String name) {
        this.name = name;
    }

    @Override
    public String getPoolName() {
        return name;
    }

}
