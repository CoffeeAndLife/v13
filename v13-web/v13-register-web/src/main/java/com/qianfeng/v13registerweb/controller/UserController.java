package com.qianfeng.v13registerweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qianfeng.v13.api.IUserService;
import com.qianfeng.v13.common.constant.RabbitMQConstant;
import com.qianfeng.v13.entity.TUser;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author huangguizhao
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Reference
    private IUserService userService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TemplateEngine templateEngine;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @RequestMapping("register1")
    public String register1(){
        return "register1";
    }

    @RequestMapping("register2")
    public String register2(){
        return "register2";
    }

    @PostMapping("add")
    public String add(TUser user){
        //1.前端需要做校验
        //2.邮箱必须是唯一的
        //3.进行用户信息的保存
        int id = userService.insertSelective(user);
        if(id > 0){
            //4.异步通知邮件服务，发送邮件
            //to subject text
            //对象
            //map
            //直接发送给队列（邮件服务）
            //直接发送给其他队列(其他服务)
            //发送给交换机
            //1.根据模板，生成邮件的内容
            Context context = new Context();
            //创建一个激活唯一标识
            String uuid = UUID.randomUUID().toString();
            String href = new StringBuilder("http://localhost:9094/user/active/").append(uuid).toString();
            //设置模板的数据
            context.setVariable("username",user.getUsername());
            context.setVariable("href",href);
            //模板+数据=输出
            String text = templateEngine.process("jihuo.html", context);

            //在redis服务中保存uuid
            redisTemplate.opsForValue().set(uuid,id);

            //2.组织给交换机发送的数据
            Map<String,String> map = new HashMap<>();
            map.put("to",user.getEmail());
            map.put("subject","疯狂购物商城激活邮件");
            map.put("text",text);
            rabbitTemplate.convertAndSend(RabbitMQConstant.REGISTER_EXCHANGE,"user.add",map);
            return "success";
        }
        return "register2";
    }
}
