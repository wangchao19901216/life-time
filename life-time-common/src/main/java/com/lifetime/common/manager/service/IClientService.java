package com.lifetime.common.manager.service;


import com.lifetime.common.manager.entity.ClientEntity;
import com.lifetime.common.service.BaseService;

public interface IClientService extends BaseService<ClientEntity> {

    Integer update(ClientEntity clientEntity);

    Integer delete(Long id);

    ClientEntity findById(Long id);

    ClientEntity findByClientId(String clientId);

    Integer deleteByClientId(String clientId);
}
