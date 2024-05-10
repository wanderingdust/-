package com.bishe.kaoyan.controller;

import com.bishe.kaoyan.pojo.model.User;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class UploadController {

    @PostMapping("/uplaod")
    public String upload(MultipartFile file, Model model, HttpServletRequest request) {
        //图片校验（图片是否为空,图片大小，上传的是不是图片、图片类型（例如只能上传png）等等）
        if (file.isEmpty()) {
            return "图片上传失败";
        }
        //可以自己加一点校验 例如上传的是不是图片或者上传的文件是不是png格式等等 这里省略
        //获取原来的文件名和后缀
        String originalFilename = file.getOriginalFilename();
        //String ext = "." + FilenameUtils.getExtension(orgFileName); --需要导依赖
        String ext = "."+ originalFilename.split("\\.")[1];
        //生成一个新的文件名（以防有重复的名字存在导致被覆盖）
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String newName = uuid + ext;
        //拼接图片上传的路径 url+图片名
        ApplicationHome applicationHome = new ApplicationHome(this.getClass());
        //String pre = "\\images\\" + applicationHome.getDir().getParentFile().getParentFile().getAbsolutePath() + "\\src\\main\\resources\\static\\cache\\";
        String pre = "\\images\\";
        String path = pre + newName;
        String insertPath = newName;
        try {
            file.transferTo(new File(insertPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        User user = (User)request.getSession().getAttribute("loginUser");
        model.addAttribute("nickName", user.getNickName());
        model.addAttribute("phone", user.getPhone());
        model.addAttribute("headSculpture", path);
        return "alter.html";
    }
}

