package com.cj.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cj.yygh.model.cmn.Dict;

import java.util.List;

/**
 * <p>
 * 组织架构表 服务类
 * </p>
 *
 * @author cj
 * @since 2023-05-13
 */
public interface DictService extends IService<Dict> {

    List<Dict> getDictListByPid(Integer pid);
}
