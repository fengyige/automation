package automation.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("framework.properties")
public class ConfigurationProperties {

    @Value("${browser}")
    private String browser;

    @Value("${email}")
    private String email;

    @Value("${password}")
    private String password;

    @Value("${username}")
    private String username;

    public String getBrowser() {
        return browser;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public static void main(String[] args) {
        ConfigurationProperties c = new ConfigurationProperties();
        System.out.println(c.getEmail());
    }
}
