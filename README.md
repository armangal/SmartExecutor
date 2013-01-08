SmartExecutor
=========

SmartExecutor is a light-weight thread execution framework. 
The goal of the framework is to provide easy controllable threads executions.

<h2>Motivation</h2>
- Developers tend to create countles Executors ("Wild Executors") or Threads/Timers ("Wild Threads") all over the application witout proper configurations and documentations. Later we find ourselfs reviewing thread dumps where thread names means nothing to us and their amount is not consistent.
- We want to distignue where logical features are executed in order to get better contorl over application life-cylce, for example we have important (I) operations and less-important (LI), when we use one thread pool to execute those operations togather, delayes in execution of LI operations will cause I operations to be delayed us well and even rejected by the thread pool when queue becomes full.


<h2 name="features">Features</h2>
- Configurable Thread Pools
- Pre/Pos Execution Hooks
- 
The framework is still under development, more documentation will be available soon.



===========================================================================================================================================================
MIT License

Copyright (c) 2013 Arman Gal

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
