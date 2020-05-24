package com.shl.crowdfunding.manager.mapper;

import com.shl.crowdfunding.bean.Message;
import java.util.List;

public interface MessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Message record);

    Message selectByPrimaryKey(Integer id);

    List<Message> selectAll();

    int updateByPrimaryKey(Message record);
}