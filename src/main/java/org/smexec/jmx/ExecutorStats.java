/**
 * Copyright (C) 2013 Arman Gal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smexec.jmx;

import org.smexec.SmartExecutor;

/**
 * represents one smart executor
 * 
 * @author armang
 */
public class ExecutorStats
    extends AbstractStats
    implements ExecutorStatsMXBean {

    private final static String BEAN_NAME = "org.smexec:type=SmartExecutor,name=";

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
