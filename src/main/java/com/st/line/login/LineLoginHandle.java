package com.st.line.login;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
@RestController
public class LineLoginHandle {

	private String client_id = "1657020008";
	private String client_secret = "2e3a8a0032ce2bb34c12828ae5dac342";
	private String redirect_uri = "https://studyline66.herokuapp.com/te/logincallback";
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
		String stateValue= String.valueOf(systemDate.getTime());
		queryString = queryString + "&state=" +stateValue ;
		//log.info("state:" + systemDate.getTime());

		String url = authURL + queryString;
		System.out.println(url);
		response.addCookie(new Cookie("state", stateValue));
		response.sendRedirect(url);
		

		return null;
	}
	
	@RequestMapping(value = "/logincallback", method = RequestMethod.GET)
	public String index(HttpServletRequest request,  
			@RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "error_description", required = false) String errMsg)  {
		Cookie[] cookieArray=request.getCookies();
		if (cookieArray.length==0) {
			return "state is no exit";
		}
		//check state 
		for (int i = 0; i < cookieArray.length; i++) {
			if (cookieArray[i].getName().equals("state")) {
				if (!cookieArray[i].getValue().equals(state)) {
					return "state value is error";
				}
			}
		}
		
		if (StringUtils.isNotBlank(error)) {
			return error+":"+ errMsg;
		}
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("grant_type", "authorization_code"));
		formparams.add(new BasicNameValuePair("code", code));
		formparams.add(new BasicNameValuePair("client_id", client_id));
		formparams.add(new BasicNameValuePair("client_secret", client_secret));
		formparams.add(new BasicNameValuePair("redirect_uri", redirect_uri));
		
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		HttpPost httppost = new HttpPost(tokenURL);
		httppost.setEntity(entity);
		try {
			CloseableHttpResponse response = httpclient.execute(httppost);
			 HttpEntity rsEntity = response.getEntity();
			 String body=EntityUtils.toString(rsEntity,"UTF-8");
			 return body;
			// JSONObject bodyObject= JSONObject.fromObject(body);
			 
			
		} catch (Exception e) {
			return "Connect auth fail";
		}
		
		
		
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
