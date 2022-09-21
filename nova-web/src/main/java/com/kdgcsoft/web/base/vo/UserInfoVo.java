package com.kdgcsoft.web.base.vo;

import com.kdgcsoft.web.common.enums.UserType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fyin
 * @date 2022年09月21日 11:25
 */
@Setter
@Getter
public class UserInfoVo {
    private Long userId;
    private Long deptId;
    private Long orgId;

    private String userName;
    private String fullName;
    private String avatar;
    private String nickName;
    private UserType userType = UserType.NORMAL;
    private List<String> permissions=new ArrayList<>();

}
