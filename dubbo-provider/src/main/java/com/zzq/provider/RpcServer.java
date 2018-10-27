package com.zzq.provider;

import com.zzq.provider.annotation.RpcAnnotation;
import com.zzq.provider.registry.IRegistryCenter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.HashMap;
import java.util.Map;

public class RpcServer {


    private IRegistryCenter registryCenter;

    private String serviceAddress;

    private Map<String,Object> handlerMap = new HashMap<>();

    public RpcServer(IRegistryCenter registryCenter ,String serviceAddress){
        this.registryCenter = registryCenter;
        this.serviceAddress = serviceAddress;
    }


    public void bind(Object ... services){
        for (Object service : services){
            RpcAnnotation rpcAnnotation = service.getClass().getAnnotation(RpcAnnotation.class);
            String serviceName = rpcAnnotation.value().getName();
            handlerMap.put(serviceName,service);
        }
    }

    //发布服务和监听端口
    public void publisher(){

        for (String serviceName:handlerMap.keySet()){
            registryCenter.register(serviceName,serviceAddress);
        }

        try {
            EventLoopGroup boosGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            //启动netty服务
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boosGroup);
            bootstrap.channel(NioServerSocketChannel.class);

            //springmvc web.xml
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel channel) {
                    //业务代码写这里
                    ChannelPipeline pipeline = channel.pipeline();

                    //netty编程 关注handler
                    pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(
                            Integer.MAX_VALUE,0,4,0,4
                    ));
                    pipeline.addLast("frameEncoder",new LengthFieldPrepender(4));
                    pipeline.addLast("encoder",new ObjectEncoder());
                    pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE
                            ,ClassResolvers.cacheDisabled(null)
                    ));

                    pipeline.addLast(new RpcServerHandler(handlerMap));
                }

            }).option(ChannelOption.SO_BACKLOG,128).childOption(ChannelOption.SO_KEEPALIVE,true);
            //netty 监听
            String [] addrs = serviceAddress.split(":");
            String ip = addrs[0];
            int port = Integer.parseInt(addrs[1]);
            ChannelFuture future = bootstrap.bind(ip,port).sync();
            System.out.print("服务启动成功，等待客户端连接...");
        }catch (Exception e){
            e.printStackTrace();
        }


    }


}
