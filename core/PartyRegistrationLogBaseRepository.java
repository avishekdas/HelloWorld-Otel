package com.chello.core.dataservice.api.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.chello.core.constants.ChelloConstants;
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
	 * This method is used to retrieve savana org id and finxact org id from
	 * PTY_RGST_LOG
	 * 
	 * @param userid
	 * @return map of savana org id and finxact org id
	 */
	@Override
	public Map<String, String> findSavanOrgIdAndFxctOrgIdByUserId(String userId) throws ChelloException {
		Map<String, String> onBoardingAnsCurrentPageIdMap = new HashedMap<>();
		DynamoDBQueryExpression<PartyRegistrationLogInfo> queryExpr = null;
		PartyRegistrationLogInfo logInfo = null;
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withS("Y"));
		if (userId != null && !userId.isEmpty()) {
			logInfo = new PartyRegistrationLogInfo();
			logInfo.setRecordChgByUserName(userId);
			queryExpr = new DynamoDBQueryExpression<PartyRegistrationLogInfo>().withHashKeyValues(logInfo)
					.withIndexName("PTY_RGST_LOG_GSI").withConsistentRead(false)
//					.withFilterExpression("PRI_APPL_FLG = :val1 ").withExpressionAttributeValues(eav)
					.withProjectionExpression("SAVN_ORG_ID,FXCT_ORG_ID,PAR_PTY_ID");
		}
		List<PartyRegistrationLogInfo> queryResult = dataMapper.query(PartyRegistrationLogInfo.class, queryExpr);
		PartyRegistrationLogInfo logInfoRecord = queryResult.isEmpty() ? null : queryResult.get(0);
		if (logInfoRecord != null) {
			onBoardingAnsCurrentPageIdMap.put(ChelloConstants.SAVANA_ORG_ID, logInfoRecord.getSavanaOrgId());
			onBoardingAnsCurrentPageIdMap.put(ChelloConstants.FINXACT_ORG_ID, logInfoRecord.getFxctOrgId());
			onBoardingAnsCurrentPageIdMap.put(ChelloConstants.PAR_PTY_ID, logInfoRecord.getParPtyId());
		}
		return onBoardingAnsCurrentPageIdMap;
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
			Condition c1 = new Condition().withAttributeValueList(new AttributeValue().withS(ChelloConstants.Y_STR))
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

	/**
	 * This method is used to retrieve user details from PTY_RGST_LOG
	 * 
	 * @param userid
	 * @return row from PartyRegistrationLogInfo
	 * @throws ChelloException
	 */
	@Override
	public PartyRegistrationLogInfo getUserDetails(String userId) throws ChelloException {
		PartyRegistrationLogInfo logInfoRecord = null;
		List<PartyRegistrationLogInfo> queryResult = getPtyRgstLogInfoObjByUserID(userId);

		if (queryResult != null && !queryResult.isEmpty()) {
			logInfoRecord = queryResult.get(0);
		}
		return logInfoRecord;
	}

	/**
	 * This method is used to retrieve all user details from PTY_RGST_LOG for given
	 * user id
	 * 
	 * @param userid
	 * @return row from PartyRegistrationLogInfo
	 * @throws ChelloException
	 */
	private List<PartyRegistrationLogInfo> getPtyRgstLogInfoObjByUserID(String userId) {
		DynamoDBQueryExpression<PartyRegistrationLogInfo> queryExpr = null;
		PartyRegistrationLogInfo logInfo = null;
		if (userId != null && !userId.isEmpty()) {
			logInfo = new PartyRegistrationLogInfo();
			logInfo.setRecordChgByUserName(userId);
			queryExpr = new DynamoDBQueryExpression<PartyRegistrationLogInfo>().withHashKeyValues(logInfo)
					.withIndexName(ChelloConstants.PTY_RGST_LOG_GSI).withConsistentRead(false);
		}
		chelloLogger.log(ChelloLogLevel.DEBUG, "queryExpr: " + queryExpr);
		List<PartyRegistrationLogInfo> queryResult = dataMapper.query(PartyRegistrationLogInfo.class, queryExpr);
		chelloLogger.log(ChelloLogLevel.DEBUG, "queryResult size: " + queryResult.size());

		return queryResult;
	}

}