<h1>Smart Executor</h1>
<i>The framework is still under development, more documentation will be available soon.</i>
----

SmartExecutor is a light-weight thread execution and thread-pools management framework. <br>
The goal of the framework is to provide easy and controllable threads execution environmet.

<h2>Motivation</h2>
- Developers tend to create countless Executors ("Wild Executors") or Threads/Timers ("Wild Threads") all over the application without proper configurations and documentations. Later we find ourselves reviewing thread dumps where thread names means nothing to us and their amount is not consistent.
- We want to distinguished where logical features are executed in order to get better control over application life-cycle, for example we have important (I) operations and less-important (LI), when we use one thread pool to execute those operations together  delays in execution of LI operations will cause I operations to be delayed us well and even rejected by the thread pool when queue becomes full.

<h2 name="features">Features</h2>
- Configurable Thread Pools
- Pre/Pos Execution Hooks
- Extensive statistics over the pools performance
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
