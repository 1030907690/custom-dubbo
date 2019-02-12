package com.zzq.consumer.customtag.pojo;

public class Dubbo {
    /*spring beanName*/
	private String id;

	/*消费方应用名，用于计算依赖关系，不是匹配条件，不要与提供方一样*/
    private String name;

    /*使用zookeeper注册中心暴露服务地址 */
    private String address;

    /*使用zookeeper注册中心暴露服务地址*/
    private String protocol;

    /*生成远程服务代理，可以像使用本地bean一样使用demoService*/
    private String basePackage;

    /*消费者调用超时设置为10秒*/
    private String timeout;

    /*端口*/
    private String port;


    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
}
