package cn.zhima.flame_project.wx.service;

import cn.zhima.flame_project.wx.entity.*;
import cn.zhima.flame_project.wx.tools.*;
import com.google.gson.Gson;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static cn.zhima.flame_project.wx.tools.GlobalConstant.PUBLIC_URL;

/**
 * @author 冫Soul丶
 */
@Service
public class MessageService {
    /**
     * 添加文件
     */
    static String ADD_FILE_TEMPORARY = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
    /**
     * 获取ticket
     */
    static String CREATE_TICKET = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
    /**
     * 获取二维码
     */
    static String CREATE_CODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
    /**
     * 自定义菜单
     */
    static String VIEW_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    /**
     * 删除自定义菜单
     */
    static String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

    /**
     * 发送客服消息
     */
    static String SEND_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
    static AccessTokenResp accessTokenResp;
    final AccessTokenReq accessTokenReq;
    final MsgDispatcher msgDispatcher;
    final EventDispatcher eventDispatcher;
    final HttpUtils httpUtils;

    public MessageService(AccessTokenReq accessTokenReq, MsgDispatcher msgDispatcher, EventDispatcher eventDispatcher, HttpUtils httpUtils) {
        this.accessTokenReq = accessTokenReq;
        this.msgDispatcher = msgDispatcher;
        this.eventDispatcher = eventDispatcher;
        this.httpUtils = httpUtils;
    }

    /**
     * 消息转发器
     *
     * @param map Map<String, String>
     */
    public String processMessage(Map<String, String> map) {
        return msgDispatcher.processMessage(map);
    }

    /**
     * 事件转发器
     *
     * @param map Map<String, String>
     */
    public String processEvent(Map<String, String> map) {
        return eventDispatcher.processEvent(map);
    }

