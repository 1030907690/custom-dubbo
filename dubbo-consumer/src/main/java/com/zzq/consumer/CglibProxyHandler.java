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
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author Zhou Zhong Qing
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: cglib方式代理
 * @date 2019/7/10 11:43
 */
public class CglibProxyHandler {

    private IServiceDiscovery serviceDiscovery;

    public CglibProxyHandler(IServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public <T> T create(final Class<T> interfaceClass) {
        if (null != interfaceClass) {
            Enhancer enhancer = createEnhancer();
            // 增强父类,cglib通过继承
            enhancer.setSuperclass(interfaceClass);
            Callback aopInterceptor = new TargetInterceptor(interfaceClass);
            Callback[] callbacks = new Callback[]{aopInterceptor};
            enhancer.setCallbacks(callbacks);
            return (T) enhancer.create();
        }
        return null;
    }

    protected Enhancer createEnhancer() {
        return new Enhancer();
    }


    public class TargetInterceptor<T> implements MethodInterceptor {

        private Class<T> interfaceClass;

        public TargetInterceptor( Class<T> interfaceClass){
            this.interfaceClass = interfaceClass;
        }
        /**
         * 重写方法拦截在方法前和方法后加入业务
         * Object obj为目标对象
         * Method method为目标方法
         * Object[] params 为参数，
         * MethodProxy proxy CGlib方法代理对象
         */
        @Override
        public Object intercept(Object obj, Method method, Object[] args,
                                MethodProxy proxy) throws Throwable {
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
                                        , ClassResolvers.cacheDisabled(null)
                                ));
                                pipeline.addLast(rpcProxyHandler);
                            }
                        });
                System.out.println("cglib host " + host + " prot " + port);
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

        private void shutdown(EventLoopGroup group) {
            group.shutdownGracefully();

        }
    }
}
