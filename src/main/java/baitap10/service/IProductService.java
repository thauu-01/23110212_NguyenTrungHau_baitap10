package baitap10.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import baitap10.entity.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    void delete(Product entity);

    void deleteById(Long id);

    long count();

    <S extends Product> Optional<S> findOne(Example<S> example);

    Optional<Product> findById(Long id);

    List<Product> findAllById(Iterable<Long> ids);

    List<Product> findAll(Sort sort);

    Page<Product> findAll(Pageable pageable);

    List<Product> findAll();

    Optional<Product> findByProductName(String name);

    <S extends Product> S save(S entity);

    Page<Product> findByProductNameContaining(String name, Pageable pageable);

    List<Product> findByProductNameContaining(String name);
}