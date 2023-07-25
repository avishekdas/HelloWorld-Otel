package com.chello.core.model.dataservices;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Model class of PTY_RGST_LOG table
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
@DynamoDBTable(tableName = "PTY_RGST_LOG")
public class PartyRegistrationLogInfo implements Serializable {

	/**
	 * attributes names of PTY_RGST_LOG table
	 */
	private static final long serialVersionUID = 1L;

	private String parPtyId;
	private String ptyId;
	private String cdtJson;// CDT_JSON
	private String ein;
	private String oldEin;
	private String pageId;// PAGE_ID
	private String pldJson; // PLD_JSON
	private String processCode;
	private String prtApplFlag;
	private String recordChgByUserName;
	private String regtStatusCode;
	private String roleName;
	private String savanaAppId;
	private String savanaIndId;
	private String savanaOrgId;
	private String tpsOrgId; // TPS_ORG_ID
	private String tpsTokenNB; // tpsAccessToken
	private String tpsRefreshToken;
	private String lastUpdated;
	private Integer versId; // VERS_ID
	private String savanaAddrId;
	private String ptyEmailId;
	private String savIndSumtFlag; // SAVN_IND_SUMT_FLG
	private String savAppSumtFlag; // SAVN_APP_SUMT_FLG
	private String fxctOrgId; // FXCT_ORG_ID
	private String ctlgPrsnFlg; // CTLG_PRSN_FLG
	private String consFlgJson;// CONS_FLG_JSON
	private String savnCustId; // SAVN_CUST_ID
	private String savnCustGrpCd; // SAVN_CUST_GRP_CD
	private String alloyCreditScore; // ALLOY_CRDT_SCR
	private String alloyLastRfshDt; // ALLOY_LAST_RFSH_DT
	private String alloyFutrRfshDt; // ALLOY_FUTR_RFSH_DT

	@DynamoDBAttribute(attributeName = "PTY_EMAIL_ID")
	public String getPtyEmailId() {
		return ptyEmailId;
	}

	public void setPtyEmailId(String ptyEmailId) {
		this.ptyEmailId = ptyEmailId;
	}

	@DynamoDBAttribute(attributeName = "CDT_JSON")
	public String getCdtJson() {
		return cdtJson;
	}

	public void setCdtJson(String cdtJson) {
		this.cdtJson = cdtJson;
	}

	@DynamoDBAttribute(attributeName = "PAGE_ID")
	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	@DynamoDBAttribute(attributeName = "PLD_JSON")
	public String getPldJson() {
		return pldJson;
	}

	public void setPldJson(String pldJson) {
		this.pldJson = pldJson;
	}

	@DynamoDBAttribute(attributeName = "TPS_ORG_ID")
	public String getTpsOrgId() {
		return tpsOrgId;
	}

	public void setTpsOrgId(String tpsOrgId) {
		this.tpsOrgId = tpsOrgId;
	}

	// @DynamoDBAttribute(attributeName = "VERS_ID")
	public Integer getVersId() {
		return versId;
	}

	public void setVersId(Integer versId) {
		this.versId = versId;
	}

	/**
	 * @return the eIN
	 */
	@DynamoDBIndexHashKey(globalSecondaryIndexName = "PTY_RGST_LOG_GSI_3")
	@DynamoDBAttribute(attributeName = "EIN")
	public String getEin() {
		return ein;
	}

	/**
	 * @param eIN the eIN to set
	 */
	public void setEin(String eIN) {
		ein = eIN;
	}

	/**
	 * @return the eIN
	 */
	@DynamoDBIndexHashKey(globalSecondaryIndexName = "PTY_RGST_LOG_GSI_4")
	@DynamoDBAttribute(attributeName = "OLD_EIN")
	public String getOldEin() {
		return oldEin;
	}

	/**
	 * @param eIN the eIN to set
	 */
	public void setOldEin(String oldEIN) {
		oldEin = oldEIN;
	}

	/**
	 * @return the tpsTokenNB
	 */
	@DynamoDBAttribute(attributeName = "TPS_TKN_NB")
	public String getTpsTokenNB() {
		return tpsTokenNB;
	}

	/**
	 * @param tPS_TKN_NB the tPS_TKN_NB to set
	 */
	public void setTpsTokenNB(String tpsTokenNB) {
		this.tpsTokenNB = tpsTokenNB;
	}

	/**
	 * @return the tpsRefreshToken
	 */
	@DynamoDBAttribute(attributeName = "TPS_RFSH_TKN_NB")
	public String getTpsRefreshToken() {
		return tpsRefreshToken;
	}

	/**
	 * @param tpsRefreshToken the TPS_RFSH_TKN_NB to set
	 */
	public void setTpsRefreshToken(String tpsRefreshToken) {
		this.tpsRefreshToken = tpsRefreshToken;
	}

	/**
	 * @return the recordChgByUserName
	 */
	@DynamoDBIndexHashKey(globalSecondaryIndexName = "PTY_RGST_LOG_GSI")
	@DynamoDBAttribute(attributeName = "RCRD_CHG_BY_USER_NM")
	public String getRecordChgByUserName() {
		return recordChgByUserName;
	}

	/**
	 * @param recordChgByUserName the recordChgByUserName to set
	 */
	public void setRecordChgByUserName(String recordChgByUserName) {
		this.recordChgByUserName = recordChgByUserName;
	}

	/**
	 * @return the rCRD_CHG_TS
	 */
	@DynamoDBAttribute(attributeName = "RCRD_CHG_TS")
	public String getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * @param lastUpdated the lastUpdated to set
	 */
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	/**
	 * @return the roleName
	 */
	@DynamoDBAttribute(attributeName = "ROLE_NM")
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@DynamoDBHashKey(attributeName = "PAR_PTY_ID")
	public String getParPtyId() {
		return parPtyId;
	}

