package com.orderprocessing.dto;



public class JwtResponse {
    private String jwtToken;
    private String userName;
    private String role;
    
    public JwtResponse() {}
    
    public JwtResponse(String jwtToken, String userName, String role) {
        this.jwtToken = jwtToken;
        this.userName = userName;
        this.role = role;
    }
    
    public String getJwtToken() {
        return jwtToken;
    }
    
    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
}