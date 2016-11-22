package com.know.tpadinputtest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import nxr.tpad.lib.TPad;
import nxr.tpad.lib.TPadImpl;
import nxr.tpad.lib.views.FrictionMapView;
import android.R.color;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    View setting;
    Button okButton;
    EditText intervalET,countET,widthET;
    EditText intervalStartET,intervalEndET,countStartET,countEndET;
    TextView inputText,requestText,inputsText;
    String request = "",inputs = "";
    long timeStart = 0;
    FrictionMapView fricView;
    boolean show = true;


    float yn,yl = -1;
    int in = 0;
    TPad mTPad;


    File fricImage;

    boolean countRandom = false,intervalRandom = false;

    int screenHeight = 1220;
    int count=4,interval=80,width=80;
    int countStart = 3,countEnd = 5,intervalStart = 50,intervalEnd = 100;
    int[] its;
    //int length=4;
    //int width = screenHeight/length/3;

    private void initCard(){
        setting = findViewById(R.id.setting);
        okButton = (Button) findViewById(R.id.bt_ok);

        intervalET = (EditText) findViewById(R.id.interval);
        intervalStartET = (EditText) findViewById(R.id.interval_from);
        intervalEndET = (EditText) findViewById(R.id.interval_to);

        countET = (EditText) findViewById(R.id.count);
        countStartET = (EditText) findViewById(R.id.count_from);
        countEndET = (EditText) findViewById(R.id.count_to);

        widthET = (EditText) findViewById(R.id.width);

    }
    private int getText(EditText et){
        String str = et.getText().toString();
        System.out.println("hehe"+str+"hehehe");
        if("".equals(str)){
            return 0;
        }else {
            return Integer.parseInt(str);
        }

    }
    private void freshData(){

        interval = getText(intervalET);

        if(interval==0){
            intervalRandom = true;
        }else{
            intervalRandom = false;
        }
        intervalStart = getText(intervalStartET);
        intervalEnd = getText(intervalEndET);


        count = getText(countET);
        if(count==0){
            countRandom = true;
        }else{
            countRandom = false;
        }
        countStart = getText(countStartET);
        countEnd = getText(countEndET);

        width = getText(widthET);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //testView = findViewById(R.id.view_test);
        //testView.setBackgroundColor(color.holo_orange_light);
        initCard();
        freshData();
        inputText = (TextView) findViewById(R.id.tv_input);
        requestText = (TextView) findViewById(R.id.tv_request);
        inputsText = (TextView) findViewById(R.id.tv_inputs);

        fricView = (FrictionMapView) findViewById(R.id.fricView);

        mTPad = new TPadImpl(this);
        fricView.setTpad(mTPad);

        its = fixeds(0);
        refreshFric(its);
        fricView.setY(50);//
        refreshRequest(6);
        //random(5,6);

        fricView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                v.onTouchEvent(event);

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        timeStart =  System.currentTimeMillis();

                        float ynn = event.getY();
                        its = fixeds(ynn);
                        refreshFric(its);
                        System.out.println("down");
                        break;

                    case MotionEvent.ACTION_MOVE:

                        yn = event.getY();
                        //System.out.println(yn);
                        if(comein(its,yn)){
                            if(!comein(its,yl)){
                                in+=1;
                                if(in>9)in=9;
                                System.out.println(in);
                                inputText.setText("in: "+in);
                            }

                            //mTPad.sendFriction(1f);

                        }else{
                            //mTPad.turnOff();
                        }

                        yl = yn;

                        break;
                    case MotionEvent.ACTION_UP:

                        long now = System.currentTimeMillis();
                        long timeTook = now - timeStart ;

                        requestText.setText(timeTook+"");

                        inputs += in;
                        inputsText.setText(inputs);
                        yl = -1;

                        inputText.setText("in: "+in);
                        in = 0;
                        //mTPad.turnOff();

                        if(inputs.length()>5){
                            inputs = inputs.substring(1, 6);
                            //next();
                        }

                        break;
                    default:
                        break;
                }

                return true;
            }
        });


    }

    private void refreshRequest(int length){
        request = "";
        for(int i =0;i<length;i++){
            request += random(0,9);
        }
        requestText.setText(request);
    }

    private void next(){
        //refreshRequest(6);

        inputs = "";
        inputsText.setText("");
        its = fixeds(0);
        refreshFric(its);
    }

    private void refreshFric(int[] its ){

        int length = its.length;
        //int width = screenHeight/length/3;

        Bitmap bitmap = Bitmap.createBitmap(720, 1080, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

		/*Bitmap fricBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fric);
		fricBitmap = Bitmap.createBitmap(fricBitmap, 0, 0, 720, width, null, false);*/

        BitmapStripe bs = new BitmapStripe(this);
        bs.generateTexture(720, width, 4, 4);
        Bitmap fricBitmap = bs.bitmap;


		/*try {
			FileInputStream inputStream = new FileInputStream(fricImage);
			fricBitmap = BitmapFactory.decodeStream(inputStream);
			fricBitmap = Bitmap.createBitmap(fricBitmap, 0, 0, 300, width, null, false);


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/


        Paint paint = new Paint();
        for(int i = 0;i<length;i++){
            Rect rect = new Rect(0, its[i], fricBitmap.getWidth(), its[i]+width);
            canvas.drawBitmap(fricBitmap, null, rect, paint);
        }
        fricView.setDataBitmap(bitmap);


    }

    private boolean comein(int[] its , float y){
        int length = its.length;

        boolean bool = false;
        for(int i = 0;i<length;i++){
            if(y>its[i]&&y<its[i]+width) bool = true;
        }

        return bool;
    }

    //产生随机数
    private int random(int start ,int end){
        Random r = new Random();
        int it = r.nextInt(end-start+1);
        Log.e("随机数", it+start+"!");
        return it+start;
    }
    //产生随机数组
	/*private int[] randoms(int length,int avoid){
		int h2 = (screenHeight)/length/2;
		int h3=h2;
		int[] its = new int[length];
		for(int i = 0;i<length;i++){
			int temp = h2/2+(h2+h3)*i;
			int random = random(temp,temp+h3);
			if(avoid>random){

			}
			its[i] = random;
		}
		return its;
	}*/
    private int[] fixeds(float yy){

        if(countRandom){
            count = random(countStart, countEnd);
        }
        if(intervalRandom){
            interval = random(intervalStart,intervalEnd);
        }

        int head = (screenHeight +width - count*(interval+width))/2;
        //head += random(-150,250);
        int jump = 0;
        int[] its = new int[count];
        for(int i = 0;i<count;i++){
            int result = head+(interval+width)*i;
            if(yy>result&&yy<result+width){
                float jumpp = yy-result>width/2 ? (yy-result)-width-20 : yy-result+20;
                jump = (int)jumpp;
            }
            its[i] = result;
        }

        if(jump!=0){
            for(int i = 0;i<count;i++){
                its[i] += jump;
            }
        }

        return its;
    }


    @Override
    protected void onDestroy() {
        mTPad.disconnectTPad();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode){
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                fricView.setDisplayShowing(show = !show);
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void changeLength(View view){
        setting.setVisibility(View.VISIBLE);

    }
    public void hideSetting(View view){
        freshData();
        next();
        setting.setVisibility(View.GONE);
    }

}

