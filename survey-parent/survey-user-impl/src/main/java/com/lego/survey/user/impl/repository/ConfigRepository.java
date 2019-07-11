package com.lego.survey.user.impl.repository;
import com.lego.survey.user.model.entity.Config;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author yanglf
 * @description
 * @since 2018/12/21
 **/
@Repository
public interface ConfigRepository extends MongoRepository<Config,String> {


    /**
     * 根据id查询配置信息
     * @param id
     * @param valid
     * @return
     */
    Config  findConfigByIdAndValid(String id, int valid);


    /**
     * 通过名称获取配置信息
     * @param name
     * @param valid
     * @return
     */
    Config   findConfigByNameAndValidOrderByUpdateTimeDesc(String name, int valid);


}
