package com.jlpay.plugin;

class HenCoderExtension {
//    def name = "rengwuxian12345"

    String name

    boolean debugOn

    HenCoderExtension() {
        name = "rengwuxian12345"
        debugOn = false
    }

    HenCoderExtension(String name, boolean debugOn) {
        this.name = name
        this.debugOn = debugOn
    }

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    boolean getDebugOn() {
        return debugOn
    }

    void setDebugOn(boolean debugOn) {
        this.debugOn = debugOn
    }
}
