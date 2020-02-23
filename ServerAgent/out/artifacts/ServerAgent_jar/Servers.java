
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.util.*;

public class Servers {


    public static void main(String[] args) throws IOException {
        Servers servers = null;
        try {
            servers = new Servers();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (servers != null) servers.close();
        }
    }


    /*
     * 客户端连接后, 将客户信息入队列(最大堆最小堆), 当用户提交成绩时, 更新成绩
     * 每2秒向遍历队列, 发送数据
     */

    private SelectorProvider provider = SelectorProvider.provider();
    private Selector selector = null;
    private Selector writeSelector = null;
    private Selector readSelector = null;
    private ServerSocketChannel server = null;
    private static final int PORT = 6775;

    private StudentInformationHandle infoHandle;
    private ArrayList<SocketChannel> broadcastHandle;

    public Servers() throws IOException {

        selector = provider.openSelector();
        writeSelector = Selector.open();
        readSelector = Selector.open();
        server = provider.openServerSocketChannel();
        /* 非阻塞模式 */
        server.configureBlocking(false);
        /* 注册 */
        SelectionKey key = server.register(selector, 0, null);
        /* 绑定端口成功 */
        if (server.bind(new InetSocketAddress(PORT)).socket().isBound()) {
            /* 监听链接请求 */
            key.interestOps(SelectionKey.OP_ACCEPT);
        }

        /* 学生信息 */
        infoHandle = new StudentInformationHandle();
        broadcastHandle = new ArrayList<>();

        threadMonitor();
        while (true) {
            monitorClientConnect(key);
        }
    }

    private void monitorClientConnect(SelectionKey key) throws IOException {
        /* 监听就绪事件 */
        selector.select();
        System.out.println("monitorClientConnect");
        Iterator<SelectionKey> selection = selector.selectedKeys().iterator();
        while (selection.hasNext()) {
            System.out.println("selector size = " + selector.selectedKeys().size());
            key = selection.next();
            selection.remove();

            if (key.isAcceptable()) {
                System.out.println("one client is connect.");
                accept();
            } else {
                handle(key);
            }
        }
    }

    private void threadMonitor() throws IOException {
        new Thread(() -> {
            while (true) {
                System.out.println("threadMonitor is running");
                try {
                    registerChannel();
//                    readToChannel();
                    writeToChannel();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void registerChannel() throws IOException {
        System.out.println("register Channel.");
        ArrayList<SocketChannel> deleteChannel = new ArrayList<>();
        for (SocketChannel sc : broadcastHandle) {
            try {
                sc.configureBlocking(false);
                sc.register(writeSelector, SelectionKey.OP_WRITE);
//                sc.register(readSelector, SelectionKey.OP_READ);
            } catch (ClosedChannelException e) {
                deleteChannel.add(sc);
            }
        }

        for (SocketChannel sc : deleteChannel) {
            broadcastHandle.remove(sc);
        }

        System.out.println("current channel count has " + broadcastHandle.size());
    }

    private void readToChannel() throws IOException {
        if (readSelector.selectNow() == 0) return;

        Iterator<SelectionKey> iterator = readSelector.selectedKeys().iterator();
        while (iterator.hasNext()) {
            SelectionKey key =  iterator.next();
            iterator.remove();

            SocketChannel channel = (SocketChannel) key.channel();
            if (key.isValid() && key.isReadable()) {
                String result = read(channel, 2048);
                if (result == null) {
                    System.err.println("read data is ERROR.");
                } else {
                    System.out.println("result: " + result);
                }
                key.interestOps(key.interestOps() & SelectionKey.OP_WRITE);
            }
        }
    }

    private void writeToChannel() throws IOException {
        if (writeSelector.selectNow() == 0) return;

        Iterator<SelectionKey> iterator = writeSelector.selectedKeys().iterator();
        while (iterator.hasNext()) {
            SelectionKey key =  iterator.next();
            iterator.remove();

            SocketChannel channel = (SocketChannel) key.channel();
            if (key.isValid() && key.isWritable()) {
                write(channel, infoHandle.broadcast());
                key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
            }
        }
    }

    private void handle(SelectionKey key) throws IOException {
        // TODO 伪装
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("handle");
        SocketChannel channel = (SocketChannel) key.channel();
        if (key.isWritable()) {
            // TODO send message to client

            write(channel, infoHandle.broadcast());
            /* 取消写就绪, 则会一直触发写就绪（写就绪为代码触发） */
            key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
            /* 防止重复writing, 设置读模式 */
        } else if (key.isValid() && key.isReadable()) {
            // TODO receive message by client

            String message = read(channel, 2048);
            if (message != null) {
                /*  写就绪, 准备写数据 */
                System.out.println("message: " + message);
                infoHandle.update(message);
                key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
            } else {
                channel.close();
            }
        }
        System.out.println("-------------------------------------");
    }

    private void accept() throws IOException {
        SocketChannel channel = null;
        try {
            /* 接受连接 */
            channel = server.accept();
            /* 非阻塞模式 */
            channel.configureBlocking(false);
            /* 监听读就绪 */
            channel.register(selector, SelectionKey.OP_READ, null);
            broadcastHandle.add(channel);
        } catch (IOException e) {
            if (channel != null) channel.close();
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
            return null;
        }
    }

    public void write(SocketChannel channel, String msg) throws IOException {
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
        }
    }

    public void close() throws IOException {
        if (server != null) server.close();
        if (selector != null) selector.close();
    }
}

class StudentInformation {
    private String name;
    private String time;
    private String subject;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getTimeInt() {
        return Integer.parseInt(time);
    }

    public int getSubjectInt() {
        return Integer.parseInt(subject);
    }
}

class RankComparator<T> implements Comparator<T> {

    @Override
    public int compare(T t, T t1) {
        StudentInformation stu01 = (StudentInformation) t;
        StudentInformation stu02 = (StudentInformation) t1;

        if (stu01.getSubjectInt() < stu02.getSubjectInt()) return -1;
        else if (stu01.getSubjectInt() == stu02.getSubjectInt()) {
            if (stu01.getTimeInt() < stu02.getTimeInt()) return -1;
        }
        return 1;
    }

}

class StudentInformationHandle {


    /*每当有学生进入后, 更新该学生的信息*/
    public PriorityQueue<StudentInformation> queue;


    public StudentInformationHandle() {
        queue = new PriorityQueue<>(30, new RankComparator<>());
    }

    public String broadcast() {
        // TODO 每两秒发送一次信息
        StringBuilder builder = new StringBuilder();
        for (StudentInformation ss : queue) {
            builder.append("~");
            builder.append(ss.getName()).append("&");
            builder.append(ss.getTime()).append("&");
            builder.append(ss.getSubject());
        }
        if (builder.toString().length() <= 0) return "";
        return builder.toString().substring(1);
    }

    public void update(String source) {
        String[] stu = source.split("&");

        StudentInformation result = get(stu[0]);
        if (result == null) {
            result = new StudentInformation();
            result.setName(stu[0]);
            result.setTime(stu[1]);
            result.setSubject(stu[2]);

            boolean isInsertCorrect = queue.offer(result);

            System.out.println("result is null, offer to queue.");
        } else {
            if (result.getSubjectInt() > Integer.parseInt(stu[2])) return;
            result.setName(stu[0]);
            result.setTime(stu[1]);
            result.setSubject(stu[2]);

            System.out.println("result not null, update this.");
        }
    }

    private StudentInformation get(String name) {
        for (StudentInformation ss : queue) {
            if (ss.getName().equals(name)) return ss;
        }
        return null;
    }
}

