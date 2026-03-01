package config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({
        "file:${user.dir}/src/test/resources/environmentConfig/${env}.properties" })

public interface OwnerConfig extends Config {

    @Key("App.url")
    String appUrl();

    @Key("App.email")
    String email();

    @Key("App.password")
    String password();

}
