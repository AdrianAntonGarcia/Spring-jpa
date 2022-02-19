package com.bolsaideas.springboot.app.models.dao;

import com.bolsaideas.springboot.app.models.entity.Cliente;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Primer parámetro la clase y el segundo el tipo de dato de la clave de la
 * tabla
 */
public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long> {

}
