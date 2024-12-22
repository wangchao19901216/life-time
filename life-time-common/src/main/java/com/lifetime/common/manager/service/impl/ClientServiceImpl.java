package com.lifetime.common.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifetime.common.manager.dao.ClientMapper;
import com.lifetime.common.manager.entity.ClientEntity;
import com.lifetime.common.manager.service.IClientService;
import com.lifetime.common.model.QueryModel;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import com.lifetime.common.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, ClientEntity> implements IClientService {

    @Autowired
    ClientMapper clientMapper;

    @Override
    public boolean save(ClientEntity clientEntity) {
        return super.save(clientEntity);
    }

    @Override
    public Integer update(ClientEntity clientEntity) {
        QueryWrapper<ClientEntity> clientEntityQueryWrapper=new QueryWrapper<>();
        clientEntityQueryWrapper.eq("client_id",clientEntity.getClientId());
        return clientMapper.update(clientEntity,clientEntityQueryWrapper);
    }

    @Override
    public Integer delete(Long id) {
        return clientMapper.deleteById(id);
    }

    @Override
    public ClientEntity findById(Long id) {
        return clientMapper.selectById(id);
    }

    @Override
    public SearchResponse<ClientEntity> searchList(SearchRequest searchRequest) {
        QueryModel<ClientEntity> myQuery = QueryUtil.buildMyQuery(searchRequest,ClientEntity.class);
        SearchResponse<ClientEntity> searchResponse = QueryUtil.executeQuery(searchRequest,myQuery, this);
        return searchResponse;
    }

    @Override
    public ClientEntity findByClientId(String clientId) {
        QueryWrapper<ClientEntity> clientEntityQueryWrapper=new QueryWrapper<>();
        clientEntityQueryWrapper.eq("client_id",clientId);
        return clientMapper.selectOne(clientEntityQueryWrapper);
    }

    @Override
    public Integer deleteByClientId(String clientId) {
        QueryWrapper<ClientEntity> clientEntityQueryWrapper=new QueryWrapper<>();
        clientEntityQueryWrapper.eq("client_id",clientId);
        return clientMapper.delete(clientEntityQueryWrapper);
    }
}
