package com.dtatest.githubclient.controller;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import com.dtatest.githubclient.model.UserEmails;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.bind.DatatypeConverter;

@SuppressWarnings("restriction")
public class UserEmailController {
	private static final Logger log = Logger
			.getLogger(UserEmailController.class.getName());
	public static final String USER_EMAILS_BASE = "https://api.github.com/user/emails";

	public UserEmails getUserEmails(final String userName, String password) {
		UserEmails emails = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();

		try {
			//Create Http Get request and add authentication fields to the header
			HttpGet httpget = new HttpGet(USER_EMAILS_BASE);
			setAuthHeader(httpget, userName, password);
			
			//Make the Http Get request
			CloseableHttpResponse response = httpclient.execute(httpget);
			//Process response and convert it to POJO
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream input = entity.getContent();
					try {
						ObjectMapper mapper = new ObjectMapper();
						emails = mapper.readValue(input, UserEmails.class);
					} finally {
						input.close();
					}
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "Failed to Communicate with GitHub API", e);
		}
		return emails;

	}

	public boolean addUserEmails(final String userName, String password,
			List<String> emailAddresses) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			//Create Http Post request and add authentication fields to the header
			HttpPost httppost = new HttpPost(USER_EMAILS_BASE);
			setAuthHeader(httppost, userName, password);
			//Convert POJO into JSON and add to the request body
			ObjectMapper mapper = new ObjectMapper();		
			StringEntity input = new StringEntity(mapper.writeValueAsString(emailAddresses));
			input.setContentType("application/json");
			httppost.setEntity(input);

			//Make the Http Post request
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				//Expectation:
				//if the response code is 201, emails were added succesfully
				//if not - it's a failure.
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
					return true;
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "Failed to Communicate with GitHub API", e);
		}
		return false;
	}

	public boolean deleteUserEmails(final String userName, String password,
			List<String> emailAddresses) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			//Create Http Delete request and add authentication fields to the header
			HttpDeleteWithBody httpdelete = new HttpDeleteWithBody(USER_EMAILS_BASE);
			setAuthHeader(httpdelete, userName, password);
			//Convert POJO into JSON and add to the request body
			ObjectMapper mapper = new ObjectMapper();		
			StringEntity input = new StringEntity(mapper.writeValueAsString(emailAddresses));
			input.setContentType("application/json");
			httpdelete.setEntity(input);

			//Make the Http Delete request
			CloseableHttpResponse response = httpclient.execute(httpdelete);
			try {
				//Expectation:
				//if the response code is 204, emails were added succesfully
				//if not - it's a failure.
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT) {
					return true;
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "Failed to Communicate with GitHub API", e);
		}
		return false;
	}

	/*
	 * Helper method to add basic authentication to Http requests
	 */
	private void setAuthHeader(HttpRequestBase request, String userName,
			String password) {
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json");
		request.setHeader(
				HttpHeaders.AUTHORIZATION,
				String.format(
						"Basic %s",
						DatatypeConverter.printBase64Binary(String.format(
								"%s:%s", userName, password).getBytes())));
	}
	
	/*
	 * Helper class to make Http Delete requests with a body
	 */
	@NotThreadSafe
	class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
		public static final String METHOD_NAME = "DELETE";

		public String getMethod() {
			return METHOD_NAME;
		}

		public HttpDeleteWithBody(final String uri) {
			super();
			setURI(URI.create(uri));
		}

		public HttpDeleteWithBody(final URI uri) {
			super();
			setURI(uri);
		}

		public HttpDeleteWithBody() {
			super();
		}
	}
}