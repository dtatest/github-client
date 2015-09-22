package com.dtatest.githubclient.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEmail implements Serializable {

	private static final long serialVersionUID = -7677003861595148236L;

	private String email;
    private boolean primary;
    private boolean verified;

	public UserEmail() {

	}

	public UserEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

    @JsonIgnore
	public boolean getPrimary() {
		return primary;
	}

	public void setPrimary(final boolean primary) {
		this.primary = primary;
	}

    @JsonIgnore
	public boolean getVerified() {
		return verified;
	}

	public void setVerified(final boolean verified) {
		this.verified = verified;
	}

}