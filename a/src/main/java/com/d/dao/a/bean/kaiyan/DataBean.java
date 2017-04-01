package com.d.dao.a.bean.kaiyan;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cuieney on 17/2/26.
 */

public class DataBean implements Serializable {
    private String dataType;
    private int id;
    private String title;
    private String description;
    private ProviderBean provider;
    private String category;
    private CoverBean cover;
    private String playUrl;
    private int duration;
    private WebUrlBean webUrl;
    private long releaseTime;
    private ConsumptionBean consumption;
    private String type;
    private int idx;
    private long date;
    private boolean collected;
    private boolean played;
    private List<PlayInfoBean> playInfo;
    private List<Tags> tags;


    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProviderBean getProvider() {
        return provider;
    }

    public void setProvider(ProviderBean provider) {
        this.provider = provider;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public CoverBean getCover() {
        return cover;
    }

    public void setCover(CoverBean cover) {
        this.cover = cover;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public WebUrlBean getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(WebUrlBean webUrl) {
        this.webUrl = webUrl;
    }

    public long getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(long releaseTime) {
        this.releaseTime = releaseTime;
    }

    public ConsumptionBean getConsumption() {
        return consumption;
    }

    public void setConsumption(ConsumptionBean consumption) {
        this.consumption = consumption;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public boolean isPlayed() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public List<PlayInfoBean> getPlayInfo() {
        return playInfo;
    }

    public void setPlayInfo(List<PlayInfoBean> playInfo) {
        this.playInfo = playInfo;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }


    public DataBean() {
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "dataType='" + dataType + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", provider=" + provider +
                ", category='" + category + '\'' +
                ", cover=" + cover +
                ", playUrl='" + playUrl + '\'' +
                ", duration=" + duration +
                ", webUrl=" + webUrl +
                ", releaseTime=" + releaseTime +
                ", consumption=" + consumption +
                ", type='" + type + '\'' +
                ", idx=" + idx +
                ", date=" + date +
                ", collected=" + collected +
                ", played=" + played +
                ", playInfo=" + playInfo +
                ", tags=" + tags +
                '}';
    }

    public DataBean(String dataType, int id, String title, String description,
                    ProviderBean provider, String category, CoverBean cover,
                    String playUrl, int duration, WebUrlBean webUrl,
                    long releaseTime, ConsumptionBean consumption,
                    String type, int idx, long date, boolean collected,
                    boolean played, List<PlayInfoBean> playInfo, List<Tags> tags) {
        this.dataType = dataType;
        this.id = id;
        this.title = title;
        this.description = description;
        this.provider = provider;
        this.category = category;
        this.cover = cover;
        this.playUrl = playUrl;
        this.duration = duration;
        this.webUrl = webUrl;
        this.releaseTime = releaseTime;
        this.consumption = consumption;
        this.type = type;
        this.idx = idx;
        this.date = date;
        this.collected = collected;
        this.played = played;
        this.playInfo = playInfo;
        this.tags = tags;
    }
}
