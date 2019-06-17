package com.qianfeng.v13searchweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qianfeng.v13.api.ISearchService;
import com.qianfeng.v13.common.pojo.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author huangguizhao
 */
@Controller
@RequestMapping("search")
public class SearchController {

    @Reference
    private ISearchService searchService;

    @RequestMapping("queryByKeywords")
    public String queryByKeywords(String keywords, Model model){
        //1.通过搜索服务获取到数据
        ResultBean resultBean = searchService.queryByKeywords(keywords);
        //2.将数据保存到model中
        model.addAttribute("result",resultBean);
        //3.跳转到页面进行展示
        return "list";
    }
}
