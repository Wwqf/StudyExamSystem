package data;

import entity.SingleElection;
import enums.AnswerType;
import enums.LevelType;

import java.util.ArrayList;
import java.util.Comparator;

public class ProblemDataSet {
    /*
     * 一个问题对应四个选项
     */

    ArrayList<SingleElection> valueSet = new ArrayList<>();
    ArrayList<AnswerType> keySet = new ArrayList<>();

    {
        String problem = "下面代码的功能是______:\n" +
                "filePath=\"./ face.jpg\"\n" +
                "f=open(filePath,\"rb\")\n" +
                "data = base64.b64encode(f.read())";
        ArrayList<String> itemOption1 = new ArrayList<>();
        itemOption1.add("用二进制形式打开图片文件，然后转换为base64编码格式");
        itemOption1.add("图片文件的位置不确定");
        itemOption1.add("用base64编码格式打开图片文件，然后转换为二进制");
        itemOption1.add("以上说法都不对");
        SingleElection item = new SingleElection(problem, itemOption1, AnswerType.A, LevelType.LEVEL_2);
        valueSet.add(item);

        ArrayList<String> itemOption2 = new ArrayList<>();
        problem = "语句“client.detect(image, image_type,options)”功能是_______:";
        itemOption2.add("调用detect函数实现人脸对比功能");
        itemOption2.add("调用detect函数实现人脸搜索功能");
        itemOption2.add("调用detect函数实现人脸检测功能");
        itemOption2.add("以上说法都不对；");
        item = new SingleElection(problem, itemOption2, AnswerType.C, LevelType.LEVEL_2);
        valueSet.add(item);

        ArrayList<String> itemOption3 = new ArrayList<>();
        problem = "下面代码的功能是______:\n" +
                "img = Image.open('./face_rec/b6.jpg')\n" +
                "img.show()";
        itemOption3.add("显示图片文件img");
        itemOption3.add("显示当前目录下的图片文件b6.jpg");
        itemOption3.add("显示当前目录下文件夹face_rec中的图片文件b6.jpg");
        itemOption3.add("以上说法都不对");
        item = new SingleElection(problem, itemOption3, AnswerType.C, LevelType.LEVEL_3);
        valueSet.add(item);

        ArrayList<String> itemOption4 = new ArrayList<>();
        problem = "访问列表list=['zhang','wang',32,12.5]的第2个元素方法是______:";
        itemOption4.add("list[1]");
        itemOption4.add("list[2]");
        itemOption4.add("list['wang']");
        itemOption4.add("以上说法都不对");
        item = new SingleElection(problem, itemOption4, AnswerType.A, LevelType.LEVEL_3);
        valueSet.add(item);

        ArrayList<String> itemOption5 = new ArrayList<>();
        problem = "下面代码的运行结果是______:\n" +
                " result={'left':222.26, 'top': 279.37, 'width': 325, 'height': 365, 'rotation': -3}\n" +
                "print(result1['width'])";
        itemOption5.add("325");
        itemOption5.add("width");
        itemOption5.add("222");
        itemOption5.add("365");
        item = new SingleElection(problem, itemOption5, AnswerType.A, LevelType.LEVEL_3);
        valueSet.add(item);

        ArrayList<String> itemOption6 = new ArrayList<>();
        problem = "下面代码的运行结果是______:\n" +
                "result={ 'result': {'face_num': 1, 'face_list': 11}}\n" +
                "print(result['result']['face_num'])";
        itemOption6.add("1");
        itemOption6.add("face_num");
        itemOption6.add("11");
        itemOption6.add("{'face_num': 1, 'face_list': 11}");
        item = new SingleElection(problem, itemOption6, AnswerType.A, LevelType.LEVEL_3);
        valueSet.add(item);

        ArrayList<String> itemOption7 = new ArrayList<>();
        problem = "语句“client.match([image1_data,image2_data])”功能是______:";
        itemOption7.add("调用match函数实现人脸对比功能");
        itemOption7.add("调用match函数实现人脸搜索功能");
        itemOption7.add("调用match函数实现人脸检测功能");
        itemOption7.add("以上说法都不对");
        item = new SingleElection(problem, itemOption7, AnswerType.A, LevelType.LEVEL_2);
        valueSet.add(item);

        ArrayList<String> itemOption8 = new ArrayList<>();
        problem = "语句“client.search(image,image_type,faceGroup)”功能是______:";
        itemOption8.add("调用search函数实现人脸对比功能");
        itemOption8.add("调用search函数实现人脸搜索功能");
        itemOption8.add("调用search函数实现人脸检测功能");
        itemOption8.add("以上说法都不对");
        item = new SingleElection(problem, itemOption8, AnswerType.B, LevelType.LEVEL_2);
        valueSet.add(item);

        ArrayList<String> itemOption9 = new ArrayList<>();
        problem = "\"人脸识别\"属于百度AI应用中______类";
        itemOption9.add("百度语音");
        itemOption9.add("视觉技术");
        itemOption9.add("自然语音");
        itemOption9.add("知识图谱");
        item = new SingleElection(problem, itemOption9, AnswerType.B, LevelType.LEVEL_1);
        valueSet.add(item);

        ArrayList<String> itemOption10 = new ArrayList<>();
        problem = "能检测图中的人脸，并为人脸标记出边框，并识别多种人脸属性的服务是______:";
        itemOption10.add("人脸检测");
        itemOption10.add("人脸对比");
        itemOption10.add("人脸搜索");
        itemOption10.add("人脸库管理");
        item = new SingleElection(problem, itemOption10, AnswerType.A, LevelType.LEVEL_1);
        valueSet.add(item);

        ArrayList<String> itemOption11 = new ArrayList<>();
        problem = "“比对两张图片中人脸的相似度，并返回相似度分值”的功能属于______:";
        itemOption11.add("人脸检测");
        itemOption11.add("人脸对比");
        itemOption11.add("人脸搜索");
        itemOption11.add("人脸库管理");
        item = new SingleElection(problem, itemOption11, AnswerType.B, LevelType.LEVEL_1);
        valueSet.add(item);

        ArrayList<String> itemOption12 = new ArrayList<>();
        problem = "“在指定人脸集合中，找到最相似的人脸”的功能属于______:";
        itemOption12.add("人脸检测");
        itemOption12.add("人脸对比");
        itemOption12.add("人脸搜索");
        itemOption12.add("人脸库管理");
        item = new SingleElection(problem, itemOption12, AnswerType.C, LevelType.LEVEL_1);
        valueSet.add(item);

        ArrayList<String> itemOption13 = new ArrayList<>();
        problem = "百度AI开放平台接入流程中，在账号注册登录成功后，需要创建______才可正式调用AI能力";
        itemOption13.add("应用");
        itemOption13.add("照片库");
        itemOption13.add("文件夹");
        itemOption13.add("函数");
        item = new SingleElection(problem, itemOption13, AnswerType.A, LevelType.LEVEL_1);
        valueSet.add(item);

        ArrayList<String> itemOption14 = new ArrayList<>();
        problem = "______、API_KEY、 SECRET_KEY 三个信息是百度AI平台为您的应用实际开发的主要凭证;";
        itemOption14.add("APP");
        itemOption14.add("APP_ID");
        itemOption14.add("AipFace");
        itemOption14.add("密码");
        item = new SingleElection(problem, itemOption14, AnswerType.B, LevelType.LEVEL_1);
        valueSet.add(item);

        ArrayList<String> itemOption15 = new ArrayList<>();
        problem = "______是人脸识别的Python SDK客户端，为使用人脸识别的开发人员提供了一系列的交互方法";
        itemOption15.add("APP");
        itemOption15.add("API ");
        itemOption15.add("AipFace");
        itemOption15.add("SDK");
        item = new SingleElection(problem, itemOption15, AnswerType.C, LevelType.LEVEL_1);
        valueSet.add(item);

        ArrayList<String> itemOption16 = new ArrayList<>();
        problem = "百度AI实现人脸检测的函数是______";
        itemOption16.add("match");
        itemOption16.add("detect");
        itemOption16.add("search");
        itemOption16.add("AipFace");
        item = new SingleElection(problem, itemOption16, AnswerType.B, LevelType.LEVEL_1);
        valueSet.add(item);

        ArrayList<String> itemOption = new ArrayList<>();
        problem = "百度AI实现人脸对比的函数是______";
        itemOption.add("match");
        itemOption.add("detect");
        itemOption.add("search");
        itemOption.add("AipFace");
        item = new SingleElection(problem, itemOption, AnswerType.A, LevelType.LEVEL_1);
        valueSet.add(item);

        ArrayList<String> itemOption17 = new ArrayList<>();
        problem = "百度AI实现人脸搜索的函数是______";
        itemOption17.add("match");
        itemOption17.add("detect");
        itemOption17.add("search");
        itemOption17.add("AipFace");
        item = new SingleElection(problem, itemOption17, AnswerType.C, LevelType.LEVEL_1);
        valueSet.add(item);
    }

    public ProblemDataSet(Comparator<? super SingleElection> comparator) {
        System.out.println("valueSet len: " + valueSet.size() + ", keySet len: " + keySet.size());
        valueSet.sort(comparator);
    }


    public String getProblem(int seId) {
        return valueSet.get(seId).getProblem();
    }

    public String getOption(int seId, int opId) {
        return valueSet.get(seId).getSingleOption(opId);
    }

    public int getOptionSize(int seId) {
        return valueSet.get(seId).getOption().size();
    }

    public AnswerType getAnswer(int seId) {
        return valueSet.get(seId).getAnswer();
    }

    public int len() {
        return valueSet.size();
    }

    public LevelType getLevel(int seId) {
        return valueSet.get(seId).getLevel();
    }

}
