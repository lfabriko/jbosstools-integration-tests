package org.jboss.tools.teiid.reddeer.condition;

import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.button.PushButton;

public class PushButtonIsEnabled implements WaitCondition {

	private String label;
	
	public PushButtonIsEnabled(String label){
		this.label = label;
	}
	
	@Override
	public boolean test() {
		return new PushButton(label).isEnabled();
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return null;
	}

}
