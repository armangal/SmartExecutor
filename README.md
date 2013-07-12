<h1>Smart Executor</h1>

----

Smart Executor is a light-weight thread execution and thread-pools management framework. 
<br>
The goal of the framework is to provide controllable tasks execution environmet while keeping the executions under tight control and monitoring.

<h2>Motivation</h2>
- Developers tend to create countless Executors ("Wild Executors") or Threads/Timers ("Wild Threads") all over the application without proper configurations and documentations. Later we find ourselves reviewing thread dumps where thread names means nothing to us and their amount is not consistent and performance aspect is totally ignored.
- We want to understand deeper logical features performance in order to get better control over application life-cycle, for example we have important (IM) operations and less-important (LI), when we use one thread pool to execute those operations together delays in execution of LI operations will cause IM operations to be delayed us well and even been rejected by the thread pool when queue becomes full. By using SmartExecutor the situation is easly solved, we'll define two separate thread pool for each operation, those both logic tasks will not interfere each-other.


<h2 name="features">Features</h2>
- Configurable Thread Pools
- Extensive statistics over the pools performance
-- Per Thread Pool
-- Per Task ID
- Tracking source of task execution and printing in logs once problems occurs during execution.
- JMX monitoring        
- Web-based monitoring UI (as a separate GWT project)

<h2>Members:</h2>
<b>Owner: </b>
- Arman Gal

<b>Contributors:</b>
* Daniel Gyupchanov
* Erdoan Veliev



----
Copyright (C) 2013 Arman Gal

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
