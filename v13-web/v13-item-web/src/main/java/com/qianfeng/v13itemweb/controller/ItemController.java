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
    public ResultBean batchCreateHTML(@RequestParam List<Long> ids){
        //单线程的写法，多核CPU
        //多线程的方式改善这段代码
        //100
        //1 100 没有先后生成的顺序要求，所以就采用多线程来批量并发生成这些页面
        //线程如何改如何正确使用
        //线程池
        //Runnable   new Thread(task)--->采用线程池的方式来走
        //线程池有几个基本的要素：
        //1.初始化线程数
        //2.最大线程数
        //3.最大发呆时间
        //4.等待队列的长度
        //JDK
        //单个线程的线程池：顺序执行一些任务，顺序执行效果
        //队列太长，导致内存出现OOM
        //ExecutorService pool = Executors.newSingleThreadExecutor();
        //队列太长，导致内存出现OOM
        //ExecutorService pool = Executors.newFixedThreadPool(10);
        //创建线程对象太多
        //ExecutorService pool = Executors.newCachedThreadPool();

        //自定义的方式来创建
        //创建线程数多少合适？
        //结合硬件环境来考虑
        //查看当前硬件有多少核
        int cpus = Runtime.getRuntime().availableProcessors();
        //
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                cpus,cpus*2,10, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(100));

        for (Long id : ids) {
            //createHTMLById(id);
            pool.submit(new CreateHTMLTask(id));
        }
        return null;
    }
}

class CreateHTMLTask2 implements Callable<String>{

    @Override
    public String call() throws Exception {
        return null;
    }
}

class CreateHTMLTask implements Runnable{

    private Long id;

    public CreateHTMLTask(Long id){
        this.id = id;
    }

    @Override
    public void run(){} /*{
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
    }*/
}
