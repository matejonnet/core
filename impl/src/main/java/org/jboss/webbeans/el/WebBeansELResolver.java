/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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

package org.jboss.webbeans.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELResolver;

import org.jboss.webbeans.CurrentManager;
import org.jboss.webbeans.context.DependentContext;
import org.jboss.webbeans.context.DependentInstancesStore;
import org.jboss.webbeans.context.DependentStorageRequest;

/**
 * An EL-resolver against the named beans
 *  
 * @author Pete Muir
 */
public class WebBeansELResolver extends ELResolver
{

   /**
    * @see javax.el.ELResolver#getCommonPropertyType(ELContext, Object)
    */
   @Override
   public Class<?> getCommonPropertyType(ELContext context, Object base)
   {
      return null;
   }

   /**
    * @see javax.el.ELResolver#getFeatureDescriptors(ELContext, Object)
    */
   @Override
   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base)
   {
      return null;
   }

   /**
    * @see javax.el.ELResolver#getType(ELContext, Object, Object)
    */
   @Override
   public Class<?> getType(ELContext context, Object base, Object property)
   {
      return null;
   }

   /**
    * @see javax.el.ELResolver#getValue(ELContext, Object, Object)
    */
   @Override
   public Object getValue(ELContext context, Object base, Object property)
   {
      if (base == null && property != null)
      {
         DependentStorageRequest dependentStorageRequest = DependentStorageRequest.of(new DependentInstancesStore(), new Object());
         try
         {
            DependentContext.INSTANCE.setActive(true);
            DependentContext.INSTANCE.startCollectingDependents(dependentStorageRequest);
            Object value = CurrentManager.rootManager().getInstanceByName(property.toString());
            if (value != null)
            {
               context.setPropertyResolved(true);
            }
            return value;
         }
         finally
         {
            DependentContext.INSTANCE.stopCollectingDependents(dependentStorageRequest);
            // TODO kinky
            dependentStorageRequest.getDependentInstancesStore().destroyDependentInstances(dependentStorageRequest.getKey());
            DependentContext.INSTANCE.setActive(false);
         }
      }
      else
      {
         return null;
      }
   }

   /**
    * @see javax.el.ELResolver#isReadOnly(ELContext, Object, Object)
    */
   @Override
   public boolean isReadOnly(ELContext context, Object base, Object property)
   {
      return false;
   }

   /**
    * @see javax.el.ELResolver#setValue(ELContext, Object, Object, Object)
    */
   @Override
   public void setValue(ELContext context, Object base, Object property, Object value)
   {
   }

}
