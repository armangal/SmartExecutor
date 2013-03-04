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
MIT License

Copyright (c) 2013 Arman Gal

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
