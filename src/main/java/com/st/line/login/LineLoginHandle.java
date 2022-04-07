package com.st.line.login;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class LineLoginHandle {

	private String client_id = "1657020008";
	private String client_secret = "2e3a8a0032ce2bb34c12828ae5dac342";
	private String redirect_uri = "/signin-callback";
	private String scope = "profile%20openid";
	private String authURL = "https://access.line.me/oauth2/v2.1/authorize";
	private String tokenURL = "https://api.line.me/oauth2/v2.1/token";
	private String revokeURL = "https://api.line.me/oauth2/v2.1/revoke";
	private String profileURL = "https://api.line.me/v2/profile";
	
	//redirect line login page
	@RequestMapping(value = "/redirect", method = RequestMethod.GET)
	public ResponseEntity<?> redirect(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String queryString = "?";
		queryString = queryString+"response_type=code";
		queryString = queryString + "&client_id=" + client_id;
		queryString = queryString + "&scope=" + scope;
		queryString = queryString + "&redirect_uri=" + redirect_uri;
		Date systemDate = new Date();
		queryString = queryString + "&state=" + String.valueOf(systemDate.getTime());
		log.info("state:" + systemDate.getTime());

		String url = authURL + queryString;
		System.out.println(url);
		response.sendRedirect(url);

		return null;
	}
	/*
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public ResponseEntity<?> doLogin(HttpServletRequest hrq) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		try {

			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("response_type", "code"));
			formparams.add(new BasicNameValuePair("client_id", client_id));
			formparams.add(new BasicNameValuePair("scope", " "));
			formparams.add(new BasicNameValuePair("redirect_uri", redirect_uri));
			formparams.add(new BasicNameValuePair("state", "1"));
			
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(entity);
            
            
            CloseableHttpResponse response2 = httpclient.execute(login);
            try {
                HttpEntity entity = response2.getEntity();

                System.out.println("Login form get: " + response2.getStatusLine());
                EntityUtils.consume(entity);

//                System.out.println("Post logon cookies:");
//                List<Cookie> cookies = cookieStore.getCookies();
//                if (cookies.isEmpty()) {
//                    System.out.println("None");
//                } else {
//                    for (int i = 0; i < cookies.size(); i++) {
//                        System.out.println("- " + cookies.get(i).toString());
//                    }
//                }
            } finally {
                response2.close();
            }

			
			
		} finally {
			httpclient.close();
		}
		return null;
	} */
}
