package com.shl.crowdfunding.manager.mapper;

import com.shl.crowdfunding.bean.Type;
import java.util.List;

public interface TypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Type record);

    Type selectByPrimaryKey(Integer id);

    List<Type> selectAll();

    int updateByPrimaryKey(Type record);
}