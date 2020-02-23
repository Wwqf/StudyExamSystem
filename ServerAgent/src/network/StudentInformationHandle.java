package network;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class StudentInformationHandle {

    /*
     *  TODO 因为是不断更新用户数据, 而在更新过程中, 优先队列不会重新排序
     *      因为使用当前用户系统的人数非常少, 考虑使用普通数组排序, 若不是则可以使用线段树
     *      那么, 还有一种方法, 更新操作换成删除-添加操作, 不知道与数组排序效率如何
     */

    /*每当有学生进入后, 更新该学生的信息*/
    public ArrayList<StuNewInfo> queue;


    public StudentInformationHandle() {
        queue = new ArrayList<>();
    }

    public String broadcast() {
        // TODO 每两秒发送一次信息
        queue.sort(new RankComparator<>());
        StringBuilder builder = new StringBuilder();
        for (StuNewInfo ss : queue) {
            builder.append("~");
            builder.append(ss.getName()).append("&");
            builder.append(ss.getScore());
        }
        if (builder.toString().length() <= 0) return "";
        return builder.toString().substring(1);
    }

    public void update(String source) {
        String[] stu = source.split("&");

        StuNewInfo result = get(stu[0]);
        if (result == null) {
            result = new StuNewInfo();
            result.setName(stu[0]);
            result.setScore(stu[1]);

            queue.add(result);

            System.out.println("result is null, offer to queue.");
        } else {
            result.setName(stu[0]);
            result.setScore(String.valueOf(Integer.parseInt(stu[1]) + result.getScoreInt()));

            System.out.println("result not null, update this.");
        }
    }

    private StuNewInfo get(String name) {
        for (StuNewInfo ss : queue) {
            if (ss.getName().equals(name)) return ss;
        }
        return null;
    }
}
