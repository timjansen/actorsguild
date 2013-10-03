Actor's Guild
==============

Actors Guild was an experimental Java framework to make concurrent programming easier. 
It combined the concept of Actors with Futures. The result is a small API that let's you write concurrent, 
multi-threaded code almost like regular Java classes.

While the concept was interesting, in order to put it into practical use a huge amount of infrastructure would 
have been required. It is not really possible to combine AG with, say, Spring. Most libraries do not provide
the multi-threading safe, immutable classes that AG requires. 


Example of a simple actor:

```java
abstract class Counter extends Actor
{
    private int count;

    @Prop abstract int getInitialCount();

    @Initializer
    public void init() {
        count = getInitialCount();
    }

    @Message
    public AsyncResult<Integer> getCount() {
        return result(count);
    }

    @Message
    public AsyncResult<Void> add(int a) {
        count = count + a;
        return noResult();
    }   
}
```


Creating the actor and using its messages:

```java
Agent agent = new DefaultAgent();      // the Agent manages Actors
Counter counter = agent.create(Counter.class, new Props("initialCount", 1));
counter.add(5).await();                // call add(), wait until message has been processed
System.out.println("Result: "+counter.getCount().get());  // will print 6
```

You can find the original documentation <a href="timjansen.github.io/actorsguild/">here</a>.
