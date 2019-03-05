package com.zzq.consumer;

import com.zzq.provider.bean.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RpcProxyHandler extends ChannelInboundHandlerAdapter {

    private Object respone;

    public Object getRespone() {
        return respone;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("client received  data "+msg );

        RpcResponse rpcResponse = (RpcResponse)msg;
        //msg服务端发过来的内容
        //ctx 发送出去
        this.respone = rpcResponse.getResult();
    }
}
