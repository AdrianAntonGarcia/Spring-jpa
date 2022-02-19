package com.bolsaideas.springboot.app.models.service;

import java.util.List;

import com.bolsaideas.springboot.app.models.entity.Cliente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IClienteService {
    public List<Cliente> findaAll();

    public Page<Cliente> findaAll(Pageable pageable);

    public void save(Cliente cliente);

    public Cliente findOne(Long id);

    public void delete(Long id);
}
