package com.qianfeng.v13itemweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qianfeng.v13.api.IProductService;
import com.qianfeng.v13.common.pojo.ResultBean;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author huangguizhao
 */
@Controller
@RequestMapping("item")
public class ItemController {

    @Reference
    private IProductService productService;

    @Autowired
    private Configuration configuration;

    @Autowired
    private ThreadPoolExecutor threadPool;

    @RequestMapping("createHTMLById/{id}")
    @ResponseBody
    public ResultBean createHTMLById(@PathVariable("id") Long id){
        return createHTMLByID(id);
    }

    private ResultBean createHTMLByID(@PathVariable("id") Long id) {
        try {
            //1.获取模板
            Template template = configuration.getTemplate("item.ftl");
            //2.获取数据
            Map<String,Object> data = new HashMap<>();
            data.put("product",productService.selectByPrimaryKey(id));
            //3.输出
            //以classpath的方式来获取保存路径
            String serverpath= ResourceUtils.getURL("classpath:static").getPath();
            System.out.println(serverpath);
            String filePath = new StringBuilder(serverpath).append(File.separator).append(id).append(".html").toString();
            FileWriter writer = new FileWriter(filePath);
            //4.集结
            template.process(data,writer);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultBean("404","获取模板失败");
        } catch (TemplateException e) {
            e.printStackTrace();
            return new ResultBean("404","生成静态页面失败");
        }
        return new ResultBean("200","生成静态页成功！");
    }

    @RequestMapping("batchCreateHTML")
    @ResponseBody
    public ResultBean batchCreateHTML(@RequestParam List<Long> ids) throws ExecutionException, InterruptedException {
        List<Future<Long>> results = new ArrayList<>(ids.size());
        for (Long id : ids) {
            results.add(threadPool.submit(new CreateHTMLTask(id)));
        }

        List<Long> errors = new ArrayList<>();
        //查看每个线程的处理结果
        for (Future<Long> result : results) {
            Long id = result.get();
            if(id != 0){
                //出问题了
                //记录下出现的id
                errors.add(id);
            }
        }
        //如果是失败了，应该怎么办？
        //1.是谁失败了？
        //2.失败了，补救方案是什么
        if(errors.size() > 0){
            //记录到失败日志
            //create_time id  --> 菜单 （1,5,8）
            //解决方案：
            //方案一：手工模式 直接调用接口去实现
            //方案二：自动模式，定时任务（重试次数，不要超过三次）
            return new ResultBean("500","批量生成页面失败！");
        }
        return new ResultBean("200","批量生成页面成功！");
    }

    //内部类
    private class CreateHTMLTask implements Callable<Long>{

        private Long id;

        public CreateHTMLTask(Long id){
            this.id = id;
        }

        @Override
        public Long call() throws Exception {
            try {
                //1.获取模板
                Template template = configuration.getTemplate("item.ftl");
                //2.获取数据
                Map<String,Object> data = new HashMap<>();
                data.put("product",productService.selectByPrimaryKey(id));
                //3.输出
                //以classpath的方式来获取保存路径
                String serverpath= ResourceUtils.getURL("classpath:static").getPath();
                System.out.println(serverpath);
                String filePath = new StringBuilder(serverpath).append(File.separator).append(id).append(".html").toString();
                FileWriter writer = new FileWriter(filePath);
                //4.集结
                template.process(data,writer);
            } catch (IOException e) {
                e.printStackTrace();
                return id;
            } catch (TemplateException e) {
                e.printStackTrace();
                return id;
            }
            return 0L;
        }
    }
}

