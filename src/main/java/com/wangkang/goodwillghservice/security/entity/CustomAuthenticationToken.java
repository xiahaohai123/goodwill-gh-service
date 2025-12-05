package com.wangkang.goodwillghservice.security.entity;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Objects;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {
    private final transient Object principal;
    private final String areaCode;
    private final String phoneNumber;
    private transient Object credentials = null;

    public CustomAuthenticationToken(String areaCode, String phoneNumber) {
        super(null);
        this.principal = null;
        this.areaCode = areaCode;
        this.phoneNumber = phoneNumber;
        setAuthenticated(false);
    }

    public CustomAuthenticationToken(Collection<? extends GrantedAuthority> authorities,
                                     String areaCode,
                                     String phoneNumber) {
        super(authorities);
        this.areaCode = areaCode;
        this.phoneNumber = phoneNumber;
        this.principal = null;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated,
                "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CustomAuthenticationToken that = (CustomAuthenticationToken) o;
        return Objects.equals(principal, that.principal) && Objects.equals(areaCode,
                that.areaCode) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(
                credentials, that.credentials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), principal, areaCode, phoneNumber, credentials);
    }
}
