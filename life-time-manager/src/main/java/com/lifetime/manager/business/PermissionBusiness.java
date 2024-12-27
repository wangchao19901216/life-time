package com.lifetime.manager.business;

import com.lifetime.common.constant.Constants;
import com.lifetime.common.constant.ResponseResultConstants;
import com.lifetime.common.constant.StatusConstants;
import com.lifetime.common.enums.CommonExceptionEnum;
import com.lifetime.common.enums.PermissionTypeEnum;
import com.lifetime.common.manager.entity.PermissionEntity;
import com.lifetime.common.manager.entity.RoleEntity;
import com.lifetime.common.manager.service.IPermissionService;
import com.lifetime.common.model.TreeModel;
import com.lifetime.common.response.ResponseResult;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import com.lifetime.common.util.LtCommonUtil;
import com.lifetime.common.util.LtModelUtil;
import com.lifetime.common.util.SnowflakeUtil;
import com.lifetime.manager.model.PermissionRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
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
public class PermissionBusiness {
    @Autowired
    IPermissionService iPermissionService;

    public ResponseResult save(PermissionEntity permissionEntity) {
        PermissionEntity resultEntity = iPermissionService.findByPermissionId(permissionEntity.getPermissionId());
        if (LtCommonUtil.isNotBlankOrNull(resultEntity)) {
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED, "编号已经存在");
        } else {
            iPermissionService.save(permissionEntity);
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        }

    }

    public ResponseResult saveWithButton(PermissionRequestModel permissionRequestModel) {
        List<PermissionEntity> list=new ArrayList<>();
        String permissionId= String.valueOf(SnowflakeUtil.nextLongId());
        PermissionEntity permissionEntity=permissionRequestModel.getPermission();
        permissionEntity.setPermissionId(permissionId);
        permissionEntity.setId(null);
        list.add(permissionEntity);
        //按钮权限
        if(LtCommonUtil.isNotBlankOrNull(permissionRequestModel.getButtons())&&permissionRequestModel.getButtons().size()>0){
            for(PermissionEntity permission:permissionRequestModel.getButtons()){
                PermissionEntity entity=LtModelUtil.copyTo(permission,PermissionEntity.class);
                entity.setId(null);
                entity.setPermissionId(String.valueOf(SnowflakeUtil.nextLongId()));
                entity.setParentId(permissionId);
                entity.setType(PermissionTypeEnum.Button.getCode());
                list.add(entity);
            }
        }
        iPermissionService.saveBatch(list);
        return ResponseResult.success(ResponseResultConstants.SUCCESS);
    }


    public ResponseResult remove(String permissionId) {
        PermissionEntity permissionEntity = iPermissionService.findByPermissionId(permissionId);
        if(permissionEntity.getDataType()==StatusConstants.CAN_NOT_DELETE){
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED_DEFAULT);
        }
        List<PermissionEntity> permissionEntityList = iPermissionService.childPermission(permissionId, StatusConstants.DISABLE);
        if (permissionEntityList.size() > 0)
            return ResponseResult.error(CommonExceptionEnum.DATA_DELETE_FAILED, "存在子菜单，无法删除!");
        else {
            iPermissionService.removeById(permissionEntity.getId());
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        }
    }

    public ResponseResult update(String permissionId, PermissionEntity permissionEntity) {
        PermissionEntity resultEntity = iPermissionService.findByPermissionId(permissionId);
        if (LtCommonUtil.isBlankOrNull(resultEntity)) {
            return ResponseResult.error(CommonExceptionEnum.DATA_UPDATE_FAILED, "权限不存在");
        } else {
            permissionEntity.setPermissionId(permissionId);
            permissionEntity.setId(resultEntity.getId());
            iPermissionService.updateById(permissionEntity);
            return ResponseResult.success(ResponseResultConstants.SUCCESS);
        }
    }


    @Transactional
    public ResponseResult updateWithButton(PermissionRequestModel permissionRequestModel) {
      List<PermissionEntity> list=new ArrayList<>();
      //库里原始数据
      PermissionRequestModel resultModel=buildPermissionRequestModel(permissionRequestModel.getPermission().getPermissionId());
      list.add(permissionRequestModel.getPermission());
      for(PermissionEntity permission:permissionRequestModel.getButtons()){
          if(LtCommonUtil.isNotBlankOrNull(permission.getId())){
              list.add(permission);
          }
          else{
              PermissionEntity entity=LtModelUtil.copyTo(permission,PermissionEntity.class);
              entity.setPermissionId(String.valueOf(SnowflakeUtil.nextLongId()));
              entity.setParentId(permissionRequestModel.getPermission().getPermissionId());
              entity.setType(PermissionTypeEnum.Button.getCode());
              list.add(entity);
          }
      }
      iPermissionService.saveOrUpdateBatch(list);

      /**
       * 处理删除的按钮权限
       * */
      List<PermissionEntity> resultButtons=resultModel.getButtons();
      //过滤掉新增的按钮
      List<PermissionEntity> sourceButtons=permissionRequestModel.getButtons().stream().filter(p-> LtCommonUtil.isNotBlankOrNull(p.getPermissionId())).collect(Collectors.toList());
      //已经删除的按钮
      List<PermissionEntity> deleteButtons=resultButtons.stream()
              .filter(p-> sourceButtons.stream().filter(r-> r.getPermissionId().equals(p.getPermissionId())).collect(Collectors.toList()).size()==0)
              .collect(Collectors.toList());
      if(deleteButtons.size()>0){
          iPermissionService.removeBatchByIds(deleteButtons);
      }
      return ResponseResult.success(ResponseResultConstants.SUCCESS);
    }


    public ResponseResult search(SearchRequest searchRequest) {
        return ResponseResult.success(iPermissionService.searchList(searchRequest));
    }
    public ResponseResult searchOneWithButton(String permissionId) {
        return ResponseResult.success(buildPermissionRequestModel(permissionId));
    }

    public PermissionRequestModel buildPermissionRequestModel(String permissionId){
        PermissionEntity permissionEntity=iPermissionService.findByPermissionId(permissionId);
        List<PermissionEntity> permissionEntityList=iPermissionService.childPermission(permissionId, StatusConstants.DISABLE);
        List<PermissionEntity> buttons=permissionEntityList.stream().filter(e->e.getType().equals(PermissionTypeEnum.Button.getCode())).collect(Collectors.toList());
        return  PermissionRequestModel.builder()
                .permission(permissionEntity)
                .buttons(buttons)
                .build();
    }



    public ResponseResult getTree(SearchRequest searchRequest) {
        searchRequest.pageParams.pageSize=-1;//树型默认查所有
        SearchResponse<PermissionEntity> searchResponse = iPermissionService.searchList(searchRequest);
        List<PermissionEntity> resultList = searchResponse.results;
        List<TreeModel> treeModelList = new ArrayList<>();

        List<PermissionEntity> rootList = resultList.stream().filter(e -> e.getParentId().equals(Constants.ROOT_VALUE)).collect(Collectors.toList());

        for(PermissionEntity entity:rootList){
            TreeModel treeModel=new TreeModel();
            treeModel.setCode(entity.getPermissionId());
            treeModel.setName(entity.getName());
            treeModel.setType(entity.getType());
            treeModel.setNote(entity.getRemark());
            treeModel.setId(entity.getId());
            List<TreeModel> recursiveList= recursive(treeModel,resultList);
            treeModel.setChild(recursiveList);
            treeModelList.add(treeModel);
        }
        return ResponseResult.success(treeModelList);
    }

    public List<TreeModel> recursive(TreeModel treeModel,List<PermissionEntity> list){

        List<PermissionEntity> chidList=list.stream().filter(e->e.getParentId().toString().equals(treeModel.getCode())).collect(Collectors.toList());
        List<TreeModel> chidTreeModelList=new ArrayList<>();
        if(chidList.size()>0){
            for(PermissionEntity entity:chidList){
                TreeModel model=new TreeModel();
                model.setCode(entity.getPermissionId());
                model.setName(entity.getName());
                model.setType(entity.getType());
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
