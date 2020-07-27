package com.byoa.system.controller;


import com.byoa.common.config.Constant;
import com.byoa.common.controller.BaseController;
import com.byoa.common.domain.FileDO;
import com.byoa.common.domain.Tree;
import com.byoa.common.service.FileService;
import com.byoa.common.utils.R;
import com.byoa.common.utils.RandomValidateCodeUtil;
import com.byoa.common.utils.ShiroUtils;
import com.byoa.system.domain.ResourcesDTO;
import com.byoa.system.service.ResourcesDTOService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class LoginController  extends BaseController {

    @Autowired
    ResourcesDTOService resourcesDTOService;

    @Autowired
    FileService fileService;

    @GetMapping({"/", ""})
    String welcome(Model model) {

        return "redirect:/login";
    }

    @GetMapping("/login")
    String login() {
        return "login";
    }

    @GetMapping("/include")
    String include() {
        return "include";
    }

    @GetMapping("/index")
    String index(Model model) {
        List<Tree<ResourcesDTO>> list = resourcesDTOService.listResourcesTree(ShiroUtils.getUserId());
        model.addAttribute("menus", list);
        model.addAttribute("name", getUser().getName());
        FileDO fileDO = fileService.get(getUser().getPicId());
        if (fileDO != null && fileDO.getUrl() != null) {
            if (fileService.isExist(fileDO.getUrl())) {
                String url = fileDO.getUrl().substring(Constant.FileUploadPath.length());
               String picUrl= Constant.FileAbPath+url;
                model.addAttribute("picUrl", picUrl);
            } else {
                model.addAttribute("picUrl", "/img/photo_s.jpg");
            }
        } else {
            model.addAttribute("picUrl", "/img/photo_s.jpg");
        }
        model.addAttribute("username", getUser().getUsername());
        return "index_v1";
    }

    @PostMapping("/login")
    @ResponseBody
    R ajaxLogin(String username, String password, String verify, HttpServletRequest request) {
        try {
            //从session中获取随机数
            String random = (String) request.getSession().getAttribute(RandomValidateCodeUtil.RANDOMCODEKEY);
            if (StringUtils.isBlank(verify)) {
                return R.error("请输入验证码");
            }
            if (random.equals(verify)) {
            } else {
                return R.error("请输入正确的验证码");
            }
        } catch (Exception e) {
            return R.error("验证码校验失败");
        }
//        System.out.println("ajaxLogin:username:"+JackSonUtils.toJackString(username));
//        System.out.println("ajaxLogin:password:"+ JackSonUtils.toJackString(password));
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return R.ok();
        } catch (AuthenticationException e) {
            return R.error("用户或密码错误");
        }
    }
    @GetMapping(value = "/getVerify")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma" , "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control" , "no-cache");
            response.setDateHeader("Expire" , 0);
            RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
            randomValidateCode.getRandcode(request, response);//输出验证码图片方法
        } catch (Exception e) {
//            logger.error("获取验证码失败>>>> " , e);
        }
    }

    @GetMapping("/main")
    String main() {
        return "common/file/file";
    }

}
