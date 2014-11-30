package test;

import java.util.concurrent.Callable;
public class Task implements Callable {
   private int seq;
   public Task() {}
   public Task(int i) { seq = i; }

   public Object call() {
      String str = "";
      long begTest = new java.util.Date().getTime();
      System.out.println("start - Task "+seq);
      try {
         // sleep for 1 second to simulate a remote call,
         // just waiting for the call to return
         Thread.sleep(1000);
         // loop that just concatenate a str to simulate
         // work on the result form remote call
         for(int i = 0; i < 20000; i++)
            str = str + 't';
      } catch (InterruptedException e) {}
      Double secs = new Double((new java.util.Date().getTime() - begTest)*0.001);
      System.out.println("run time " + secs + " secs");
      return seq;
    }
}