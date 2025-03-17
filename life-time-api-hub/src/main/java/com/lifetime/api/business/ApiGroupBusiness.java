package com.lifetime.api.business;

import com.lifetime.api.entity.ApiGroupEntity;
import com.lifetime.api.service.IApiGroupService;
import com.lifetime.common.constant.Constants;
import com.lifetime.common.constant.ResponseResultConstants;
import com.lifetime.common.constant.StatusConstants;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.manager.entity.CodeEntity;
import com.lifetime.common.manager.service.ICodeService;
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
public class ApiGroupBusiness {

    @Autowired
    IApiGroupService iApiGroupService;

    public ResponseResult save(ApiGroupEntity entity) {

        if (iApiGroupService.isExist(entity.getGroupCode())) {
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED, "编号已经存在");
        } else {
            iApiGroupService.save(entity);
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        }
    }

    public ResponseResult remove(BigInteger id) {
        ApiGroupEntity codeEntity = iApiGroupService.getById(id);
        if (LtCommonUtil.isBlankOrNull(codeEntity)) {
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED, "未查到数据!");
        }
        if(codeEntity.getDataType()== StatusConstants.CAN_NOT_DELETE){
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED_DEFAULT);
        }
        List<ApiGroupEntity> codeList = iApiGroupService.childEntity(codeEntity.getGroupCode());
        if (codeList.size() > 0)
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED, "存在子数据，无法删除!");
        else {
            iApiGroupService.removeById(id);
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        }
    }

    public ResponseResult update(BigInteger id, ApiGroupEntity codeEntity) {
        ApiGroupEntity resultEntity = iApiGroupService.getById(id);
        if (LtCommonUtil.isBlankOrNull(resultEntity)) {
            return ResponseResult.error(CommonExceptionEnum.DATA_UPDATE_FAILED, "未查到数据!");
        } else {
            codeEntity.setId(id);
            codeEntity.setGroupCode(resultEntity.getGroupCode());
            iApiGroupService.updateById(codeEntity);
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        }
    }

    public ResponseResult search(SearchRequest searchRequest) {
        return ResponseResult.success(iApiGroupService.searchList(searchRequest));
    }


    public ResponseResult getTree(SearchRequest searchRequest) {
        searchRequest.pageParams.pageSize=-1;//树型默认查所有
        SearchResponse<ApiGroupEntity> searchResponse = iApiGroupService.searchList(searchRequest);
        List<ApiGroupEntity> resultList = searchResponse.results;
        List<TreeModel> treeModelList = new ArrayList<>();

        List<ApiGroupEntity> rootList = resultList.stream().filter(e -> e.getParentCode().equals(Constants.ROOT_VALUE)).collect(Collectors.toList());

        for(ApiGroupEntity entity:rootList){
            TreeModel treeModel=new TreeModel();
            treeModel.setCode(entity.getGroupCode());
            treeModel.setName(entity.getGroupName());
            treeModel.setNote(entity.getRemark());
            treeModel.setId(entity.getId());
            List<TreeModel> recursiveList= recursive(treeModel,resultList);
            treeModel.setChild(recursiveList);
            treeModelList.add(treeModel);
        }
        return ResponseResult.success(treeModelList);
    }

    public List<TreeModel> recursive(TreeModel treeModel,List<ApiGroupEntity> list){

        List<ApiGroupEntity> chidList=list.stream().filter(e->e.getParentCode().toString().equals(treeModel.getCode())).collect(Collectors.toList());

        List<TreeModel> chidTreeModelList=new ArrayList<>();
        if(chidList.size()>0){
            for(ApiGroupEntity entity:chidList){
                TreeModel model=new TreeModel();
                model.setCode(entity.getGroupCode());
                model.setName(entity.getGroupName());
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
