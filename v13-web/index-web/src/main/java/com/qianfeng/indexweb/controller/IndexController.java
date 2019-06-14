package com.qianfeng.indexweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qianfeng.v13.api.IProductTypeService;
import com.qianfeng.v13.entity.TProductType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author huangguizhao
 */
@Controller
@RequestMapping("index")
public class IndexController {

    @Reference
    private IProductTypeService productTypeService;

    @RequestMapping("home")
    public String showHome(Model model){
        //1.调用远程服务，获取商品类别的数据
        List<TProductType> list = productTypeService.list();
        //2.将数据保存起来，到前端页面展示
        model.addAttribute("list",list);
        return "home";
    }
}
