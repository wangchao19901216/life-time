package com.lifetime.common.manager.vo;

import com.lifetime.common.manager.entity.*;
import com.lifetime.common.model.AuthTokenModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author:wangchao
 * @date: 2024/12/17-11:18
 * @description: com.lifetime.manager.vo
 * @Version:1.0
 */
@Data
@Builder
public class UserVo {
     AuthTokenModel authToken;
     UserDetailEntity userDetail;
     List<DepartmentVo> departments;
     String activeDept;
}