    /**
     * 定时任务 Get请求
     * 每两个小时获取accessToken
     * accessToken才能获取更多公众号接口
     */
    @Scheduled(fixedRate = 1000 * 7200)
    private void getAccessTokenResp() {
        System.out.println("-----开始获取token-----");
        String requestUrl = String.format(accessTokenReq.getAccessTokenUrl(),
                accessTokenReq.getAppId(),
                accessTokenReq.getAppSecret());
        JSONObject jsonObject = httpUtils.doGetstr(requestUrl);
        if (null != jsonObject) {
            try {
                accessTokenResp = new AccessTokenResp();
                accessTokenResp.setAccessToken(jsonObject.getString("access_token"));
                accessTokenResp.setExpiresIn(jsonObject.getInt("expires_in"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("-----获取时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " - token - " + accessTokenResp + "-----");
    }

    /**
     * 发送客服消息
     * @param title 标题
     * @param description 描述
     * @param url 路径
     * @param picurl 图片路径
     * @param OPENID 发给谁
     */
    public static void sendMessage(String title,String description,String url,String picurl,String OPENID){
        KfArticle article1 = new KfArticle();
        article1.setTitle(title);
        article1.setDescription(description);
        article1.setUrl(url);
        article1.setPicurl(picurl);

        List<KfArticle> kfArticleList = new ArrayList<>();
        kfArticleList.add(article1);
        KfArticleList kfArticleList1 = new KfArticleList();
        kfArticleList1.setArticles(kfArticleList);

        KfNews kfNews = new KfNews();
        kfNews.setNews(kfArticleList1);
        kfNews.setMsgtype("news");
        kfNews.setTouser(OPENID);

        Gson gson=new Gson();
        String json = gson.toJson(kfNews);
        String token = accessTokenResp.getAccessToken();
        String url1 = SEND_MESSAGE.replace("ACCESS_TOKEN", token);
        try {
            JSONObject rs = HttpUtils.doPoststr(url1, json);
            System.out.println(rs);
        } catch (Exception e) {
            System.out.println("请求错误！");
        }
    }

    /**
     * 生成永久的以字符串为参数的二维码票据
     *
     * @return ticket
     */
    public static String createTicket(String s) {
        String token = accessTokenResp.getAccessToken();
        String url = CREATE_TICKET.replace("ACCESS_TOKEN", token);
        String code = JSONObject.fromObject(QRCodeUtil.initStrCodePer(s)).toString();
        JSONObject result = HttpUtils.doPoststr(url, code);
        if ("ok".equals(result.getString("ticket"))) {
            System.out.println("result" + result);
        }
        return result.getString("ticket");
    }

    /**
     * 返回二维码链接
     * @return string
     */
    public static String createCode(String s){
        String ticket = MessageService.createTicket(s);
        return CREATE_CODE.replace("TICKET", ticket);
    }

    /**
     * 自定义菜单
     */
    public static void customMenu() {
        ClickButton one = new ClickButton();
        one.setKey("one");
        one.setName("查看1条");
        one.setType("click");

        ClickButton eight = new ClickButton();
        eight.setKey("eight");
        eight.setName("查看8条");
        eight.setType("click");

        JSONArray subButton1 = new JSONArray();
        subButton1.add(one);
        subButton1.add(eight);

        ClickButton week = new ClickButton();
        week.setKey("week");
        week.setName("最近7天");
        week.setType("click");

        ClickButton month = new ClickButton();
        month.setKey("month");
        month.setName("最近30天");
        month.setType("click");

        ClickButton all = new ClickButton();
        all.setKey("all");
        all.setName("全部报警");
        all.setType("click");

        JSONArray subButton2 = new JSONArray();
        subButton2.add(week);
        subButton2.add(month);
        subButton2.add(all);

        JSONObject buttonOne = new JSONObject();
        buttonOne.put("name", "查看报警");
        buttonOne.put("sub_button", subButton1);

        JSONObject buttonTwo = new JSONObject();
        buttonTwo.put("name", "最近报警");
        buttonTwo.put("sub_button", subButton2);

        ViewButton system = new ViewButton();
        system.setUrl(PUBLIC_URL);
        system.setName("进入系统");
        system.setType("view");

        JSONArray button = new JSONArray();
        button.add(buttonOne);
        button.add(buttonTwo);
        button.add(system);

        com.alibaba.fastjson.JSONObject menujson = new com.alibaba.fastjson.JSONObject();
        menujson.put("button", button);
        System.out.println(menujson);
        String token = accessTokenResp.getAccessToken();
        String url = VIEW_MENU_URL.replace("ACCESS_TOKEN", token);
        try {
            JSONObject rs = HttpUtils.doPoststr(url, menujson.toJSONString());
            System.out.println(rs);
        } catch (Exception e) {
            System.out.println("请求错误！");
        }
    }

    /**
     * 删除菜单
     */
    public static void deleteMenu() {
        String token = accessTokenResp.getAccessToken();
        String url = DELETE_MENU_URL.replace("ACCESS_TOKEN", token);
        JSONObject result = HttpUtils.doGetstr(url);
        if("ok".equals(result.getString("errmsg"))){
            System.out.println("删除成功");
            System.out.println("删除菜单结果：{}"+result);;
        }
        System.out.println("删除失败");
        System.out.println("删除菜单结果：{}"+result);;
    }

    /**
     * 文件上传的方法
     *
     * @param filePath 绝对路径
     * @param fileType 文件类型
     * @return mediaId
     */
    public static String upload(String filePath, String fileType) {
        String result = null;
        String mediaId = null;
        File file = new File(filePath);
        try {
            if (!file.exists() || !file.isFile()) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String token = accessTokenResp.getAccessToken();
            String urlString = ADD_FILE_TEMPORARY.replace("ACCESS_TOKEN", token).replace("TYPE", fileType);
            URL url = new URL(urlString);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            //设置请求头信息
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            //设置边界
            String BOUNDARY = "----------" + System.currentTimeMillis();
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            //请求正文信息
            //第一部分
            StringBuilder sb = new StringBuilder();
            //必须多两条道
            sb.append("--");
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"media\"; filename=\"" + file.getName() + "\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");
            System.out.println("sb:" + sb);
            //获得输出流
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            //输出表头
            out.write(sb.toString().getBytes("UTF-8"));
            //文件正文部分
            //把文件以流的方式 推送道URL中
            DataInputStream din = new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] buffer = new byte[1024];
            while ((bytes = din.read(buffer)) != -1) {
                out.write(buffer, 0, bytes);
            }
            din.close();
            //结尾部分
            //定义数据最后分割线
            byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
            out.write(foot);
            out.flush();
            out.close();
            if (HttpsURLConnection.HTTP_OK == conn.getResponseCode()) {
                StringBuffer strbuffer = null;
                BufferedReader reader = null;
                try {
                    strbuffer = new StringBuffer();
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String lineString = null;
                    while ((lineString = reader.readLine()) != null) {
                        strbuffer.append(lineString);

                    }
                    if (result == null) {
                        result = strbuffer.toString();
                        JSONObject jsonObj = JSONObject.fromObject(result);
                        System.out.println(jsonObj);
                        String typeName = "media_id";
                        if (!"image".equals(fileType)) {
                            typeName = fileType + "_media_id";
                        }
                        mediaId = jsonObj.getString(typeName);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        reader.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaId;
    }
}
