package com.lifetime.common.aspectj;

import com.lifetime.common.annotation.DataScope;
import com.lifetime.common.entity.BaseEntity;
import com.lifetime.common.response.SearchModel;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import com.lifetime.common.util.LtCommonUtil;
import com.lifetime.common.util.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:wangchao
 * @date: 2024/12/24-18:37
 * @description: com.lifetime.common.aspectj
 * @Version:1.0
 */
@Aspect
@Component
public class DataScopeAspect {
    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "3";

    /**
     * 部门及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "5";

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";


    @Before("@annotation(controllerDataScope)")
    public void doBefore(JoinPoint point, DataScope controllerDataScope) throws Throwable {

//        handleDataScope(point, controllerDataScope);
    }

    protected void handleDataScope(final JoinPoint joinPoint, DataScope controllerDataScope) {
        // 获取当前的用户
        String userCode = SecurityUtils.getUserCode();
        if (LtCommonUtil.isNotBlankOrNull(userCode)) {
            // 如果是超级管理员，则不过滤数据
            if (!SecurityUtils.isAdmin()) {
                Object params = joinPoint.getArgs()[0];
                if(LtCommonUtil.isNotBlankOrNull(params) && params instanceof SearchRequest)
                    dataScopeFilter(joinPoint, userCode, controllerDataScope.deptAlias(), controllerDataScope.userAlias());
            }
        }
    }


    //获取用户的角色，
    public static void dataScopeFilter(JoinPoint joinPoint, String userCode, String deptAlias, String userAlias) {
        List<SearchModel> searchList=new ArrayList<>();


    }
}
