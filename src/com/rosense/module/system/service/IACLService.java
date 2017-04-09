package com.rosense.module.system.service;

import com.rosense.basic.model.Msg;
import com.rosense.module.system.web.form.ACLForm;

public interface IACLService {

	public Msg grantPermits(ACLForm form);
	
	public ACLForm getPermits(ACLForm form) ;

}
