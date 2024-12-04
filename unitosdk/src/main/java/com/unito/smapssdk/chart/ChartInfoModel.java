package com.unito.smapssdk.chart;

public class ChartInfoModel {


    private String msg;
    private int code;
    private DataDTO data;

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

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        private int coldWaterPercentage;
        private int hotWaterPercentage;
        private int sodaWaterPercentage;
        private int filteredWaterPercentage;
        private int mixedWaterPercentage;
        private int coldFlavorWaterPercentage;
        private int hotFlavorWaterPercentage;
        private int sodaFlavorWaterPercentage;
        private int filteredFlavorWaterPercentage;
        private int mixedFlavorWaterPercentage;

        public int getColdFlavorWaterPercentage() {
            return coldFlavorWaterPercentage;
        }

        public void setColdFlavorWaterPercentage(int coldFlavorWaterPercentage) {
            this.coldFlavorWaterPercentage = coldFlavorWaterPercentage;
        }

        public int getHotFlavorWaterPercentage() {
            return hotFlavorWaterPercentage;
        }

        public void setHotFlavorWaterPercentage(int hotFlavorWaterPercentage) {
            this.hotFlavorWaterPercentage = hotFlavorWaterPercentage;
        }

        public int getSodaFlavorWaterPercentage() {
            return sodaFlavorWaterPercentage;
        }

        public void setSodaFlavorWaterPercentage(int sodaFlavorWaterPercentage) {
            this.sodaFlavorWaterPercentage = sodaFlavorWaterPercentage;
        }

        public int getFilteredFlavorWaterPercentage() {
            return filteredFlavorWaterPercentage;
        }

        public void setFilteredFlavorWaterPercentage(int filteredFlavorWaterPercentage) {
            this.filteredFlavorWaterPercentage = filteredFlavorWaterPercentage;
        }

        public int getMixedFlavorWaterPercentage() {
            return mixedFlavorWaterPercentage;
        }

        public void setMixedFlavorWaterPercentage(int mixedFlavorWaterPercentage) {
            this.mixedFlavorWaterPercentage = mixedFlavorWaterPercentage;
        }

        public int getColdWaterPercentage() {
            return coldWaterPercentage;
        }

        public void setColdWaterPercentage(int coldWaterPercentage) {
            this.coldWaterPercentage = coldWaterPercentage;
        }

        public int getHotWaterPercentage() {
            return hotWaterPercentage;
        }

        public void setHotWaterPercentage(int hotWaterPercentage) {
            this.hotWaterPercentage = hotWaterPercentage;
        }

        public int getSodaWaterPercentage() {
            return sodaWaterPercentage;
        }

        public void setSodaWaterPercentage(int sodaWaterPercentage) {
            this.sodaWaterPercentage = sodaWaterPercentage;
        }

        public int getFilteredWaterPercentage() {
            return filteredWaterPercentage;
        }

        public void setFilteredWaterPercentage(int filteredWaterPercentage) {
            this.filteredWaterPercentage = filteredWaterPercentage;
        }

        public int getMixedWaterPercentage() {
            return mixedWaterPercentage;
        }

        public void setMixedWaterPercentage(int mixedWaterPercentage) {
            this.mixedWaterPercentage = mixedWaterPercentage;
        }
    }
}
