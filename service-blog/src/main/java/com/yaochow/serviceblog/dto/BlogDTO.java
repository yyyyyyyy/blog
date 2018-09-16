package com.yaochow.serviceblog.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Data
public class BlogDTO implements Serializable {
    @Id
    private String id;
    @Field("name")
    private String name;
    @Field("content")
    private String content;
    @Field("gmt_created")
    protected Date gmtCreated;
    @Field("creator")
    protected String creator;
    @Field("gmt_modified")
    protected Date gmtModified;
    @Field("modifier")
    protected String modifier;
    @Field("is_delete")
    protected String delete;
}
