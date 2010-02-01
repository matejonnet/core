package org.jboss.weld.environment.se.test.events;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.se.ShutdownManager;
import org.jboss.weld.environment.se.events.ContainerInitialized;

public class Foo
{
   
   private static boolean observedEventTest;
   
   public static void reset()
   {
      observedEventTest = false;
   }
   
   public static boolean isObservedEventTest()
   {
      return observedEventTest;
   }
   
   @Inject
   @EventQualifier1
   private Event<Bar> eventTest;

   public void start(@Observes ContainerInitialized event, ShutdownManager shutdownManager)
   {
      eventTest.fire(new Bar());
   }

   public void observeEventTest(@Observes @EventQualifier2 Bar eventTest)
   {
      this.observedEventTest = true;
   }

}
