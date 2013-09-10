package org.jboss.tools.teiid.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.junit.Test;

/**
 * Basic test for all platforms
 * @author lfabriko
 *
 */
@Perspective(name = "Teiid Designer")
public class BasicTest extends SWTBotTestCase {
	
	@Test
	public void test(){
		System.out.println("***************Basic test***************");
	}

}
