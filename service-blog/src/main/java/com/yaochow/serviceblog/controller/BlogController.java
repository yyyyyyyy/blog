package com.yaochow.serviceblog.controller;

import com.alibaba.fastjson.JSONObject;
import com.yaochow.serviceblog.common.ResponseMsgEnum;
import com.yaochow.serviceblog.common.ResultBase;
import com.yaochow.serviceblog.dto.BlogDTO;
import com.yaochow.serviceblog.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ResultBase<BlogDTO> insert(@RequestBody BlogDTO blogDTO) {
        long start = System.currentTimeMillis();
        ResultBase<BlogDTO> result = new ResultBase<>();
        log.info("insert, param : {}", JSONObject.toJSONString(blogDTO));
        try {
            blogDTO = blogService.insert(blogDTO);
            result.setResult(blogDTO);
            result.setSuccess(true);
            result.setErrorCode(ResponseMsgEnum.SUCCESS.getErrorCode());
            result.setErrorMsg(ResponseMsgEnum.SUCCESS.getErrorMsg());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setErrorCode(ResponseMsgEnum.SYSTEM_ERROR.getErrorCode());
            result.setErrorMsg(ResponseMsgEnum.SYSTEM_ERROR.getErrorMsg());
        }
        log.info("insert, result : {}, cost : {}ms", result.isSuccess(), System.currentTimeMillis() - start);
        return result;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultBase<BlogDTO> update(@RequestBody BlogDTO blogDTO) {
        long start = System.currentTimeMillis();
        ResultBase<BlogDTO> result = new ResultBase<>();
        log.info("update, param : {}", JSONObject.toJSONString(blogDTO));
        try {
            blogDTO = blogService.update(blogDTO);
            result.setResult(blogDTO);
            result.setSuccess(true);
            result.setErrorCode(ResponseMsgEnum.SUCCESS.getErrorCode());
            result.setErrorMsg(ResponseMsgEnum.SUCCESS.getErrorMsg());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setErrorCode(ResponseMsgEnum.SYSTEM_ERROR.getErrorCode());
            result.setErrorMsg(ResponseMsgEnum.SYSTEM_ERROR.getErrorMsg());
        }
        log.info("update, result : {}, cost : {}ms", result.isSuccess(), System.currentTimeMillis() - start);
        return result;
    }

    @RequestMapping(value = "/getBlogById/{id}", method = RequestMethod.GET)
    public ResultBase<BlogDTO> getBlogById(@PathVariable String id) {
        long start = System.currentTimeMillis();
        ResultBase<BlogDTO> result = new ResultBase<>();
        log.info("get blog, id : {}", id);
        try {
            BlogDTO blogDTO = blogService.getBlogById(id);
            result.setResult(blogDTO);
            result.setSuccess(true);
            result.setErrorCode(ResponseMsgEnum.SUCCESS.getErrorCode());
            result.setErrorMsg(ResponseMsgEnum.SUCCESS.getErrorMsg());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setErrorCode(ResponseMsgEnum.SYSTEM_ERROR.getErrorCode());
            result.setErrorMsg(ResponseMsgEnum.SYSTEM_ERROR.getErrorMsg());
        }
        log.info("get blog, result : {}, cost : {}ms", result.isSuccess(), System.currentTimeMillis() - start);
        return result;
    }

    @RequestMapping(value = "/listBlogs", method = RequestMethod.GET)
    public ResultBase<List<BlogDTO>> listBlogs() {
        long start = System.currentTimeMillis();
        ResultBase<List<BlogDTO>> result = new ResultBase<>();
        log.info("list blogs");
        try {
            List<BlogDTO> blogDTOs = blogService.findAll();
            result.setResult(blogDTOs);
            result.setSuccess(true);
            result.setErrorCode(ResponseMsgEnum.SUCCESS.getErrorCode());
            result.setErrorMsg(ResponseMsgEnum.SUCCESS.getErrorMsg());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setErrorCode(ResponseMsgEnum.SYSTEM_ERROR.getErrorCode());
            result.setErrorMsg(ResponseMsgEnum.SYSTEM_ERROR.getErrorMsg());
        }
        log.info("list blogs, result : {}, cost : {}ms", result.isSuccess(), System.currentTimeMillis() - start);
        return result;
    }
}
