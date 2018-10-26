package com.zzq.consumer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RpcProxyHandler extends ChannelInboundHandlerAdapter {

    private Object respone;

    public Object getRespone() {
        return respone;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //msg服务端发过来的内容
        //ctx 发送出去
        this.respone = msg;
    }
}