	public void setParPtyId(String parPtyId) {
		this.parPtyId = parPtyId;
	}

	@DynamoDBRangeKey(attributeName = "PTY_ID")
	public String getPtyId() {
		return ptyId;
	}

	public void setPtyId(String ptyId) {
		this.ptyId = ptyId;
	}

	@DynamoDBAttribute(attributeName = "RGST_STS_CD")
	public String getRegtStatusCode() {
		return regtStatusCode;
	}

	public void setRegtStatusCode(String regtStatusCode) {
		this.regtStatusCode = regtStatusCode;
	}

	@DynamoDBAttribute(attributeName = "PRCSS_CD")
	public String getProcessCode() {
		return processCode;
	}

	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}

	@DynamoDBAttribute(attributeName = "SAVN_APP_ID")
	public String getSavanaAppId() {
		return savanaAppId;
	}

	public void setSavanaAppId(String savanaAppId) {
		this.savanaAppId = savanaAppId;
	}

	@DynamoDBIndexHashKey(globalSecondaryIndexName = "PTY_RGST_LOG_GSI_5")
	@DynamoDBAttribute(attributeName = "SAVN_IND_ID")
	public String getSavanaIndId() {
		return savanaIndId;
	}

	public void setSavanaIndId(String savanaIndId) {
		this.savanaIndId = savanaIndId;
	}

	@DynamoDBAttribute(attributeName = "SAVN_ORG_ID")
	public String getSavanaOrgId() {
		return savanaOrgId;
	}

	public void setSavanaOrgId(String savanaOrgId) {
		this.savanaOrgId = savanaOrgId;
	}

	/**
	 * @return the prtApplFlag
	 */
	@DynamoDBAttribute(attributeName = "PRI_APPL_FLG")
	public String getPrtApplFlag() {
		return prtApplFlag;
	}

	/**
	 * @param prtApplFlag the prtApplFlag to set
	 */
	public void setPrtApplFlag(String prtApplFlag) {
		this.prtApplFlag = prtApplFlag;
	}

	/**
	 * @return the savanaAddrId
	 */
	@DynamoDBAttribute(attributeName = "SAVN_ADD_ID")
	public String getSavanaAddrId() {
		return savanaAddrId;
	}

	/**
	 * @param savanaAddrId the savanaAddrId to set
	 */
	public void setSavanaAddrId(String savanaAddrId) {
		this.savanaAddrId = savanaAddrId;
	}

	@DynamoDBAttribute(attributeName = "SAVN_IND_SUMT_FLG")
	public String getSavIndSumtFlag() {
		return savIndSumtFlag;
	}

	public void setSavIndSumtFlag(String savIndSumtFlag) {
		this.savIndSumtFlag = savIndSumtFlag;
	}

	@DynamoDBAttribute(attributeName = "SAVN_APP_SUMT_FLG")
	public String getSavAppSumtFlag() {
		return savAppSumtFlag;
	}

	public void setSavAppSumtFlag(String savAppSumtFlag) {
		this.savAppSumtFlag = savAppSumtFlag;
	}

	@DynamoDBAttribute(attributeName = "FXCT_ORG_ID")
	public String getFxctOrgId() {
		return fxctOrgId;
	}

	public void setFxctOrgId(String fxctOrgId) {
		this.fxctOrgId = fxctOrgId;
	}

	@DynamoDBAttribute(attributeName = "CTLG_PRSN_FLG")
	public String getCtlgPrsnFlg() {
		return ctlgPrsnFlg;
	}

	public void setCtlgPrsnFlg(String ctlgPrsnFlg) {
		this.ctlgPrsnFlg = ctlgPrsnFlg;
	}

	@DynamoDBAttribute(attributeName = "SAVN_CUST_ID")
	public String getSavnCustId() {
		return savnCustId;
	}

	public void setSavnCustId(String savnCustId) {
		this.savnCustId = savnCustId;
	}

	@DynamoDBAttribute(attributeName = "SAVN_CUST_GRP_CD")
	public String getSavnCustGrpCd() {
		return savnCustGrpCd;
	}

	public void setSavnCustGrpCd(String savnCustGrpCd) {
		this.savnCustGrpCd = savnCustGrpCd;
	}

	@DynamoDBAttribute(attributeName = "CONS_FLG_JSON")
	public String getConsFlgJson() {
		return consFlgJson;
	}

	public void setConsFlgJson(String consFlgJson) {
		this.consFlgJson = consFlgJson;
	}

	@DynamoDBAttribute(attributeName = "ALLOY_CRDT_SCR")
	public String getAlloyCreditScore() {
		return alloyCreditScore;
	}

	public void setAlloyCreditScore(String alloyCreditScore) {
		this.alloyCreditScore = alloyCreditScore;
	}

	@DynamoDBAttribute(attributeName = "ALLOY_LAST_RFSH_DT")
	public String getAlloyLastRfshDt() {
		return alloyLastRfshDt;
	}

	public void setAlloyLastRfshDt(String alloyLastRfshDt) {
		this.alloyLastRfshDt = alloyLastRfshDt;
	}

	@DynamoDBAttribute(attributeName = "ALLOY_FUTR_RFSH_DT")
	public String getAlloyFutrRfshDt() {
		return alloyFutrRfshDt;
	}

	public void setAlloyFutrRfshDt(String alloyFutrRfshDt) {
		this.alloyFutrRfshDt = alloyFutrRfshDt;
	}
}
