package com.example.product.repository;


import com.example.product.po.Category;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CategoryRepository extends ReactiveCrudRepository<Category, Long> {

    @Query("select * from category c where c.is_parent=:isParent")
    Flux<Category> list(int isParent);

    @Query("select count(*) from category c where c.parent_id=:pid")
    Mono<Integer> findCountByParentId(long pid);

    @Query(
            "WITH RECURSIVE CategoryPath AS (" +
                    "  SELECT id, name, parent_id FROM category WHERE parent_id = 0" + // 查询根节点
                    "  UNION ALL" +
                    "  SELECT c.id, c.name, c.parent_id FROM category c JOIN CategoryPath cp ON c.id = cp.parent_id" +
                    ")" +
                    "SELECT id, name FROM CategoryPath"
    )
    Flux<Category> findAllParents();
}
