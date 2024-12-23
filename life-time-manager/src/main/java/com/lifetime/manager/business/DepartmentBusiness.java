package com.lifetime.manager.business;

import com.lifetime.common.constant.ResponseResultConstants;
import com.lifetime.common.constant.StatusConstants;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.enums.PermissionTypeEnum;
import com.lifetime.common.manager.entity.CodeEntity;
import com.lifetime.common.manager.entity.DepartmentEntity;
import com.lifetime.common.manager.entity.PermissionEntity;
import com.lifetime.common.manager.service.IDepartmentService;
import com.lifetime.common.manager.service.IPermissionService;
import com.lifetime.common.model.TreeModel;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import com.lifetime.common.util.LtCommonUtil;
import com.lifetime.common.util.LtModelUtil;
import com.lifetime.common.util.SnowflakeUtil;
import com.lifetime.manager.model.PermissionRequestModel;
import jdk.nashorn.internal.runtime.RewriteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author:wangchao
 * @date: 2024/12/19-20:52
 * @description: com.lifetime.manager.business
 * @Version:1.0
 */
@Component
public class DepartmentBusiness {
    @Autowired
    IDepartmentService iDepartmentService;

    public ResponseResult save(DepartmentEntity  departmentEntity) {
        DepartmentEntity resultEntity = iDepartmentService.findByDeptCode(departmentEntity.getDepartmentCode());
        if (LtCommonUtil.isNotBlankOrNull(resultEntity)) {
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED, "编号已经存在");
        } else {
            iDepartmentService.save(departmentEntity);
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        }
    }

    public ResponseResult remove(String deptCode) {
        DepartmentEntity departmentEntity = iDepartmentService.findByDeptCode(deptCode);
        if(departmentEntity.getStatus()==StatusConstants.CAN_NOT_DELETE){
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED_DEFAULT);
        }
        List<DepartmentEntity> departmentEntityList = iDepartmentService.childDept(deptCode, StatusConstants.DISABLE);
        if (departmentEntityList.size() > 0)
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED, "存在子部门，无法删除!");
        else {
            iDepartmentService.removeById(departmentEntity.getId());
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        }
    }

    public ResponseResult update(String deptCode, DepartmentEntity departmentEntity) {
        DepartmentEntity resultEntity = iDepartmentService.findByDeptCode(deptCode);
        if (LtCommonUtil.isBlankOrNull(resultEntity)) {
            return ResponseResult.error(CommonExceptionEnum.DATA_UPDATE_FAILED, "部门不存在");
        } else {
            departmentEntity.setDepartmentCode(deptCode);
            departmentEntity.setId(resultEntity.getId());
            iDepartmentService.updateById(departmentEntity);
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        }
    }
    public ResponseResult search(SearchRequest searchRequest) {
        return ResponseResult.success(iDepartmentService.searchList(searchRequest));
    }

    public ResponseResult getTree(SearchRequest searchRequest) {
        searchRequest.pageParams.pageSize=-1;//树型默认查所有
        SearchResponse<DepartmentEntity> searchResponse = iDepartmentService.searchList(searchRequest);
        List<DepartmentEntity> resultList = searchResponse.results;
        List<TreeModel> treeModelList = new ArrayList<>();

        List<DepartmentEntity> rootList = resultList.stream().filter(e -> e.getDepartmentParentCode().equals("-1")).collect(Collectors.toList());

        for(DepartmentEntity entity:rootList){
            TreeModel treeModel=new TreeModel();
            treeModel.setCode(entity.getDepartmentCode());
            treeModel.setName(entity.getDepartmentName());
            treeModel.setType(entity.getDepartmentType());
            treeModel.setNote(entity.getRemark());
            List<TreeModel> recursiveList= recursive(treeModel,resultList);
            treeModel.setChild(recursiveList);
            treeModelList.add(treeModel);
        }
        return ResponseResult.success(treeModelList);
    }

    public List<TreeModel> recursive(TreeModel treeModel,List<DepartmentEntity> list){

        List<DepartmentEntity> chidList=list.stream().filter(e->e.getDepartmentParentCode().toString().equals(treeModel.getCode())).collect(Collectors.toList());
        List<TreeModel> chidTreeModelList=new ArrayList<>();
        if(chidList.size()>0){
            for(DepartmentEntity entity:chidList){
                TreeModel model=new TreeModel();
                model.setCode(entity.getDepartmentCode());
                model.setType(entity.getDepartmentType());
                model.setName(entity.getDepartmentName());
                model.setNote(entity.getRemark());
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
