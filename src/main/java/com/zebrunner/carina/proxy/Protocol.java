package com.zebrunner.carina.proxy;

public enum Protocol {
    HTTP, HTTPS, FTP, SOCKS;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
