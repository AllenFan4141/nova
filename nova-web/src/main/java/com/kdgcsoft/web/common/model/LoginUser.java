package com.kdgcsoft.web.common.model;

import com.kdgcsoft.web.common.enums.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 当前登录用户
 *
 * @author fyin
 * @date 2022年08月30日 10:05
 */
public class LoginUser implements UserDetails {
    private Long userId;
    private Long deptId;
    private Long orgId;

    private String userName;
    private String userPassword;
    private String fullName;
    private String avatar;

    private UserType userType = UserType.NORMAL;
    private String ipAddress;
    private String browser;
    private String os;
    /**
     * 用户唯一标识
     */
    private String uuid;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;


    /**
     * 密码未过期
     */
    private boolean credentialsNonExpired = true;
    /**
     * 账号未过期
     */
    private boolean accountNonExpired = true;
    /**
     * 账号未锁定
     */
    private boolean accountNonLocked = true;
    /**
     * 账号启用
     */
    private boolean enabled = true;

    private Map<String, Object> properties = new HashMap<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }



    public Long getUserId() {
        return userId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getBrowser() {
        return browser;
    }

    public String getOs() {
        return os;
    }

    public String getUuid() {
        return uuid;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }
}
