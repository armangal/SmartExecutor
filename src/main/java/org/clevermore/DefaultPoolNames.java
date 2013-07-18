/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.clevermore;

/**
 * default names for default pools defined in default configuration XML.
 * 
 * @author armang
 */
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
