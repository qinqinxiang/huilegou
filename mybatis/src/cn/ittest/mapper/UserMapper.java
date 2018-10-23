package cn.ittest.mapper;

import cn.ittest.domain.Query;
import cn.ittest.domain.User;

public interface UserMapper {
    User getQueryByUser(Query query);

    Integer getQueryCount();
}
