package com.zzq.provider;

import com.zzq.provider.bean.RpcRequest;
import com.zzq.provider.bean.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;
import java.util.Map;

public class RpcServerHandler extends ChannelInboundHandlerAdapter {

    private Map<String,Object> handlerMap;

    public RpcServerHandler(Map<String,Object> handlerMap){
        this.handlerMap = handlerMap;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //ctx 可以用来向客户端发送数据
        //msg 可以接收到数据


        System.out.println("server received data " + msg);
        RpcResponse result = new RpcResponse();


        //获得消费者传过来的数据

        try {
            RpcRequest rpcRequest = (RpcRequest) msg;
            //得到服务名称
            if(handlerMap.containsKey(rpcRequest.getClassName())){
                //如果有,用实现类对象去执行
               Object object = handlerMap.get(rpcRequest.getClassName());
              Method method = object.getClass().getMethod(rpcRequest.getMethodName()
                ,rpcRequest.getTypes());
             Object  data = method.invoke(object,rpcRequest.getParams());
             result.setResult(data);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        ctx.write(result);
        ctx.flush();
        //关闭客户端Channel连接
        ctx.close();

    }
}
