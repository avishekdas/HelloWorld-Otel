package com.chello.core.dataservice.api.core;

import org.springframework.stereotype.Component;

import com.chello.core.dataservice.api.IChelloRefDataBaseRepository;
import com.chello.core.model.dataservices.ChelloRefData;

/**
 * 
 * Any specific methods aligned to Chello reference data like findBy**
 * findBetween* should be constructed within this specific repository .The base
 * repository should not be fiddled with any specific methods related to Chello
 * Reference data
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 *
 */
@Component
public class ChelloRefDataBaseRepository extends AbstractBaseRepository<ChelloRefData>
		implements IChelloRefDataBaseRepository {

	public ChelloRefDataBaseRepository() {
		super();

	}

}
