package com.qianfeng.v13.api;

import com.qianfeng.v13.common.pojo.ResultBean;

/**
 * @author huangguizhao
 */
public interface ISearchService {

    /**
     * 全量同步
     * @return
     */
    public ResultBean synAllData();

    /**
     * 增量同步
     * @param id
     * @return
     */
    public ResultBean synDataById(Long id);

    /**
     * 按照关键词进行搜索
     * @param keywords
     * @return
     */
    public ResultBean queryByKeywords(String keywords);


}
