package com.me10zyl.serverstatus.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Data
public class ServerStatus {
    private Status status = Status.NOT_STARTED;
    private Date startTime;
    private String startTimeText;
    private long usedMemory;
    private List<String> builders;

    public String getStatusText2(){
        StringBuilder sb = new StringBuilder();
        if(status == Status.STARTED){
            sb.append("已启动");
        }else{
            sb.append("未启动");
        }
        return sb.toString();
    }
    public String getStatusText(){
        StringBuilder sb = new StringBuilder();
        sb.append("启动时间：").append(startTimeText).append("(已运行").append(getTimeDiff()).append(")");
        sb.append(" 内存使用情况：").append(getFreeMemText());
        return sb.toString();
    }

    private String getTimeDiff() {
        if(startTime == null){
            return null;
        }
        final long l = new Date().getTime() - startTime.getTime();
        return l / 1000 / 60 / 60 + "h" + l/1000/60%60 + "m";
    }

    public String getFreeMemText(){
        final double gb = (double) usedMemory / 1024 / 1024.0;
        return BigDecimal.valueOf(gb).setScale(2, RoundingMode.FLOOR) + "GB";
    }
}
