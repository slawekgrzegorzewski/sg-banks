package pl.sg;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import pl.sg.configuration.ConfigurationPropertyGetter;
import pl.sg.configuration.model.ConfigurationProperty;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Component
public class Configuration {

    private final static Path SECRETS_PATH = Paths.get("/", "run", "secrets");
    private final static ConfigurationPropertyGetter configurationPropertyGetter = new ConfigurationPropertyGetter(
            Map.of(
                    "GO_CARDLESS_SECRET_ID", new ConfigurationProperty("GO_CARDLESS_SECRET_ID", SECRETS_PATH.resolve("go_cardless_secret_id")),
                    "GO_CARDLESS_SECRET_KEY", new ConfigurationProperty("GO_CARDLESS_SECRET_KEY", SECRETS_PATH.resolve("go_cardless_secret_key"))
            )
    );

    public String getNordigenSecretId() {
        return configurationPropertyGetter.getOrDefault("GO_CARDLESS_SECRET_ID", "");
    }

    public String getNordigenSecretKey() {
        return configurationPropertyGetter.getOrDefault("GO_CARDLESS_SECRET_KEY", "");
    }

}
