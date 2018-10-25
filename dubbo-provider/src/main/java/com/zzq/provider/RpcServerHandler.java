package com.zzq.provider;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;

import java.util.Map;

public class RpcServerHandler extends ChannelInboundHandlerAdapter {

    public RpcServerHandler(Map<String,Object> handlerMap){

    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //ctx 可以用来向客户端发送数据
        //msg 可以接收到数据

        super.channelRead(ctx, msg);
    }
}
