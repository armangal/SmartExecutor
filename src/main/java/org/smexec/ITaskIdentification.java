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
package org.smexec;

/**
 * The framework collect statistics about tasks executed by it. The default task identification is it's full
 * class name but here we give ability to provide other name under it those statistics will be collected and
 * shown in monitoring system and logs.
 */
public interface ITaskIdentification {

    /**
     * @return the task ID
     */
    String getTaskId();
}
