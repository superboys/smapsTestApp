package com.unito.smapssdk.chart;

import java.util.List;

public class BarInfoModel {

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
        private String statisticsTime;
        private double heaterPowerConsumption = 0;
        private double compressorPowerConsumption;

        public String getStatisticsTime() {
            return statisticsTime;
        }

        public void setStatisticsTime(String statisticsTime) {
            this.statisticsTime = statisticsTime;
        }

        public double getHeaterPowerConsumption() {
            return heaterPowerConsumption;
        }

        public void setHeaterPowerConsumption(double heaterPowerConsumption) {
            this.heaterPowerConsumption = heaterPowerConsumption;
        }

        public double getCompressorPowerConsumption() {
            return compressorPowerConsumption;
        }

        public void setCompressorPowerConsumption(double compressorPowerConsumption) {
            this.compressorPowerConsumption = compressorPowerConsumption;
        }
    }
}
