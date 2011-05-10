/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.weld.tests.interceptors.lhotse.snd;

import java.lang.reflect.Method;

import org.jboss.weld.tests.interceptors.lhotse.fst.TimestampedDAO;
import org.jboss.weld.tests.interceptors.lhotse.fst.Tx;

import ch.qos.cal10n.util.AnnotationExtractor;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ClientDAO extends TimestampedDAO<Client> implements CDAO
{
   public Class<Client> entityClass()
   {
      return Client.class;
   }

   @Tx(0)
   public Client checkClient(Long l)
   {
   	Method m = null;
   	Class<? extends ClientDAO> clazz = this.getClass();
   	System.out.print("class: " + clazz.getCanonicalName());

   	try {
   		m = clazz.getMethod("checkClient", Long.class);
		} catch (SecurityException e) {
			throw new RuntimeException("Method not found.", e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Method not found.", e);
		}
		
		if (m.getAnnotation(Tx.class) == null) {
			throw new RuntimeException("Annotation not found.");
		}
		
   	return null;
   }
}
