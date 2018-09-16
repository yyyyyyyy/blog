package com.yaochow.serviceblog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yaochow.serviceblog.cache.RedisCache;
import com.yaochow.serviceblog.common.DBConstant;
import com.yaochow.serviceblog.dto.BlogDTO;
import com.yaochow.serviceblog.entity.Blog;
import com.yaochow.serviceblog.repository.BlogRepository;
import com.yaochow.serviceblog.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private RedisCache redisCache;

    private volatile AtomicBoolean state = new AtomicBoolean();

    @Override
    public BlogDTO insert(BlogDTO blogDTO) {
        Blog blog = new Blog();
        BeanUtils.copyProperties(blogDTO, blog);
        blog.setCreator(DBConstant.SYSTEM);
        blog.setGmtCreated(Calendar.getInstance().getTime());
        blog.setModifier(DBConstant.SYSTEM);
        blog.setGmtModified(Calendar.getInstance().getTime());
        blog.setDelete(DBConstant.UN_DELETE);
        blogDTO.setId(blogRepository.insert(blog).getId());
        String key = "redis:key:blog:all";
        redisCache.remove(key);
        log.info("remove redis key : {}", key);
        return blogDTO;
    }

    @Override
    public BlogDTO getBlogById(String id) {
        String key = "redis:key:blog:id:" + id;
        Object value = redisCache.get(key);
        if (Objects.isNull(value)) {
            log.info("get value from mongodb, id : {}", id);
            Optional<Blog> optional = blogRepository.findById(id);
            if (optional.isPresent()) {
                BlogDTO blogDTO = new BlogDTO();
                BeanUtils.copyProperties(optional.get(), blogDTO);
                redisCache.set(key, JSONObject.toJSONString(blogDTO), 3600L * 6);
                return blogDTO;
            }
        } else {
            log.info("get value from redis, key : {}", key);
            String blogDTOJson = (String) value;
            BlogDTO blogDTO = JSONObject.parseObject(blogDTOJson, BlogDTO.class);
            return blogDTO;
        }

        return null;
    }

    @Override
    public BlogDTO update(BlogDTO blogDTO) {
        Optional<Blog> optional = blogRepository.findById(blogDTO.getId());
        if (optional.isPresent()) {
            Blog blog = optional.get();
            blog.setName(blogDTO.getName());
            blog.setContent(blogDTO.getContent());
            blog.setModifier(DBConstant.SYSTEM);
            blog.setGmtModified(Calendar.getInstance().getTime());
            blogRepository.save(blog);
            String key = "redis:key:blog:id:" + blogDTO.getId();
            redisCache.remove(key);
            log.info("remove redis key : {}", key);
            return blogDTO;
        }
        return null;
    }

    @Override
    public List<BlogDTO> findAll() {
        String key = "redis:key:blog:all";
        List value = redisCache.lRange(key, 0, -1);

        if (CollectionUtils.isEmpty(value)) {
            log.info("get value from mongodb");
            List<Blog> blogs = blogRepository.findAll();
            if (!CollectionUtils.isEmpty(blogs)) {
                final List<BlogDTO> blogDTOS = new ArrayList<>(blogs.size());
                boolean success = state.compareAndSet(false, true);
                if (success) {
                    blogs.forEach(blog -> {
                        BlogDTO blogDTO = new BlogDTO();
                        blogDTO.setId(blog.getId());
                        blogDTO.setName(blog.getName());
                        redisCache.lPush(key, JSONObject.toJSONString(blogDTO));
                        blogDTOS.add(blogDTO);
                    });
                    redisCache.expired(key, 3600L * 3);
                    state.set(false);
                } else
                    return this.findAll();
                return blogDTOS;
            }
        } else {
            log.info("get value from redis");
            final List<BlogDTO> blogDTOS = new ArrayList<>(value.size());
            value.forEach(blog ->
                    blogDTOS.add(JSONObject.parseObject((String) blog, BlogDTO.class))
            );
            return blogDTOS;
        }

        return null;
    }
}
