package com.chello.core.dataservice.api.core;

import java.util.List;

import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.chello.core.dataservice.api.IProfileDetailBaseRepository;
import com.chello.core.logging.api.ChelloLogLevel;
import com.chello.core.logging.api.ChelloLogger;
import com.chello.core.logging.api.ChelloLoggerFactory;
import com.chello.core.model.dataservices.ProfileDetailData;

/**
 * Repository class for PTY_PRFL_DTL
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */

@Component
public class ProfileDetailBaseRepository extends AbstractBaseRepository<ProfileDetailData>
		implements IProfileDetailBaseRepository {

	private static final ChelloLogger chelloLogger = ChelloLoggerFactory.getInstance(ProfileDetailBaseRepository.class);

	public ProfileDetailBaseRepository() {
		super();
	}

	/**
	 * Get Profile details for given EIN
	 * 
	 * @param EIN
	 * @return List of ProfileDetailData
	 */
	@Override
	public List<ProfileDetailData> getPrflDtlObjByEin(String ein) {
		DynamoDBQueryExpression<ProfileDetailData> queryExpr = null;
		ProfileDetailData profileDetail = null;
		if (ein != null && !ein.isEmpty()) {
			profileDetail = new ProfileDetailData();
			profileDetail.setOrgEin(ein);
			queryExpr = new DynamoDBQueryExpression<ProfileDetailData>().withHashKeyValues(profileDetail);
		}
		chelloLogger.log(ChelloLogLevel.DEBUG, "queryExpr: " + queryExpr);
		List<ProfileDetailData> queryResult = dataMapper.query(ProfileDetailData.class, queryExpr);
		chelloLogger.log(ChelloLogLevel.DEBUG, "queryResult size: " + queryResult.size());
		return queryResult;
	}

}
