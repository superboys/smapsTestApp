package com.unito.smapssdk.chart;

import java.util.List;

public class LineInfoModel {


    private String msg;
    private int code;
    private List<DataDTO> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public static class DataDTO {
        private int temperature;
        private String statisticsTime;

        public int getTemperature() {
            return temperature;
        }

        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }

        public String getStatisticsTime() {
            return statisticsTime;
        }

        public void setStatisticsTime(String statisticsTime) {
            this.statisticsTime = statisticsTime;
        }
    }
}
