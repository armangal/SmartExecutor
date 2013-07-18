/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.clevermore;

import org.clevermore.IPoolName;

public enum TestPoolNames implements IPoolName {
    SCHEDULED_POOL(TestPoolNamesConstants.SCHEDULED), //
    CACHED1(TestPoolNamesConstants.CACHED1), //
    FAST(TestPoolNamesConstants.FAST), //
    CUSTOM1(TestPoolNamesConstants.CUSTOM1)//
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
