package com.pw.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pw.common.entity.BaseEntity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/***
 * @author cyd
 * @date 2023/5/17 11:35
 * @description <node-red接收参数>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "node-red接收参数")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NodeRedParams extends BaseEntity implements Serializable {

    @Schema(description = "KEY")
    private Map<String, Object> KEY;

    @Schema(description = "value")
    private Map<String, Object> value;

    @Schema(description = "option")
    private Map<String, Object> option;
}
