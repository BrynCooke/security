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
package org.jboss.seam.security.externaltest.integration.openid.op;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.seam.security.external.api.OpenIdProviderApi;
import org.jboss.seam.security.external.api.OpenIdRequestedAttribute;
import org.jboss.seam.security.external.api.ResponseHolder;
import org.jboss.seam.security.external.dialogues.DialogueManager;
import org.jboss.seam.security.external.dialogues.api.Dialogue;
import org.jboss.seam.security.external.spi.OpenIdProviderSpi;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@ApplicationScoped
public class OpenIdProviderApplicationMock implements OpenIdProviderSpi
{
   @Inject
   private ResponseHolder responseHolder;

   @Inject
   private OpenIdProviderApi opApi;

   private String dialogueId;

   @Inject
   private Dialogue dialogue;

   @Inject
   private DialogueManager dialogueManager;

   public void handleLogin(String userName)
   {
      dialogueManager.attachDialogue(dialogueId);
      opApi.authenticationSucceeded(userName);
      dialogueManager.detachDialogue();
   }

   public void setAttribute(String alias, String value)
   {
      dialogueManager.attachDialogue(dialogueId);
      Map<String, List<String>> attributes = Maps.newHashMap();
      attributes.put(alias, Lists.newArrayList(value));
      opApi.setAttributes(attributes);
      dialogueManager.detachDialogue();
   }

   public void authenticate(String realm, String userName, boolean immediate)
   {
      if (userName == null)
      {
         writeMessageToResponse("Please login.");
      }
      else
      {
         writeMessageToResponse("Please provide the password for " + userName + ".");
      }
      dialogueId = dialogue.getDialogueId();
   }

   private void writeMessageToResponse(String message)
   {
      try
      {
         responseHolder.getResponse().getWriter().print(message);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   public boolean userExists(String userName)
   {
      return true;
   }

   public void fetchParameters(List<OpenIdRequestedAttribute> requestedAttributes)
   {
      writeMessageToResponse("Please provide your " + requestedAttributes.get(0).getAlias() + ".");
      dialogueId = dialogue.getDialogueId();
   }
}
