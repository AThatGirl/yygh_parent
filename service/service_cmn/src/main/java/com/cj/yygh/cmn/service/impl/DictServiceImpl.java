package com.cj.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cj.yygh.cmn.listener.DictListener;
import com.cj.yygh.cmn.mapper.DictMapper;
import com.cj.yygh.cmn.service.DictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cj.yygh.model.cmn.Dict;
import com.cj.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 组织架构表 服务实现类
 * </p>
 *
 * @author cj
 * @since 2023-05-13
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {


    @Cacheable(value = "dict", key = "'selectIndexList' + #pid")
    @Override
    public List<Dict> getDictListByPid(Integer pid) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", pid);

        List<Dict> dicts = baseMapper.selectList(queryWrapper);
        for (Dict dict : dicts) {
            boolean result = hasChild(dict);
            dict.setHasChildren(result);

        }
        return dicts;
    }

    @Override
    public void exportExcel(HttpServletResponse response) throws IOException {

        //导出文件要设置两个头信息Mime-type、content-type:attachment
        //测试时不要使用swagger
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        //这里URLEncoder.encode可以防止中文乱码当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        List<Dict> dicts = baseMapper.selectList(null);
        List<DictEeVo> dictEeVoList = new ArrayList<>();
        for (Dict dict : dicts) {
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict, dictEeVo);
            dictEeVoList.add(dictEeVo);
        }

        EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet().doWrite(dictEeVoList);
    }

    @Override
    public void importData(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(baseMapper)).sheet().doRead();
    }

    //判断有没有子元素
    private boolean hasChild(Dict dict) {

        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", dict.getId());
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }
}
