package com.qianfeng.v13centerweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.qianfeng.v13.api.IProductService;
import com.qianfeng.v13.entity.TProduct;
import com.qianfeng.v13.pojo.TProductVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author huangguizhao
 */
@Controller
@RequestMapping("product")
public class ProductController {

    @Reference
    private IProductService productService;

    @RequestMapping("get/{id}")
    @ResponseBody
    public TProduct geById(@PathVariable("id") Long id){
        return productService.selectByPrimaryKey(id);
    }

    @RequestMapping("list")
    public String list(Model model){
        //1.获取数据
        List<TProduct> list = productService.list();
        //2.保存数据到model
        model.addAttribute("list",list);
        //3.跳转到页面展示
        return "product/list";
    }

    @RequestMapping("page/{pageIndex}/{pageSize}")
    public String page(@PathVariable("pageIndex") Integer pageIndex,
                       @PathVariable("pageSize") Integer pageSize,
                       Model model){
        PageInfo<TProduct> page = productService.page(pageIndex, pageSize);
        model.addAttribute("page",page);

        return "product/list";
    }


    @PostMapping("add")
    public String add(TProductVO vo){
        Long id = productService.save(vo);
        //重定向到第一页
        //order by update_time desc
        return "redirect:/product/page/1/1";
    }

}
