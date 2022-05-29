package com.example.jtechstack.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author carl-rabbit
 * @since 2022-05-29
 */
@Getter
@Setter
@TableName("maven_repo")
@ApiModel(value = "MavenRepo对象", description = "")
public class MavenRepo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("group_id")
    private String groupId;

    @TableField("artifact_id")
    private String artifactId;

    @TableField("description")
    private String description;

    @TableField("categories")
    private String categories;

    @TableField("tags")
    private String tags;

    @TableField("used_by")
    private Integer usedBy;

    @TableField("version_usages")
    private String versionUsages;

    @TableField("jts_timestamp")
    private LocalDateTime jtsTimestamp;


}
