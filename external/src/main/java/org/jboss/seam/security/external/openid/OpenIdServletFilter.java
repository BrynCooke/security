/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.seam.security.external.openid;

import java.io.IOException;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.security.external.InvalidRequestException;
import org.jboss.seam.security.external.api.ResponseHolder;
import org.slf4j.Logger;

/**
 * @author Marcel Kolsteren
 * 
 */
public class OpenIdServletFilter implements Filter
{
   @Inject
   private Logger log;

   @Inject
   private Instance<OpenIdMessageHandler> openIdMessageHandler;

   @Inject
   private ResponseHolder responseHolder;

   public void init(FilterConfig filterConfig) throws ServletException
   {
   }

   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
   {
      try
      {
         responseHolder.setResponse((HttpServletResponse) response);
         openIdMessageHandler.get().handleMessage((HttpServletRequest) request);
      }
      catch (InvalidRequestException e)
      {
         ((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST, e.getDescription());
         if (log.isInfoEnabled())
         {
            log.info("Bad request received from {}: {}", request.getRemoteHost(), e.getDescription());
         }
      }
   }

   public void destroy()
   {
   }
}
