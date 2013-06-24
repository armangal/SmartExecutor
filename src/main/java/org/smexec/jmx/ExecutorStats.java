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
import org.smexec.configuration.Config;

public class ExecutorStats
    implements ExecutorStatsMXBean {

    private final SmartExecutor smartExecutor;
    private final Config config;

    public ExecutorStats(SmartExecutor smartExecutor, Config config) {
        super();
        this.smartExecutor = smartExecutor;
        this.config = config;
    }

    @Override
    public String getName() {
        return config.getExecutorConfiguration().getName();
    }

    @Override
    public String getDescription() {
        return config.getExecutorConfiguration().getDescription();
    }

    @Override
    public int getActivePools() {
        return smartExecutor.getActivePools();
    }

}
