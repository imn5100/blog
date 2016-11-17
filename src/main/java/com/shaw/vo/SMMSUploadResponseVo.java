package com.shaw.vo;

/**
 * Created by Administrator on 2016/11/17 0017.
 */
public class SMMSUploadResponseVo {
    private String code;
    private ImageInfo data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ImageInfo getData() {
        return data;
    }

    public void setData(ImageInfo data) {
        this.data = data;
    }

    public static class ImageInfo {
        private Integer width;
        private Integer height;
        private String filename;
        private String storename;
        private Long size;
        private String path;
        private String hash;
        private Long timestamp;
        private String ip;
        private String url;
        private String delete;

        public String getDetail() {
            return "{'width':" + width + ",'height':" + height + ",'uploadIP':'" + ip + "','deleteUrl':'" + delete + "'}";
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getStorename() {
            return storename;
        }

        public void setStorename(String storename) {
            this.storename = storename;
        }

        public Long getSize() {
            return size;
        }

        public void setSize(Long size) {
            this.size = size;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDelete() {
            return delete;
        }

        public void setDelete(String delete) {
            this.delete = delete;
        }
    }
}
