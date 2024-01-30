package com.me10zyl.serverstatus.util;

import com.me10zyl.serverstatus.dto.ServerStatus;
import com.me10zyl.serverstatus.dto.Status;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ServerUtil {

    @SneakyThrows
    public static void restartServer() {
        log.info("重启服务器");
        final Process exec = Runtime.getRuntime().exec(new String[]{"cmd", "/c", "taskkill", "/F", "/IM", "PalServer-Win64-Test-Cmd.exe"});
        exec.waitFor();
        Desktop.getDesktop().browse(new URI("steam://run/2394010"));
    }

    @SneakyThrows
    public static ServerStatus getServerStatus() {
        final ServerStatus serverStatus = new ServerStatus();
        final List<String> builders = runCmd(new String[]{"cmd", "/c", "tasklist", "|", "findstr", "PalServer"});
        serverStatus.setBuilders(builders);
        log.info("服务器状态："+builders);
        final List<String> builders2 = runCmd(new String[]{"powershell", "-Command", "Get-Process", "PalServer-Win64-Test-Cmd", "|", "Select", "StartTime"});
        setStartTime(builders2, serverStatus);
        setStatus(builders, serverStatus);
        setMem(builders, serverStatus);
        return serverStatus;
    }

    private static void setStartTime(List<String> builders2, ServerStatus serverStatus) {
        final Optional<String> any = builders2.stream().filter(e -> {
            Date parse = null;
            try {
                parse = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(e.trim());
            } catch (ParseException ex) {

            }
            return parse != null;
        }).findAny();
        if(any.isEmpty()){
            return;
        }
        final String s = any.get();
        serverStatus.setStartTimeText(s);
        try {
            serverStatus.setStartTime( new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(s));
        } catch (ParseException e) {

        }
    }

    private static void setMem(List<String> builders, ServerStatus serverStatus) {
        long kbs = 0L;
        for (String builder : builders) {
            String kbString = builder.split("\\s+")[4];
            kbs += Long.parseLong(kbString.replaceAll(",", "")
                    .replaceAll("\\s+", "")
                    .replaceAll("K", ""));
        }
        serverStatus.setUsedMemory(kbs);
    }

    private static void setStatus(List<String> builders, ServerStatus serverStatus) {
        if (builders.size() > 0) {
            serverStatus.setStatus(Status.STARTED);
        }
    }

    @SneakyThrows
    private static List<String> runCmd(String[] strings) {
        final Process exec = Runtime.getRuntime().exec(strings);
        exec.waitFor();
        final InputStream inputStream = exec.getInputStream();
        final InputStream errorStream = exec.getErrorStream();
        List<String> builders = new ArrayList<>();
        final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String str = null;
        while ((str = br.readLine()) != null) {
            builders.add(str);
        }
        return builders;
    }

    public static void main(String[] args) {
        final ServerStatus serverStatus = ServerUtil.getServerStatus();
        System.out.println(serverStatus);
    }

    public static List<String> userList() {
        log.info("用户列表");
        return runCmd(new String[]{"S:\\palserver\\rcon-0.10.3-win64\\rcon.exe", "-a","localhost:25685","-p", "myzyl", "ShowPlayers"});
    }

    public static List<String> sendNotify(String noticeText){
        final List<String> strings = runCmd(new String[]{"S:\\palserver\\rcon-0.10.3-win64\\rcon.exe", "-a", "localhost:25685", "-p", "myzyl", "Broadcast " + noticeText});
        log.info("发送通知:" + noticeText + ",result:" + strings);
        return strings;
    }
}
