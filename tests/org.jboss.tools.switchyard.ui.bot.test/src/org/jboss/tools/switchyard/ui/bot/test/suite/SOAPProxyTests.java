package org.jboss.tools.switchyard.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.tools.switchyard.ui.bot.test.WSProxySOAPTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test Suite
 * 
 * @author apodhrad
 * 
 */
@SuiteClasses({ WSProxySOAPTest.class })
@RunWith(SwitchyardSuite.class)
public class SOAPProxyTests extends TestSuite {

}
