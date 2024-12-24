package com.lifetime.manager.business;

import com.lifetime.common.constant.Constants;
import com.lifetime.common.constant.ResponseResultConstants;
import com.lifetime.common.constant.StatusConstants;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.manager.entity.CodeEntity;
import com.lifetime.common.manager.entity.RoleEntity;
import com.lifetime.common.manager.service.ICodeService;
import com.lifetime.common.manager.service.IRoleService;
import com.lifetime.common.model.TreeModel;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import com.lifetime.common.util.LtCommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author:wangchao
 * @date: 2024/12/23-12:47
 * @description: com.lifetime.manager.business
 * @Version:1.0
 */
@Component
public class RoleBusiness {

    @Autowired
    IRoleService  iRoleService;

    public ResponseResult save(RoleEntity entity) {
        if (iRoleService.isExist(entity.getRoleCode())) {
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED, "编号已经存在");
        } else {
            iRoleService.save(entity);
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        }
    }

    public ResponseResult remove(BigInteger id) {
        RoleEntity resultEntity = iRoleService.getById(id);
        if (LtCommonUtil.isBlankOrNull(resultEntity)) {
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED, "未查到改字典，无法删除!");
        }
        if(resultEntity.getStatus()== StatusConstants.CAN_NOT_DELETE){
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED_DEFAULT);
        }
        List<RoleEntity> childList = iRoleService.childEntity(resultEntity.getRoleCode());
        if (childList.size() > 0)
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED, "存在子数据，无法删除!");
        else {
            iRoleService.removeById(id);
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        }
    }

    public ResponseResult update(BigInteger id, RoleEntity updateEntity) {
        RoleEntity resultEntity = iRoleService.getById(id);
        if (LtCommonUtil.isBlankOrNull(resultEntity)) {
            return ResponseResult.error(CommonExceptionEnum.DATA_UPDATE_FAILED, "未查到对应编号的字典");
        } else {
            updateEntity.setId(id);
            updateEntity.setRoleCode(resultEntity.roleCode);
            iRoleService.updateById(updateEntity);
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        }
    }

    public ResponseResult search(SearchRequest searchRequest) {
        return ResponseResult.success(iRoleService.searchList(searchRequest));
    }
    public ResponseResult getTree(SearchRequest searchRequest) {
        searchRequest.pageParams.pageSize=-1;//树型默认查所有
        SearchResponse<RoleEntity> searchResponse = iRoleService.searchList(searchRequest);
        List<RoleEntity> resultList = searchResponse.results;
        List<TreeModel> treeModelList = new ArrayList<>();

        List<RoleEntity> rootList = resultList.stream().filter(e -> e.getRoleParentCode().equals(Constants.ROOT_VALUE)).collect(Collectors.toList());

        for(RoleEntity entity:rootList){
            TreeModel treeModel=new TreeModel();
            treeModel.setCode(entity.getRoleCode());
            treeModel.setName(entity.getRoleName());
            treeModel.setNote(entity.getRemark());
            treeModel.setId(entity.getId());
            List<TreeModel> recursiveList= recursive(treeModel,resultList);
            treeModel.setChild(recursiveList);
            treeModelList.add(treeModel);
        }
        return ResponseResult.success(treeModelList);
    }

    public List<TreeModel> recursive(TreeModel treeModel,List<RoleEntity> list){

        List<RoleEntity> chidList=list.stream().filter(e->e.getRoleParentCode().toString().equals(treeModel.getCode())).collect(Collectors.toList());
        List<TreeModel> chidTreeModelList=new ArrayList<>();
        if(chidList.size()>0){
            for(RoleEntity entity:chidList){
                TreeModel model=new TreeModel();
                model.setCode(entity.getRoleCode());
                model.setName(entity.getRoleName());
                model.setNote(entity.getRemark());
                model.setId(entity.getId());
                chidTreeModelList.add(model);
            }

            for(TreeModel model:chidTreeModelList){
                List<TreeModel> recursiveList=recursive(model,list);
                model.setChild(recursiveList);
            }
            return  chidTreeModelList;
        }
        else{
            return null;
        }
    }
}
