package com.example.jtechstack.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author carl-rabbit
 * @since 2022-05-28
 */
@Data
@Builder
@TableName("repository")
@ApiModel(value = "Repository对象", description = "")
public class Repository implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("full_name")
    private String fullName;

    @TableField("owner_id")
    private Integer ownerId;

    @TableField("created_at")
    private String createdAt;

    @TableField("updated_at")
    private String updatedAt;

    @TableField("pushed_at")
    private String pushedAt;

    @TableField("size")
    private Integer size;

    @TableField("stars")
    private Integer stars;

    @TableField("forks")
    private Integer forks;

    @TableField("open_issues")
    private Integer openIssues;

    @TableField("watchers")
    private Integer watchers;

    @TableField("license_key")
    private String licenseKey;

    @TableField("allow_forking")
    private Boolean allowForking;

    @TableField("is_template")
    private Boolean isTemplate;

    @TableField("topics")
    private String topics;

    @TableField("content")
    private String content;

    @TableField("java_version")
    private String javaVersion;

    @TableField("management")
    private String management;

    @TableField("jts_timestamp")
    private LocalDateTime jtsTimestamp;


}
