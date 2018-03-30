package com.vmloft.develop.library.example.socket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.tools.VMActivity;
import com.vmloft.develop.library.tools.utils.VMLog;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 测试 TCP 和 UDP Socket 连接收发数据
 * Created by lzan13 on 2017/3/12.
 */
public class VMSocketActivity extends VMActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

        ButterKnife.bind(activity);
    }

    /**
     * 按钮点击监听
     */
    @OnClick({R.id.btn_tcp_socket_server, R.id.btn_tcp_socket_client, R.id.btn_udp_socket_server,
                     R.id.btn_udp_socket_client})
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_tcp_socket_server:
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        addToServerLog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            //            onStartActivity(activity, new Intent(activity, VMTCPSocketServer.class));
            break;
        case R.id.btn_tcp_socket_client:
            onStartActivity(activity, new Intent(activity, VMTCPSocketClient.class));
            break;
        case R.id.btn_udp_socket_server:
            onStartActivity(activity, new Intent(activity, VMUDPSocketServer.class));
            break;
        case R.id.btn_udp_socket_client:
            onStartActivity(activity, new Intent(activity, VMUDPSocketClient.class));
            break;
        }
    }

    public String addToServerLog() throws Exception {

        String WSDL_URI = "http://60.195.250.211:8180/sss/services/IESealServiceService?wsdl";//wsdl 的uri
        String namespace = "http://www.oscca.gov.cn/eseal";//namespace
        String methodName = "sendData";//要调用的方法名称
        VMLog.d(WSDL_URI + namespace + methodName);

        SoapObject request = new SoapObject(namespace, methodName);

        //        UserInfo user = AppConfig.getUser();

        StringBuilder s = new StringBuilder("<dataType>6902S</dataType>");
        s.append("<appID>" + "" + "</appID>");
        s.append("<dataTime>" + "2018-01-30 11:37:54" + "</dataTime>");
        s.append("<version>1</version>");

        s.append("<systemID>6902</systemID>");
        s.append("<dataInfo><dataInfoValue>");
        s.append("<time>20170908072503Z</time>");
        s.append("<stampId>" + "201710090609041001" + "</stampId>");
        s.append("<id>" + "13070109986" + "</id>");
        s.append("<type>" + "2" + "</type>");
        s.append("<userId>" + "158" + "</userId>");
        s.append("<userType>" + "13" + "</userType>");
        s.append("<content>" + "" + "</content>");
        s.append("<sealFileFrom>" + 1 + "</sealFileFrom>");
        s.append("<longitude>" + "116.3982445869062" + "</longitude>");
        s.append("<latitude>" + "39.9927739557198" + "</latitude>");
        s.append("</dataInfoValue></dataInfo>");

        String content = Base64.encodeToString(s.toString().getBytes(), Base64.DEFAULT);
        StringBuilder finalmlStrX = new StringBuilder("<sealDataInfo>\n" + "        <appData>");
        finalmlStrX.append(content);
        finalmlStrX.append("</appData>\n"
                + "        <signature xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\"></signature>\n"
                + "</sealDataInfo>\n"
                + "\n");

               /* MsgHandler msgHandler = new MsgHandler();
        String ret = msgHandler.buildMsg(OsspConstant.SEALRECORD,"1.2.156.10197.1.501",dataInfoValueList,"");*/

        // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
        request.addProperty("xmlStr",
                Base64.encodeToString(finalmlStrX.toString().getBytes(), Base64.DEFAULT));

        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapSerializationEnvelope.VER12);
        envelope.bodyOut = request;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = false;//由于是.net开发的webservice，所以这里要设置为true

        HttpTransportSE httpTransportSE = new HttpTransportSE(WSDL_URI);
        httpTransportSE.call(null, envelope);//调用

        // 获取返回的数据
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();

        VMLog.d(result);
        return result;
    }
}
