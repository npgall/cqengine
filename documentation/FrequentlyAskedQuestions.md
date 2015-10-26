Frequently Asked Questions. For various values of "frequently".


## Myths, Lies and Microbenchmarks: Isn't _x_ milliseconds fast enough? ##

For those not familiar with microbenchmarks, it might be tempting to think that _x_ milliseconds to iterate a collection or perform some task as seen in a microbenchmark, would be fine for application xyz. Unfortunately this neglects an important lesson in designing for scalability: microbenchmarks do not measure absolute performance at all, and it is very dangeorous to design applications around this misconception.

There are factors that come into play in production environments which are never replicated in microbenchmarks. Microbenchmarks can be useful to very roughly gague the **relative** merits of one approach versus another, but **never** can they demonstrate any **absolute** performance (throughput or latency) that would be seen in a real-world application.

To explain further, this question is best answered with another question:

**What is the average latency per request for a queue of 10 requests where each request takes 10ms to process?**

Here is a nice diagram:
![http://cqengine.googlecode.com/svn/wiki/images/latency-in-a-queue.png](http://cqengine.googlecode.com/svn/wiki/images/latency-in-a-queue.png)

Overall latency per request, is processing time for that request, plus the sum of processing times for requests ahead of it in the queue. Or overall latency = **_processing time_** + **_queueing delay_**.

Some interesting facts about this:
  * Average latency is 50ms
  * 50% of requests take longer than 50ms to process
  * 10% of requests take 100ms to process

**Queue-like behaviour and CPU starvation occur in applications implicitly when spikes in traffic occur.**

Even in applications which do not have explicit queues, queue-like behaviour occurs behind any synchronized logic, blocking IO calls, or behind any processing step which cannot accept requests faster than they are received. _Queueing delay_ multiplies latencies by orders of magnitude. Failure to account for queueing delay in the design of applications, is a major reason for the sudden unresponsiveness of applications under high load: queues just start growing, number of threads explodes, latency skyrockets. Sometimes components processing requests might report that processing time is reasonable, yet latency might be high.

Anecdote: If you suspect that a particular database query is slowing down your application, but the DBA says its processing time is 10ms, the correct response is _"what's the queue length?"_.

When designing applications, even looking at **average** response time for various workloads alone is dangerous. From the diagram above it should be clear that _variability_ in response times is both likely, and problematic. If requirements specified that 50ms response time would be acceptable, what about 1 in 10 requests having latency of 100ms? Looking at average response time alone would mask that problem. The _distribution_ of latencies at various workloads or for spikes in traffic is important.

A related culprit is _CPU starvation_, caused by oversubscription of number of threads-to-CPU cores, on CPU-bound workloads (such as iteration). If you have 8 threads trying to iterate collections in parallel, and only 4 cores, latency per request will _more than_ double (it gets compounded by the overhead of _context-switching_). Tying up threads to process requests for longer, additionally _compounds_ the problem by requiring more threads to be created in thread pools, to handle additional incoming requests.

The answer to this question really is:

**The results of microbenchmarks represent rarely achieved best-case scenarios from laboratory conditions**.

Queueing delay and CPU starvation will often multiply latencies in real applications by orders of magnitude.

This applies to the benchmarks in this project for both iteration and CQEngine: latency for both these approaches will get multiplied by orders of magnitude in production. The goal of CQEngine is to be faster in the first place so that when latency does increase under load, it will increase from a much lower starting point.

## Which Transaction Isolation levels does CQEngine support? ##

  * See TransactionIsolation