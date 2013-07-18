/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.clevermore.jmx;

import org.clevermore.SmartExecutor;

/**
 * represents one smart executor
 * 
 * @author armang
 */
public class ExecutorStats
    extends AbstractStats
    implements ExecutorStatsMXBean {

    private final static String BEAN_NAME = "org.clevermore:type=SmartExecutor,name=";

    private final SmartExecutor smartExecutor;
    private final String name;
    private final String description;

    public ExecutorStats(final SmartExecutor smartExecutor, final String name, final String description) {
        super(BEAN_NAME + name);
        this.smartExecutor = smartExecutor;
        this.description = description;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getActivePools() {
        return smartExecutor.getActivePools();
    }

}
