package edu.swarthmore.cs.cs71.blueboapplication.Model;


// The purpose of this is to implement onComplete functionality for firebase calls.
public interface Continuation<T> {

    void run(T t);
}
