package com.vany.model;

public class JwtResponse{

	private String token;
	private Long expire;
	private String refreshToken;
	

	public JwtResponse() {
		System.out.println("JWT Respoense Called");
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public Long getExpire() {
		return expire;
	}


	public void setExpire(Long expire) {
		this.expire = expire;
	}


	public String getRefreshToken() {
		return refreshToken;
	}


	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	
	
	

}
