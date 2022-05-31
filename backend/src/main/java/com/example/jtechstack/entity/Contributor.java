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
 * @since 2022-05-29
 */
@Data
@Builder
@TableName("contributor")
@ApiModel(value = "Contributor对象", description = "")
public class Contributor implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("repo_id")
    private Integer repoId;

    @TableField("user_id")
    private Integer userId;

    @TableField("contributions")
    private Integer contributions;

    @TableField("content")
    private String content;

    @TableField("jts_timestamp")
    private LocalDateTime jtsTimestamp;


}
