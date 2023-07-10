package com.oa.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.charles.model.process.Process;
import com.charles.vo.process.ProcessQueryVo;
import com.charles.vo.process.ProcessVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Charles
 * @create 2023-06-07-0:22
 */
@Mapper
public interface ProcessMapper extends BaseMapper<Process> {

    IPage<ProcessVo> selectPage(Page<ProcessVo> myPage, @Param("vo") ProcessQueryVo processQueryVo);
}
