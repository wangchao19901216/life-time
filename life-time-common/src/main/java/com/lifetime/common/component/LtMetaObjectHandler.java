package com.lifetime.common.component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.lifetime.common.util.LtCommonUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author:wangchao
 * @date: 2024/5/4-17:59
 * @description: 自动填充字段
 * @Version:1.0
 */
@Component
public class LtMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        if(!LtCommonUtil.isNotBlankOrNull(metaObject.getValue("createTime")) ){
            setFieldValByName("createTime",new Date(),metaObject);
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!LtCommonUtil.isNotBlankOrNull(metaObject.getValue("createUser")) ){
             setFieldValByName("createUser",principal,metaObject);
        }
    }
    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("modifierTime",new Date(),metaObject);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        setFieldValByName("modifierUser",principal,metaObject);
    }
}
