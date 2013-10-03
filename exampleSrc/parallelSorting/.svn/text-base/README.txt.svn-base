ParallelSorting is an example that shows how to split a task into several sub-tasks
in order to take advantage of a multi-core system. Its class ParallelSorter sorts a 
simple String list. It splits the list into several smaller arrays that will
then processed in parallel. When two parts have been processed, they will be merged
into a single large list.

Using the parameter 'maxElemPerMessage' you can decide into how many
messages the task will be split. For example, a list with 1 million elements and a 
'maxElemPerMessage' value of 250000 will be split into four messages, and thus up to 
four threads can work on the task.
 
As you can see from the output below, splitting the task into smaller sub-tasks is
not always faster. Each message has an overhead. For optimal performance you need
to distribute the work evenly on all CPU cores with as few messages as possible.


Output on a Intel Core2Quad Q9450 (4 cores):
------------------------------------------
Reference / single-threaded sort done after 65 ms.
Starting sort of 100000 elements with 100000 elements per message...done after 70 ms.
Starting sort of 100000 elements with 50000 elements per message...done after 42 ms.
Starting sort of 100000 elements with 25000 elements per message...done after 28 ms.
Starting sort of 100000 elements with 10000 elements per message...done after 38 ms.
Starting sort of 100000 elements with 1000 elements per message...done after 42 ms.
Starting sort of 100000 elements with 100 elements per message...done after 42 ms.
Starting sort of 100000 elements with 10 elements per message...done after 118 ms.

