package com.kdgcsoft.web.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kdgcsoft.web.base.enums.Gender;
import com.kdgcsoft.web.base.enums.UserStatus;
import com.kdgcsoft.web.base.enums.YesNo;
import com.kdgcsoft.web.common.enums.UserType;
import com.kdgcsoft.web.common.model.LoginUser;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息
 *
 * @author YINFAN
 * @TableName base_user
 */
@TableName(value = "base_user")
@Data
public class BaseUser extends BaseEntity implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * 组织机构ID
     */
    private Long orgId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户登录名
     */
    private String loginName;

    /**
     * 登录密码
     */
    private String loginPassword;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 性别
     */
    private Gender gender;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 工号
     */
    private String accountNo;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 是否启用
     */
    private YesNo enabled;

    /**
     * 用户状态
     */
    private UserStatus userStatus;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 密码最后修改时间
     */
    private Date passwordModifyTime;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String orgName;
    @TableField(exist = false)
    private String deptName;


    public LoginUser toLoginUser() {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(this.userId);
        loginUser.setOrgId(this.orgId);
        loginUser.setDeptId(this.deptId);
        loginUser.setUserPassword(this.loginPassword);
        loginUser.setFullName(this.userName);
        loginUser.setAvatar(this.userAvatar);
        loginUser.setUserType(UserType.NORMAL);
        loginUser.setEnabled(this.enabled == YesNo.Y);
        switch (this.userStatus) {
            case LOCK:
                //用户锁定
                loginUser.setAccountNonLocked(false);
                break;
            case EXPIRE:
                //用户密码过期,长时间未修改密码
                loginUser.setCredentialsNonExpired(false);
                break;
            case DISABLE:
                //用户禁用
                loginUser.setEnabled(false);
                break;
            case DORMANT:
                //用户休眠,长时间未登录
                loginUser.setAccountNonExpired(false);
                break;
            default:
                break;
        }
        return loginUser;
    }

}