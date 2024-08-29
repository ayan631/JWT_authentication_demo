package com.springboot.securitydemo.service;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.lang.Function;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private String secretKey = "";

	public JwtService() {
		try {

			// Generate a raw String by using Base64 Encoder and "HmacSHA256" algorithm//

			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk = keyGen.generateKey();
			this.secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
		}
	}

	public String generateToken(String username) {
		// A JWT token has 3 parts separated by "." : Header, Payload and Verify
		// Signature.//

		// We will create a HashMap to store claims in key value pairs
		Map<String, Object> claims = new HashMap<>();

		return Jwts.builder().claims().add(claims).subject(username).issuedAt(new Date(System.currentTimeMillis())) // issuing
																													// date
				.expiration(new Date(System.currentTimeMillis() + (30 * 60 * 1000))) // expiring date(issue+30min)
				.and().signWith(getKey()).compact();
	}

	private SecretKey getKey() {

		// Generate the Key object from raw String by using Base64 Decoder and
		// "HmacSHA256" algorithm

		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String extractUserName(String token) {
		// extract the username from jwt token
		return extractClaim(token, Claims::getSubject);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.verifyWith(getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String userName = extractUserName(token);
		return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

}
