PERFIDIX 

there are currently two main problems with the perfidix implementation which 
need to be addressed in time. 

= METHOD INVOCATION =
we cannot invoke methods by simply adding the method to an ArrayList of methods
and then invoke them without knowledge of the declaring class or object. the 
current implementation needs the parent to be an instance of Benchmarkable, and 
this is in our way at the moment - we cannot add methods which are not declared 
within instances of Benchmarkable.

one possible solution would be to make the API weaker and allow any object to be 
invoked and use reflection for everything. we don't like the idea of this
because we would lose the explicitness.

= HEAP SPACE =
if Perfidix invokes a method which itself causes an OutOfMemoryError, e.g. 
because it uses up too much heap space, we cannot catch this error in Perfidix
itself and quit gracefully or warn beforehand.

the possible solution would be to use a thread for the invocation and control
the memory allocation of that thread.
