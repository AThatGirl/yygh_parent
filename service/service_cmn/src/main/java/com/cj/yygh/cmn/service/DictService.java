package com.cj.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cj.yygh.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

    void exportExcel(HttpServletResponse response) throws IOException;

    void importData(MultipartFile file) throws IOException;

    /**
     * 根据上级编码与值获取数据字典名称
     * @param parentDictCode
     * @param value
     */
    String getNameByParentDictCodeAndValue(String parentDictCode, String value);

    List<Dict> findByDictCode(String dictCode);
}
