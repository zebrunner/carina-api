/*******************************************************************************
 * Copyright 2020-2022 Zebrunner Inc (https://www.zebrunner.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.zebrunner.carina.proxy;

import com.zebrunner.carina.api.config.APIConfiguration;
import com.zebrunner.carina.utils.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SystemProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private SystemProxy() {
        // hide
    }

    /**
     * Set host and port to the system proxy properties for specified {@link Protocol}
     *
     * @param proxyHost proxy host, for example {@code 0.0.0.0}
     * @param proxyPort proxy port, for example {@code 80}
     * @param protocol  see {@link Protocol}
     * @param noProxy   a list of hosts that should be reached directly, bypassing the proxy.
     *                  This is a list of patterns separated by '|'. The patterns may start or
     *                  end with a '*' for wildcards. Any host matching one of these patterns
     *                  will be reached through a direct connection instead of through a proxy.
     *                  For example: {@code localhost|host.example.com} -  proxy won't be used
     *                  when connecting to either localhost or host.example.com.
     *                  Ignored by {@link Protocol#SOCKS}.
     */
    public static void setupSystemProxy(String proxyHost, String proxyPort, Protocol protocol, String noProxy) {
        if (protocol.equals(Protocol.HTTP)) {
            initProxy("http", proxyHost, String.valueOf(proxyPort), noProxy);
        } else if (protocol.equals(Protocol.HTTPS)) {
            initProxy("https", proxyHost, String.valueOf(proxyPort), noProxy);
        } else if (protocol.equals(Protocol.FTP)) {
            initProxy("ftp", proxyHost, String.valueOf(proxyPort), noProxy);
        } else if (protocol.equals(Protocol.SOCKS)) {
            /*
             * http://docs.oracle.com/javase/8/docs/technotes/guides/net/proxies.html
             * Once a SOCKS proxy is specified in this manner, all TCP connections will be attempted through the proxy.
             * i.e. There is no provision for setting non-proxy hosts via the socks properties.
             */
            LOGGER.info("HTTP client will use socks proxies: {}:{}", proxyHost, proxyPort);
            System.setProperty("socksProxyHost", proxyHost);
            System.setProperty("socksProxyPort", String.valueOf(proxyPort));
        }
    }

    /**
     * Setup system proxy from properties.<br>
     * <b>If there are started dynamic proxy, it's values will be in priority</b>
     */
    public static void setupProxy() {
        Optional<String> proxyHost = Configuration.get(APIConfiguration.Parameter.PROXY_HOST);
        Optional<String> proxyPort = Configuration.get(APIConfiguration.Parameter.PROXY_PORT);
        List<String> protocols = Arrays.asList(Configuration.get(APIConfiguration.Parameter.PROXY_PROTOCOLS).orElse("")
                .split("[\\s,]+"));

        if (proxyHost.isEmpty() ||
                proxyPort.isEmpty() || protocols.isEmpty() ||
                !Configuration.get(APIConfiguration.Parameter.PROXY_SET_TO_SYSTEM, Boolean.class).orElse(false)) {
            return;
        }

        // there are difference between system noproxy (parttern with '|' delimiter) and selenium noproxy (addresses with ',' delimiter)
        // in configuration we use set noproxy in selenium style, so for system proxy we should rewrite it for compatibility
        String systemNoProxy = "";
        Optional<String> noProxyHosts = Configuration.get(APIConfiguration.Parameter.NO_PROXY);
        if (noProxyHosts.isPresent()) {
            systemNoProxy = noProxyHosts.get().contains(",") ? Arrays.stream(noProxyHosts.get().split(","))
                    .map(String::trim)
                    .collect(Collectors.joining("|")) : noProxyHosts.get();
        }

        if (protocols.contains("http")) {
            initProxy("http", proxyHost.get(), proxyPort.get(), systemNoProxy);
        }
        if (protocols.contains("https")) {
            initProxy("https", proxyHost.get(), proxyPort.get(), systemNoProxy);
        }
        if (protocols.contains("ftp")) {
            initProxy("ftp", proxyHost.get(), proxyPort.get(), systemNoProxy);
        }

        if (protocols.contains("socks")) {
            /*
             * http://docs.oracle.com/javase/8/docs/technotes/guides/net/proxies.html
             * Once a SOCKS proxy is specified in this manner, all TCP connections will be attempted through the proxy.
             * i.e. There is no provision for setting non-proxy hosts via the socks properties.
             */
            LOGGER.info("HTTP client will use socks proxies: {}:{}", proxyHost, proxyPort);
            System.setProperty("socksProxyHost", proxyHost.get());
            System.setProperty("socksProxyPort", proxyPort.get());
        }
    }

    private static void initProxy(String protocolName, String proxyHost, String proxyPort, String noProxyHostNames) {
        LOGGER.info("HTTP client will use {} proxies: {}:{}", protocolName, proxyHost, proxyPort);
        System.setProperty(String.format("%s.proxyHost", protocolName), proxyHost);
        System.setProperty(String.format("%s.proxyPort", protocolName), proxyPort);
        if (!noProxyHostNames.isEmpty()) {
            System.setProperty(String.format("%s.nonProxyHosts", protocolName), noProxyHostNames);
        }
    }

    /**
     * Checks if a proxy server is available
     *
     * @param address       ip address
     * @param port          int
     * @param timeoutMillis int
     * @return true if proxy is available at the specified id and port, and false otherwise
     * @throws UnknownHostException if address is not correct
     */
    public static boolean isProxyAlive(String address, int port, int timeoutMillis) throws UnknownHostException {
        boolean isAlive = false;
        InetAddress proxyAddress = InetAddress.getByName(address);
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(proxyAddress, port), timeoutMillis);
            isAlive = true;
        } catch (IOException e) {
            // do nothing
        }
        return isAlive;
    }

    /**
     * Checks the availability of a resource through a proxy
     *
     * @param proxyAddress       ip address
     * @param proxyPort          int
     * @param protocol           HTTP or SOCKS (for FTP use SOCKS)
     * @param destinationAddress ip resource address or url (for example google.com)
     * @param destinationPort    resource port
     * @param timeoutMillis      int
     * @return true if resource is available through proxy and false otherwise
     * @throws UnknownHostException if no IP address for the host could be found, or if a scope_id was specified for a global IPv6 address.
     */
    public static boolean isResourceAvailableUsingProxy(String proxyAddress, int proxyPort, Proxy.Type protocol,
            String destinationAddress, int destinationPort, int timeoutMillis) throws UnknownHostException {
        boolean isAvailable = false;

        InetAddress inetProxyAddress;
        try {
            inetProxyAddress = InetAddress.getByName(proxyAddress);
        } catch (UnknownHostException e) {
            LOGGER.error("Proxy address is not correct");
            throw e;
        }

        SocketAddress proxySocketAddress = new InetSocketAddress(inetProxyAddress, proxyPort);
        Proxy proxy;
        try {
            proxy = new Proxy(protocol, proxySocketAddress);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }

        InetAddress inetDestinationAddress;
        try {
            inetDestinationAddress = InetAddress.getByName(destinationAddress);
        } catch (UnknownHostException e) {
            LOGGER.error("Destination address is not correct");
            throw e;
        }

        try (Socket socket = new Socket(proxy)) {
            socket.connect(new InetSocketAddress(inetDestinationAddress, destinationPort), timeoutMillis);
            isAvailable = true;
        } catch (SocketTimeoutException e) {
            LOGGER.error("Timeout when try to connect to {} by port {} with proxy {} by port {}",
                    destinationAddress, destinationPort, proxyAddress, proxyPort);
        } catch (IOException | InternalError e) {
            // do nothing
        }
        return isAvailable;
    }

}
