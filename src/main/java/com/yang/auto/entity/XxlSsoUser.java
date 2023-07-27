package com.yang.auto.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * xxl sso user
 *
 * @author xuxueli 2018-04-02 19:59:49
 */
public class XxlSsoUser implements Serializable {
	private static final long serialVersionUID = 42L;

	private String userId;
	private Integer type;
	private int expireMinite;
	private long expireFreshTime;
	private String sessionId;
	private String mobilePhone;
	private Integer accountStatus;
	private Long createId;
	private Integer clockDays;
	private String version;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public int getExpireMinite() {
		return expireMinite;
	}

	public void setExpireMinite(int expireMinite) {
		this.expireMinite = expireMinite;
	}

	public long getExpireFreshTime() {
		return expireFreshTime;
	}

	public void setExpireFreshTime(long expireFreshTime) {
		this.expireFreshTime = expireFreshTime;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public Integer getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(Integer accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Integer getClockDays() {
		return clockDays;
	}

	public void setClockDays(Integer clockDays) {
		this.clockDays = clockDays;
	}
}
