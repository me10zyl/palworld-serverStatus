package com.me10zyl.serverstatus.controller;

import com.me10zyl.serverstatus.dto.ServerStatus;
import com.me10zyl.serverstatus.dto.UserList;
import com.me10zyl.serverstatus.util.ServerUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ServerStatusController {

    @GetMapping
    public String serverHtml(){
        return "server";
    }

    @RequestMapping("/restart")
    @ResponseBody
    public void restart(){
        ServerUtil.restartServer();
    }

    @RequestMapping("/serverStatus")
    @ResponseBody
    public ServerStatus getServerStatus(){
        return ServerUtil.getServerStatus();
    }

    @RequestMapping("/userList")
    @ResponseBody
    private UserList userList(){
        final List<String> strings = ServerUtil.userList();
        final UserList userList = new UserList();
        userList.setPlayerCount(strings.size()-1);
        if(userList.getPlayerCount() > 0) {
            userList.setFormat(String.join("\n", strings));
        }
        return userList;
    }

    @RequestMapping("/sendNotice")
    @ResponseBody
    public String getServerStatus(@RequestParam String text){
        final List<String> strings = ServerUtil.sendNotify(text);
        if(strings.size() > 0 && strings.get(0).contains("Broadcasted")){
            return strings.get(0);
        }
        throw new RuntimeException("发送失败！");
    }
}
