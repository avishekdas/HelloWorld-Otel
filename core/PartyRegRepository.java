package com.chello.core.dataservice.api.core;

import org.springframework.stereotype.Component;
import com.chello.core.dataservice.api.IPartyRegRepository;
import com.chello.core.model.dataservices.PartyRegistrationInfo;

/**
 * The Class PartyRegRepository for PTY_RGST.
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01 
 */
@Component 
public class  PartyRegRepository extends  AbstractBaseRepository<PartyRegistrationInfo> implements IPartyRegRepository {	

	/**
	 * Instantiates a new party registration repository.
	 */
	public PartyRegRepository() {
		super();
	}
	
	
}
