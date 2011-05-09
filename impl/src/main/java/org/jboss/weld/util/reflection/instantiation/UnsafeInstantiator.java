/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.weld.util.reflection.instantiation;

import static org.jboss.weld.logging.messages.ReflectionMessage.UNSAFE_INSTANTIATION_FAILED;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.jboss.weld.exceptions.WeldException;
import org.jboss.weld.util.reflection.SecureReflections;


/**
 * An instantiator for sun.misc.Unsafe
 * 
 * @author Nicklas Karlsson
 *
 */
public class UnsafeInstantiator implements Instantiator
{
   private static final String REFLECTION_CLASS_NAME = "sun.misc.Unsafe";

   private Method allocateInstanceMethod = null;
   private Object unsafeInstance = null;
   
   private static Logger log = Logger.getLogger(ReflectionFactoryInstantiator.class.getName());

   public UnsafeInstantiator()
   {
      try
      {
         Class<?> unsafe = Class.forName(REFLECTION_CLASS_NAME);
         Field accessor = unsafe.getDeclaredField("theUnsafe");
         SecureReflections.ensureAccessible(accessor);
         unsafeInstance = accessor.get(null);
         allocateInstanceMethod = unsafe.getDeclaredMethod("allocateInstance", Class.class);
      }
      catch (Throwable e)
      {
         // TODO Catch explicit subclasses
         // OK to fail
      	log.warning("Exception swollowed:" + e);
      }
   }

   @SuppressWarnings("unchecked")
   public <T> T instantiate(Class<T> clazz)
   {
      T instance = null;
      try
      {
         instance = (T) allocateInstanceMethod.invoke(unsafeInstance, clazz);
      }
      catch (Exception e)
      {
         throw new WeldException(UNSAFE_INSTANTIATION_FAILED, e, clazz);
      }
      return instance;
   }

   public boolean isAvailable()
   {
      return allocateInstanceMethod != null && unsafeInstance != null;
   }

}