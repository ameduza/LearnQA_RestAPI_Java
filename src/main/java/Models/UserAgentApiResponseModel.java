package Models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"user_agent", "platform", "browser", "device"})
public class UserAgentApiResponseModel {
    String user_agent;
    String platform;
    String browser;
    String device;

    public UserAgentApiResponseModel() {
    }

    public UserAgentApiResponseModel(String user_agent, String platform, String browser, String device) {
        this.user_agent = user_agent;
        this.platform = platform;
        this.browser = browser;
        this.device = device;
    }

    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAgentApiResponseModel that = (UserAgentApiResponseModel) o;
        return user_agent.equals(that.user_agent) && platform.equals(that.platform) && browser.equals(that.browser) && device.equals(that.device);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_agent, platform, browser, device);
    }
}