package com.lifetime.common.manager.vo;

import com.lifetime.common.manager.entity.UserDetailEntity;
import com.lifetime.common.manager.entity.UserEntity;
import lombok.Data;

/**
 * @author:wangchao
 * @date: 2024/12/17-11:18
 * @description: com.lifetime.manager.vo
 * @Version:1.0
 */
@Data
public class UserVo {
     String token;
     UserDetailEntity userDetail;
}
