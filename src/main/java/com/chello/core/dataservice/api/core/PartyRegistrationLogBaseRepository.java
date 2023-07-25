package com.chello.core.dataservice.api.core;

import java.util.List;

import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.chello.core.dataservice.api.IRegistrationLogBaseRepository;
import com.chello.core.exception.ChelloException;
import com.chello.core.logging.api.ChelloLogLevel;
import com.chello.core.logging.api.ChelloLogger;
import com.chello.core.logging.api.ChelloLoggerFactory;
import com.chello.core.model.dataservices.PartyRegistrationLogInfo;

/**
 * Repository class for PTY_RGST_LOG
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
@Component
public class PartyRegistrationLogBaseRepository extends AbstractBaseRepository<PartyRegistrationLogInfo>
		implements IRegistrationLogBaseRepository {

	private static final ChelloLogger chelloLogger = ChelloLoggerFactory
			.getInstance(PartyRegistrationLogBaseRepository.class);

	public PartyRegistrationLogBaseRepository() {
		super();
	}

	/**
	 * This method is used to retrieve primary user details from PTY_RGST_LOG
	 * 
	 * @param userid
	 * @return row from PartyRegistrationLogInfo
	 * @throws ChelloException
	 */
	@Override
	public PartyRegistrationLogInfo getPrimaryUserDetails(String userId) throws ChelloException {
		PartyRegistrationLogInfo logInfoRecord = null;
		DynamoDBQueryExpression<PartyRegistrationLogInfo> queryExpr = null;
		PartyRegistrationLogInfo logInfo = null;
		if (userId != null && !userId.isEmpty()) {
			logInfo = new PartyRegistrationLogInfo();
			logInfo.setRecordChgByUserName(userId);
			Condition c1 = new Condition().withAttributeValueList(new AttributeValue().withS("Y"))
					.withComparisonOperator(ComparisonOperator.EQ);

			queryExpr = new DynamoDBQueryExpression<PartyRegistrationLogInfo>().withHashKeyValues(logInfo)
					.withIndexName("PTY_RGST_LOG_GSI").withConsistentRead(false)
					.withQueryFilterEntry("PRI_APPL_FLG", c1);
		}
		chelloLogger.log(ChelloLogLevel.DEBUG, "queryExpr: " + queryExpr);
		List<PartyRegistrationLogInfo> queryResult = dataMapper.query(PartyRegistrationLogInfo.class, queryExpr);
		chelloLogger.log(ChelloLogLevel.DEBUG, "queryResult size: " + queryResult.size());

		if (queryResult != null && !queryResult.isEmpty()) {
			logInfoRecord = queryResult.get(0);
		}
		return logInfoRecord;
	}
}