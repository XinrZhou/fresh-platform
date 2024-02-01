package com.example.product.repository;


import com.example.product.po.Category;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CategoryRepository extends ReactiveCrudRepository<Category, Long> {

    @Query("select * from category c where c.is_parent=:isParent")
    Flux<Category> list(int isParent);
}
