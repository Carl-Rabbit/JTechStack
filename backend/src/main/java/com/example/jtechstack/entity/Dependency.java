package com.example.jtechstack.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

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
@TableName("dependency")
@ApiModel(value = "Dependency对象", description = "")
public class Dependency implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("repo_id")
    private Integer repoId;

    @TableField("dep_id")
    private Integer depId;

    @TableField("version")
    private String version;

    @TableField("content")
    private String content;

    @TableField("jts_timestamp")
    private LocalDateTime jtsTimestamp;


}
