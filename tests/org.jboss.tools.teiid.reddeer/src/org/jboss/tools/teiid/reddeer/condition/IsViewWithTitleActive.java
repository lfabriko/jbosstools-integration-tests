package org.jboss.tools.teiid.reddeer.condition;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.condition.WaitCondition;

public class IsViewWithTitleActive implements WaitCondition {

	private String label;
	
	public IsViewWithTitleActive(String label){
		this.label = label;
	}
	
	@Override
	public boolean test() {
		return new SWTWorkbenchBot().activeView().getTitle().equals(label);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return null;
	}

}
