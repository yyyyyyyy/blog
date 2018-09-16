package com.yaochow.serviceblog.service;

import com.yaochow.serviceblog.dto.BlogDTO;

import java.util.List;

public interface BlogService {

    BlogDTO insert(BlogDTO blogDTO);

    BlogDTO getBlogById(String id);

    BlogDTO update(BlogDTO blogDTO);

    List<BlogDTO> findAll();

}
