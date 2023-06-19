package com.zebrunner.carina.api.config;

import com.zebrunner.carina.utils.config.Configuration;
import com.zebrunner.carina.utils.config.IParameter;

import java.util.Optional;

public final class APIConfiguration extends Configuration {

    public enum Parameter implements IParameter {

        /**
         * API response will be logged in JSON format. <b>Default: true</b>
         */
        LOG_ALL_JSON("log_all_json"),

        /**
         * Path to a directory with tls secure keys.<br>
         * Example: {@code ./tls/keysecure}
         */
        TLS_KEYSECURE_LOCATION("tls_keysecure_location"),

        /**
         * API requests/responses to ignore SSL errors. <b>Default: false</b>
         */
        IGNORE_SSL("ignore_ssl"),

        //todo should be renamed to the 'proxy_system_host' for example. It should not depend from the carina-webdriver proxy
        PROXY_HOST("proxy_host"),

        //todo should be renamed to the 'proxy_system_port' for example. It should not depend from the carina-webdriver proxy
        PROXY_PORT("proxy_port"),

        //todo should be renamed to the 'proxy_system_protocols' for example. It should not depend from the carina-webdriver proxy
        PROXY_PROTOCOLS("proxy_protocols"),

        //todo should be renamed to the 'no_proxy_system' for example. It should not depend from the carina-webdriver proxy
        NO_PROXY("no_proxy"),

        /**
         * Boolean parameter which enables or disables the setup of a proxy. <b>Default: false</b>
         */
        PROXY_SET_TO_SYSTEM("proxy_set_to_system");

        private final String key;

        Parameter(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    @Override
    public String toString() {
        Optional<String> asString = asString(Parameter.values());
        if (asString.isEmpty()) {
            return "";
        }
        return "\n============= API configuration =============\n" +
                asString.get();
    }
}
