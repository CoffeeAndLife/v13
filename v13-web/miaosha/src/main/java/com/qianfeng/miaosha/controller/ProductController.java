package com.qianfeng.miaosha.controller;

import com.qianfeng.miaosha.entity.TProduct;
import com.qianfeng.miaosha.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author huangguizhao
 */
@Controller
@RequestMapping("product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @RequestMapping("getById")
    public String getById(Long id,Model model){
        TProduct product = productService.getById(id);
        model.addAttribute("product",product);
        return "item";
    }

    @RequestMapping("sale")
    @ResponseBody
    public String sale(Long id){
        boolean result = productService.sale(id);
        if(result){
            return "success";
        }
        return "faild";
    }
}
