package com.chello.insights.util;

/**
 * ENUM for Custom ErrorCodes
 *
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
public enum ErrorCodes {

	SUCCESS(200, "Success"), BUSINESS_ERROR(1002, "Error occurred due to business condition failures"),
	DATABASE_CONNECTION_FAILED(1003, "Database connection is failed"),
	DATABASE_NO_RECORDS(1004, "Database table has no records"),
	REQUEST_HANDLER_ERROR(1005, "Error while getting request handler"),
	REQUEST_HANDLER_NOT_SUPPORTED(1006, "Request handler found is not supported"),
	ENDPOINT_NOT_FOUND(1007, "End point is not found in request headers"),
	USERID_NOT_FOUND(1008, "user id not found in request headers"),
	EIN_NOT_FOUND(1009, "EIN not found in request headers"),
	CORRELATIONID_NOT_FOUND(1010, "Correlation id is not found in request headers"),
	EMAIL_NOT_FOUND(1011, "Emial is not found in request headers"),
	GROUP_NOT_FOUND(1012, "Group is not found in request headers"),
	PATH_PARAMS_NOT_FOUND(1013, "Path params are not found in request"),
	QUERY_PARAMS_NOT_FOUND(1014, "Query params are not found in request"),
	CALLER_CONTEXT_NOT_FOUND(1015, "Caller context is not found or null in request"),
	REQUEST_BODY_NOT_FOUND(1016, "Request body is not found"),
	HEADERS_NOT_FOUND(1017, "Headers are missing in request"), SERVER_ERROR(1018, "Server error occurred"),
	TECHNICAL_ERROR(1019, "Error occurred due to technical failures"), NO_DATA(1020, "No data found"),
	CONFLICT(1021, "There is a conflict occurred"), BAD_REQUEST(1022, "InSufficient parameters from Request"),
	ENDPOINT_FETCH_ERROR(1023, "Error while fetching endpoint and http method"),
	SAVE_FAILED(1024, "Error while saving an entry"), FIND_FAILED(1025, "Error while fetching an entry"),
	DELETE_FAILED(1026, "Error while deleting an entry"),
	CACHE_EXPIRY_FAILED(1027, "Error while fetching cache expiry period"),
	SAVANA_URL_NOT_FOUND(1028, "Savana url not found"),
	EXTERNAL_SERVICE_ERROR(1029, "Issue occured while calling external service"),
	SAVANA_URL_PARAM_STORE_NOT_CONFIGURED(1030, "Savana base URL is not configured in Parameter Store"),
	SAVANA_ENDPOINT_PARAM_STORE_EMPTY(1031, "Savana end point parameter store key cant be null or empty"),
	SAVANA_ENDPOINT_PARAM_STORE_NOT_CONFIGURED(1032,
			"Savana end point url for endpoint is not configured in Parameter Store"),
	CONTEXT_CACHE_CLEAR_ERROR(1033,
			"Error occurred while clearing context cache for Parameter Store. Error: [%s] due to [%s]"),
	GET_ROLE_CODE_ERROR(1034, "Error occurred while trying to find the role code. Error [%s] due to [%s]."),
	GET_ENT_DETIALS_RES_TRANSFORMATION_ERROR(1035,
			"Error occurred while caching and transforming the EntitlementDetails. Error [%s] due to [%s]."),
	VERIFY_ENTITLEMENT_ERROR(1036, "Error occurred while trying to verify entitlement check. Error [%s] due to [%s]."),
	UNAUTHORIZED_ERROR(1037, "User having role [%s] is not authorized to perform the action [%s]."),
	VERIFY_WL_SERVICE_ERROR(1038,
			"Error occurred while trying to verify whitelisted service check. Error [%s] due to [%s]."),
	TEAM_TPS_ERROR(1039,
			"Data saved to DB. However, Exception occured during UpdateOwnerRelated Party Service invocation while saving to TPS. Error: [%s] due to [%s]."),
	BMI_TPS_ERROR(1040,
			"Data saved to DB. However, Exception occured during BitMoreInfo Service invocation while saving to TPS. Error: [%s] due to [%s]."),
	ADDONEACC_TPS_SAVE_ERROR(1041,
			"Account is created. However, an exception occurred on addOneAccount Service invocation while saving to TPS. Error: [%s] due to [%s]."),
	CRSSO_TPS_SAVE_ERROR(1042,
			"Onboarding is initiated. However, an exception occurred on createSSOUser Service invocation while saving to TPS. Error: [%s] due to [%s]."),
	UPDSTS_TPS_SAVE_ERROR(1043,
			"Onboarding status received. However, an exception occurred on updateStatus Service invocation while saving to TPS. Error: [%s] due to [%s]."),
	UNVERIFIED_CUSTOMER_ERROR(1044, "Unable to proceed as customer is unverified"),

	// TPS Service
	TPS_SERVICE_EXECUTION_ERROR(1050,
			"Exception occurred during get practice info Service invocation. Error: [%s] due to [%s]."),
	TPS_URL_ERROR(1051, "Empty URL Passed"),
	TPS_BASE_URL_ERROR(1052, "Base URL is not configured in Parameter Store for key: [%s]."),
	TPS_ENDPOINTKEY_NULL_ERROR(1053, "TPS end point parameter store key cant be null or empty."),
	TPS_ENDPOINTKEY_ERROR(1054, "TPS end point url for endpoint [%s] is not configured in Parameter Store."),
	TPS_SERVER_STATUS_ERROR(1055, "Server returned non-OK status: "),
	TPS_TOKEN_PARSE_ERROR(1056, "Failed to parse token"), TPS_TOKEN_ERROR(1057, "Failed to get token"),
	TPS_OAUTH_EXECUTION_ERROR(1058, "Issue occured while calling TPS OAuth end point. Message : [%s]."),
	TPS_EXECUTION_ERROR(1059, "Issue occured while calling external service with error code: [%s] and Message: [%s]."),
	TPS_TOKEN_REQ_ERROR(1060, "Chello TPS getToken Request Payload is invalid "),
	TPS_TOKEN_RES_ERROR(1061, "TPS getToken Response Payload is invalid "),
	TPS_TOKEN_RES_NULL_ERROR(1062, ":: TPS getToken Response Payload is null or empty"),
	TPS_INT_RES_ERROR(1063, "TPS introspect Response Payload is invalid "),
	TPS_INT_RES_NULL_ERROR(1064, ":: TPS introspect Response Payload is null or empty"),
	TPS_TOKEN_REQ_TRANSFORMATION_ERROR(1065, "Error occurred while transforming Chello TPS getToken Request Payload."),
	TPS_REFTOKEN_REQ_ERROR(1066, "Chello TPS getRefreshToken Request Payload is invalid "),
	TPS_REFTOKEN_REQ_TRANSFORMATION_ERROR(1067,
			"Error occurred while transforming Chello TPS getRefreshToken Request Payload."),
	TPS_INVALID_REQUEST_ERROR(1068, "Invoked withSSL method with invalid data."),
	TPS_INVALID_REQUEST_METHOD(1069, "Inoked HTTP Method not yet supported."),
	TPS_SSLCONTEXT_INSTANTIATION_FAILED(1070, "Not able to instantiate SSLContext."),

	SNS_SEND_ERROR(1071, "Error occurred while trying to send msg for event [%s]. Error: [%s] due to [%s].."),
	COGNITO_DELETE_USER_EXCEPTION(1072, "Error occured while deleting user [%s] from cognito. Error: [%s]"),
	COGNITO_DISABLE_USER_EXCEPTION(1073, "Error occured while disabling user [%s] from cognito. Error: [%s]"),
	SAVANA_CUSTOMER_TOKEN_FETCH_EXPO_EXCEPTION(1074, "Invalid Customer Token - Issue occured while fetching customer token."),
	ONB_SSO_EXCEPTION_MSG_500(1075, "Exception occurred while calling onboarding createSSO API."),
	CUSTOMER_TOKEN_CACHE_EXCEPTION_MSG_500(1076, "Exception occured while storing cutomer token in cache."),
	CUSTOMER_TOKEN_AUTORIZATION_EXCEPTION_MSG_500(1077, " value not found in cache hence skipping savana customer token call."),
	CUSTOMER_TOKEN_EIN_NOT_FOUND_EXCEPTION_MSG_500(1078, "Exception occured while fetching ein from database."),
	CUSTOMER_TOKEN_SNS_EXCEPTION_MSG_500(1079, "Exception occured while sending unable to fetch savana customer token notification."),
	CUSTOMER_TOKEN_NOT_FOUND_CACHE_EXCEPTION_MSG_500(1080, "Customer token not found in cache."),
	SAVANA_CUSTOMER_TOKEN_FETCH_EXCEPTION(1081, "Issue occured while calling savana api to fetch customer token."),
	GET_PROCESSOR_EXCEPTION(3061,"Exception occured while indentify processor. Error: [%s] due to [%s].");

	ErrorCodes(int code, String message) {
		this.code = code;
		this.message = message;
	}

	private int code;
	private String message;

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
