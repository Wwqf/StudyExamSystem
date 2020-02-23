package network;

import impl.ReadCallback;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.util.Iterator;

public class ClientAgent {

    private static ClientAgent agent;

    public static synchronized ClientAgent instance(ReadCallback callback) throws IOException {
        if (agent == null) {
            agent = new ClientAgent(callback);
        }
        return agent;

    }

    public static void main(String[] args) throws IOException {
//        ClientAgent agent = new ClientAgent();
//        if (agent == null)
//            System.out.println("agent is null.");
//        else
//            System.out.println("agent not null");
//        agent.getClient();
//        agent.writeMessage("hello it me.");
    }

    private SelectorProvider provider = SelectorProvider.provider();
    private Selector selector = null;
    private Selector writeSelector = null;
    private Selector readSelector = null;
    private SocketChannel client = null;
    private static final String IP = "172.30.117.152";
    private static final int PORT = 6775;

    private ReadCallback callback;

    public ClientAgent(ReadCallback callback) {
        super();
        this.callback = callback;
    }

    public void registerReadCallback(ReadCallback readCallback) {
        this.callback = readCallback;
    }

    public ClientAgent() throws IOException {

        /* 向服务端发送数据 */
        selector = provider.openSelector();
        writeSelector = Selector.open();
        readSelector = Selector.open();
        client = provider.openSocketChannel();
        /* 非阻塞模式 */
        client.configureBlocking(false);
        /* 注册 */
        SelectionKey key = client.register(selector, 0, null);
        /* 连接状态 */
        boolean isConnect = client.connect(new InetSocketAddress(IP, 6775));
        if (isConnect) {
            /* 如果连接成功 */
            System.out.println("connected...");
            /*  监听读就绪和写就绪（准备写数据） */
            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        } else {
            /* 连接失败（正常情况下）, 监听连接就绪 */
            key.interestOps(SelectionKey.OP_CONNECT);
        }


        new Thread(() -> {
            while (true) {
                try {
                    monitorServerConnect(key);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void monitorServerConnect(SelectionKey key) throws IOException {
        selector.select();
        Iterator<SelectionKey> selection = selector.selectedKeys().iterator();

        while (selection.hasNext()) {
            key = selection.next();
            selection.remove();
            if (key.isConnectable()) {
                client.finishConnect();
                System.out.println("connected the servers.");
                /* 取消监听连接就绪（否则selector会不断提醒连接就绪） */
                key.interestOps(key.interestOps() & ~SelectionKey.OP_CONNECT);
                /* 监听读就绪和写就绪 */
                key.interestOps(key.interestOps() | SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            } else {
                handle(key);
            }
        }
    }

    private void handle(SelectionKey key) throws IOException {
        if (key.isWritable()) {
            // TODO send message to server
            /* 取消写就绪，否则会一直触发写就绪（写就绪为代码触发） */
            key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
        } else if (key.isValid() && key.isReadable()) {
            // TODO receive message by server
            String message = read(client, 2048);
            if (message != null) {

                // TODO 读取到信息, 进行处理
                // TODO 开个线程, 每两秒发送一次信息, 发送的信息为队列中的数据
                if (callback != null)
                    callback.message(message);
                else
                    System.out.println("rec message : " + message);


                /*  写就绪, 准备写数据 */
                key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
            }
        }
    }





    private String read(SocketChannel channel, int bufSize) throws IOException {
        if (channel == null) return null;

        String result = null;
        try {
            /* 分配HeapByteBuffer */
            ByteBuffer buffer = ByteBuffer.allocate(bufSize);
            /* 直到没有数据 or buffer溢满 */
            int len = channel.read(buffer);
            /* buffer.array() 取HeapByteBuffer中的原始byte[] */
            if (len > 0) {
                result = new String(buffer.array(), 0, len, Charset.forName("UTF-8"));
            }
            return result;
        } catch (IOException e) {
            channel.close();
            System.out.println("channel is close.");
            return null;
        }
    }

    private void write(SocketChannel channel, String msg) throws IOException {
        try {
            /* 转化为byte[] */
            byte[] bytes = msg.getBytes(Charset.forName("UTF-8"));
            /* 分配HeapByteBuffer */
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);
            /* 切换为读模式 */
            buffer.flip();
            channel.write(buffer);
        } catch (IOException e) {
            channel.close();
            System.out.println("channel is close.");
        }
    }

    public void close() throws IOException {
        if (client != null) client.close();
        if (selector != null) selector.close();
    }

    public void registerClientWriteEvent() {
        try {
            client.configureBlocking(false);
            client.register(writeSelector, SelectionKey.OP_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SocketChannel getClient() {
        return client;
    }

    public void writeMessage(String message) throws IOException {
        System.out.println("write message");
        if (writeSelector.selectNow() == 0) return;

        Iterator<SelectionKey> iterator = writeSelector.selectedKeys().iterator();
        while (iterator.hasNext()) {
            System.out.println("has next.");
            SelectionKey key =  iterator.next();
            iterator.remove();

            SocketChannel channel = (SocketChannel) key.channel();
            if (key.isWritable()) {
                write(channel, message);
                key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
            }
        }
    }
}
