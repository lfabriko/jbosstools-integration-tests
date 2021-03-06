 /*******************************************************************************
  * Copyright (c) 2007-2012 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.ui.bot.ext.test.config;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerLocation;
import org.junit.Test;
/**
 * Parent test for tests using remote server in default state.
 * @author lzoubek
 *
 */
@Require(server=@Server(location=ServerLocation.Remote),perspective="Java EE")
public class AnnotatedRemoteServer extends SWTTestExt{
  /**
   * Checks server state
   */
	@Test
	public void configuredState() {
		assertTrue(configuredState.getServer().isRunning);
		assertTrue(configuredState.getServer().isConfigured);
		assertNotNull(configuredState.getServer().version);
		assertNotNull(configuredState.getServer().type);
		assertNotNull(configuredState.getServer().name);
		assertNotNull(configuredState.getServer().withJavaVersion);
		assertFalse(configuredState.getServer().isLocal);
	}
	 /**
   * Checks server existence
   */
	@Test
	public void serverExists() {
		ServerUtil.serverExists();
	}
}
