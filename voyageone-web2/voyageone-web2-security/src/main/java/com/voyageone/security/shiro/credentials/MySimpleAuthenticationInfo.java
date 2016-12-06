package com.voyageone.security.shiro.credentials;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.MergableAuthenticationInfo;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.subject.MutablePrincipalCollection;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Ethan Shi on 2016-12-01.
 */

public class MySimpleAuthenticationInfo implements MergableAuthenticationInfo {
    protected PrincipalCollection principals;
    protected Object credentials;
    protected String credentialsSalt;

    public MySimpleAuthenticationInfo() {
    }

    public MySimpleAuthenticationInfo(Object principal, Object credentials, String realmName) {
        this.principals = new SimplePrincipalCollection(principal, realmName);
        this.credentials = credentials;
    }

    public MySimpleAuthenticationInfo(Object principal, Object hashedCredentials, String credentialsSalt, String realmName) {
        this.principals = new SimplePrincipalCollection(principal, realmName);
        this.credentials = hashedCredentials;
        this.credentialsSalt = credentialsSalt;
    }

    public MySimpleAuthenticationInfo(PrincipalCollection principals, Object credentials) {
        this.principals = new SimplePrincipalCollection(principals);
        this.credentials = credentials;
    }

    public MySimpleAuthenticationInfo(PrincipalCollection principals, Object hashedCredentials, String credentialsSalt) {
        this.principals = new SimplePrincipalCollection(principals);
        this.credentials = hashedCredentials;
        this.credentialsSalt = credentialsSalt;
    }

    public PrincipalCollection getPrincipals() {
        return this.principals;
    }

    public void setPrincipals(PrincipalCollection principals) {
        this.principals = principals;
    }

    public Object getCredentials() {
        return this.credentials;
    }

    public void setCredentials(Object credentials) {
        this.credentials = credentials;
    }

    public String getCredentialsSalt() {
        return this.credentialsSalt;
    }


    public void setCredentialsSalt(String salt) {
        this.credentialsSalt = salt;
    }

    public void merge(AuthenticationInfo info) {
        if(info != null && info.getPrincipals() != null && !info.getPrincipals().isEmpty()) {
            if(this.principals == null) {
                this.principals = info.getPrincipals();
            } else {
                if(!(this.principals instanceof MutablePrincipalCollection)) {
                    this.principals = new SimplePrincipalCollection(this.principals);
                }

                ((MutablePrincipalCollection)this.principals).addAll(info.getPrincipals());
            }

            if(this.credentialsSalt == null && info instanceof SaltedAuthenticationInfo) {
                this.credentialsSalt = ((SaltedAuthenticationInfo) info).getCredentialsSalt().toBase64();
            }

            Object thisCredentials = this.getCredentials();
            Object otherCredentials = info.getCredentials();
            if(otherCredentials != null) {
                if(thisCredentials == null) {
                    this.credentials = otherCredentials;
                } else {
                    if(!(thisCredentials instanceof Collection)) {
                        HashSet credentialCollection = new HashSet();
                        credentialCollection.add(thisCredentials);
                        this.setCredentials(credentialCollection);
                    }

                    Collection credentialCollection1 = (Collection)this.getCredentials();
                    if(otherCredentials instanceof Collection) {
                        credentialCollection1.addAll((Collection)otherCredentials);
                    } else {
                        credentialCollection1.add(otherCredentials);
                    }

                }
            }
        }
    }

    public boolean equals(Object o) {
        if(this == o) {
            return true;
        } else if(!(o instanceof MySimpleAuthenticationInfo)) {
            return false;
        } else {
            MySimpleAuthenticationInfo that = (MySimpleAuthenticationInfo)o;
            if(this.principals != null) {
                if(!this.principals.equals(that.principals)) {
                    return false;
                }
            } else if(that.principals != null) {
                return false;
            }

            return true;
        }
    }

    public int hashCode() {
        return this.principals != null?this.principals.hashCode():0;
    }

    public String toString() {
        return this.principals.toString();
    }
}


