package com.zzq.consumer;

import com.zzq.consumer.registry.IServiceDiscovery;

import com.zzq.provider.bean.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * jdk的代理
 * */
public class RpcClientProxy {

    private IServiceDiscovery serviceDiscovery;

    public RpcClientProxy(IServiceDiscovery serviceDiscovery){
        this.serviceDiscovery = serviceDiscovery;
    }

    public <T> T create(final Class<T> interfaceClass){
        if(null != interfaceClass){
            return (T)Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                    new Class<?>[]{interfaceClass},
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                            //封装对象
                            RpcRequest request = new RpcRequest();
                            request.setClassName(method.getDeclaringClass().getName());
                            request.setMethodName(method.getName());
                            request.setTypes(method.getParameterTypes());
                            request.setParams(args);

                            //服务发现
                            String serviceName = interfaceClass.getName();
                            //得到地址
                            String serviceAddress = serviceDiscovery.discovery(serviceName);

                            //解析ip和port
                            String [] address = serviceAddress.split(":");
                            String host = address[0];
                            int port = Integer.parseInt(address[1]);

                            //socket netty连
                            final RpcProxyHandler rpcProxyHandler = new RpcProxyHandler();


                            EventLoopGroup group = new NioEventLoopGroup();
                            try {
                                Bootstrap b = new Bootstrap();
                                b.group(group).channel(NioSocketChannel.class)
                                .option(ChannelOption.TCP_NODELAY,true)
                                        .handler(new ChannelInitializer<SocketChannel>() {
                                            @Override
                                            protected void initChannel(SocketChannel socketChannel) {
                                                ChannelPipeline pipeline = socketChannel.pipeline();
                                                pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(
                                                        Integer.MAX_VALUE,0,4,0,4
                                                ));
                                                pipeline.addLast("frameEncoder",new LengthFieldPrepender(4));
                                                pipeline.addLast("encoder",new ObjectEncoder());
                                                pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE
                                                ,ClassResolvers.cacheDisabled(null)
                                                ));
                                                pipeline.addLast(rpcProxyHandler);
                                            }
                                        });
                                System.out.println("host " + host + " prot " + port);
                                ChannelFuture future = b.connect(host,port).sync();
                                future.channel().writeAndFlush(request);
                                //阻塞，直到  Channel 关闭
                                future.channel().closeFuture().sync();
                                return rpcProxyHandler.getRespone();
                            }catch (Exception e){
                                e.printStackTrace();
                            }finally {
                                // 关闭线程池并且  释放所有的资源
                                shutdown(group);
                            }
                            return rpcProxyHandler.getRespone();
                        }
                    });
        }
        return null;
    }


    private void shutdown(EventLoopGroup group) {
        group.shutdownGracefully();

    }

}
