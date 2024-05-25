package com.example.product.repository;


import com.example.product.po.Category;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CategoryRepository extends ReactiveCrudRepository<Category, Long> {
    @Query("select * from category order by update_time desc;")
    Flux<Category> findAll();

    @Query("select * from category order by update_time desc limit :pageSize offset :offset")
    Flux<Category> findAll(int offset, int pageSize);

    @Query("select count(*) from category")
    Mono<Integer> findCount();

    @Query("select count(*) from category c where c.parent_id=:pid")
    Mono<Integer> findCount(long pid);

    @Query("select count(*) from category c where c.parent_id=:pid")
    Mono<Integer> findCountByParentId(long pid);

    @Query("select * from category c where c.parent_id=:pid order by update_time desc limit :pageSize offset :page")
    Flux<Category> findByParentId(long pid, int page, int pageSize);

    Flux<Category> findByParentId(long pid);

    Flux<Category> findByLevel(int level);

    Flux<Category> findByParentIdOrderByInsertTime(long pid);
}
