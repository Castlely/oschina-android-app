package cn.gov.psxq.bean;

import java.util.List;

public class Weather {
    private String error;

    private String status;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<results> getResultss() {
        return resultss;
    }

    public void setResultss(List<results> resultss) {
        this.resultss = resultss;
    }

    private String        date;

    private List<results> resultss;

    class index {

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getZs() {
            return zs;
        }

        public void setZs(String zs) {
            this.zs = zs;
        }

        public String getTipt() {
            return tipt;
        }

        public void setTipt(String tipt) {
            this.tipt = tipt;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        private String title;

        private String zs;

        private String tipt;

        private String des;

    }

    public class weather_data {
        private String date;

        private String dayPictureUrl;

        private String nightPictureUrl;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDayPictureUrl() {
            return dayPictureUrl;
        }

        public void setDayPictureUrl(String dayPictureUrl) {
            this.dayPictureUrl = dayPictureUrl;
        }

        public String getNightPictureUrl() {
            return nightPictureUrl;
        }

        public void setNightPictureUrl(String nightPictureUrl) {
            this.nightPictureUrl = nightPictureUrl;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getWind() {
            return wind;
        }

        public void setWind(String wind) {
            this.wind = wind;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        private String weather;

        private String wind;

        private String temperature;

    }

    public class results {
        private String currentCity;

        private String pm25;

        public String getCurrentCity() {
            return currentCity;
        }

        public void setCurrentCity(String currentCity) {
            this.currentCity = currentCity;
        }

        public String getPm25() {
            return pm25;
        }

        public void setPm25(String pm25) {
            this.pm25 = pm25;
        }

        public List<index> getIndexs() {
            return indexs;
        }

        public void setIndexs(List<index> indexs) {
            this.indexs = indexs;
        }

        public List<weather_data> getWeather_datas() {
            return weather_datas;
        }

        public void setWeather_datas(List<weather_data> weather_datas) {
            this.weather_datas = weather_datas;
        }

        private List<index>        indexs;

        private List<weather_data> weather_datas;

    }
}
