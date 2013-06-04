package org.smexec;

public enum TestPoolNames implements IPoolName {
    SCHEDULED_POOL(TestPoolNamesConstants.SCHEDULED), //
    CACHED1(TestPoolNamesConstants.CACHED1), //
    FAST(TestPoolNamesConstants.FAST)//
    ;

    private String name;

    private TestPoolNames(String name) {
        this.name = name;
    }

    @Override
    public String getPoolName() {
        return name;
    }
}
