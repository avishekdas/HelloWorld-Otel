package com.chello.core.dataservice.api.core;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.chello.core.dataservice.api.IChelloEntitlement;
import com.chello.core.exception.ChelloException;
import com.chello.core.logging.api.ChelloLogLevel;
import com.chello.core.logging.api.ChelloLogger;
import com.chello.core.logging.api.ChelloLoggerFactory;
import com.chello.core.model.dataservices.ChelloEntitlement;

/**
 * Repository class for CHLL_ETLM
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
@Repository
public class ChelloEntitlementRepository extends AbstractBaseRepository<ChelloEntitlement>
		implements IChelloEntitlement {

	private static final ChelloLogger chelloLogger = ChelloLoggerFactory.getInstance(ChelloEntitlementRepository.class);

	public ChelloEntitlementRepository() {
		super();
	}

	/**
	 * Method to find entitlement records based on role code
	 * 
	 * @param role code
	 * @return ChelloEntitlement List
	 * @throws ChelloException
	 */
	@Override
	public List<ChelloEntitlement> findChllEtmlListByRoleCode(String roleCode) throws ChelloException {
		return getChllEtmlObjsByPartitionKey(roleCode);
	}

	/**
	 * Get ChelloEntitlement object by Partition key
	 * 
	 * @param roleCode
	 * @return ChelloEntitlement List
	 */
	private List<ChelloEntitlement> getChllEtmlObjsByPartitionKey(String roleCode) {
		DynamoDBQueryExpression<ChelloEntitlement> queryExpr = null;
		ChelloEntitlement chllEtlm = null;
		if (roleCode != null && !roleCode.isEmpty()) {
			chllEtlm = new ChelloEntitlement();
			chllEtlm.setRoleCode(roleCode);
			queryExpr = new DynamoDBQueryExpression<ChelloEntitlement>().withHashKeyValues(chllEtlm);
		}
		chelloLogger.log(ChelloLogLevel.DEBUG, "queryExpr: " + queryExpr);
		List<ChelloEntitlement> queryResult = dataMapper.query(ChelloEntitlement.class, queryExpr);
		chelloLogger.log(ChelloLogLevel.DEBUG, "queryResult size: " + queryResult.size());

		return queryResult;
	}

}
