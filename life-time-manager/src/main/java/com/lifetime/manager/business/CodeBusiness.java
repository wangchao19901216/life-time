package com.lifetime.manager.business;

import com.lifetime.common.constant.ResponseResultConstants;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.manager.entity.CodeEntity;
import com.lifetime.common.manager.service.ICodeService;
import com.lifetime.common.model.TreeModel;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import com.lifetime.common.util.LtCommonUtil;
import org.aspectj.apache.bcel.classfile.Code;
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
public class CodeBusiness {

    @Autowired
    ICodeService iCodeService;

    public ResponseResult save(CodeEntity codeEntity) {
        if (iCodeService.isExist(codeEntity.getCodeType(),codeEntity.getCodeValue())) {
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED, "编号已经存在");
        } else {
            iCodeService.save(codeEntity);
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        }
    }

    public ResponseResult remove(BigInteger id) {
        CodeEntity codeEntity = iCodeService.getById(id);
        if (LtCommonUtil.isNotBlankOrNull(codeEntity)) {
            List<CodeEntity> codeList = iCodeService.childEntity(codeEntity.codeType, codeEntity.codeValue);
            if (codeList.size() > 0)
                return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED, "存在子数据，无法删除!");
            else {
                iCodeService.removeById(id);
                return ResponseResult.success(ResponseResultConstants.SUCCESS);
            }
        } else {
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED, "未查到改字典，无法删除!");
        }
    }

    public ResponseResult update(BigInteger id, CodeEntity codeEntity) {
        CodeEntity resultEntity = iCodeService.getById(id);
        if (LtCommonUtil.isBlankOrNull(resultEntity)) {
            return ResponseResult.error(CommonExceptionEnum.DATA_UPDATE_FAILED, "未查到对应编号的字典");
        } else {
            codeEntity.setId(id);
            codeEntity.setCodeValue(resultEntity.codeValue);
            codeEntity.setCodeType(resultEntity.codeType);
            iCodeService.updateById(codeEntity);
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        }
    }

    public ResponseResult search(SearchRequest searchRequest) {
        return ResponseResult.success(iCodeService.searchList(searchRequest));
    }
    public ResponseResult getTree(SearchRequest searchRequest) {
        searchRequest.pageParams.pageSize=-1;//树型默认查所有
        SearchResponse<CodeEntity> searchResponse = iCodeService.searchList(searchRequest);
        List<CodeEntity> resultList = searchResponse.results;
        List<TreeModel> treeModelList = new ArrayList<>();

        List<CodeEntity> rootList = resultList.stream().filter(e -> e.codeParent.equals("0")).collect(Collectors.toList());

        for(CodeEntity entity:rootList){
            TreeModel treeModel=new TreeModel();
            treeModel.setCode(entity.getCodeValue());
            treeModel.setType(entity.getCodeType());
            treeModel.setName(entity.getCodeName());
            treeModel.setNote(entity.getCodeNote());
            List<TreeModel> recursiveList= recursive(treeModel,resultList);
            treeModel.setChild(recursiveList);
            treeModelList.add(treeModel);
        }
        return ResponseResult.success(treeModelList);
    }

    public List<TreeModel> recursive(TreeModel treeModel,List<CodeEntity> list){

        List<CodeEntity> chidList=list.stream().filter(e->e.getCodeParent().toString().equals(treeModel.getCode())&&e.getCodeType().equals(treeModel.getType())).collect(Collectors.toList());
        List<TreeModel> chidTreeModelList=new ArrayList<>();
        if(chidList.size()>0){
            for(CodeEntity entity:chidList){
                TreeModel model=new TreeModel();
                model.setCode(entity.getCodeValue());
                model.setType(entity.getCodeType());
                model.setName(entity.getCodeName());
                model.setNote(entity.getCodeNote());
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
