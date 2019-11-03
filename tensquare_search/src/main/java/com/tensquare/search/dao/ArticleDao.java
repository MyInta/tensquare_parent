package com.tensquare.search.dao;

import com.tensquare.search.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author inta
 * @date 2019/10/28
 * @describe
 */
public interface ArticleDao  extends ElasticsearchRepository<Article, String> {
        public Page<Article> findByTitleOrContentLike(String title, String content, Pageable pageable);
}
