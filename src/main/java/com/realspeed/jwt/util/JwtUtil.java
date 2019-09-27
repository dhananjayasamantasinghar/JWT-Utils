package com.realspeed.jwt.util;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realspeed.jwt.model.Data;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {

	public static final String BEARER_TOKEN = "Bearer ";

	private static final String SIGNINGKEY = "Dhananjayass";

	// 30 Sec
	public static final long JWT_TOKEN_VALIDITY = 3000;

	public static void main(String[] args) throws IOException {

		String jwt = JwtUtil.generate(new Data(1, "USER", "Uz9867y"), SIGNINGKEY);
		JwtUtil.validate(jwt, SIGNINGKEY);
		System.out.println("Generated JWT Token : " + jwt + "\n\n");
		String response = parseJWT(jwt, SIGNINGKEY);
		System.out.println("Response :" + response);
		System.out.println("Cust Id: " + getCustId(response));

	}

	public static String getCustId(String response) throws IOException {
		JsonNode rootNode = new ObjectMapper().readTree(response);
		if (rootNode != null && rootNode.has("cId")) {
			return rootNode.get("cId").asText();
		}
		return null;
	}

	private static String parseJWT(String jwt, String clientSecret) throws JsonProcessingException {

		Claims claims = Jwts.parser().setSigningKey(clientSecret).parseClaimsJws(jwt).getBody();
		return new ObjectMapper().writeValueAsString(claims);

	}

	public static String generate(Data jwtUser, String clientSecret) {
		Claims claims = Jwts.claims().setSubject(jwtUser.getSubject());
		claims.setIssuedAt(new Date(System.currentTimeMillis()));
		claims.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000));
		claims.put("userId", String.valueOf(jwtUser.getId()));
		claims.put("role", jwtUser.getRole());
		claims.put("cId", "F09875656");

		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, clientSecret).compact();
	}

	public static Data validate(String token, String clientSecret) {
		Data jwtUser = null;
		try {
			Claims body = Jwts.parser().setSigningKey(clientSecret).parseClaimsJws(token).getBody();
			Date expiration = body.getExpiration();
			if (expiration.before(new Date())) {
				throw new RuntimeException("JWT token is not valid");
			}
			jwtUser = new Data();
			jwtUser.setSubject(body.getSubject());
			jwtUser.setId(Long.parseLong((String) body.get("userId")));
			jwtUser.setRole((String) body.get("role"));
			return jwtUser;
		} catch (Exception e) {
			throw new RuntimeException("JWT token is not valid");
		}
	}

}
