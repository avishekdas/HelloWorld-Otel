package com.chello.core.dataservice.api;

import com.chello.core.exception.ChelloException;
import com.chello.core.model.dataservices.PartyRegistrationLogInfo;

/**
 *
 * Interface to accommodate only Specific interface methods aligned to
 * PartyRegistrationLogInfo.
 *
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
public interface IRegistrationLogBaseRepository extends IBaseRepository<PartyRegistrationLogInfo> {

	public PartyRegistrationLogInfo getPrimaryUserDetails(String userId) throws ChelloException;

}